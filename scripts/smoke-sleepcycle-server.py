#!/usr/bin/env python3
"""Dedicated-server registry/data/save probe using the established smart wrapper.

No players are simulated: sleep progression, healing and network gameplay remain
manual checks. Every invocation owns a fresh world and its Gradle/game processes.
"""
import gzip
import json
import os
from pathlib import Path
import re
import runpy
import struct
import sys
import time
import uuid
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[1]
WRAPPER = runpy.run_path(str(ROOT / 'build-smart.py'))


def text_payload(value):
    data = value.encode('utf-8')
    return struct.pack('>H', len(data)) + data


def named(kind, name, value):
    return bytes([kind]) + text_payload(name) + value


def compound(*tags):
    return b''.join(tags) + b'\0'


def integer(value):
    return struct.pack('>i', value)


def listing(kind, values):
    return bytes([kind]) + integer(len(values)) + b''.join(values)


def template():
    states = [compound(named(8, 'Name', text_payload('minecraft:stone')))]
    for part in ('foot', 'head'):
        properties = compound(*(named(8, name, text_payload(value)) for name, value in
                                [('facing', 'south'), ('part', part), ('occupied', 'false')]))
        states.append(compound(named(8, 'Name', text_payload('sleepcycle:sleeping_bag_red')),
                               named(10, 'Properties', properties)))
    blocks = []
    for state, pos in [(0, (0, 0, 0)), (0, (0, 0, 1)), (1, (0, 1, 0)), (2, (0, 1, 1))]:
        tags = [named(3, 'state', integer(state)), named(9, 'pos', listing(3, [integer(p) for p in pos]))]
        if state:
            tags.append(named(10, 'nbt', compound(named(8, 'id', text_payload('sleepcycle:sleeping_bag')))))
        blocks.append(compound(*tags))
    # The 1.21 structure representation is forward-readable; mod IDs/states do
    # not change during vanilla's data fixing.
    return named(10, '', compound(named(3, 'DataVersion', integer(3955)),
                                 named(9, 'size', listing(3, [integer(p) for p in (1, 2, 2)])),
                                 named(9, 'palette', listing(10, states)),
                                 named(9, 'blocks', listing(10, blocks)),
                                 named(9, 'entities', listing(10, []))))


def prepare(project, target, run_dir):
    run_dir.mkdir(parents=True)
    (run_dir / 'eula.txt').write_text('eula=true\n')
    (run_dir / 'server.properties').write_text(
        'server-ip=127.0.0.1\nserver-port=0\nonline-mode=false\nview-distance=2\n'
        'simulation-distance=2\nspawn-protection=0\nlevel-name=probe-world\n')
    pack = run_dir / 'probe-world/datapacks/sleepcycle_probe'
    data = pack / 'data/sleepcycle_probe/structure'
    data.mkdir(parents=True)
    gradle_home = Path(os.environ.get('GRADLE_USER_HOME', Path.home() / '.gradle'))
    game = gradle_home / 'caches/fabric-loom' / target.version / 'minecraft-client.jar'
    with ZipFile(game) as jar:
        format = json.loads(jar.read('version.json'))['pack_version']
    metadata = dict(description='Temporary SleepCycle server validation')
    if 'data_major' in format:
        metadata.update(min_format=[format['data_major'], format['data_minor']],
                        max_format=[format['data_major'], format['data_minor']])
    else:
        metadata['pack_format'] = format['data']
    (pack / 'pack.mcmeta').write_text(json.dumps(dict(pack=metadata)))
    (data / 'bags.nbt').write_bytes(gzip.compress(template()))


def run(project, target, args):
    token = uuid.uuid4().hex
    run_dir = ROOT / 'runs' / target.name / 'server-smoke' / token
    prepare(project, target, run_dir)
    extra = [f'-Psleepcycle_server_smoke_dir={run_dir}', *args]
    java, message, fatal = WRAPPER['select_gradle_java'](project, extra)
    if fatal:
        raise RuntimeError(message)
    command = WRAPPER['gradle_command'](project, [f':{target.name}:runServer', *extra], java)
    print(f'Dedicated-server probe: {target.name} | fresh world {run_dir}', flush=True)
    process = WRAPPER['popen'](command, ROOT, WRAPPER['subprocess'].PIPE, isolate=True)
    lines, ready, reload_done, checked, stopping = [], False, False, False, False
    phase_at = started = time.monotonic()
    stage = 'starting'

    def send(commands):
        process.stdin.write('\n'.join(commands) + '\n')
        process.stdin.flush()

    log = ROOT / 'build/server-smoke' / f'{target.name}-{token}.log'
    log.parent.mkdir(parents=True, exist_ok=True)
    try:
        with log.open('w', encoding='utf-8') as output:
            for line in WRAPPER['stream_lines'](process):
                now = time.monotonic()
                if line is not None:
                    line = WRAPPER['ANSI_ESCAPE'].sub('', line.rstrip())
                    lines.append(line)
                    output.write(line + '\n')
                    output.flush()
                    WRAPPER['show_runtime'](line)
                    if '/ERROR]' in line and not any(x in line for x in WRAPPER['BENIGN_RUNTIME_ERRORS']):
                        raise RuntimeError('Runtime error: ' + line)
                    if not ready and WRAPPER['SERVER_READY'].search(line):
                        ready, stage, phase_at = True, 'reloading', now
                        send(['reload', 'forceload add 0 0'])
                    elif stage == 'reloading' and re.search(r'Loaded \d+ advancements', line):
                        reload_done = True
                    elif stage == 'checking' and 'Saved the game' in line:
                        checked, stage, phase_at = True, 'stopping', now
                        send(['stop'])
                    stopping = stopping or bool(WRAPPER['SERVER_STOPPING'].search(line))
                if stage == 'reloading' and reload_done and now - phase_at >= 3:
                    stage, phase_at = 'checking', now
                    send(['place template sleepcycle_probe:bags 0 70 0',
                          'data get block 0 71 0 id', 'data get block 0 71 1 id',
                          'execute if block 0 71 0 sleepcycle:sleeping_bag_red[facing=south,part=foot] run say SLEEPCYCLE_BAG_FOOT_OK',
                          'execute if block 0 71 1 sleepcycle:sleeping_bag_red[facing=south,part=head] run say SLEEPCYCLE_BAG_HEAD_OK',
                          'save-all flush'])
                if now - started > float(os.environ.get('BUILD_SMART_SERVER_READY_TIMEOUT', '600')):
                    raise RuntimeError('Dedicated server timed out')
                if ready and now - phase_at > 120:
                    raise RuntimeError('Server probe did not complete stage: ' + stage)
        process.wait(timeout=20)
        runtime = '\n'.join(lines)
        if not (ready and reload_done and checked and stopping and process.returncode == 0):
            raise RuntimeError('Server did not reach ready/reload/save/clean shutdown milestones')
        if not all(marker in runtime for marker in ('SLEEPCYCLE_BAG_FOOT_OK', 'SLEEPCYCLE_BAG_HEAD_OK')):
            raise RuntimeError('Sleeping-bag block state probe failed')
        if runtime.count('"sleepcycle:sleeping_bag"') < 2:
            raise RuntimeError('Sleeping-bag block entity identity probe failed')
        evidence = log.with_suffix('.json')
        evidence.write_text(json.dumps(dict(target=target.name, java=target.java, mode='development dedicated server',
                                           ready=True, reload=True, sleeping_bag_states=True,
                                           sleeping_bag_block_entity_ids=True, save_flush=True, clean_stop=True,
                                           gameplay_with_players='manual check required', run_dir=str(run_dir)), indent=2) + '\n')
        print(f'PASS {target.name}: ready, data reload, bag states/block entity IDs, save flush, clean stop | {evidence}', flush=True)
        return True
    except (OSError, RuntimeError, WRAPPER['subprocess'].SubprocessError) as error:
        print(f'FAIL {target.name}: {error} | retained log {log}', flush=True)
        for line in lines[-12:]:
            print(line, flush=True)
        return False
    finally:
        WRAPPER['kill_tree'](process)
        process.stdin.close()


if __name__ == '__main__':
    project = WRAPPER['Project'](ROOT)
    selected = sys.argv[1] if len(sys.argv) > 1 else 'representatives'
    targets = ([target for target in project.targets if target.props.get('release_smoke') == 'true']
               if selected == 'representatives' else [target for target in project.targets if target.name == selected])
    if not targets:
        raise SystemExit('Use a registered target or representatives')
    for target in sorted(targets, key=lambda t: (t.legacy, t.loader, t.version)):
        if not run(project, target, sys.argv[2:]):
            raise SystemExit(1)
    print(f'SERVER PROBE PASS ({len(targets)}/{len(targets)})')

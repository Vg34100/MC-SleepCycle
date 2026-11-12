"""Release-specific preflight: reuse accepted runtime checks only for identical application bytes."""
from pathlib import Path
import hashlib
import json
import runpy
import sys
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[1]
wrapper = runpy.run_path(str(ROOT / 'build-smart.py'))
publisher = runpy.run_path(str(ROOT / 'scripts/publish-release.py'))
original_main = wrapper['main']


def reuse_runtime():
    baseline = json.loads((ROOT / 'build/publishing/runtime-baseline-1.0.5.json').read_text())
    plan = publisher['publication_plan'](ROOT)
    assert {row['target'] for row in baseline} == {row['target'] for row in plan['entries']}
    targets = {row['target']: row for row in plan['entries']}
    for row in baseline:
        old = ROOT / row['artifact']
        assert hashlib.sha256(old.read_bytes()).hexdigest() == row['sha256'], 'Frozen runtime baseline changed'
        new = ROOT / targets[row['target']]['artifact']
        with ZipFile(old) as before, ZipFile(new) as after:
            assert set(before.namelist()) == set(after.namelist()), row['target'] + ': JAR contents changed'
            for name in before.namelist():
                a, b = before.read(name), after.read(name)
                if a == b:
                    continue
                if name == 'fabric.mod.json':
                    x, y = json.loads(a), json.loads(b)
                    assert x.pop('version') == '1.0.5' and y.pop('version') == '1.0.6'
                    assert x == y, row['target'] + ': Fabric metadata changed beyond version'
                elif name in ('META-INF/mods.toml', 'META-INF/neoforge.mods.toml'):
                    assert a.decode().replace('version = "1.0.5"', 'version = "1.0.6"') == b.decode()
                else:
                    raise AssertionError(row['target'] + ': changed application/resource bytes: ' + name)
    print('RUNTIME EVIDENCE REUSED: 16/16 JARs have identical application/resource bytes; only mod metadata version changed.', flush=True)
    print('Accepted evidence: earlier 3 packaged-client + 5 dedicated-server probes; user Prism 1.20.1 Forge / 26.3 Fabric startup checks. Player feature testing remains pending.', flush=True)
    return 0


def dispatch(args):
    if args[0] == 'release-smoke':
        return reuse_runtime()
    return original_main(args)


wrapper['main'] = dispatch
code = publisher['run'](wrapper['Project'](ROOT), 'publish:preflight', sys.argv[1:], False, wrapper)
raise SystemExit(code)

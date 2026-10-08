# SleepCycle release and conversion acceptance

## Release 1.0.5

The release changes version/publication metadata and documentation only. It keeps
all application source, dependency pins, registry identities and compatibility
mechanisms from conversion commit `06a45d0a05027461921c67c5173ad0f1f6c0e755`.

The single final `publish:preflight` passed: twelve 1.0.5 installable JARs,
twelve artifact checks, four representative packaged-client smokes, and both
platform dry-runs. The collection selects exactly twelve GitHub JAR assets.
All twenty publishing safety tests passed with synthetic publication facts.
The five dedicated-server conversion probes below are reused because no server
source, dependency or compatibility behavior changed. Player gameplay checks
remain pending; no new gameplay validation is claimed.

Publication facts are owned by `gradle/publishing.properties`: Modrinth
`NIRflwRc` / `sleepcycle`, CurseForge `1061863` / `sleepcycle`, and canonical
GitHub repository `Vg34100/MC-SleepCycle`. Both client and server sides are
required. Uploads use target-qualified version numbers, exact-artifact SHA-256
checks, semantic duplicate guards and API receipt-backed sequential publishing.
Older 1.0.4 public files are retained. Local old JARs were moved into an ignored
archive so release selection contains exactly one 1.0.5 JAR for each target.
Upload receipts, manifests, smoke logs and temporary worlds remain untracked.

## Conversion 1.0.4 baseline evidence

Baseline: Minecraft 26.1.2, mod 1.0.4, Fabric and NeoForge at `f5a4c87` on
`feature/26.1.2-migration`. Current target facts live in `gradle/matrix/`.

## Final matrix gates

Run once after the compatibility/runtime fixes:

```text
python build-smart.py matrix:compile
python build-smart.py matrix:package
python scripts/verify-matrix-artifacts.py
```

| Target | Java | Compile | Package | Installable artifact |
| --- | --- | --- | --- | --- |
| 1.21-fabric | 21 | PASS | PASS | PASS |
| 1.21-neoforge | 21 | PASS | PASS | PASS |
| 1.21.1-fabric | 21 | PASS | PASS | PASS |
| 1.21.1-neoforge | 21 | PASS | PASS | PASS |
| 26.1-fabric | 25 | PASS | PASS | PASS |
| 26.1-neoforge | 25 | PASS | PASS | PASS |
| 26.1.1-fabric | 25 | PASS | PASS | PASS |
| 26.1.1-neoforge | 25 | PASS | PASS | PASS |
| 26.1.2-fabric | 25 | PASS | PASS | PASS |
| 26.1.2-neoforge | 25 | PASS | PASS | PASS |
| 26.2-fabric | 25 | PASS | PASS | PASS |
| 26.2-neoforge | 25 | PASS | PASS | PASS |

Artifact checks cover exact filenames/version, one release JAR per target,
Java bytecode, loader-only classes/entrypoints, dependencies/environment,
remapped legacy Fabric classes, active mixins, modern Fabric access wideners,
all sixteen bag recipes/models/textures/item definitions, sound resources,
persisted advancement IDs and valid JSON/TOML. Foreign classes/resources,
embedded dependencies, development/common JARs and stale versions are rejected.
Installable artifacts live under `build/libs/<target>/`.

## Packaged production clients

| Target | Outcome |
| --- | --- |
| 26.2 Fabric | PASS |
| 26.2 NeoForge | PASS |
| 1.21.1 Fabric | PASS |
| 1.21.1 NeoForge | PASS |

The existing `smoke-release-client:<target>` commands stage an exact installable
JAR into a fresh runtime, verify SHA-256 and actual SleepCycle class origin,
observe startup/resource reload and terminate the owned process tree. Legacy
clients additionally establish the initial screen-class initialization marker.
Loom's production client launcher serves Fabric; the seeded PortableMC launcher
serves NeoForge. Ordinary external production dependencies are staged separately.
Evidence remains in ignored runtime `passed.txt` files and wrapper smoke logs.

## Dedicated servers

`python scripts/smoke-sleepcycle-server.py representatives` discovers the four
release-smoke targets. Exact target names allow narrow probes; early
`26.1-neoforge` adds one unique Architectury event-ABI sentinel.

| Target | Outcome |
| --- | --- |
| 26.2 Fabric | PASS |
| 26.2 NeoForge | PASS |
| 1.21.1 Fabric | PASS |
| 1.21.1 NeoForge | PASS |
| 26.1 NeoForge | PASS |

Each development server uses a fresh local world and the target Java launcher.
It reaches ready, reloads data, places a two-part red bag from a temporary
validation structure, verifies both block states and saved block-entity IDs,
flushes the save and shuts down with exit code zero. The early-NeoForge probe
loads the ordinary Architectury 20.0.4 dependency with the scoped descriptor
adapter. No players are simulated. These checks establish server bootstrap,
registry/data compatibility and save writes, not player sleep gameplay or a
full world reopen. Evidence stays in ignored `build/server-smoke/` JSON/logs;
probe worlds and validation datapacks stay under ignored `runs/`.

At conversion acceptance, the twenty publishing safety tests passed with
synthetic publication facts and no external requests. That conversion performed
no publication and retained mod version 1.0.4; the separate release evidence
appears above.

## Manual player regression checklist

Use one modern and one legacy game on each loader where the behavior differs.
These checks remain pending; client startup does not prove them.

- Sleep in a vanilla bed and a bag: verify gradual time passage and the 100-tick
  wind-up, configured speed, wake buttons and day sleeping enabled/disabled.
- Injure a player, sleep across the configured effect thresholds, and check
  regeneration, Well Rested healing/absorption, Tired penalties and both sounds.
  Check the four statistics and four advancement criteria against baseline behavior.
- Craft/place/use/break colored bags; inspect foot/head models and camera position,
  occupied behavior, creative-tab variants and the unchanged no-respawn behavior.
- Change local config/screens, save and restart: confirm file/key persistence,
  tick-speed restoration and experimental server-tick behavior. Open Fabric config
  with Mod Menu and confirm startup without it; open NeoForge's native config UI.
- With two clients, test the sleeping-percentage threshold, timed-wake payloads,
  synchronized time/effects and disconnect/wake handling. Server config is local;
  the baseline has no config synchronization protocol.
- Reopen a saved world containing bags and player effects/stats/advancements;
  verify persistent IDs/data and the intentionally transient sleep/wake timers.

## Local launch notes

Use target Loom `runClient` / `runServer` tasks for development. Both legacy run
tasks use Java 21; current targets use Java 25. Keep `JavaExec.javaLauncher` as the
single toolchain setting, without a conflicting `executable`. On this workstation,
Windows Python runs the wrapper and an explicit local Gradle JDK override selects
the installed stable Java 25; local JDK paths are not committed.

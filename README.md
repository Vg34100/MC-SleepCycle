# Sleep Cycle

Sleep Cycle 1.0.5 makes time pass progressively while players sleep. The existing
sleep duration, healing, effects, advancements, statistics and resources are
preserved across a Stonecutter 0.9.7 matrix.

## Build support

| Minecraft | Loaders | Java |
| --- | --- | --- |
| 1.21, 1.21.1 | Fabric, NeoForge | 21 |
| 26.1, 26.1.1, 26.1.2, 26.2 | Fabric, NeoForge | 25 |

Architectury API is required on both loaders. Fabric also requires Fabric API.
Mod Menu is optional on Fabric and provides access to the config screen;
NeoForge uses its native mod config-screen integration. Choose the JAR matching
both your Minecraft version and loader, with the matching dependency versions.
See the [1.0.5 release notes](docs/wiki/release-notes.md).

## Features

- Progressive time passage with a sleep wind-up and configurable time/tick speed.
- Configurable sleep regeneration, Well Rested buffs and Tired penalties.
- Four timed-wake buttons, sleeping camera adjustment and effect sound cues.
- Four custom sleep advancements and four sleep statistics.
- Sixteen wool-colored sleeping bags with two-part placement. Bags start sleep
  without setting a respawn point.
- Multiplayer sleeping-percentage control and server handling of wake requests.

The config file is `config/sleepcycleconfig-1.0.5.properties`. Config screens edit
local values; server configuration is not automatically synchronized to clients.

## Development and validation

Canonical source is in `common/`, `fabric/` and `neoforge/`; target dependencies
are defined once in `gradle/matrix/*.properties`. Use the existing wrapper:

```text
python build-smart.py matrix:compile
python build-smart.py matrix:package
python scripts/verify-matrix-artifacts.py
python build-smart.py release-smoke
```

Installable JARs are written to `build/libs/<minecraft>-<loader>/`. The build
requires Java 25 for Gradle and Java 21/25 target toolchains.

See [acceptance results and manual gameplay checks](docs/development/testing.md),
[SleepCycle compatibility decisions](docs/development/sleepcycle-compatibility.md)
and the [multiversion playbook](docs/development/multiversion-playbook.md).
Automated startup/artifact/server probes do not establish gameplay correctness.

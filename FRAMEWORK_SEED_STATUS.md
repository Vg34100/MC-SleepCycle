# Framework adaptation status

The standardized Minecraft-Mod-Template framework is adapted to SleepCycle's
Stonecutter 0.9.7 matrix. Canonical source remains under `common/`, `fabric/` and
`neoforge/`; registered targets and dependency pins come from `gradle/matrix/`.

Completed adaptations:

- SleepCycle identity, archive names, package/class origin and both-side metadata.
- Java 21 legacy and Java 25 current toolchains, loader dependencies and resources.
- Installable-JAR checks for all colored bags, mixins, persisted IDs and loader
  entrypoints; no bundled dependencies, common/dev JARs or stale releases.
- Packaged client smoke, isolated server probes and workflow labels/artifact names.
- Publishing helpers supporting common client/server mods and optional Mod Menu.
  Their twenty unit tests use synthetic platform facts and make no real uploads.

Acceptance evidence and the remaining manual gameplay checklist live in
[testing.md](docs/development/testing.md). Compatibility decisions live in
[sleepcycle-compatibility.md](docs/development/sleepcycle-compatibility.md).

This task is conversion only. Mod version remains **1.0.4**. Project IDs,
CurseForge slug and release notes are deliberately unset for a later publication
stage; no tags, releases, uploads or public platform changes are authorized here.

Reusable template corrections to carry back: publishing environment/side checks
must support both-side mods while retaining single-side plans, and test fixtures
must own synthetic publication facts rather than requiring another mod's real project IDs. The SleepCycle
registry/data/save probe is project-specific; the existing client-smoke and smart
wrapper infrastructure remains shared.

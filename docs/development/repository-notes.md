# SleepCycle repository notes

Use the standardized [multiversion playbook](multiversion-playbook.md),
[compatibility policy](compatibility-policy.md) and
[validation guide](validation-and-release.md) for current work. Proven
SleepCycle-specific boundaries live in [sleepcycle-compatibility.md](sleepcycle-compatibility.md).

Useful instructions retained from the previous repository guidance:

- Search current source with `rg` before reading or editing it. Preserve user-owned
  textures, resources and unrelated changes; do not stage them incidentally.
- Inspect the exact vanilla dispatch path before adapting vanilla-derived block,
  sleeping, camera or screen behavior. Start with Minecraft Dev MCP, then narrow
  project/dependency source and JAR inspection.
- Keep loader-native initialization under its loader. Gate NeoForge's config UI
  to the client distribution; shared initialization must survive dedicated servers.
- Use `build-smart.py` for Gradle work. Windows builds can run it through
  `cmd.exe /d /c "python -u build-smart.py ..."`; Linux builds need native Java 21
  and Java 25 toolchains. Avoid mixing native and Windows cache/toolchain paths.
  Keep local JDK paths out of tracked project properties; use wrapper discovery,
  `BUILD_SMART_JAVA_HOME`, or Gradle's toolchain settings.
- Keep gameplay/wiki content in the repository, normally under `docs/wiki/`.
  GitHub wiki or storefront updates require a separately authorized sync; they
  are not a substitute for accurate repository docs.
- Prefer short conventional commit messages and one coherent conversion commit.
  Installable matrix JARs now live under `build/libs/<target>/`; older module
  shadow/raw JARs are not release candidates.

The retained `stonecutter-multiversion-migration.md` and
`stonecutter-port-acceptance-checklist.md` contain historical Fishing Frenzy
architecture examples. Their API-specific transformations and validation claims
are reference material, not SleepCycle implementation or acceptance evidence.
The standardized documents and the current task take priority over their older
full-source generation or exhaustive launch recommendations.

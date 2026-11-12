# SleepCycle compatibility and validation

## Canonical baseline

The conversion starts at `f5a4c87` on `feature/26.1.2-migration`, with mod version
1.0.4 and Minecraft 26.1.2 on Fabric and NeoForge. The older Fabric-only `main`
implementation is not the behavior reference. An initial `shadowJar` build of
both existing loader modules passed against the current source. Earlier JARs
predated the colored sleeping-bag commit and were not accepted as current evidence.

Actual production dependencies are Architectury on both loaders and Fabric API
on Fabric. Mod Menu is an optional Fabric config-screen integration. The baseline
pins are Architectury 20.0.7, Fabric Loader 0.19.3, Fabric API 0.153.0+26.1.2,
NeoForge 26.1.2.76 and Mod Menu 18.0.0-beta.1. Other targets own their pins in
`gradle/matrix/`; no optional test mods are bundled or required.

## Preserved behavior and storage

The current source implements time advancement with a 100-tick wind-up, the
players-sleeping-percentage threshold, random-tick acceleration/restoration,
configurable regeneration and sleep buffs, the experimental extra server-tick
setting, four timed-wake buttons, sleeping camera adjustment, four custom
statistics, four advancements, sounds, and 16 wool-colored sleeping bags.

Bags use two-part bed placement and JSON block/item models. Their interaction
intentionally starts sleep directly, without setting a respawn point. There is
no custom renderer. The `sleepcycle:sleeping_bag` block-entity type serves the 16
`sleepcycle:sleeping_bag_<color>` blocks. Existing dormant unregistered mixins and
unused legacy assets are not activated by the conversion.

Sleep duration/wake targets and world bookkeeping are transient maps. Effects,
statistics, advancements and sleeping-bag block entities use vanilla saved data.
The config filename stays `sleepcycleconfig-1.0.4.properties`, with the same keys
and defaults. The network payload stays `sleepcycle:wake_at`, carrying a VAR_LONG
and handling the wake target on the server thread. Configuration screens edit
local config; there is no existing server-config synchronization protocol.

The unusual persisted trigger `sleepcycle:tutorialmod.sleep` is retained. The
conversion does not rename registries or change existing statistics accounting,
regeneration counters, threshold math or experimental time acceleration.

## Proven boundaries

| Boundary | Affected targets | Mechanism |
| --- | --- | --- |
| Identifier / ResourceLocation | 1.21.x | Exact bidirectional Stonecutter class-name replacement |
| World clocks and typed gamerules | 1.21.x vs 26.x | Small local day/time and gamerule accessors; legacy advances the overworld day |
| Day-sleep checks | 1.21.x vs 26.x | Legacy loader-native sleep-time events; modern `BedRule.canSleep` redirects |
| Camera setup | 1.21.x vs 26.x | Local injection signature: `setup` versus `alignWithEntity` |
| Block entity construction | 1.21.x vs 26.x | Vanilla Builder versus modern constructor; Fabric-only modern widener |
| Vanilla bed loses block entities | 26.2 | Sleeping bags explicitly implement EntityBlock to retain saved block entities |
| Registry holders / effect tick arguments / property IDs | 1.21.x vs 26.x | Source-local conditions; keep all mod registry IDs |
| Advancement packages | 1.21.x / 26.1.x / 26.2 | Local import conditions; shared trigger codec and behavior |
| Screen transitions | 26.2 | Local calls through `minecraft.gui` |
| RegistrySupplier no longer implements Holder | Architectury 21 / 26.2 | Shared supplier `get()` calls |
| Recipe ingredients / item definitions | 1.21.x | Parsed JSON ingredient transform; omit modern item definitions |
| NeoForge config UI | Both generations | Client helper called only when injected Dist is CLIENT |
| Architectury break-event descriptor | 26.1 / 26.1.1 NeoForge | One NeoForge-only Mixin plugin remaps one external handler's event type |

Fabric API 1.8.0+2b27e0a419 already redirects legacy Player/ServerPlayer
`Level.isDay` checks. A competing SleepCycle redirect caused an actual production
InjectionError. Legacy Fabric now registers `ALLOW_SLEEP_TIME`, returning SUCCESS
when day sleeping is enabled and PASS otherwise; the same event covers sleep
entry and ongoing wake checks. NeoForge keeps its native CanPlayerSleep /
CanContinueSleeping events. The two BedRule mixins and their classes exist only
on modern targets, with parsed legacy mixin-config filtering.

The final published 26.1 and 26.1.1 NeoForge loaders expose
`BlockEvent.BreakEvent`. Architectury 20.0.4 instead references the later flat
`event.level.block.BreakBlockEvent`; 20.0.2 has the same mismatch, and newer
Architectury releases requiring 26.1.2 cannot solve it with a dependency pin.
The independently implemented early-NeoForge plugin changes only the single
Architectury `event(BreakBlockEvent)` handler's descriptor and matching bytecode
references to the old event. Both APIs have the same getters and cancellation
method. It requires exactly one matching handler and is excluded from every
other target. Architectury remains an ordinary external dependency; no foreign
classes or patched dependency JARs are bundled. Reassess this adapter when the
dependency pins change.

The old Architectury Gradle plugin's `arch$tab` call is replaced by
CreativeTabRegistry registration of the same items in the same functional-blocks
tab. Matrix nodes already compile common plus loader code together, so they need
Architectury API, without the old common-module transform/shadow packaging.

## Publication boundary

The conversion commit keeps mod version 1.0.4. The separately authorized 1.0.5
release changes version/publication facts and documentation, retaining all source,
registry IDs, dependency pins and loader compatibility decisions. Public project
IDs, display-name convention and source links are verified before upload.
The publisher uses both client/server metadata and only actual required/optional
dependencies. Its twenty unit tests use synthetic facts and no external requests.
The five conversion server probes remain accepted for this metadata-only release;
player gameplay remains on the manual checklist.

## Validation

Final results and remaining manual gameplay checks are recorded in
[testing.md](testing.md). Compilation, JAR inspection and title-screen smoke do
not establish gameplay correctness.

## 1.20.1 / 26.3 port (unreleased source)

The 16-target matrix retains the existing twelve pins and mod version 1.0.5.
New dependency pins and native Forge setup follow the completed Stonecutter
starter and Fishing Frenzy port. Java 17 is used only for 1.20.1; Java 21/25
continues on the existing targets. No application source is generated by Gradle.

- `ModIds` owns the 1.20.1 identifier constructor; `ModProperties` owns modern
  pre-construction registry keys. The existing combined item/block registration
  and all 16 bag identifiers remain intact.
- 1.20.1 uses raw Architectury C2S buffers instead of typed payloads. The channel
  `sleepcycle:wake_at` and VAR_LONG encoding are unchanged. Decode occurs before
  queuing server work, and common registration is guarded against duplicate calls.
  No S2C config protocol or new server authority is introduced.
- Forge registers its event bus before common deferred registries. Its native
  SleepingTimeCheckEvent handles daytime sleep. Config-screen registration is in
  a physical-client event subscriber; common bootstrap contains no client types.
- 1.20.1 uses the old bed `use` signature, double camera movement/zoom, direct
  MobEffect instances, UUID attribute modifiers, and pre-codec advancement
  triggers. A tiny invoker registers the unchanged `sleepcycle:tutorialmod.sleep`
  trigger. Its missing mining-speed attribute is represented by a 30% Tired
  penalty at Player.getDestroySpeed; Well Rested uses the legacy absorption
  lifecycle while retaining its healing interval and four absorption points.
- 26.3 moves vanilla interaction into AbstractBedBlock, so the bed-use mixin
  targets that declaring class. BedRule.destroyOnUse, ClockInstance.totalTicks
  and Optional<Holder<LootItemCondition>> replace the prior API shapes. Existing
  day-sleep redirects still target real BedRule.canSleep calls in Player.tick
  and ServerPlayer.startSleepInBed, verified against Minecraft Dev source.
- Parsed 1.20.1 resources use plural recipe/advancement directories, item-shaped
  results/icons and legacy predicates, including nested recipe advancements.
  Modern item definitions are excluded. 26.3 recipe-unlock conditions use the
  new recipes holder set, following the proven Fishing Frenzy transform. Forge metadata and mixin manifest are
  separate from NeoForge; 26.3 uses its pinned FML major and iconFile field.
- The existing config implementation is retained. UTF-8 input is closed after
  reading; provider defaults supply missing keys without recreating a user's
  file or dropping unknown keys/comments. The existing versioned filename rule,
  13 defaults and parsing/settings semantics are unchanged. Historical 1.0.4 and
  current 1.0.5 filenames are tested independently; no automatic file renaming
  or version migration is introduced. The earlier baseline paragraph describing
  a fixed 1.0.4 filename is historical: current code derives it from mod version.
- A small shared OptionsSubScreen base supplies only the missing 1.20.1 list,
  Done button and rendering lifecycle. Existing settings controls and immediate
  local edits/save behavior are retained; there is no new draft/cancel policy.

The production-smoke helper now accepts Forge using the template's PortableMC
route and stages published parent dependency JARs without transitive development
modules. Publication helpers/IDs and historical release assets are unchanged. The separately authorized 1.0.6 release adds Forge manifest support using the
completed suite implementation: mandatory dependency flags, Forge loader labels
and META-INF/mods.toml inspection. GitHub builds include Java 17 and the proven
root ASM 9.11 pin. Publication identities and dependency relationships are retained.
See testing.md for actual checks and remaining player gameplay work.

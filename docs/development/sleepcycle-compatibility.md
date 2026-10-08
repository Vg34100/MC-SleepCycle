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

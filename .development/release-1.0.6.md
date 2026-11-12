# SleepCycle 1.0.6 release acceptance

The user authorized Modrinth, CurseForge and GitHub publication of the completed
16-target port. The previous twelve public 1.0.5 nodes remain untouched.
Only the supplied two bullets and parenthetical sentence form the new release
changelog; no generated summary or feature claim is appended.

The prior 16/16 compile/package/artifact gate, three packaged client checks, five
dedicated-server launches and config round trips remain the implementation
baseline. The user additionally confirmed startup in Prism for 1.20.1 Forge and
26.3 Fabric and accepted that per-feature gameplay testing remains incomplete.

For 1.0.6, the release-specific preflight packages all sixteen targets, verifies
artifacts, compares every class/resource to frozen accepted 1.0.5 JARs (allowing
only loader-metadata version changes), then executes both official platform dry
runs. This explicit runtime-evidence reuse avoids repeating client launches.
The frozen baseline and manifests/receipts live only in ignored build output.

Forge publication metadata uses the completed sibling implementation: native
mandatory dependency fields, mods.toml selection, Forge platform labels and
semantic duplicate detection. Twenty-one publishing safety tests pass. CI uses
Java 17/21/25 and the template's root ASM 9.11 classloader pin. Public IDs and actual
required/optional dependency relationships are unchanged.

Stay on work. Commit/push the release revision, allow the standard public snapshot
workflow to generate main, validate that snapshot's source against work, complete
both external-platform uploads, and tag the exact generated public snapshot.
The tag workflow publishes only the sixteen installable GitHub JARs; it does not
re-upload to Modrinth or CurseForge. No existing public tag/release is rewritten.

Packaging and all 16 artifact checks passed from a clean temporary workspace on
C: after the ignored A: build-output directory reported filesystem corruption.
Every packaged class/resource matched the frozen accepted baseline; only loader
metadata changed from 1.0.5 to 1.0.6. No application source changed for this fix.

Final release preflight passed: 16/16 package/artifact checks, accepted runtime
evidence reused, and 16/16 dry runs on each platform. No extra client launches.

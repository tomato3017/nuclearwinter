# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Nuclear Winter is a Minecraft Forge mod (1.20.1, Forge 47.3.0, Java 17) that implements a post-apocalyptic radiation system. Players survive nuclear winter by managing radiation exposure and sheltering underground. The mod is in early development (v0.1.0).

## Build Commands

```bash
./gradlew build              # Build the mod JAR
./gradlew runClient          # Run Minecraft client with the mod loaded
./gradlew runServer          # Run dedicated server with the mod loaded
./gradlew runData            # Run data generation
./gradlew runGameTestServer  # Run game tests
./gradlew clean              # Clean build artifacts
```

IDE setup: `./gradlew genIntellijRuns` or `./gradlew genEclipseRuns`

No unit test suite exists yet. Testing is done via `runClient`/`runServer` and the `/nuclearwinter` in-game command.

## Architecture

### Entry Point
`NuclearWinter.java` (`@Mod("nuclearwinter")`) — registers event buses, loads config, registers commands, loads radiation settings on server start.

### Core Systems

**Staging System** (`staging/`) — Drives world progression through 5 phases:
`PREAPOC(0) → APOCLOW(1) → APOCMED(2) → APOCHIGH(3) → POSTAPOC(4)`

- `StageController` manages active stages per dimension, handles lifecycle (load/tick/unload/finalize) and timed transitions
- `StageBase` is the abstract base class; concrete stages override `doStageTick()`, `doPlayerTick()`, `initStage()`, `finalizeStage()`
- Each stage has `StageSettings` (radiation level, player radiation flag) built via builder pattern
- APOCMED, APOCHIGH, POSTAPOC are currently placeholders

**Radiation System** (`radiation/`) — Simulates radiation propagating from sky to ground:
- `RadiationSource.emitRadiation()` raycasts from sky to player, reducing radiation by per-block resistance; can degrade blocks to air
- `RadBlockRegistry` loads block resistance configs from JSON files in `config/nuclearwinter/radsettings/` — supports exact block IDs, tags (`#minecraft:logs`), and wildcards (`minecraft:*_ore`)
- `RadiationConfig` holds static constants (max radiation, light thresholds)
- `RadiationSettings` configures per-stage behavior (player affected, block degradation, light degradation)

**Capability System** (`capabilities/`, plus attachers in each package) — Forge capabilities attach persistent data:
- `IRadiationReceiver` / `RadiationReceiverImplementer` — player radiation level (serialized to NBT)
- `IStageLevelSettings` / `StageLevelSettingsImplementer` — per-world stage state
- `CapabiltiesAttacher` auto-attaches capabilities on entity/level creation

### Conventions

- **Capability pattern**: Interface (`I*.java`) → Implementer (`*Implementer.java`) → Attacher (`*Attacher.java`) → Registration (`*.java`)
- **Event handling**: `@SubscribeEvent` annotations; mod bus for registration events, forge bus for gameplay events
- **Logging**: `NuclearWinter.LOGGER` (SLF4J); debug logging gated by `Config.DEBUG_LOGGING`
- **Config**: Forge config spec in `Config.java` (`DAYS_BEFORE_APOCALYPSE`, `DAYS_APOCALYPSE_LOW`, `DEBUG_LOGGING`)
- **Commands**: Brigadier-based `/nuclearwinter` with subcommands `start`, `stop`, `status`, `setstage`
- **Builder pattern** used for `StageSettings`
- Base package: `net.tomatonet.nuclearwinter`

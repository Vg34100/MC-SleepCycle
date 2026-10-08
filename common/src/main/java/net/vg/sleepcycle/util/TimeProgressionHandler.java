package net.vg.sleepcycle.util;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
//? if >=26.1 {
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.level.gamerules.GameRules;
//? } else {
/*import net.minecraft.world.level.GameRules;
*///? }
import net.minecraft.world.level.chunk.LevelChunk;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.advancement.ModCriteria;
import net.vg.sleepcycle.config.ModConfigs;
import net.vg.sleepcycle.effect.ModEffects;
import net.vg.sleepcycle.sounds.ModSounds;
import net.vg.sleepcycle.stats.ModStats;

import java.util.*;

public class TimeProgressionHandler {
    private static final Map<ServerLevel, Integer> worldSleepTicks = new HashMap<>();
    private static final Map<ServerPlayer, Integer> playerSleepTicks = new HashMap<>();
    private static final Set<ServerPlayer> sleepingPlayers = new HashSet<>();
    private static final Map<UUID, Long> playerWakeTargets = new HashMap<>();
    private static Integer originalTickSpeed = null;
    private static final int SLEEP_ADVANCEMENT_DURATION = 6000; // 5 minutes in ticks (20 ticks * 60 seconds * 5 minutes) = 6000

    public static void register() {
        TickEvent.SERVER_LEVEL_POST.register(TimeProgressionHandler::onWorldTick);
        PlayerEvent.PLAYER_QUIT.register(TimeProgressionHandler::onPlayerDisconnect);
//        LifecycleEvent.SERVER_LEVEL_LOAD.register(TimeProgressionHandler::onWorldLoad);
    }

    public static void addWorld(ServerLevel world) {
        Constants.LOGGER.info("World being Added");
        if (!worldSleepTicks.containsKey(world)) {
            originalTickSpeed = randomTickSpeed(world);
        }
        worldSleepTicks.putIfAbsent(world, 0);
    }

    public static void setWakeTarget(ServerPlayer player, long targetDayTime) {
        long totalTicks = dayTicks(player.level());
        long currentDayTime = totalTicks % 24000;
        long ticksUntilTarget = (targetDayTime - currentDayTime + 24000) % 24000;
        if (ticksUntilTarget == 0) ticksUntilTarget = 24000;
        playerWakeTargets.put(player.getUUID(), totalTicks + ticksUntilTarget);
    }

    public static void removeWorld(ServerLevel world) {
        worldSleepTicks.remove(world);
        // Reset the tick speed
        if (originalTickSpeed != null) {
            setRandomTickSpeed(world, originalTickSpeed);
        }
    }

    private static void onWorldTick(ServerLevel world) {
        if (!worldSleepTicks.containsKey(world)) {
            if (world.players().stream().anyMatch(ServerPlayer::isSleeping)) {
                addWorld(world);
            }
        }

        if (worldSleepTicks.containsKey(world)) {
            List<ServerPlayer> players = world.players();
            int playerCount = players.size();

            double playersRequiredToSleepRatio = sleepingPercentage(world) / 100d;
            int playersRequiredToSleep = (int) Math.ceil(playersRequiredToSleepRatio * playerCount);
            long sleepingPlayerCount = players.stream().filter(ServerPlayer::isSleeping).count();

            for (ServerPlayer player : players) {
                if (player.isSleeping()) {
                    sleepingPlayers.add(player);
                    playerSleepTicks.putIfAbsent(player, 0);
                    if (ModConfigs.DO_REGEN) {
                        if (!player.hasEffect(MobEffects.REGENERATION)) {
                            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0)); // 2 minutes
                            if (player.getHealth() != player.getMaxHealth()) {
                                player.awardStat(ModStats.HEALTH_REGAINED.get(), 1);
                            }
                        }
                    }

                    int playerTicksAsleep = playerSleepTicks.get(player);
                    if (ModConfigs.GRANT_BUFFS) {
                        if (!player.hasEffect(ModEffects.getWellRestedHolder()) && playerTicksAsleep >= ModConfigs.WELL_RESTED_WAIT && playerTicksAsleep < ModConfigs.TIRED_WAIT) {
                            player.addEffect(new MobEffectInstance(ModEffects.getWellRestedHolder(), ModConfigs.WELL_RESTED_LENGTH, 0), player);
                            playSound(player, ModSounds.WELL_RESTED_SOUND.get());
                        } else if (!player.hasEffect(ModEffects.getTiredHolder()) && playerTicksAsleep >= ModConfigs.TIRED_WAIT) {
                            player.addEffect(new MobEffectInstance(ModEffects.getTiredHolder(), ModConfigs.TIRED_LENGTH, 0), player);
                            playSound(player, ModSounds.TIRED_SOUND.get());
                        }



                    }

                    // Check for advancement condition
                    if (playerTicksAsleep >= SLEEP_ADVANCEMENT_DURATION) {
                        ModCriteria.getSleepCriterion().trigger(player); // Trigger the custom criterion
                    }

                    playerSleepTicks.put(player, playerTicksAsleep + 1); // Increment player sleep ticks
                    player.awardStat(ModStats.TIME_SLEPT.get(), 1);

                    // Wake player when their chosen target time is reached
                    if (playerWakeTargets.containsKey(player.getUUID())) {
                        long currentTotalTicks = dayTicks(world);
                        if (currentTotalTicks >= playerWakeTargets.get(player.getUUID())) {
                            player.stopSleepInBed(false, true);
                            playerWakeTargets.remove(player.getUUID());
                        }
                    }

                } else {
                    // Reset player's sleep ticks if they are not sleeping
                    if (player.hasEffect(ModEffects.getWellRestedHolder())) {
                        player.awardStat(ModStats.WELL_RESTED_SLEEPS.get(), 1);
                    }
                    if (player.hasEffect(ModEffects.getTiredHolder())) {
                        player.awardStat(ModStats.TIRED_SLEEPS.get(), 1);
                    }
                    playerSleepTicks.remove(player);
                    sleepingPlayers.remove(player);
                    playerWakeTargets.remove(player.getUUID());
                }
            }

            if (sleepingPlayerCount >= playersRequiredToSleep) {
                if (ModConfigs.CHANGE_TICK_SPEED && sleepingPlayerCount > 0) {
                    if (originalTickSpeed != null && originalTickSpeed.equals(randomTickSpeed(world))) {
                        setRandomTickSpeed(world, (int) (originalTickSpeed * ModConfigs.DAY_SKIP_SPEED * ModConfigs.SLEEP_TICK_MULTIPLIER));
                        System.out.println("Increasing world tick speed");
                    }
                }

                int ticksAsleep = worldSleepTicks.get(world);
                long timeIncrement = calculateTimeIncrement(ticksAsleep);

                advanceTime(world, timeIncrement);

                if (ticksAsleep % 20 == 0) { // Tick chunks less frequently
                    tickChunks(world);
                }

                worldSleepTicks.put(world, ticksAsleep + 1); // Increment sleep ticks
            } else {
                if (originalTickSpeed != null) {
                    setRandomTickSpeed(world, originalTickSpeed);
                }
                if (sleepingPlayerCount == 0) {
                    removeWorld(world);
                }
            }
        }
    }


    // Minecraft 26 uses the shared overworld clock and typed gamerules.
    // Legacy levels expose the same day through level data and GameRules values.
    private static long dayTicks(Level world) {
        //? if >=26.1 {
        var clock = world.registryAccess().lookupOrThrow(Registries.WORLD_CLOCK).getOrThrow(WorldClocks.OVERWORLD);
        return world.clockManager().getTotalTicks(clock);
        //? } else {
        /*return world.getDayTime();
        *///? }
    }

    private static void advanceTime(ServerLevel world, long increment) {
        //? if >=26.1 {
        var clock = world.registryAccess().lookupOrThrow(Registries.WORLD_CLOCK).getOrThrow(WorldClocks.OVERWORLD);
        world.clockManager().addTicks(clock, (int) increment);
        //? } else {
        /*world.getServer().overworld().setDayTime(dayTicks(world) + increment);
        *///? }
    }

    private static int randomTickSpeed(ServerLevel world) {
        //? if >=26.1 {
        return world.getGameRules().get(GameRules.RANDOM_TICK_SPEED);
        //? } else {
        /*return world.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        *///? }
    }

    private static void setRandomTickSpeed(ServerLevel world, int value) {
        //? if >=26.1 {
        world.getGameRules().set(GameRules.RANDOM_TICK_SPEED, value, world.getServer());
        //? } else {
        /*world.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).set(value, world.getServer());
        *///? }
    }

    private static int sleepingPercentage(ServerLevel world) {
        //? if >=26.1 {
        return world.getServer().getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
        //? } else {
        /*return world.getServer().getGameRules().getInt(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE);
        *///? }
    }

    private static long calculateTimeIncrement(int ticksAsleep) {
        // Calculate the time increment with a wind-up effect
        return (long) (ModConfigs.DAY_SKIP_SPEED * Math.min(1, ticksAsleep / 100.0));
    }

    private static void playSound(ServerPlayer player, SoundEvent soundId) {
        player.level().playSound(
                null, // Player that hears the sound
                player.blockPosition(), // Position of the sound
                soundId,
                SoundSource.NEUTRAL, // This determines which slider affects this sound
                1.0F, // Volume multiplier, 1 is normal, 0.5 is half volume, etc
                1.0F // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
        );
    }

    private static void tickChunks(ServerLevel world) {
        ServerChunkCache chunkManager = world.getChunkSource();
        chunkManager.tick(() -> true, true);
    }

    private static void onPlayerDisconnect(ServerPlayer player) {
        System.out.println("SleepCycle: on player disconnect");

        for (ServerLevel world : player.level().getServer().getAllLevels()) {
            List<ServerPlayer> players = world.players();
            long sleepingPlayerCount = players.stream().filter(ServerPlayer::isSleeping).count();

            if (sleepingPlayers.contains(player)) {
                playerSleepTicks.remove(player);
                sleepingPlayers.remove(player);
                playerWakeTargets.remove(player.getUUID());

                removeWorld(world);
                System.out.println("Removing the World");
            }

            if (originalTickSpeed != null) {
                setRandomTickSpeed(world, originalTickSpeed);
                System.out.println("Resetting world tick speed");
            }
        }
    }

}

package net.vg.sleepcycle.config;

import com.mojang.datafixers.util.Pair;
import net.vg.sleepcycle.Constants;

import java.util.Arrays;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static boolean ALLOW_DAY_SLEEPING;
    public static boolean CHANGE_TICK_SPEED;
    public static boolean CHANGE_CAMERA_POS;
    public static double DAY_SKIP_SPEED;
    public static double SLEEP_TICK_MULTIPLIER;

    public static boolean GRANT_BUFFS;
    public static boolean DO_REGEN;
    public static int SLEEP_BUTTON_HEIGHT;

    public static int WELL_RESTED_WAIT;
    public static int TIRED_WAIT;
    public static int WELL_RESTED_LENGTH;
    public static int TIRED_LENGTH;

    // experimental
    public static int TICKS_PER_TICK;


    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(Constants.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("sleep.allow.day.sleep", true), Arrays.asList(
                "Enables or disables the player's ability to sleep during the day.",
                "Default: true"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.day.skip.speed", 60.0d), Arrays.asList(
                "Number of ticks the time of day advances per tick while sleeping.",
                "General Examples of day.skip.speed Settings (approximate*):",
                "skip speed = 0 -> Normal Minecraft day (20 minutes)",
                "skip speed = 20 -> 1 full Minecraft day (24,000 ticks) in 1 real-life minute (60 seconds)",
                "skip speed = 50 -> 5,000 ticks of Minecraft time fit in 5 real-life seconds",
                "skip speed = 100 -> 6 hours (6,000 ticks) in 10 real-life seconds",
                "skip speed = 168 -> 1 Minecraft week (168,000 ticks) in 1 real-life hour (3,600 seconds)",
                "skip speed = 600 -> 12 hours (12,000 ticks) in 1 real-life second",
                "These speeds are approximations and also do not consider the wind up effect of falling sleep.",
                "Default: 60.0 -> night to day transition (13000 to 1000) in 10 real-life seconds (~12 seconds with wind up)"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.sleep.tick.multiplier", 1.0d), Arrays.asList(
                "Multiplier for if the random tick speed should be further increased while sleeping.",
                "Default: 1.0"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.well.rested.wait", 240), Arrays.asList(
                "Time to wait before the 'Well Rested' status is applied during sleep.",
                "Default: 240"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.tired.wait", 350), Arrays.asList(
                "Time to wait before the 'Tired' status is applied during sleep.",
                "Default: 350"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.well.rested.length", 2400), Arrays.asList(
                "Duration of the 'Well Rested' status from sleeping.",
                "Default: 2400"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.tired.length", 600), Arrays.asList(
                "Duration of the 'Tired' status from sleeping.",
                "Default: 600"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.grant.buffs", true), Arrays.asList(
                "Enables or disables buffs to players during sleeping.",
                "Default: true"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.do.regeneration", true), Arrays.asList(
                "Enables or disables health regeneration while sleeping.",
                "Default: true"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.change.tick.speed", true), Arrays.asList(
                "Enables or disables the increased tick speed while sleeping.",
                "Default: true"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.sleep.button.height", 30), Arrays.asList(
                "Adjusts the height of the 'Leave Bed' button when laying in bed.",
                "Client-side setting.",
                "Default: 30"
        ));
        configs.addKeyValuePair(new Pair<>("sleep.change.camera.pos", true), Arrays.asList(
                "Enables or disables the adjusted camera position when laying in bed.",
                "Client-side setting.",
                "Default: true"
        ));

        // Experimental
        configs.addKeyValuePair(new Pair<>("sleep.tick.per.tick", 1), Arrays.asList(
                "*Experimental Settings*",
                "Defines how many ticks to tick the server every tick.",
                "Ex) A value of 2 will make the server tick twice for every normal tick.",
                "Affectively doubles the speed of a Minecraft day.",
                "Could become the future new method for the mod, but currently haveTime() limits how fast it can go.",
                "Default: 1"
        ));
    }

    private static void assignConfigs() {
        // Server Settings
        // Boolean
        ALLOW_DAY_SLEEPING = CONFIG.getOrDefault("sleep.allow.day.sleep", true);
        GRANT_BUFFS = CONFIG.getOrDefault("sleep.grant.buffs", true);
        DO_REGEN = CONFIG.getOrDefault("sleep.do.regeneration", true);
        CHANGE_TICK_SPEED = CONFIG.getOrDefault("sleep.change.tick.speed", true);
        // Integer or Double
        WELL_RESTED_WAIT = CONFIG.getOrDefault("sleep.well.rested.wait", 240);
        TIRED_WAIT = CONFIG.getOrDefault("sleep.tired.wait", 350);
        WELL_RESTED_LENGTH = CONFIG.getOrDefault("sleep.well.rested.length", 2400);
        TIRED_LENGTH = CONFIG.getOrDefault("sleep.tired.length", 600);
        // Double
        DAY_SKIP_SPEED = CONFIG.getOrDefault("sleep.day.skip.speed", 60.0d);
        SLEEP_TICK_MULTIPLIER = CONFIG.getOrDefault("sleep.sleep.tick.multiplier", 1.0d);


        // Client Settings
        CHANGE_CAMERA_POS = CONFIG.getOrDefault("sleep.change.camera.pos", true);
        SLEEP_BUTTON_HEIGHT = CONFIG.getOrDefault("sleep.sleep.button.height", 30);

        // Experimental
        TICKS_PER_TICK = CONFIG.getOrDefault("sleep.tick.per.tick", 1);

        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }

    public static void saveConfigs() {
        CONFIG.set("sleep.allow.day.sleep", ALLOW_DAY_SLEEPING);
        CONFIG.set("sleep.day.skip.speed", DAY_SKIP_SPEED);
        CONFIG.set("sleep.sleep.tick.multiplier", SLEEP_TICK_MULTIPLIER);
        CONFIG.set("sleep.well.rested.wait", WELL_RESTED_WAIT);
        CONFIG.set("sleep.tired.wait", TIRED_WAIT);
        CONFIG.set("sleep.well.rested.length", WELL_RESTED_LENGTH);
        CONFIG.set("sleep.tired.length", TIRED_LENGTH);
        CONFIG.set("sleep.grant.buffs", GRANT_BUFFS);
        CONFIG.set("sleep.do.regeneration", DO_REGEN);
        CONFIG.set("sleep.change.tick.speed", CHANGE_TICK_SPEED);

        CONFIG.set("sleep.tick.per.tick", TICKS_PER_TICK);


        CONFIG.set("sleep.sleep.button.height", SLEEP_BUTTON_HEIGHT);
        CONFIG.set("sleep.change.camera.pos", CHANGE_CAMERA_POS);


        CONFIG.save();
    }
}
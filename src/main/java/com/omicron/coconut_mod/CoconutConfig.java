package com.omicron.coconut_mod;

import net.minecraftforge.common.ForgeConfigSpec;

public class CoconutConfig {
    public static ForgeConfigSpec spec;

    public static ForgeConfigSpec.IntValue DAMAGE;

    public static ForgeConfigSpec.IntValue COOLDOWN;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Settings").push("coconuts");

        DAMAGE = builder.comment("coconut damage").defineInRange("damage", 5, 0, 1000);

        COOLDOWN = builder.comment("coconut cooldown in ticks").defineInRange("cooldown", 15, 1, 1000);

        builder.pop();
        spec = builder.build();

    }
}

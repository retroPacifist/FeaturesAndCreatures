package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.platform.services.FnCIConfigHelper;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FnCFabricConfigHelper extends MidnightConfig implements FnCIConfigHelper {

    @MidnightConfig.Entry
    public static double jockeySpawnChance = 0.5;
    @MidnightConfig.Entry
    public static boolean namedJockeyDespawn = true;
    @MidnightConfig.Entry
    public static List<String> jockeyEffectBlacklist = FnCIConfigHelper.defaultBlacklistedEffects();

    @MidnightConfig.Entry
    public static int jockeyDespawnTime = JOCKEY_DEFAULT_ALIVE_TIME;

    @MidnightConfig.Entry
    public static int jockeyCooldownTime = JOCKEY_DEFAULT_ALIVE_TIME;


    /**
     * Jockey spawn chance. Clamped between 0 and 1. Default value is 0.5
     * @return A double representing the chance for the jockey to spawn
     */
    @Override
    public double getJockeySpawnChance(){
        return Mth.clamp(jockeySpawnChance, 0, 1);
    }

    /**
     * Whether named jockeys should still despawn.
     *
     * @return Whether named jockeys should despawn.
     */
    @Override
    public boolean namedJockeyDespawn(){
        return namedJockeyDespawn;
    }

    /**
     * Serialized as a List<String> mapped with ResourceLocation::new and Registry#get
     * Contains Purity by default
     *
     * @return A list of mob effects that are blacklisted from appearing in the Jockey's shop
     */
    @Override
    public Set<MobEffect> jockeyEffectBlacklist(){
        try {
            return jockeyEffectBlacklist.stream().map(ResourceLocation::new).map(BuiltInRegistries.MOB_EFFECT::get).collect(Collectors.toSet());
        }
        catch (ResourceLocationException exception) {
            FnCConstants.LOG.warn("Invalid resourcelocation caught in FeaturesAndCreatures Config", exception);
            return Set.of();
        }
    }

    /**
     * The amount of in game time it takes the Jockey to despawn when completely loaded.
     * The default amount is 3 in game days (3 * 20 * 60 * 20), since a day is 20 mins
     * Clamped between 0 and max integer.
     *
     * @return The amount of time a jockey is around for
     */
    @Override
    public int jockeyDespawnTime(){
        return Mth.clamp(jockeyDespawnTime, 0, Integer.MAX_VALUE);
    }

    /**
     * The amount of time the game takes between attempting to even spawn a jockey.
     * Resets whenever there is a failed attempt.
     * Clamped between 0 and max integer.
     *
     * @return The amount of time the game waits before attempting to spawn a jockey.
     */
    @Override
    public int jockeySpawningCooldown(){
        return Mth.clamp(jockeyCooldownTime, 0, Integer.MAX_VALUE);
    }
}

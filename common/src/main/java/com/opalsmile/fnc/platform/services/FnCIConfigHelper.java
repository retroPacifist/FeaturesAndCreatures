package com.opalsmile.fnc.platform.services;

import net.minecraft.world.effect.MobEffect;

import java.util.Set;

public interface FnCIConfigHelper {

    //Spawning cooldown : 3 hours
    // 3 * 1 hour * to ticks conversion
    int JOCKEY_DEFAULT_COOLDOWN = 3 * (60 * 60) * 20;

    //Alive time: 1 hour or 3 in game days
    // 3 * 1 in game day * to ticks
    int JOCKEY_DEFAULT_ALIVE_TIME = 3 * (20 * 60) * 20;

    /**
     * Jockey spawn chance. Clamped between 0 and 1. Default value is 0.5
     * @return A double representing the chance for the jockey to spawn
     */
    double getJockeySpawnChance();

    /**
     * Whether named jockeys should still despawn.
     * Default: true
     * @return Whether named jockeys should despawn.
     */

    boolean namedJockeyDespawn();

    /**
     * Serialized as a List<String> mapped with ResourceLocation::new and Registry#get
     * Contains Purity by default
     *
     * @return A list of mob effects that are blacklisted from appearing in the Jockey's shop
     */
    Set<MobEffect> jockeyEffectBlacklist();

    /**
     * The amount of in game time it takes the Jockey to despawn when completely loaded.
     * The default amount is 3 in game days (3 * 20 * 60 * 20), since a day is 20 mins
     * Clamped between 0 and max integer.
     * @return The amount of time a jockey is around for
     */
    int jockeyDespawnTime();

    /**
     * The amount of time the game takes between attempting to even spawn a jockey.
     * Resets whenever there is a failed attempt.
     * Clamped between 0 and max integer.
     * @return The amount of time the game waits before attempting to spawn a jockey.
     */
    int jockeySpawningCooldown();
}

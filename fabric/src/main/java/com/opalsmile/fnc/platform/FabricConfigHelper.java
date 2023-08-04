package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.platform.services.IConfigHelper;
import net.minecraft.world.effect.MobEffect;

import java.util.Set;

public class FabricConfigHelper implements IConfigHelper {

    /**
     * Jockey spawn chance. Clamped between 0 and 1. Default value is 0.5
     * @return A double representing the chance for the jockey to spawn
     */
    @Override
    public double getJockeySpawnChance(){
        return 0.5;
    }

    /**
     * Whether named jockeys should still despawn.
     *
     * @return Whether named jockeys should despawn.
     */
    @Override
    public boolean namedJockeyDespawn(){
        return true;
    }

    /**
     * Serialized as a List<String> mapped with ResourceLocation::new and Registry#get
     * Contains Purity by default
     *
     * @return A list of mob effects that are blacklisted from appearing in the Jockey's shop
     */
    @Override
    public Set<MobEffect> jockeyEffectBlacklist(){
        return Set.of();
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
        return JOCKEY_DEFAULT_ALIVE_TIME;
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
        return JOCKEY_DEFAULT_COOLDOWN;
    }
}

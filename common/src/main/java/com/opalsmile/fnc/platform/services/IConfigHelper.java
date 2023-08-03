package com.opalsmile.fnc.platform.services;

import net.minecraft.world.effect.MobEffect;

import java.util.Set;

public interface IConfigHelper {

    double getJockeySpawnChance();
    boolean namedJockeyDespawn();

    /**
     * Serialized as a List<String> mapped with ResourceLocation::new and Registry#get
     * @return
     */
    Set<MobEffect> jockeyEffectBlacklist();
}

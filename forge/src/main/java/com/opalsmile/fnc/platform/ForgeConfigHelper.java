package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.platform.services.IConfigHelper;
import net.minecraft.world.effect.MobEffect;

import java.util.Set;

public class ForgeConfigHelper implements IConfigHelper {
    @Override
    public double getJockeySpawnChance(){
        return 50;
    }

    @Override
    public boolean namedJockeyDespawn(){
        return false;
    }

    @Override
    public Set<MobEffect> jockeyEffectBlacklist(){
        return Set.of();
    }
}

package com.opalsmile.fnc.platform.services;

import com.opalsmile.fnc.item.AntlerHeaddress;
import com.opalsmile.fnc.item.SpearItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

public interface FnCIPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    TagKey<Biome> snowBiomes();
    TagKey<Biome> swampBiomes();
    TagKey<Biome> plainsBiomes();
    TagKey<Biome> mountainBiomes();

    Supplier<? extends AntlerHeaddress> getAntlerHeaddress();

    Supplier<? extends SpearItem> getSpear();

    float getHeaddressSpeed();

}
package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.FnCForge;
import com.opalsmile.fnc.item.AntlerHeaddress;
import com.opalsmile.fnc.item.SpearItem;
import com.opalsmile.fnc.platform.services.FnCIPlatformHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Supplier;

public class FnCForgePlatformHelper implements FnCIPlatformHelper {

    private static final TagKey<Biome> SNOW_BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation("forge:is_snowy"));
    private static final TagKey<Biome> SWAMP_BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation("forge:is_swamp"));
    private static final TagKey<Biome> PLAINS_BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation("forge:is_plains"));
    private static final TagKey<Biome> MOUNTAIN_BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation("forge:is_mountain"));

    @Override
    public String getPlatformName(){

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId){

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment(){

        return !FMLLoader.isProduction();
    }

    @Override
    public TagKey<Biome> snowBiomes(){
        return SNOW_BIOMES;
    }

    @Override
    public TagKey<Biome> swampBiomes(){
        return SWAMP_BIOMES;
    }

    @Override
    public TagKey<Biome> plainsBiomes(){
        return PLAINS_BIOMES;
    }

    @Override
    public TagKey<Biome> mountainBiomes(){
        return MOUNTAIN_BIOMES;
    }

    @Override
    public Supplier<? extends AntlerHeaddress> getAntlerHeaddress(){
        return FnCForge.ANTLER_HEADDRESS;
    }

    @Override
    public Supplier<? extends SpearItem> getSpear(){
        return FnCForge.SPEAR;
    }

    @Override
    public float getHeaddressSpeed(){
        return 0.015f;
    }
}
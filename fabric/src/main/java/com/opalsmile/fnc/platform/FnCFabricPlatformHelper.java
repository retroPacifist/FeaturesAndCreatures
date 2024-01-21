package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.FnCFabric;
import com.opalsmile.fnc.item.AntlerHeaddress;
import com.opalsmile.fnc.item.SpearItem;
import com.opalsmile.fnc.platform.services.FnCIPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

public class FnCFabricPlatformHelper implements FnCIPlatformHelper {

    private static final TagKey<Biome> SNOW_BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation("c:snowy"));
    private static final TagKey<Biome> SWAMP_BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation("c:swamp"));
    private static final TagKey<Biome> PLAINS_BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation("c:plains"));
    private static final TagKey<Biome> MOUNTAIN_BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation("c:mountain"));

    @Override
    public String getPlatformName(){
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId){

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment(){

        return FabricLoader.getInstance().isDevelopmentEnvironment();
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
        return () -> FnCFabric.ANTLER_HEADDRESS;
    }

    @Override
    public Supplier<? extends SpearItem> getSpear() {
        return () -> FnCFabric.SPEAR;
    }

    @Override
    public float getHeaddressSpeed(){
        return 0.03f;
    }
}

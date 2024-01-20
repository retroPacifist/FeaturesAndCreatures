package com.opalsmile.fnc.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AntlerHeaddress extends ArmorItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AntlerHeaddress(ArmorMaterial material, ArmorItem.Type type, Properties itemProperties){
        super(material, type, itemProperties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }
}

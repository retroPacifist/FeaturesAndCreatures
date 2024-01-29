package com.opalsmile.fnc.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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


    //No attributes, no armor
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot $$0){
        return ImmutableMultimap.of();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){

    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }
}

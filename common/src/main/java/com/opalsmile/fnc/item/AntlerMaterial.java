package com.opalsmile.fnc.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class AntlerMaterial implements ArmorMaterial, StringRepresentable {

    private final String name;

    public AntlerMaterial(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName(){
        return name;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type var1){
        return 0;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type var1){
        return 0;
    }

    @Override
    public int getEnchantmentValue(){
        return 0;
    }

    @Override
    public SoundEvent getEquipSound(){
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }

    @Override
    public Ingredient getRepairIngredient(){
        return Ingredient.EMPTY;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public float getToughness(){
        return 0;
    }

    @Override
    public float getKnockbackResistance(){
        return 0;
    }
}

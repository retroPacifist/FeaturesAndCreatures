package com.opalsmile.fnc.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DowsingRod extends Item {

    public DowsingRod(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity holder, int slotIndex, boolean selected){
        super.inventoryTick(itemStack, level, holder, slotIndex, selected);
    }

    public void setOff(ItemStack stack) {

    }

    public void setDimLit(ItemStack stack) {

    }

    public void setFullyLit(ItemStack stack) {

    }


}

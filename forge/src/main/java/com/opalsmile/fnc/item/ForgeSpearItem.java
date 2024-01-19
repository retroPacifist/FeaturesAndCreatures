package com.opalsmile.fnc.item;

import com.opalsmile.fnc.client.renderer.SpearItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.Map;
import java.util.function.Consumer;

public class ForgeSpearItem extends SpearItem {

    public ForgeSpearItem(Properties itemProperties){
        super(itemProperties);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(book);
        return enchantments.containsKey(Enchantments.FIRE_ASPECT) || enchantments.containsKey(Enchantments.MENDING)
                || enchantments.containsKey(Enchantments.UNBREAKING);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.FIRE_ASPECT;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack){
        return 1;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer){
       consumer.accept(new IClientItemExtensions() {
           final SpearItemRenderer renderer = new SpearItemRenderer();
           @Override
           public BlockEntityWithoutLevelRenderer getCustomRenderer(){
               return renderer;
           }
       });
    }


}

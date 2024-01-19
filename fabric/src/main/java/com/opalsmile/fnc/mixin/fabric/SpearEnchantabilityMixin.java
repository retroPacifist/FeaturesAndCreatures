package com.opalsmile.fnc.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.opalsmile.fnc.FnCFabric;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true, print = true)
@Mixin(EnchantmentHelper.class)
public class SpearEnchantabilityMixin {

    @ModifyExpressionValue(method = "getAvailableEnchantmentResults",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentCategory;canEnchant(Lnet/minecraft/world/item/Item;)Z")
    )
    private static boolean makeSpearEnchantable(boolean acceptable,
                                                int level,
                                                ItemStack stack,
                                                boolean treasureAllowed,
                                                @Local Enchantment enchantment) {
        return FnCFabric.handleEnchantability(acceptable, stack, enchantment);
    }
}

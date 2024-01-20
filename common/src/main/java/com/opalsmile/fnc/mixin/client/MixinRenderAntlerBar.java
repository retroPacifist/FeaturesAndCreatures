package com.opalsmile.fnc.mixin.client;

import com.opalsmile.fnc.client.FnCClient;
import com.opalsmile.fnc.registries.FnCItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinRenderAntlerBar {

    @Shadow
    int screenHeight;

    @Inject(method = "Lnet/minecraft/client/gui/Gui;renderExperienceBar(Lnet/minecraft/client/gui/GuiGraphics;I)V", at = @At("HEAD"), cancellable = true)
    private void featuresandcreatures_renderExperienceBar(GuiGraphics graphics, int position, CallbackInfo callbackInfo) {
        ItemStack headSlot = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD);
        if (headSlot.is(FnCItems.ANTLER_HEADDRESS.get())) {
            if (FnCClient.renderAntlerHeaddress(graphics, position, screenHeight, headSlot)) {
                callbackInfo.cancel();
            }
        }
    }
}

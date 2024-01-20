package com.opalsmile.fnc.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.opalsmile.fnc.client.DowsingRodHandRenderer;
import com.opalsmile.fnc.registries.FnCItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class MixinDowsingRod {

    @Shadow
    private ItemStack offHandItem;

    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V"), cancellable = true)
    private void handleArmRendering(AbstractClientPlayer clientPlayer, float partialTick, float playerRotation, InteractionHand hand,
                                           float handAttackAnim, ItemStack stack, float lerpedArmHeight, PoseStack poseStack, MultiBufferSource multiBufferSource,
                                    int packedLight, CallbackInfo callbackInfo) {
            if (stack.is(FnCItems.DOWSING_ROD.get())) {
                if (hand == InteractionHand.MAIN_HAND && offHandItem.isEmpty()) {
                    DowsingRodHandRenderer.render(poseStack, multiBufferSource, packedLight, playerRotation, lerpedArmHeight, handAttackAnim,
                             clientPlayer);
                }
            }
    }
}

package com.opalsmile.fnc.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class DowsingRodHandRenderer {

    private static void renderHand(AbstractClientPlayer player, PoseStack stack, MultiBufferSource source, int packedLight, HumanoidArm arm) {
        PlayerRenderer playerRenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        stack.pushPose();
        float armTranslation = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        stack.mulPose(Axis.YP.rotationDegrees(92.0F));
        stack.mulPose(Axis.XP.rotationDegrees(45.0F));
        stack.mulPose(Axis.ZP.rotationDegrees(armTranslation * -41.0F));
        stack.translate(armTranslation * 0.3F, -1.1F, 0.45F);
        if (arm == HumanoidArm.RIGHT) {
            playerRenderer.renderRightHand(stack, source, packedLight, player);
        } else {
            playerRenderer.renderLeftHand(stack, source, packedLight, player);
        }
        stack.popPose();
    }

    public static void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, float playerRotation, float lerpedArmHeight, float handAttackAnim,
                             AbstractClientPlayer player) {
        float attackAnimSqrt = Mth.sqrt(handAttackAnim);
        float yOffset = -0.2F * Mth.sin(handAttackAnim * (float) Math.PI);
        float ZOffset = -0.4F * Mth.sin(attackAnimSqrt * (float) Math.PI);
        poseStack.translate(0.0F, -yOffset / 2.0F, ZOffset);
        poseStack.translate(0.0F, 0.04F + lerpedArmHeight * -1.2F, -0.72F);
        if (!player.isInvisible()) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            renderHand(player, poseStack, multiBufferSource, packedLight, HumanoidArm.RIGHT);
            renderHand(player, poseStack, multiBufferSource, packedLight, HumanoidArm.LEFT);
            poseStack.popPose();
        }

        float rotation = Mth.sin(attackAnimSqrt * (float) Math.PI);
        poseStack.mulPose(Axis.XP.rotationDegrees(rotation * 20.0F));
        poseStack.scale(2.0F, 2.0F, 2.0F);
        poseStack.translate(-0.575, 0.3, 0.0);
    }
}

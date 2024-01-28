package com.opalsmile.fnc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.entity.Jockey;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class JockeyRenderer extends GeoEntityRenderer<Jockey> {
    public JockeyRenderer(EntityRendererProvider.Context renderManager){
        super(renderManager, new DefaultedEntityGeoModel<>(FnCConstants.resourceLocation("jockey"), true));
        addRenderLayer(new BlockAndItemGeoLayer<>(this, this::getItems, (bone, jockey) -> null) {
            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, Jockey animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay){

                poseStack.pushPose();

                poseStack.scale(0.5f, 0.5f, 0.5f);
                //poseStack.mulPose(Axis.XN.rotation(3f));
                poseStack.mulPose(Axis.XN.rotation(1.8f));
                poseStack.translate(-0.2f, 0.0f, -0.3f);

                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight,
                        packedOverlay);

                poseStack.popPose();
            }
        });
    }

    @Nullable
    private ItemStack getItems(GeoBone bone, Jockey jockey) {
        if ("rightArm".equals(bone.getName())) {
            if (jockey.isHolding(Items.POTION)) {
                return jockey.getItemBySlot(EquipmentSlot.MAINHAND);
            }
        }
        return null;
    }
}

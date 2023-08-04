package com.opalsmile.fnc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.opalsmile.fnc.client.model.JackalopeModel;
import com.opalsmile.fnc.entity.Jackalope;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class JackalopeRenderer extends GeoEntityRenderer<Jackalope> {

    public JackalopeRenderer(EntityRendererProvider.Context dispatcher){
        super(dispatcher, new JackalopeModel(FnCConstants.resourceLocation("jackalope")));
    }

    @Override
    public void render(Jackalope entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn){
        if(entity.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
            shadowRadius = 0.5f;
        } else {
            shadowRadius = 0.8f;
        }
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

}

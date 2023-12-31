package com.opalsmile.fnc.item;

import com.opalsmile.fnc.client.renderer.SpearItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class ForgeSpearItem extends SpearItem {
    public ForgeSpearItem(Properties itemProperties){
        super(itemProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer){
       consumer.accept(new IClientItemExtensions() {
           SpearItemRenderer renderer = new SpearItemRenderer();
           @Override
           public BlockEntityWithoutLevelRenderer getCustomRenderer(){
               return renderer;
           }
       });
    }
}

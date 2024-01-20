package com.opalsmile.fnc.item;

import com.opalsmile.fnc.client.renderer.SpearItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FabricSpearItem extends SpearItem {

    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public FabricSpearItem(Properties itemProperties){
        super(itemProperties);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer){
        consumer.accept(new RenderProvider() {

            private final GeoItemRenderer renderer = new SpearItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer(){
                return renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider(){
        return renderProvider;
    }
}

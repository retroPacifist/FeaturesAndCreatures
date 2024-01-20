package com.opalsmile.fnc.item;

import com.opalsmile.fnc.client.renderer.AntlerRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FabricAntlerHeadress extends AntlerHeaddress {

    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public FabricAntlerHeadress(ArmorMaterial material, Type type, Properties itemProperties){
        super(material, type, itemProperties);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer){
            consumer.accept(new RenderProvider() {

                private GeoArmorRenderer<?> renderer;

                @Override
                public HumanoidModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<LivingEntity> original){
                    if (renderer == null) renderer = new AntlerRenderer();
                    this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                    return renderer;
                }
            });
    }

    @Override
    public Supplier<Object> getRenderProvider(){
        return renderProvider;
    }
}

package com.opalsmile.fnc.advancements;

import com.google.gson.JsonObject;
import com.opalsmile.fnc.FnCConstants;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class AntlerHeaddressTrigger extends SimpleCriterionTrigger<AntlerHeaddressTrigger.Instance> {
    static final ResourceLocation ID = FnCConstants.resourceLocation("used_headdress");

    @Override
    public ResourceLocation getId(){
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext context){
        ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item_predicate"));
        return new Instance(predicate, itemPredicate);
    }

    public void trigger(ServerPlayer player, ItemStack stack){
        this.trigger(player, instance -> instance.test(stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate itemPredicate;

        public Instance(ContextAwarePredicate predicate, ItemPredicate itemPredicate){
            super(AntlerHeaddressTrigger.ID, predicate);
            this.itemPredicate = itemPredicate;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext){
            JsonObject jsonObject = super.serializeToJson(serializationContext);
            jsonObject.add("item_predicate", this.itemPredicate.serializeToJson());
            return jsonObject;
        }

        public boolean test(ItemStack stack){
            return itemPredicate.matches(stack);
        }
    }
}

package com.opalsmile.fnc.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class AntlerHeaddressTrigger extends SimpleCriterionTrigger<AntlerHeaddressTrigger.Instance> {
    @Override
    public Instance createInstance(JsonObject jsonObject, Optional<ContextAwarePredicate> predicate, DeserializationContext context){
        Optional<ItemPredicate> itemPredicate = ItemPredicate.fromJson(jsonObject.get("item"));
        return new Instance(predicate, itemPredicate);
    }

    public void trigger(ServerPlayer player, ItemStack stack){
        this.trigger(player, instance -> instance.test(stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final Optional<ItemPredicate> itemPredicate;

        public Instance(Optional<ContextAwarePredicate> predicate,  Optional<ItemPredicate> itemPredicate){
            super(predicate);
            this.itemPredicate = itemPredicate;
        }

        @Override
        public JsonObject serializeToJson(){
            JsonObject jsonObject = super.serializeToJson();
            itemPredicate.ifPresent(predicate -> jsonObject.add("item", predicate.serializeToJson()));
            return jsonObject;
        }

        public boolean test(ItemStack stack){
            return itemPredicate.isPresent() && itemPredicate.get().matches(stack);
        }
    }
}

package com.opalsmile.fnc.advancements;

import com.google.gson.JsonObject;
import com.opalsmile.fnc.common.entity.Jockey;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class JockeyTradeTrigger extends SimpleCriterionTrigger<JockeyTradeTrigger.Instance> {
    static final ResourceLocation ID = new ResourceLocation("jockey_trade");

    @Override
    public ResourceLocation getId(){
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext context){
        ContextAwarePredicate entityPredicate = EntityPredicate.fromJson(jsonObject, "jockey", context);
        ItemPredicate itemPredicate = ItemPredicate.fromJson(jsonObject.get("item_predicate"));
        return new Instance(predicate, entityPredicate, itemPredicate);
    }

    public void trigger(ServerPlayer player, Jockey jockey, ItemStack item) {
        LootContext lootcontext = EntityPredicate.createContext(player, jockey);
        this.trigger(player, instance -> instance.matches(lootcontext, item));
    }
    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate jockey;
        private final ItemPredicate itemPredicate;

        public Instance(ContextAwarePredicate predicate, ContextAwarePredicate entityPredicate, ItemPredicate itemPredicate){
            super(JockeyTradeTrigger.ID, predicate);
            this.jockey = entityPredicate;
            this.itemPredicate = itemPredicate;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext){
            JsonObject jsonObject = super.serializeToJson(serializationContext);
            jsonObject.add("item_predicate", this.itemPredicate.serializeToJson());
            jsonObject.add("jockey", this.jockey.toJson(serializationContext));
            return jsonObject;
        }

        public boolean matches(LootContext lootContext, ItemStack stack) {
            if (!this.jockey.matches(lootContext)) {
                return false;
            } else {
                return this.itemPredicate.matches(stack);
            }
        }
    }
}

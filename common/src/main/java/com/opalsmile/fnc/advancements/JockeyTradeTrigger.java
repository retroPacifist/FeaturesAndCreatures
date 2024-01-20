package com.opalsmile.fnc.advancements;

import com.google.gson.JsonObject;
import com.opalsmile.fnc.entity.Jockey;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class JockeyTradeTrigger extends SimpleCriterionTrigger<JockeyTradeTrigger.Instance> {

    @Override
    public Instance createInstance(JsonObject jsonObject, Optional<ContextAwarePredicate> playerPredicate, DeserializationContext context){
        Optional<ContextAwarePredicate> entityPredicate = EntityPredicate.fromJson(jsonObject, "jockey", context);
        Optional<ItemPredicate> itemPredicate = ItemPredicate.fromJson(jsonObject.get("item_predicate"));
        return new Instance(playerPredicate, entityPredicate, itemPredicate);
    }

    public void trigger(ServerPlayer player, Jockey jockey, ItemStack item){
        LootContext lootcontext = EntityPredicate.createContext(player, jockey);
        this.trigger(player, instance -> instance.matches(lootcontext, item));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final Optional<ContextAwarePredicate> jockey;
        private final Optional<ItemPredicate> itemPredicate;

        public Instance(Optional<ContextAwarePredicate> playerPredicate, Optional<ContextAwarePredicate> jockeyPredicate, Optional<ItemPredicate> itemPredicate){
            super(playerPredicate);
            this.jockey = jockeyPredicate;
            this.itemPredicate = itemPredicate;
        }

        @Override
        public JsonObject serializeToJson(){
            JsonObject jsonObject = super.serializeToJson();
            itemPredicate.ifPresent(predicate -> jsonObject.add("item_predicate", predicate.serializeToJson()));
            jockey.ifPresent(predicate -> jsonObject.add("jockey", predicate.toJson()));
            return jsonObject;
        }

        public boolean matches(LootContext lootContext, ItemStack stack){
            if(this.jockey.isEmpty() || !this.jockey.get().matches(lootContext)) {
                return false;
            } else {
                return this.itemPredicate.isPresent() && this.itemPredicate.get().matches(stack);
            }
        }
    }
}


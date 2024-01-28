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
        return new Instance(playerPredicate);
    }

    public void trigger(ServerPlayer player){
        this.trigger(player, instance -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {


        public Instance(Optional<ContextAwarePredicate> playerPredicate){
            super(playerPredicate);
        }

    }
}


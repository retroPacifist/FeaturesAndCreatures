package com.opalsmile.fnc.entity.goals;

import com.opalsmile.fnc.entity.Boar;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class BoarMeleeAttackGoal extends MeleeAttackGoal {

    public BoarMeleeAttackGoal(Boar boar, double speedModifier, boolean followsIfNotSeen){
        super(boar, speedModifier, followsIfNotSeen);
    }

    @Override
    public void start(){
        super.start();
        ((Boar)this.mob).triggerAnim("controller", "attack");
    }
}

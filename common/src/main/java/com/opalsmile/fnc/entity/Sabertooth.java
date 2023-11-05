package com.opalsmile.fnc.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.UUID;

public class Sabertooth extends RideableNeutralMob implements NeutralMob {

    private static final RawAnimation WALK = RawAnimation.begin().thenPlay("animation.sabertooth.walk");
    private static final RawAnimation ATTACK = RawAnimation.begin().thenLoop("animation.sabertooth.attack");
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 30);
    private int remainingPersistentAngerTime;
    @javax.annotation.Nullable
    private UUID persistentAngerTarget;

    public Sabertooth(EntityType<? extends RideableNeutralMob> entityType, Level level){
        super(entityType, level);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.25, false));
        //TODO test
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Fox.class, 10, true, true, fox -> ((Fox)fox).getVariant() == Fox.Type.SNOW));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Sheep.class, 10, true, true, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    public void tick(){
        super.tick();
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }
    }


    @Override
    TagKey<Item> getFoodTag(){
        return ItemTags.FISHES;
    }

    @Override
    SoundEvent getSaddleSound(){
        return SoundEvents.HORSE_SADDLE;
    }

    @Override
    public boolean isPlayerRideable() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 4);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar){
        controllerRegistrar.add(new AnimationController<Sabertooth>(this, 20, state -> {
            if (state.isMoving()) {
                state.setAnimation(WALK);
                return PlayState.CONTINUE;
            }
            Sabertooth sabertooth = state.getAnimatable();
            if (sabertooth.swinging){
                state.setAnimation(ATTACK);
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        }));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.readPersistentAngerSaveData(this.level(), compoundTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.addPersistentAngerSaveData(compoundTag);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {
        this.remainingPersistentAngerTime = i;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) {
        this.persistentAngerTarget = uuid;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }
}

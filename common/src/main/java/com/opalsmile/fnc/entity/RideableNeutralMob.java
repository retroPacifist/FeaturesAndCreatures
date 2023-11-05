package com.opalsmile.fnc.entity;

import com.opalsmile.fnc.registries.FnCSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public abstract class RideableNeutralMob extends Animal implements GeoEntity {

    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(RideableNeutralMob.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public RideableNeutralMob(EntityType<? extends Animal> entityType, Level level){
        super(entityType, level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(this.getFoodTag()), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundNBT){
        super.readAdditionalSaveData(compoundNBT);
        this.setSaddled(compoundNBT.getBoolean("Saddled"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT){
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putBoolean("Saddled", this.isSaddled());
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SADDLED, false);
    }

    public boolean isSaddled(){
        return this.entityData.get(SADDLED);
    }

    public void setSaddled(boolean saddled){
        this.entityData.set(SADDLED, saddled);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand){
        if (this.isFood(player.getItemInHand(hand))) {
            return super.mobInteract(player, hand);
        }
        if(!this.isPlayerRideable() || player.level().isClientSide) return super.mobInteract(player, hand);
        if (this.isSaddled()) {
            if(player.isCrouching()) {
                this.setSaddled(false);
                this.spawnAtLocation(Items.SADDLE);
                player.level().playSound(null, this.getX(), this.getY() + 0.33f, this.getZ(), FnCSounds.ENTITY_DESADDLE.get(), SoundSource.AMBIENT, 1, 1);
                return InteractionResult.SUCCESS;
            }
            else {
                //Check if not occupied and ride
                if (this.getControllingPassenger() == null) {
                    player.startRiding(this);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        else {
            if(player.isHolding(Items.SADDLE)) {
                player.level().playSound(null, this.getX(), this.getY() + 0.33f, this.getZ(), this.getSaddleSound(), SoundSource.AMBIENT, 1, 1);
                this.setSaddled(true);
                if(!player.getAbilities().instabuild) {
                    player.getItemInHand(hand).shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }


    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity){
        return (AgeableMob) this.getType().create(level);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(this.getFoodTag());
    }

    @Override
    protected void dropEquipment(){
        if(this.isSaddled()) this.spawnAtLocation(Items.SADDLE);
    }

    @Override
    protected void tickRidden(Player player, Vec3 movement){
        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 input){
        float horizontal = player.xxa * 0.5F;
        float forwards = player.zza;
        if (forwards <= 0.0F) {
            //Make movement slow if we're backtracking
            forwards *= 0.25F;
        }
        //By default this won't jump.
        return new Vec3(horizontal, 0.0D, forwards);
    }

    @Override
    protected float getRiddenSpeed(Player player){
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.isSaddled()) {
            Entity entity = this.getFirstPassenger();
            if (entity instanceof Player player) {
                return player;
            }
        }
        return null;
    }

    abstract TagKey<Item> getFoodTag();

    abstract SoundEvent getSaddleSound();

    abstract boolean isPlayerRideable();

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }
}

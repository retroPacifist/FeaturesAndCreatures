package com.opalsmile.fnc.entity;

import com.opalsmile.fnc.registries.FnCEntities;
import com.opalsmile.fnc.registries.FnCSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;


public class Spear extends AbstractArrow implements GeoEntity {

    private ItemStack thrownStack = ItemStack.EMPTY;
    private boolean dealtDamage;

    public Spear(EntityType<? extends AbstractArrow> entityType, Level level){
        super(entityType, level);
    }

    public Spear(Level level, LivingEntity thrower, ItemStack stack){
        super(FnCEntities.SPEAR.get(), thrower, level);
        this.thrownStack = stack.copy();
    }

    @Override
    protected ItemStack getPickupItem(){
        return this.thrownStack.copy();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 $$0, Vec3 $$1){
        return this.dealtDamage ? null : super.findHitEntity($$0, $$1);
    }


    @Override
    protected void onHitEntity(EntityHitResult result){
        Entity entity = result.getEntity();
        float f = 12.0F;
        if(entity instanceof LivingEntity livingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.thrownStack, livingEntity.getMobType());
        }

        Entity owner = this.getOwner();
        DamageSource damagesource = this.level().damageSources().trident(this, (owner == null ? this : owner));
        this.dealtDamage = true;
        SoundEvent soundevent = FnCSounds.SPEAR_ATTACK.get();
        if(entity.hurt(damagesource, f)) {
            if(entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if(entity instanceof LivingEntity hitEntity) {
                if(owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(hitEntity, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, hitEntity);
                }

                this.doPostHurtEffects(hitEntity);
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        this.playSound(soundevent, 1.0F, 1.0F);
    }

    @Override
    public void playerTouch(Player player){
        Entity entity = this.getOwner();
        if(entity == null || entity.getUUID().equals(player.getUUID())) {
            super.playerTouch(player);
        }
    }

    public void readAdditionalSaveData(CompoundTag compoundTag){
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.contains("Spear", 10)) {
            this.thrownStack = ItemStack.of(compoundTag.getCompound("Spear"));
        }
        this.dealtDamage = compoundTag.getBoolean("DealtDamage");
    }

    public void addAdditionalSaveData(CompoundTag compoundTag){
        super.addAdditionalSaveData(compoundTag);
        compoundTag.put("Spear", this.thrownStack.save(new CompoundTag()));
        compoundTag.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn(){
        if(this.pickup != AbstractArrow.Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar){
        //No animation geo renderer
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }
}

package com.opalsmile.fnc.entity;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.platform.FnCServices;
import com.opalsmile.fnc.registries.FnCSounds;
import com.opalsmile.fnc.registries.FnCTriggers;
import com.opalsmile.fnc.util.FnCSavedData;
import com.opalsmile.fnc.util.FnCUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Jockey extends PathfinderMob implements Npc, Merchant, GeoEntity, RangedAttackMob {
    private static final String POTION_TRANSLATION_KEY = "entity." + FnCConstants.MOD_ID + ".jockey.potion";
    private static final String ARROW_TRANSLATION_KEY = "entity." + FnCConstants.MOD_ID + ".jockey.arrow";
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(Jockey.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ATTACK_TIMER = SynchedEntityData.defineId(Jockey.class,
            EntityDataSerializers.INT);
    private static final TargetingConditions TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().range(32);
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("animation.jockey.walk");
    private static final RawAnimation POTION_ANIMATION = RawAnimation.begin().thenLoop("animation.jockey.potion");
    private static final RawAnimation DRINK_ANIMATION = RawAnimation.begin().thenLoop("animation.jockey.drink");
    private static final RawAnimation SIT_ANIMATION = RawAnimation.begin().thenLoop("animation.jockey.sit");
    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private Player tradingPlayer;
    private Player followingPlayer;
    private MerchantOffers offers;
    private BlockPos lastBlockPos = BlockPos.ZERO;

    private int timeAlive;

    public Jockey(EntityType<? extends Jockey> entityType, Level level){
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12.0).add(Attributes.MOVEMENT_SPEED, 0.2);
    }


    public static boolean isRiding(Jockey jockey){
        Entity entity = jockey.getVehicle();
        return entity instanceof LivingEntity;
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(ATTACK_TIMER, 10);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
        this.goalSelector.addGoal(2, new FloatGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.35D));
        this.goalSelector.addGoal(4, new FollowPlayerGoal(this, 12, 0.6F));
        this.goalSelector.addGoal(4, new MountFollowPlayerGoal(this, 12, 1.2F));
        this.goalSelector.addGoal(4, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35D));
        this.goalSelector.addGoal(0, new UseItemGoal<>(this, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HEALING),
                        SoundEvents.GENERIC_DRINK, entity -> this.getHealth() <= 6));
        this.targetSelector.addGoal(7, new HurtByTargetGoal(this));
    }

    @Override
    public boolean showProgressBar(){
        return false;
    }

    @Override
    public SoundEvent getNotifyTradeSound(){
        return FnCSounds.JOCKEY_YES.get();
    }

    @Override
    public boolean isClientSide(){
        return this.level().isClientSide();
    }

    protected SoundEvent getTradeUpdatedSound(boolean agrees){
        return agrees ? FnCSounds.JOCKEY_YES.get() : FnCSounds.JOCKEY_NO.get();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand){
        ItemStack itemstack = player.getItemInHand(hand);
        if(!(itemstack.getItem() instanceof SpawnEggItem) && this.isAlive() && tradingPlayer == null) {
            if (!this.level().isClientSide) {
                if (this.getOffers().isEmpty()) return InteractionResult.PASS;
                this.setTradingPlayer(player);
                this.openTradingScreen(player, this.getDisplayName(), 1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size){
        return size.height * 0.85F;
    }

    @Override
    public boolean canBeLeashed(Player player){
        return false;
    }

    @Nullable
    @Override
    public Player getTradingPlayer(){
        return tradingPlayer;
    }

    @Override
    public void setTradingPlayer(@Nullable Player player){
        this.tradingPlayer = player;
    }

    public boolean isAttacking(){
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(boolean attack){
        this.entityData.set(ATTACKING, attack);
    }

    public int getAttackTimer(){
        return this.entityData.get(ATTACK_TIMER);

    }

    public void setAttackTimer(int attackTimer){
        this.entityData.set(ATTACK_TIMER, attackTimer);
    }

    @Override
    public MerchantOffers getOffers(){
        if(this.offers == null) {
            this.offers = new MerchantOffers();

            final Predicate<MobEffect> blacklisted = FnCServices.CONFIG.jockeyEffectBlacklist()::contains;
            final List<MobEffect> availableEffects = BuiltInRegistries.MOB_EFFECT.stream().filter(blacklisted.negate()).collect(
                    Collectors.toList());
            final boolean empty = availableEffects.isEmpty();

            for(int i = 0; i < 7; ++i) {
                final List<MobEffectInstance> effects = new ArrayList<>();
                final int price = random.nextInt(8) + 5;
                final int effectCount = generateEffectCount();
                final TradeType type = generateTradeType();

                int amount = type.getAmount();

                for(int j = 0; j < effectCount; ++j) {
                    MobEffect effect = empty ? MobEffects.REGENERATION : FnCUtil.getRandomElement(random, availableEffects);
                    availableEffects.remove(effect);
                    effects.add(new MobEffectInstance(effect, 1800, generatePotionStrength(effectCount)));
                }

                Item item = type.getTrade();
                String translationKey = type.getTranslationKey();

                ItemStack potion = PotionUtils.setCustomEffects(new ItemStack(item, amount), effects).setHoverName(
                        Component.translatable(translationKey).withStyle(Style.EMPTY.withItalic(false)).withStyle(
                                ChatFormatting.YELLOW));
                potion.getTag().putInt("CustomPotionColor", PotionUtils.getColor(effects));

                offers.add(new MerchantOffer(new ItemStack(Items.DIAMOND, price), ItemStack.EMPTY, potion,
                        Integer.MAX_VALUE, 0, 1));
            }
            Collections.shuffle(offers); //Shuffle the order of the offers.
        }
        return offers;
    }

    private TradeType generateTradeType(){
        TradeType type;
        int typeChance = random.nextInt(20);
        if(typeChance < 2) {
            type = TradeType.ARROWS_32;
        } else if(typeChance < 5) {
            type = TradeType.ARROWS_16;
        } else if(typeChance < 8) {
            type = TradeType.LINGERING;
        } else if(typeChance < 12) {
            type = TradeType.SPLASH;
        } else {
            type = TradeType.DRINK;
        }
        return type;
    }

    private int generateEffectCount(){
        int effectCount;
        int effectCountChance = random.nextInt(20);
        if(effectCountChance < 3) {
            effectCount = 3;
        } else if(effectCountChance < 8) {
            effectCount = 2;
        } else {
            effectCount = 1;
        }
        return effectCount;
    }

    private int generatePotionStrength(int effectCount){
        int strength;
        int strengthChance = random.nextInt(100);
        switch (effectCount) {
            case 1 -> {
                if(strengthChance < 10) {
                    strength = 4;
                } else if(strengthChance < 35) {
                    strength = 3;
                } else if(strengthChance < 65) {
                    strength = 2;
                } else if(strengthChance < 90) {
                    strength = 1;
                } else {
                    strength = 0;
                }
            }
            case 2 -> {
                if(strengthChance == 0) {
                    strength = 4;
                } else if(strengthChance < 8) {
                    strength = 3;
                } else if(strengthChance < 38) {
                    strength = 2;
                } else if(strengthChance < 80) {
                    strength = 1;
                } else {
                    strength = 0;
                }
            }
            default -> {
                if(strengthChance < 17) {
                    strength = 2;
                } else if(strengthChance < 40) {
                    strength = 1;
                } else {
                    strength = 0;
                }
            }
        }
        return strength;
    }

    @Override
    public void overrideOffers(@Nullable MerchantOffers offers){
    }

    public void notifyTrade(MerchantOffer offer){
        offer.increaseUses();
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        if(this.tradingPlayer instanceof ServerPlayer serverPlayer) {
            FnCTriggers.JOCKEY_TRADE.trigger(serverPlayer);
        }
    }

    @Override
    public void notifyTradeUpdated(ItemStack stack)
    {
        if(!this.level().isClientSide && this.ambientSoundTime > -this.getAmbientSoundInterval() + 20) {
            this.ambientSoundTime = -this.getAmbientSoundInterval();
            this.playSound(this.getTradeUpdatedSound(!stack.isEmpty()), this.getSoundVolume(), this.getVoicePitch());
        }
    }

    @Override
    public int getVillagerXp(){
        return 0;
    }

    @Override
    public void overrideXp(int xp){
    }

    @Override
    public void tick(){
        //TODO probably see how other mobs ride vehicles as movement is janky as of now.
        super.tick();
        ++timeAlive;
        if(isAttacking()) {
            this.setAttackTimer(this.getAttackTimer() - 1);
            if(getAttackTimer() <= 0) {
                setAttacking(false);
                setAttackTimer(10);
            }
        }
        if(this.getVehicle() instanceof Mob mount) {
            if(getTarget() != null) {
                mount.setTarget(this.getTarget());
                if(mount instanceof NeutralMob neutralMob)
                    neutralMob.startPersistentAngerTimer();
            } else if(mount.getTarget() != null)
                setTarget(mount.getTarget());
        }
        if(!this.level().isClientSide) {
            updateJockeyPosition();
            if (this.timeAlive >= FnCServices.CONFIG.jockeyDespawnTime() && (FnCServices.CONFIG.namedJockeyDespawn() || !this.hasCustomName())) {
                this.removeJockey();
                if (this.getVehicle() != null) {
                    this.getVehicle().discard();
                }
                this.discard();
            }
        }
    }

    @Override
    public void die(DamageSource $$0) {
        super.die($$0);
        if (!this.level().isClientSide()) {
            if (this.isHolding(Items.POTION)) {
                final ItemStack stack = this.getMainHandItem();
                this.spawnAtLocation(stack);
            }
            this.removeJockey();
        }
    }

    //TODO Figure out why the Jockey can't change dimensions.
    //Is it the mount?
    @org.jetbrains.annotations.Nullable
    @Override
    public Entity changeDimension(ServerLevel $$0) {
        if (this.level() instanceof ServerLevel level) {
            this.removeJockey();
            final Entity target = super.changeDimension($$0);
            if (target instanceof Jockey jockey) {
                FnCSavedData savedData = FnCSavedData.get(level.getServer());
                savedData.setJockeyUUID(jockey.getUUID());
                savedData.setJockeyCooldown(-1);
                savedData.setSpawnPosition(jockey.blockPosition());
                savedData.setJockeySpawned(true);
                savedData.setDimensionId($$0.dimension());
                savedData.setDirty();
            }
            return target;
        }
        return null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    protected SoundEvent getAmbientSound(){
        return getTarget() != null ? FnCSounds.JOCKEY_NO.get() : FnCSounds.JOCKEY_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource){
        return FnCSounds.JOCKEY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound(){
        return FnCSounds.JOCKEY_DEATH.get();
    }

    /**
     * May only be called on the server side.
     */
    private void updateJockeyPosition(){
        final FnCSavedData savedData = FnCSavedData.get(this.getServer());
        if(!this.lastBlockPos.equals(this.blockPosition())) {
            final UUID uuid = savedData.getJockeyUUID();
            if(this.uuid.equals(uuid)) {
                savedData.setSpawnPosition(this.blockPosition());
                savedData.setDirty();
                //TODO sync dimension too NetworkHandler.sendToAllClients(((ServerWorld) this.level).players(), new JockeyPosPacket(this.blockPosition()));
                this.lastBlockPos = this.blockPosition();
            }
        }
    }

    public void removeJockey() {
        final FnCSavedData savedData = FnCSavedData.get(this.getServer());
        final UUID jockeyUUID = savedData.getJockeyUUID();
        if(this.uuid.equals(jockeyUUID)) {
            savedData.setJockeyUUID(null);
            savedData.setSpawnPosition(null);
            savedData.clearDimensionId();
            savedData.setJockeySpawned(false);
            FnCServices.NETWORK.notifyJockeyDeath((ServerLevel) this.level());
        }
    }

    @Override
    public void load(CompoundTag $$0){
        super.load($$0);
        if (!this.level().isClientSide()) {
            FnCSavedData savedData = FnCSavedData.get(this.getServer());
            savedData.setSpawnPosition(this.blockPosition());
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag){
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("TimeAlive", timeAlive);
        compoundTag.putBoolean("Attacking", isAttacking());
        compoundTag.putInt("AttackTimer", getAttackTimer());
        compoundTag.put("Offers", getOffers().createTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt){
        super.readAdditionalSaveData(nbt);
        this.timeAlive = nbt.getInt("TimeAlive");
        setAttacking(nbt.getBoolean("Attacking"));
        setAttackTimer(nbt.getInt("AttackTimer"));
        if (nbt.contains("Offers", Tag.TAG_COMPOUND))
            offers = new MerchantOffers(nbt.getCompound("Offers"));
    }

    private PlayState predicate(AnimationState<Jockey> event){
        AnimationController<?> controller = event.getController();
        if(this.onGround() && event.isMoving()) {
            controller.setAnimation(WALK_ANIMATION);
            return PlayState.CONTINUE;
        } else if(this.isAttacking()) {
            controller.setAnimation(POTION_ANIMATION);
            return PlayState.CONTINUE;
        } else if(this.isHolding(Items.POTION)) {
            controller.setAnimation(DRINK_ANIMATION);
            return PlayState.CONTINUE;
        }
        if(isRiding(this)) {
            controller.setAnimation(SIT_ANIMATION);
            return PlayState.CONTINUE;
        } else {
            return PlayState.STOP;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar){
        registrar.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    @Override
    public void rideTick(){
        super.rideTick();
        if(this.getVehicle() instanceof PathfinderMob pathfinderMob) {
            this.yBodyRot = pathfinderMob.yBodyRot;
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return this.factory;
    }

    @Override
        public void performRangedAttack(LivingEntity target, float v){
        this.setAttacking(true);
        Vec3 vector3d = target.getDeltaMovement();
        double d0 = target.getX() + vector3d.x - this.getX();
        double d1 = target.getEyeY() - (double) 1.1F - this.getY();
        double d2 = target.getZ() + vector3d.z - this.getZ();
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2));
        Potion potion = Potions.HARMING;
        ThrownPotion thrownPotion = new ThrownPotion(this.level(), this);
        thrownPotion.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
        thrownPotion.setXRot(thrownPotion.getXRot() + 20);
        thrownPotion.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 8.0F);
        if(!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), FnCSounds.JOCKEY_ATTACK.get(),
                    this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        }
        this.level().addFreshEntity(thrownPotion); //TODO Drop potion if killed while drinking
    }

    public enum TradeType {

        DRINK(1, Items.POTION, POTION_TRANSLATION_KEY),
        SPLASH(1, Items.SPLASH_POTION, POTION_TRANSLATION_KEY),
        LINGERING(1, Items.LINGERING_POTION, POTION_TRANSLATION_KEY),
        ARROWS_16(16, Items.TIPPED_ARROW, ARROW_TRANSLATION_KEY),
        ARROWS_32(32, Items.TIPPED_ARROW, ARROW_TRANSLATION_KEY);

        int amount;
        Item trade;
        String translationKey;

        TradeType(int amount, Item item, String translationKey) {
            this.amount = amount;
            this.trade = item;
            this.translationKey = translationKey;
        }

        public int getAmount(){
            return amount;
        }

        public Item getTrade(){
            return trade;
        }

        public String getTranslationKey(){
            return translationKey;
        }
    }

    public static class FollowPlayerGoal extends Goal {

        public Jockey jockey;
        public double distance;
        public float speed;


        public FollowPlayerGoal(Jockey jockey, double distance, float speed){
            this.jockey = jockey;
            this.distance = distance;
            this.speed = speed;
        }

        @Override
        public boolean canUse(){
            return jockey.isAlive();
        }

        @Override
        public void start(){
            super.start();
        }

        @Override
        public void stop(){
            this.jockey.getNavigation().stop();
            super.stop();
        }

        @Override
        public void tick(){
            super.tick();
            this.jockey.followingPlayer = this.jockey.level().getNearestPlayer(TARGETING, this.jockey);
            if(this.jockey.followingPlayer == null) return;
            if(this.jockey.distanceToSqr(this.jockey.followingPlayer) < distance) {
                this.jockey.getNavigation().stop();
            } else {
                this.jockey.getNavigation().moveTo(this.jockey.followingPlayer, speed);
            }
        }
    }

    public static class MountFollowPlayerGoal extends Goal {

        public Jockey jockey;
        public double distance;
        public float speed;
        public Mob mount;


        public MountFollowPlayerGoal(Jockey jockey, double distance, float speed){
            this.jockey = jockey;
            this.distance = distance;
            this.speed = speed;
        }

        @Override
        public boolean canUse(){
            return isRiding(jockey);
        }

        @Override
        public void start(){
            mount = (Mob) jockey.getVehicle();
            super.start();
        }

        @Override
        public void stop(){
            this.jockey.getNavigation().stop();
            super.stop();
        }

        @Override
        public void tick(){
            super.tick();
            this.jockey.followingPlayer = this.jockey.level().getNearestPlayer(TARGETING, this.jockey);
            if(this.jockey.followingPlayer == null) return;
            if(this.mount.distanceToSqr(this.jockey.followingPlayer) < distance) {
                this.mount.getNavigation().stop();
            } else {
                this.mount.getNavigation().moveTo(this.jockey.followingPlayer, speed);
            }
        }
    }
}
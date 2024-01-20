package com.opalsmile.fnc.entity;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.registries.FnCSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;


public class Jackalope extends RideableNeutralMob {
    public static final TagKey<Item> JACKALOPE_FOOD = TagKey.create(Registries.ITEM, FnCConstants.resourceLocation("jackalope_food"));
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.jackalope.walk");

    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int jumpDelayTicks;

    public Jackalope(EntityType<? extends Jackalope> type, Level level){
        super(type, level);
        this.jumpControl = new JumpHelperController(this);
        this.moveControl = new MoveHelperController(this);
        this.setSpeedModifier(0.0D);
    }


    public static AttributeSupplier.Builder createAttributes(){
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected float getJumpPower(){
        if(!this.horizontalCollision && (!this.moveControl.hasWanted() || !(this.moveControl.getWantedY() > this.getY() + 0.7D))) {
            Path path = this.navigation.getPath();
            if(path != null && !path.isDone()) {
                Vec3 vector3d = path.getNextEntityPos(this);
                if(vector3d.y > this.getY() + 0.6D) {
                    return 0.6F;
                }
            }
            return this.moveControl.getSpeedModifier() <= 0.6D ? 0.2F : 0.3F;
        } else {
            return 0.7F;
        }
    }

    @Override
    protected void jumpFromGround(){
        super.jumpFromGround();
        double d0 = this.moveControl.getSpeedModifier();
        if(d0 > 0.0D) {
            double d1 = this.getDeltaMovement().horizontalDistanceSqr();
            if(d1 < 0.01D) {
                this.moveRelative(0.2F, new Vec3(0.0D, 0.0D, 1.0D));
            }
        }
        if(!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 1); //Spawns particle on the clientside as well as syncs
        }
    }

    public void setSpeedModifier(double speedModifier){
        this.getNavigation().setSpeedModifier(speedModifier);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(),
                this.moveControl.getWantedZ(), speedModifier);
    }

    @Override
    public void setJumping(boolean isJumping){
        super.setJumping(isJumping);
        if(isJumping) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(),
                    ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }
    }


    @Override
    public TagKey<Item> getFoodTag(){
        return JACKALOPE_FOOD;
    }

    @Override
    public SoundEvent getSaddleSound(){
        return FnCSounds.JACKALOPE_SADDLE.get();
    }

    @Override
    public boolean isPlayerRideable() {
        return true;
    }

    public void startJumping(){
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }

    @Override
    public void customServerAiStep(){
        if(this.jumpDelayTicks > 0) {
            --this.jumpDelayTicks;
        }

        if(this.onGround()) {
            if(!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }

            JumpHelperController controller = (JumpHelperController) this.jumpControl;
            if(!controller.wantJump()) {
                if(this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
                    Path path = this.navigation.getPath();
                    Vec3 vector3d = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(),
                            this.moveControl.getWantedZ());
                    if(path != null && !path.isDone()) {
                        vector3d = path.getNextEntityPos(this);
                    }

                    this.facePoint(vector3d.x, vector3d.z);
                    this.startJumping();
                }
            } else if(!controller.canJump()) {
                this.enableJumpControl();
            }
        }
        this.wasOnGround = this.onGround();
    }

    @Override
    public boolean canSpawnSprintParticle(){
        return false;
    }

    //Rotates towards the given coordinates
    private void facePoint(double xCoord, double yCoord){
        this.setYRot((float) (Mth.atan2(yCoord - this.getZ(), xCoord - this.getX()) * (double) (180F / (float) Math.PI)) - 90.0F);
    }

    private void enableJumpControl(){
        ((JumpHelperController) this.jumpControl).setCanJump(true);
    }

    private void disableJumpControl(){
        ((JumpHelperController) this.jumpControl).setCanJump(false);
    }

    private void setLandingDelay(){
        if(this.moveControl.getSpeedModifier() < 2.2D) {
            this.jumpDelayTicks = 10;
        } else {
            this.jumpDelayTicks = 1;
        }
    }

    private void checkLandingDelay(){
        this.setLandingDelay();
        this.disableJumpControl();
    }

    @Override
    public void aiStep(){
        super.aiStep();
        if(this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        } else if(this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }
    }

    protected SoundEvent getJumpSound(){
        return FnCSounds.JACKALOPE_STEP.get();
    }

    @Override
    protected SoundEvent getAmbientSound(){
        return FnCSounds.JACKALOPE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource){
        return FnCSounds.JACKALOPE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound(){
        return FnCSounds.JACKALOPE_DEATH.get();
    }

    @Override
    public void handleEntityEvent(byte dataValue){
        if(dataValue == 1) {
            this.spawnSprintParticle();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        } else {
            super.handleEntityEvent(dataValue);
        }
    }
    @Override
    protected void registerGoals(){
        super.registerGoals();
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.25D));

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar){
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(final AnimationState<Jackalope> event){

        if(this.jumpDuration >= 1) {
            return event.setAndContinue(WALK);
        }
        return PlayState.STOP;
    }

    static class MoveHelperController extends MoveControl {
        private final Jackalope jack;
        private double nextJumpSpeed;

        public MoveHelperController(Jackalope jackalope){
            super(jackalope);
            this.jack = jackalope;
        }

        @Override
        public void tick(){
            if(this.jack.onGround() && !this.jack.jumping && !((JumpHelperController) this.jack.jumpControl).wantJump()) {
                this.jack.setSpeedModifier(0.0D);
            } else if(this.hasWanted()) {
                this.jack.setSpeedModifier(this.nextJumpSpeed);
            }
            super.tick();
        }

        @Override
        public void setWantedPosition(double x, double y, double z, double speedModifier){
            if(this.jack.isInWater()) {
                speedModifier = 1.5D;
            }
            super.setWantedPosition(x, y, z, speedModifier);
            if(speedModifier > 0.0D) {
                this.nextJumpSpeed = speedModifier;
            }
        }
    }

    public class JumpHelperController extends JumpControl {
        private final Jackalope jackalope;
        private boolean canJump;

        public JumpHelperController(Jackalope jackalope){
            super(jackalope);
            this.jackalope = jackalope;
        }

        public boolean wantJump(){
            return this.jump;
        }

        public boolean canJump(){
            return this.canJump;
        }

        public void setCanJump(boolean canJump){
            this.canJump = canJump;
        }

        @Override
        public void tick(){
            if(this.jump) {
                this.jackalope.startJumping();
                this.jump = false;
            }
        }
    }
}

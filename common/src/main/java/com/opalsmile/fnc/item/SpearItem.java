package com.opalsmile.fnc.item;

import com.opalsmile.fnc.entity.Spear;
import com.opalsmile.fnc.registries.FnCSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;

public abstract class SpearItem extends Item implements Vanishable, GeoItem {

    private final AnimatableInstanceCache instanceCache = GeckoLibUtil.createInstanceCache(this);

    public SpearItem(Properties itemProperties){
        super(itemProperties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    //TODO Forge uses this method, figure out how to do on Fabric
    //I'm leaning towards having this class once on each loader and to both handle the BEWLR (ISTER in the past)
    //As well as these methods + Fabric mixins on each loader.
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(book);
        return enchantments.containsKey(Enchantments.FIRE_ASPECT) || enchantments.containsKey(Enchantments.MENDING)
                || enchantments.containsKey(Enchantments.UNBREAKING);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.FIRE_ASPECT;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft){
        if (!livingEntity.level().isClientSide && livingEntity instanceof Player player) {
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                stack.hurtAndBreak(1, player, (playerEntity) -> {
                    playerEntity.broadcastBreakEvent(player.getUsedItemHand());
                });
                Spear spear = new Spear(level, player, stack);
                spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3.5f, 1.0F);
                if(player.getAbilities().instabuild) {
                    spear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
                level.addFreshEntity(spear);
                level.playSound(null, spear, FnCSounds.SPEAR_THROW.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                if(!player.getAbilities().instabuild) {
                    player.getInventory().removeItem(stack);
                }
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    /**
     * Register your {@link AnimationController AnimationControllers} and their respective animations and conditions.
     * Override this method in your animatable object and add your controllers via {@link AnimatableManager.ControllerRegistrar#add ControllerRegistrar.add}.
     * You may add as many controllers as wanted.
     * <br><br>
     * Each controller can only play <u>one</u> animation at a time, and so animations that you intend to play concurrently should be handled in independent controllers.
     * Note having multiple animations playing via multiple controllers can override parts of one animation with another if both animations use the same bones or child bones.
     *
     * @param controllers The object to register your controller instances to
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){

    }

    /**
     * Each instance of a {@code GeoAnimatable} must return an instance of an {@link AnimatableInstanceCache}, which handles instance-specific animation info.
     * Generally speaking, you should create your cache using {@code GeckoLibUtil#createCache} and store it in your animatable instance, returning that cached instance when called.
     *
     * @return A cached instance of an {@code AnimatableInstanceCache}
     */
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return instanceCache;
    }
}

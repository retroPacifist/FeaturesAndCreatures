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

    @Override
    public int getEnchantmentValue(){
        return 1;
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
                int fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
                if (fireAspectLevel > 0) {
                    spear.setSecondsOnFire(fireAspectLevel * 10);
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return instanceCache;
    }

}

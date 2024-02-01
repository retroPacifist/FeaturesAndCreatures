package com.opalsmile.fnc.util;

import com.opalsmile.fnc.platform.FnCServices;
import com.opalsmile.fnc.registries.FnCSounds;
import com.opalsmile.fnc.registries.FnCTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntlerHandler {

    private static final UUID ANTLER_HEADDRESS_UUID = UUID.fromString("37822605-a8d7-4ae4-bc85-c11ff9f211c4");
    private static final AttributeModifier ANTLER_HEADDRESS_MODIFIER = new AttributeModifier(ANTLER_HEADDRESS_UUID, "Antler Headdress Slow", -0.05, AttributeModifier.Operation.ADDITION);

    private static final float MAX_KNOCKBACK = 3;
    private static final float MAX_CHARGE = 4f;

    private static final float MAX_DAMAGE = 2.5f;
    private static final int ANTLER_COOLDOWN = (int) (1.5 * 20);

    private static final Map<UUID, AntlerStorage> antlerValues = new HashMap<>();

    public static void handlePacket(final ServerPlayer player, final boolean released) {
        if (!player.getItemBySlot(EquipmentSlot.HEAD).is(FnCServices.PLATFORM.getAntlerHeaddress().get())) return;
        AntlerStorage storage = antlerValues.computeIfAbsent(player.getUUID(), uuid -> new AntlerStorage());
        if (storage.pastTick) {
            if (released) {
                releaseHeaddress(player, ANTLER_HEADDRESS_MODIFIER);
                chargeForward(player, storage.value);
                player.getCooldowns().addCooldown(FnCServices.PLATFORM.getAntlerHeaddress().get(), ANTLER_COOLDOWN);
                storage.reset();
            }
            else {
                storage.increment();
            }
        }
        else {
            if (!released && !player.getCooldowns().isOnCooldown(FnCServices.PLATFORM.getAntlerHeaddress().get())) {
                startChargingHeaddress(player, ANTLER_HEADDRESS_MODIFIER);
                storage.start();
            }
        }
    }

    private static void startChargingHeaddress(ServerPlayer player, AttributeModifier modifier) {
        if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(modifier)) {
            player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(modifier);
        }
    }

    private static void releaseHeaddress(ServerPlayer player, AttributeModifier modifier) {
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(ANTLER_HEADDRESS_MODIFIER.getId());
    }

    private static void chargeForward(ServerPlayer player, float chargeTime) {
        ServerLevel level = player.serverLevel();
        float chargePercentage = chargeTime * chargeTime;
        float forwards = MAX_CHARGE * chargePercentage;
        float knockback = MAX_KNOCKBACK * chargePercentage;
        Vec3 vec = player.getLookAngle();
        double xForwards = vec.x * forwards + 1 * vec.x;
        double zForwards = vec.z * forwards + 1 * vec.z;
        playSound(chargePercentage, player);
        player.setDeltaMovement(player.getDeltaMovement().add(xForwards, 0.0, zForwards));
        AABB targetBoundingBox = player.getBoundingBox().expandTowards(xForwards, 0, zForwards);
        level.getEntities(EntityTypeTest.forClass(Entity.class), targetBoundingBox, entity -> entity.isAlive() && entity != player).forEach(entity -> {
            FnCTriggers.ANTLER_HEADDRESS.trigger(player, player.getItemBySlot(EquipmentSlot.HEAD));
            entity.move(MoverType.PLAYER, new Vec3(knockback * vec.x, 2.0, knockback * vec.z));
            entity.hurt(level.damageSources().mobAttack(player), MAX_DAMAGE * chargePercentage);
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), FnCSounds.ANTLER_HEADDRESS_ATTACK_STRONG.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        });
        player.hurtMarked = true;
    }

    private static void playSound(float percentage, ServerPlayer player) {
        if (Math.abs(1 - percentage) < 0.01) {
            player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
                    FnCSounds.ANTLER_HEADDRESS_FINISHED_CHARGING.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        else if (percentage < 0.25) {
            player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
                    FnCSounds.ANTLER_HEADDRESS_CHARGE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        else {
            player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
                    FnCSounds.ANTLER_HEADDRESS_CHARGE.get(), SoundSource.PLAYERS, 4.0f, 1.0f);
        }
    }


    private static class AntlerStorage {
        private float value;
        private boolean pastTick;

        public void reset() {
            pastTick = false;
            value = 0.0f;
        }

        public void increment() {
            if (value < 1) value += 0.02f;
        }

        public void start() {
            pastTick = true;
            value = 0.0f;
        }
    }
}

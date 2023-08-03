package com.opalsmile.fnc.common.entity.spawner;

import com.opalsmile.fnc.common.FnCSavedData;
import com.opalsmile.fnc.common.entity.Jockey;
import com.opalsmile.fnc.core.FnCEntities;
import com.opalsmile.fnc.platform.FnCServices;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.CustomSpawner;

public class JockeySpawner implements CustomSpawner {
    private static final long COOLDOWN = 72000L;
    public static final int MAX_OFFSET = 10;
    private int spawnChance;

    @Override
    public int tick(ServerLevel level, boolean spawnFriendlies, boolean spawnEnemies) {
        double successChance = FnCServices.CONFIG.getJockeySpawnChance();
        if (successChance <= 0) return 0;

        FnCSavedData savedData = FnCSavedData.get(level);

        if (savedData.hasJockeySpawned()) {
            return 0;
        }

        long jockeySpawnCooldown = savedData.getJockeyCooldown();
        if (jockeySpawnCooldown <= 0) {
            return attemptSpawnJockey(level, savedData, successChance);
        }

        savedData.setJockeyCooldown(jockeySpawnCooldown - 1);
        savedData.setDirty();
        return 0;
    }

    private int attemptSpawnJockey(ServerLevel world, FnCSavedData savedData, double successChance) {
        int defaultSpawnChance = (int) (25 * successChance);
        if (spawnChance == 0) {
            spawnChance = defaultSpawnChance;
        }

        // Increasing chance, similarly to how the wandering trader functions
        int i = spawnChance;
        spawnChance = Mth.clamp(spawnChance + defaultSpawnChance, 0, 75);

        if (world.random.nextInt(100) > i) {
            savedData.setJockeyCooldown(COOLDOWN);
            savedData.setDirty();
            return 0;
        }

        // Extra check that is never changed, except by the config value
        if (world.random.nextInt((int) (successChance * 10)) != 0) {
            savedData.setJockeyCooldown(COOLDOWN);
            savedData.setDirty();
            return 0;
        }

        ServerPlayer player = world.getRandomPlayer();
        if (player != null) {
            BlockPos position = player.blockPosition().offset(world.random.nextInt(MAX_OFFSET) - (MAX_OFFSET / 2), world.random.nextInt(MAX_OFFSET) - (MAX_OFFSET / 2), world.random.nextInt(MAX_OFFSET) - (MAX_OFFSET / 2));

            // Prevent suffocating
            for (BlockPos blockPos : BlockPos.betweenClosed(position.offset(-2, 0, -2), position.offset(2, 3, 2))) {
                if (!world.isEmptyBlock(blockPos)) {
                    return 0;
                }
            }

            // Floor finder
            for (BlockPos blockPos : BlockPos.betweenClosed(position.offset(-1, -1, -1), position.offset(1, -1, 1))) {
                if (world.isEmptyBlock(blockPos) && world.getFluidState(blockPos).isEmpty()) {
                    return 0;
                }
            }

            Jockey jockey = FnCEntities.JOCKEY.get().create(world);
            if (jockey == null) {
                return 0;
            }

            jockey.moveTo(position.getX(), position.getY(), position.getZ());
            jockey.finalizeSpawn(world, world.getCurrentDifficultyAt(position), MobSpawnType.NATURAL, null, null);

            if (handleMount(world, jockey) && world.addFreshEntity(jockey)) {
                savedData.setJockeyUUID(jockey.getUUID());
                savedData.setJockeyCooldown(-1);
                savedData.setSpawnPosition(jockey.blockPosition());
                savedData.setJockeySpawned(true);
                savedData.setDirty();
                spawnChance = defaultSpawnChance;
                return 1;
            }
        }
        return 0;
    }

    private static boolean handleMount(ServerLevel world, Jockey jockey) {
        final Mob mountEntity = Jockey.getMountEntity(world, jockey);
        if (mountEntity != null) {
            mountEntity.moveTo(jockey.position());
            jockey.startRiding(mountEntity);
            return world.addFreshEntity(mountEntity);
        }
        return false;
    }

}

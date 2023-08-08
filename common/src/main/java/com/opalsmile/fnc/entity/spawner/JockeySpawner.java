package com.opalsmile.fnc.entity.spawner;

import com.opalsmile.fnc.util.FnCSavedData;
import com.opalsmile.fnc.entity.Jackalope;
import com.opalsmile.fnc.entity.Jockey;
import com.opalsmile.fnc.registries.FnCEntities;
import com.opalsmile.fnc.platform.FnCServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;

public class JockeySpawner implements CustomSpawner {
    public static final int MAX_OFFSET = 10;
    private int spawnChance;

    private static boolean handleMount(ServerLevel world, Jockey jockey){
        final Mob mountEntity = getMountEntity(world, jockey);
        if(mountEntity != null) {
            mountEntity.moveTo(jockey.position());
            jockey.startRiding(mountEntity);
            return world.addFreshEntity (mountEntity);
        }
        return false;
    }

    @Override
    public int tick(ServerLevel level, boolean spawnFriendlies, boolean spawnEnemies){
        double successChance = FnCServices.CONFIG.getJockeySpawnChance();
        if(successChance <= 0) return 0;

        FnCSavedData savedData = FnCSavedData.get(level);

        if(savedData.hasJockeySpawned()) {
            return 0;
        }

        long jockeySpawnCooldown = savedData.getJockeyCooldown();
        if(jockeySpawnCooldown <= 0) {
            return attemptSpawnJockey(level, savedData, successChance);
        }

        savedData.setJockeyCooldown(jockeySpawnCooldown - 1);
        savedData.setDirty();
        return 0;
    }

    private int attemptSpawnJockey(ServerLevel world, FnCSavedData savedData, double successChance){
        int defaultSpawnChance = (int) (25 * successChance);
        if(spawnChance == 0) {
            spawnChance = defaultSpawnChance;
        }

        // Increasing chance, similarly to how the wandering trader functions
        int oldSpawnChance = spawnChance;
        spawnChance = Mth.clamp(spawnChance + defaultSpawnChance, 0, 75);

        if(world.random.nextInt(100) > oldSpawnChance) {
            savedData.setJockeyCooldown(FnCServices.CONFIG.jockeySpawningCooldown());
            savedData.setDirty();
            return 0;
        }

        // Extra check that is never changed, except by the config value
        ServerPlayer player = world.getRandomPlayer();
        if(player != null) {
            BlockPos position = player.blockPosition().offset(world.random.nextInt(MAX_OFFSET) - (MAX_OFFSET / 2),
                    world.random.nextInt(MAX_OFFSET) - (MAX_OFFSET / 2),
                    world.random.nextInt(MAX_OFFSET) - (MAX_OFFSET / 2));

            // Prevent suffocating
            for(BlockPos blockPos : BlockPos.betweenClosed(position.offset(-2, 0, -2), position.offset(2, 3, 2))) {
                if(!world.isEmptyBlock(blockPos)) {
                    return 0;
                }
            }

            // Floor finder
            for(BlockPos blockPos : BlockPos.betweenClosed(position.offset(-1, -1, -1), position.offset(1, -1, 1))) {
                if(world.isEmptyBlock(blockPos) && world.getFluidState(blockPos).isEmpty()) {
                    return 0;
                }
            }

            Jockey jockey = FnCEntities.JOCKEY.get().create(world);
            if(jockey == null) {
                return 0;
            }

            jockey.moveTo(position.getX(), position.getY(), position.getZ());
            jockey.finalizeSpawn(world, world.getCurrentDifficultyAt(position), MobSpawnType.NATURAL, null, null);
            if(handleMount(world, jockey) && world.addFreshEntity(jockey)) {
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

    @Nullable
    public static Mob getMountEntity(Level level, Jockey jockey){
        if(jockey.getY() < 30) {
            return EntityType.CAVE_SPIDER.create(level);
        }
        final Holder<Biome> biome = level.getBiome(jockey.blockPosition());
        if(biome.value().coldEnoughToSnow(jockey.blockPosition())) {
            //Sabertooth
            return null;
        } else if(biome.is(BiomeTags.HAS_SWAMP_HUT)) {
            Slime slime = EntityType.SLIME.create(level);
            if(slime != null) slime.setSize(2, true);
            return slime;
        } else if(biome.is(BiomeTags.IS_MOUNTAIN)) {
            Jackalope jackalope = FnCEntities.JACKALOPE.get().create(level);
            if(jackalope != null) jackalope.setSaddled(true);
            return jackalope;
        } else if(biome.is(BiomeTags.HAS_VILLAGE_PLAINS)) {
            Horse horse = EntityType.HORSE.create(level);
            if(horse != null) {
                horse.equipSaddle(SoundSource.NEUTRAL);
                horse.setBaby(true);
            }
            return horse;
        } else {
            //Summon boar
            return null;
        }
    }


}

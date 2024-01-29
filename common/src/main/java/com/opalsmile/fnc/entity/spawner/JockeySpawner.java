package com.opalsmile.fnc.entity.spawner;

import com.opalsmile.fnc.entity.Boar;
import com.opalsmile.fnc.entity.Jackalope;
import com.opalsmile.fnc.entity.Jockey;
import com.opalsmile.fnc.platform.FnCServices;
import com.opalsmile.fnc.registries.FnCEntities;
import com.opalsmile.fnc.util.JockeySavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;

/**
 * CustomSpawner for the Jockey. Ticks only in the overworld.
 */
public class JockeySpawner implements CustomSpawner {

    private int spawnChance;
    private final RandomSource randomSource = RandomSource.create();

    private static boolean handleMount(ServerLevel level, Jockey jockey){
        final Mob mountEntity = getMountEntity(level, jockey);
        if(mountEntity != null) {
            mountEntity.moveTo(jockey.position());
            jockey.startRiding(mountEntity);
            return level.addFreshEntity(mountEntity);
        }
        return false;
    }

    @Override
    public int tick(ServerLevel level, boolean spawnFriendlies, boolean spawnEnemies){
        double successChance = FnCServices.CONFIG.getJockeySpawnChance();
        if(successChance <= 0) return 0;

        JockeySavedData savedData = JockeySavedData.get(level.getServer());

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

    private int attemptSpawnJockey(ServerLevel level, JockeySavedData savedData, double successChance){
        int defaultSpawnChance = (int) (25 * successChance);
        if(spawnChance == 0) {
            spawnChance = defaultSpawnChance;
        }

        // Increasing chance, similarly to how the wandering trader functions
        int oldSpawnChance = spawnChance;
        spawnChance = Mth.clamp(spawnChance + defaultSpawnChance, 0, 75);

        if(level.random.nextInt(100) > oldSpawnChance) {
            savedData.setJockeyCooldown(FnCServices.CONFIG.jockeySpawningCooldown());
            savedData.setDirty();
            return 0;
        }

        ServerPlayer player = level.getRandomPlayer();
        if(player == null) return 0;

        BlockPos startPosition = player.blockPosition();

        BlockPos position = findSpawnPositionNear(level, startPosition, 48);

        if (position == null || !this.hasEnoughSpace(level, position)) return 0;

        Jockey jockey = FnCEntities.JOCKEY.get().create(level);

        if(jockey == null) {
            return 0;
        }

        jockey.moveTo(position.getX(), position.getY(), position.getZ());
        jockey.finalizeSpawn(level, level.getCurrentDifficultyAt(position), MobSpawnType.NATURAL, null, null);
        if(handleMount(level, jockey) && level.addFreshEntity(jockey)) {
            savedData.setJockeyUUID(jockey.getUUID());
            savedData.setJockeyCooldown(-1);
            savedData.setSpawnPosition(jockey.blockPosition());
            savedData.setDimensionId(level.dimension());
            savedData.setJockeySpawned(true);
            savedData.setDirty();
            spawnChance = defaultSpawnChance;
            FnCServices.NETWORK.broadcastJockeySpawning(level, jockey.blockPosition());
            return 1;
        }
        return 0;
    }

    @Nullable //TODO Shouldn't probably be nullable
    public static Mob getMountEntity(Level level, Jockey jockey){
        if(jockey.getY() < 30) {
            return EntityType.CAVE_SPIDER.create(level);
        }
        final Holder<Biome> biome = level.getBiome(jockey.blockPosition());
        if(biome.is(FnCServices.PLATFORM.snowBiomes())) {
            return FnCEntities.SABERTOOTH.get().create(level);
        } else if(biome.is(FnCServices.PLATFORM.swampBiomes())) {
            Slime slime = EntityType.SLIME.create(level);
            slime.setSize(2, true);
            return slime;
        } else if(biome.is(FnCServices.PLATFORM.mountainBiomes())) {
            //TODO Adjust jackalope riding position / rotation
            Jackalope jackalope = FnCEntities.JACKALOPE.get().create(level);
            jackalope.setSaddled(true);
            return jackalope;
        } else if(biome.is(FnCServices.PLATFORM.plainsBiomes())) {
            Horse horse = EntityType.HORSE.create(level);
            if(horse != null) {
                horse.equipSaddle(SoundSource.NEUTRAL);
                horse.setBaby(true);
            }
            return horse;
        } else {
            Boar boar = FnCEntities.BOAR.get().create(level);
            boar.setSaddled(true);
            return boar;
        }
    }

    //TODO test with all rides
    //TODO adjust riding position.

    @Nullable
    private BlockPos findSpawnPositionNear(LevelReader levelReader, BlockPos centerPos, int radius) {
        BlockPos targetPosition = null;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for(int i = 0; i < 10; ++i) {
            int randX = centerPos.getX() + this.randomSource.nextInt(radius * 2) - radius;
            int randZ = centerPos.getZ() + this.randomSource.nextInt(radius * 2) - radius;
            int levelHeight = levelReader.getHeight(Heightmap.Types.WORLD_SURFACE, randX, randZ);
            mutableBlockPos.set(randX, levelHeight, randZ);
            if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, levelReader, mutableBlockPos, EntityType.WANDERING_TRADER)) {
                targetPosition = mutableBlockPos;
                break;
            }
        }

        return targetPosition;
    }

    private boolean hasEnoughSpace(BlockGetter blockGetter, BlockPos blockPos) {
        for(BlockPos position : BlockPos.betweenClosed(blockPos, blockPos.offset(2, 4, 2))) {
            if (!blockGetter.getBlockState(position).getCollisionShape(blockGetter, position).isEmpty()) {
                return false;
            }
        }

        return true;
    }

}

package com.opalsmile.fnc.util;

import com.opalsmile.fnc.FnCConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.UUID;

public class JockeySavedData extends SavedData {

    //Used to keep track of the one jockey instance per overworld
    private BlockPos spawnPosition;
    private UUID jockeyUUID;
    private long jockeyCooldown;
    private boolean jockeySpawned;

    private String dimensionId = "";

    public static JockeySavedData get(MinecraftServer server){
        return server.overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(JockeySavedData::new, JockeySavedData::load, DataFixTypes.SAVED_DATA_MAP_DATA), FnCConstants.MOD_ID);
    }

    public static JockeySavedData load(final CompoundTag tag){
        JockeySavedData savedData = new JockeySavedData();
        if(tag.getBoolean("jockey_spawned")) {
            savedData.jockeySpawned = true;
            savedData.jockeyUUID = tag.getUUID("jockey_uuid");
            savedData.spawnPosition = NbtUtils.readBlockPos(tag.getCompound("jockey_position"));
            savedData.dimensionId = tag.getString("jockey_dimension");
        }
        savedData.jockeyCooldown = tag.getLong("jockey_cooldown");
        return savedData;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag){
        compoundTag.putBoolean("jockey_spawned", this.jockeySpawned);
        if(jockeySpawned) {
            compoundTag.putUUID("jockey_uuid", jockeyUUID);
            compoundTag.put("jockey_position", NbtUtils.writeBlockPos(spawnPosition));
            compoundTag.putString("jockey_dimension", dimensionId);
        }
        compoundTag.putLong("jockey_cooldown", jockeyCooldown);
        return compoundTag;
    }

    public BlockPos getSpawnPosition(){
        return spawnPosition;
    }

    public void setSpawnPosition(BlockPos spawnPosition){
        this.spawnPosition = spawnPosition;
    }

    public UUID getJockeyUUID(){
        return jockeyUUID;
    }

    public void setJockeyUUID(UUID jockeyUUID){
        this.jockeyUUID = jockeyUUID;
    }

    public boolean hasJockeySpawned(){
        return jockeySpawned;
    }

    public void setJockeySpawned(boolean jockeySpawned){
        this.jockeySpawned = jockeySpawned;
    }

    public long getJockeyCooldown(){
        return jockeyCooldown;
    }

    public void setJockeyCooldown(long jockeyCooldown){
        this.jockeyCooldown = jockeyCooldown;
    }

    public ResourceLocation getDimensionId() {
        return new ResourceLocation(dimensionId);
    }

    public void setDimensionId(ResourceKey<Level> dimension) {
        this.dimensionId = dimension.location().toString();
    }

    public void clearDimensionId() {
        this.dimensionId = null;
    }
}

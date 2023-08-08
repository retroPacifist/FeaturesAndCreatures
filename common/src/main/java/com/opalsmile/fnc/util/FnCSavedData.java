package com.opalsmile.fnc.util;

import com.opalsmile.fnc.FnCConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.UUID;

//Only attached to the overworld in theory.
public class FnCSavedData extends SavedData {

    //Used to keep track of the one jockey instance per overworld

    public static final String FNC_SAVED_CLAIM_DATA_ID = FnCConstants.MOD_ID + "_fnc_saved_claim_data";
    private BlockPos spawnPosition;
    private UUID jockeyUUID;
    private long jockeyCooldown;
    private boolean jockeySpawned;

    public static FnCSavedData get(MinecraftServer server){
        return server.overworld().getDataStorage().computeIfAbsent(FnCSavedData::load, FnCSavedData::new, FNC_SAVED_CLAIM_DATA_ID);
    }

    public static FnCSavedData load(final CompoundTag tag){
        FnCSavedData savedData = new FnCSavedData();
        if(tag.getBoolean("jockey_spawned")) {
            savedData.jockeySpawned = true;
            savedData.jockeyUUID = tag.getUUID("jockey_uuid");
            savedData.spawnPosition = NbtUtils.readBlockPos(tag.getCompound("jockey_position"));
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
}

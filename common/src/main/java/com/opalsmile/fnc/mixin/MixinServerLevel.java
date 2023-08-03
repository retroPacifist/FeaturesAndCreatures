package com.opalsmile.fnc.mixin;

import com.opalsmile.fnc.common.entity.spawner.JockeySpawner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public class MixinServerLevel {

    @Mutable
    @Shadow
    @Final
    private List<CustomSpawner> customSpawners;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void featurescritters_addFnCSpawners(MinecraftServer $$0, Executor $$1,
                                                 LevelStorageSource.LevelStorageAccess $$2, ServerLevelData $$3, ResourceKey<Level> resourceKey,
                                                 LevelStem $$5, ChunkProgressListener $$6, boolean $$7, long $$8,
                                                 List<CustomSpawner> $$9, boolean $$10, @Nullable RandomSequences $$11, CallbackInfo cInfo) {
        this.customSpawners = new ArrayList<>(this.customSpawners);
        if (resourceKey.equals(Level.OVERWORLD)) {
            this.customSpawners.add(new JockeySpawner());
        }
    }
}

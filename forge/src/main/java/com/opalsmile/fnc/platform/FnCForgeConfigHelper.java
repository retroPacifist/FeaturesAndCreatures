package com.opalsmile.fnc.platform;

import com.opalsmile.fnc.FnCConstants;
import com.opalsmile.fnc.platform.services.FnCIConfigHelper;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FnCForgeConfigHelper implements FnCIConfigHelper {


    public static double jockeySpawnChance;
    public static boolean namedJockeyDespawn;
    public static Set<MobEffect> jockeyEffectBlacklist;
    public static int jockeyDespawnTime;

    public static int jockeyCooldownTime;


    /**
     * Jockey spawn chance. Clamped between 0 and 1. Default value is 0.5
     * @return A double representing the chance for the jockey to spawn
     */
    @Override
    public double getJockeySpawnChance(){
        return jockeySpawnChance;
    }

    /**
     * Whether named jockeys should still despawn.
     *
     * @return Whether named jockeys should despawn.
     */
    @Override
    public boolean namedJockeyDespawn(){
        return namedJockeyDespawn;
    }

    /**
     * Serialized as a List<String> mapped with ResourceLocation::new and Registry#get
     * Contains Purity by default
     *
     * @return A list of mob effects that are blacklisted from appearing in the Jockey's shop
     */
    @Override
    public Set<MobEffect> jockeyEffectBlacklist(){
        return jockeyEffectBlacklist;
    }

    /**
     * The amount of in game time it takes the Jockey to despawn when completely loaded.
     * The default amount is 3 in game days (3 * 20 * 60 * 20), since a day is 20 mins
     * Clamped between 0 and max integer.
     *
     * @return The amount of time a jockey is around for
     */
    @Override
    public int jockeyDespawnTime(){
        return jockeyDespawnTime;
    }

    /**
     * The amount of time the game takes between attempting to even spawn a jockey.
     * Resets whenever there is a failed attempt.
     * Clamped between 0 and max integer.
     *
     * @return The amount of time the game waits before attempting to spawn a jockey.
     */
    @Override
    public int jockeySpawningCooldown(){
        return jockeyCooldownTime;
    }

    public static final ForgeConfigSpec GENERAL_SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> jockeyEffectBlacklistInternal;
    public static final ForgeConfigSpec.DoubleValue jockeySpawnChanceInternal;
    public static final ForgeConfigSpec.BooleanValue namedJockeyDespawnInternal;

    public static final ForgeConfigSpec.IntValue jockeyDespawnTimeInternal;
    public static final ForgeConfigSpec.IntValue jockeyCooldownTimeInternal;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();

        jockeyEffectBlacklistInternal = configBuilder.defineList("jockeyEffectBlacklist", List.of(), it -> it instanceof String str
            && ForgeRegistries.MOB_EFFECTS.containsKey(new ResourceLocation(str)));

        jockeySpawnChanceInternal = configBuilder.defineInRange("jockeySpawnChance", 0.5, 0, 1);

        namedJockeyDespawnInternal = configBuilder.comment("Used to determine whether named jockeys still despawn at the" +
                "end of their lifetime").define("namedJockeyDespawn", true);

        jockeyDespawnTimeInternal = configBuilder.defineInRange("jockeyDespawnTime", JOCKEY_DEFAULT_ALIVE_TIME, 0, Integer.MAX_VALUE);
        jockeyCooldownTimeInternal = configBuilder.defineInRange("jockeySpawningCooldown", JOCKEY_DEFAULT_COOLDOWN, 0, Integer.MAX_VALUE);

        GENERAL_SPEC = configBuilder.build();
    }

    public static void update() {
        try {
            jockeyEffectBlacklist = jockeyEffectBlacklistInternal.get().stream().map(ResourceLocation::new).map(ForgeRegistries.MOB_EFFECTS::getValue).collect(
                    Collectors.toSet());
        }
        catch (ResourceLocationException e) {
            FnCConstants.LOG.warn("Invalid resourcelocation supplied in config: ", e);
        }
        jockeySpawnChance = jockeySpawnChanceInternal.get();
        namedJockeyDespawn = namedJockeyDespawnInternal.get();
        jockeyDespawnTime = jockeyDespawnTimeInternal.get();
        jockeyCooldownTime = jockeyCooldownTimeInternal.get();
    }
}

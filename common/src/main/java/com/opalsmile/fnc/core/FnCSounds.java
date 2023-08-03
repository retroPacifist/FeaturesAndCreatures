package com.opalsmile.fnc.core;

import com.opalsmile.fnc.FnCConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import opalsmile.fnc.reg.RegistrationProvider;
import opalsmile.fnc.reg.RegistryObject;


public class FnCSounds {
    public static final RegistrationProvider<SoundEvent> REGISTRAR = RegistrationProvider.get(Registries.SOUND_EVENT, FnCConstants.MOD_ID);

    public static RegistryObject<SoundEvent> BOAR_HURT = createSound("entity.boar.hurt");
    public static RegistryObject<SoundEvent> BOAR_AMBIENT = createSound("entity.boar.ambient");
    public static RegistryObject<SoundEvent> BOAR_DEATH = createSound("entity.boar.death");
    public static RegistryObject<SoundEvent> BOAR_ATTACK = createSound("entity.boar.attack");
    public static RegistryObject<SoundEvent> BOAR_SADDLE = createSound("entity.boar.saddle");
    public static RegistryObject<SoundEvent> BOAR_STEP = createSound("entity.boar.step");

    public static RegistryObject<SoundEvent> JOCKEY_AMBIENT = createSound("entity.jockey.ambient");
    public static RegistryObject<SoundEvent> JOCKEY_HURT = createSound("entity.jockey.hurt");
    public static RegistryObject<SoundEvent> JOCKEY_DEATH = createSound("entity.jockey.death");
    public static RegistryObject<SoundEvent> JOCKEY_ATTACK = createSound("entity.jockey.attack");
    public static RegistryObject<SoundEvent> JOCKEY_YES = createSound("entity.jockey.yes");
    public static RegistryObject<SoundEvent> JOCKEY_NO = createSound("entity.jockey.no");

    public static RegistryObject<SoundEvent> JACKALOPE_HURT = createSound("entity.jackalope.hurt");
    public static RegistryObject<SoundEvent> JACKALOPE_AMBIENT = createSound("entity.jackalope.ambient");
    public static RegistryObject<SoundEvent> JACKALOPE_DEATH = createSound("entity.jackalope.death");
    public static RegistryObject<SoundEvent> JACKALOPE_SADDLE = createSound("entity.jackalope.saddle");
    public static RegistryObject<SoundEvent> JACKALOPE_STEP = createSound("entity.jackalope.step");

    public static RegistryObject<SoundEvent> SABERTOOTH_AMBIENT = createSound("entity.sabertooth.ambient");
    public static RegistryObject<SoundEvent> SABERTOOTH_HURT = createSound("entity.sabertooth.hurt");
    public static RegistryObject<SoundEvent> SABERTOOTH_ATTACK = createSound("entity.sabertooth.attack");
    public static RegistryObject<SoundEvent> SABERTOOTH_SADDLE = createSound("entity.sabertooth.saddle");
    public static RegistryObject<SoundEvent> SABERTOOTH_DEATH = createSound("entity.sabertooth.death");

    public static RegistryObject<SoundEvent> SPEAR_THROW = createSound("entity.spear.throw");
    public static RegistryObject<SoundEvent> SPEAR_ATTACK = createSound("entity.spear.attack");
    public static RegistryObject<SoundEvent> SPEAR_MISS = createSound("entity.spear.miss");

    public static RegistryObject<SoundEvent> ANTLER_HEADDRESS_ATTACK_STRONG = createSound("entity.antler_headdress.attack_strong");
    public static RegistryObject<SoundEvent> ANTLER_HEADDRESS_CHARGE = createSound("entity.antler_headdress.charge");
    public static RegistryObject<SoundEvent> ANTLER_HEADDRESS_FINISHED_CHARGING = createSound("entity.antler_headdress.finished_charging");

    public static RegistryObject<SoundEvent> ENTITY_DESADDLE = createSound("entity.saddled.desaddle");

    public static RegistryObject<SoundEvent> DOWSING_ROD_LOCATES = createSound("item.dowsing_rod.locates");

    public static RegistryObject<SoundEvent> createSound(String location) {
        return REGISTRAR.register(
                location.replace('.', '_'),
                () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(FnCConstants.MOD_ID, location), 1)
        );
    }

    public static void init() {

    }
}
package com.gapherd.gapherdutil.sound;

import com.gapherd.gapherdutil.GapherdUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GapherdUtil.MOD_ID);

    public static final RegistryObject<SoundEvent> ELECTRICITY_SOUND = registerSoundEvent("electricity_sound");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {

        ResourceLocation resourceLocation = new ResourceLocation(GapherdUtil.MOD_ID, name);

        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(resourceLocation));

    }

    public static void register(IEventBus iEventBus) {

        SOUND_EVENTS.register(iEventBus);

    }

}

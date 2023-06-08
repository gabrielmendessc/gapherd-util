package com.gapherd.gapherdutil.item;

import com.gapherd.gapherdutil.GapherdUtil;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GapherdUtil.MOD_ID);

    public static final List<RegistryObject<Item>> MOD_ITEMS = new ArrayList<>();

    public static void register(IEventBus iEventBus) {

        ITEMS.register(iEventBus);

    }



}

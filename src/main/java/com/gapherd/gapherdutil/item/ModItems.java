package com.gapherd.gapherdutil.item;

import com.gapherd.gapherdutil.GapherdUtil;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GapherdUtil.MOD_ID);
    public static final RegistryObject<Item> EMPTY_BATTERY_SHELL = ITEMS.register("empty_battery_shell", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHARGED_BATTERY_SHELL = ITEMS.register("charged_battery_shell", () -> new Item(new Item.Properties()));

    public static final DeferredRegister<Item> ITEMS_VANILLA = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public static final List<RegistryObject<Item>> MOD_ITEMS = Arrays.asList(EMPTY_BATTERY_SHELL, CHARGED_BATTERY_SHELL);

    public static void register(IEventBus iEventBus) {

        ITEMS.register(iEventBus);
        ITEMS_VANILLA.register(iEventBus);

    }



}

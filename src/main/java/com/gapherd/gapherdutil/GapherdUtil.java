package com.gapherd.gapherdutil;

import com.gapherd.gapherdutil.block.ModBlockEntities;
import com.gapherd.gapherdutil.block.ModBlocks;
import com.gapherd.gapherdutil.item.ModCreativeModeTabs;
import com.gapherd.gapherdutil.item.ModItems;
import com.gapherd.gapherdutil.menu.ModMenuTypes;
import com.gapherd.gapherdutil.menu.pump.screen.PumpScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.Objects;

@Mod(GapherdUtil.MOD_ID)
public class GapherdUtil {

    public static final String MOD_ID = "gapherdutil";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public GapherdUtil() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

    }

    private void addCreative(CreativeModeTabEvent.BuildContents event) {

        if (Objects.equals(event.getTab(), ModCreativeModeTabs.LIQUIDCRAFT_TAB)) {

            //ModItems.MOD_ITEMS.forEach(event::accept);
            ModBlocks.MOD_BLOCKS.forEach(event::accept);

        }

        if (Objects.equals(event.getTab(), CreativeModeTabs.REDSTONE_BLOCKS)) {

            event.accept(ModBlocks.PUMP_BLOCK);

        }

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            MenuScreens.register(ModMenuTypes.PUMP_MENU.get(), PumpScreen::new);

        }

    }

}

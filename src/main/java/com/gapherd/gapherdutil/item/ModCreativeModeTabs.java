package com.gapherd.gapherdutil.item;

import com.gapherd.gapherdutil.GapherdUtil;
import com.gapherd.gapherdutil.block.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GapherdUtil.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {

    public static CreativeModeTab GAPHERDUTIL_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {

        GAPHERDUTIL_TAB = event.registerCreativeModeTab(new ResourceLocation(GapherdUtil.MOD_ID, "gapherdutil_tab"), builder -> {
            builder.icon(() -> new ItemStack(ModBlocks.PUMP_BLOCK.get()))
                    .title(Component.translatable("creativemodetab.gapherdutil"));
        });

    }

}

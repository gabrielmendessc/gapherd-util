package com.gapherd.gapherdutil.block;

import com.gapherd.gapherdutil.GapherdUtil;
import com.gapherd.gapherdutil.block.pump.PumpBlock;
import com.gapherd.gapherdutil.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GapherdUtil.MOD_ID);

    public static final RegistryObject<Block> PUMP_BLOCK = registerBlock("pump_block", () -> new PumpBlock(BlockBehaviour.Properties.of(Material.STONE)
            .requiresCorrectToolForDrops()
            .strength(3.5f)));

    public static final List<RegistryObject<Block>> MOD_BLOCKS = Collections.singletonList(PUMP_BLOCK);

    public static void register(IEventBus eventBus) {

        BLOCKS.register(eventBus);

    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {

        RegistryObject<T> registryBlock = BLOCKS.register(name, block);
        registerBlockItem(name, registryBlock);

        return registryBlock;

    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {

        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));

    }

}

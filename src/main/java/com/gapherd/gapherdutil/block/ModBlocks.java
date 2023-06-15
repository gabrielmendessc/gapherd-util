package com.gapherd.gapherdutil.block;

import com.gapherd.gapherdutil.GapherdUtil;
import com.gapherd.gapherdutil.block.battery.BatteryBlock;
import com.gapherd.gapherdutil.block.pump.PumpBlock;
import com.gapherd.gapherdutil.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GapherdUtil.MOD_ID);

    public static final RegistryObject<Block> PUMP_BLOCK = registerBlock("pump_block", () -> new PumpBlock(BlockBehaviour.Properties
            .of(Material.STONE)
            .requiresCorrectToolForDrops()
            .strength(3.5f)));
    public static final RegistryObject<Block> BATTERY_BLOCK = registerBlock("battery_block", () -> new BatteryBlock(BlockBehaviour.Properties
            .of(Material.METAL, MaterialColor.COLOR_ORANGE)
            .lightLevel(BatteryBlock.getLightEmission())
            .requiresCorrectToolForDrops()
            .strength(3.0F, 6.0F)
            .sound(SoundType.COPPER)));

    public static final DeferredRegister<Block> VANILLA_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "minecraft");
    /*public static final RegistryObject<Block> LIGHTNING_ROD_BLOCK = registerBlockVanilla("lightning_rod", () -> new LightningRodBlock(BlockBehaviour.Properties
            .of(Material.METAL, MaterialColor.COLOR_ORANGE)
            .requiresCorrectToolForDrops()
            .strength(3.0F, 6.0F)
            .sound(SoundType.COPPER)
            .noOcclusion()));*/

    public static final List<RegistryObject<Block>> MOD_BLOCKS = Arrays.asList(PUMP_BLOCK, BATTERY_BLOCK);

    public static void register(IEventBus eventBus) {

        BLOCKS.register(eventBus);
        VANILLA_BLOCKS.register(eventBus);

    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {

        RegistryObject<T> registryBlock = BLOCKS.register(name, block);
        registerBlockItem(name, registryBlock);

        return registryBlock;

    }

    private static <T extends Block> RegistryObject<T> registerBlockVanilla(String name, Supplier<T> block) {

        RegistryObject<T> registryBlock = VANILLA_BLOCKS.register(name, block);
        registerBlockItemVanilla(name, registryBlock);

        return registryBlock;

    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {

        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));

    }

    private static <T extends Block> RegistryObject<Item> registerBlockItemVanilla(String name, RegistryObject<T> block) {

        return ModItems.ITEMS_VANILLA.register(name, () -> new BlockItem(block.get(), new Item.Properties()));

    }

}

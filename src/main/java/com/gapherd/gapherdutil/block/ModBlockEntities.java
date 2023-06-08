package com.gapherd.gapherdutil.block;

import com.gapherd.gapherdutil.GapherdUtil;
import com.gapherd.gapherdutil.block.pump.entity.PumpBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GapherdUtil.MOD_ID);

    public static final RegistryObject<BlockEntityType<PumpBlockEntity>> PUMP_BLOCK_ENTITY = BLOCK_ENTITIES.register("pump_block_entity", () ->
            BlockEntityType.Builder.of(PumpBlockEntity::new, ModBlocks.PUMP_BLOCK.get()).build(null)
    );

    public static void register(IEventBus iEventBus) {
        BLOCK_ENTITIES.register(iEventBus);
    }

}

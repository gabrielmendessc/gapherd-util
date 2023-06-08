package com.gapherd.gapherdutil.block.pump.entity;

import com.gapherd.gapherdutil.block.ModBlockEntities;
import com.gapherd.gapherdutil.menu.pump.PumpMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PumpBlockEntity extends DispenserBlockEntity {

    public PumpBlockEntity(BlockPos blockPos, BlockState blockState) {

        super(ModBlockEntities.PUMP_BLOCK_ENTITY.get(), blockPos, blockState);

    }

    @Override
    protected Component getDefaultName() {

        return Component.literal("Ejector");

    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new PumpMenu(id, inventory, this);
    }
}

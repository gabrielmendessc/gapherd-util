package com.gapherd.gapherdutil.block.pump;

import com.gapherd.gapherdutil.block.pump.entity.PumpBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.LavaCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Objects;

public class PumpBlock extends DropperBlock {

    private static final DispenseItemBehavior DISPENSE_BEHAVIOUR = new DefaultDispenseItemBehavior();

    public PumpBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {

        return new PumpBlockEntity(blockPos, blockState);

    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        if (!level.isClientSide()) {

            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof PumpBlockEntity) {

                player.openMenu((PumpBlockEntity) blockEntity);

            }

        }

        return InteractionResult.sidedSuccess(level.isClientSide);

    }

    @Override
    protected void dispenseFrom(ServerLevel serverLevel, BlockPos blockPos) {

        Direction facingDirection = serverLevel.getBlockState(blockPos).getValue(DispenserBlock.FACING);

        BlockPos behindBlockPos = blockPos.offset(facingDirection.getOpposite().getNormal());
        Block behindBlock = serverLevel.getBlockState(behindBlockPos).getBlock();
        if (behindBlock instanceof LavaCauldronBlock lavaCauldronBlock) {

            if (lavaCauldronBlock.isFull(null)) {

                BlockSourceImpl blockSource = new BlockSourceImpl(serverLevel, blockPos);
                PumpBlockEntity pumpBlockEntity = blockSource.getEntity();

                int slotIndex = pumpBlockEntity.getRandomSlot(serverLevel.random);
                if (slotIndex < 0) {

                    return;

                }

                ItemStack itemStack = pumpBlockEntity.getItem(slotIndex);
                if (itemStack.is(Items.BUCKET)) {

                    ItemStack ejectedStack = new ItemStack(Items.LAVA_BUCKET, itemStack.getCount());

                    Container container = HopperBlockEntity.getContainerAt(serverLevel, blockPos.relative(facingDirection));
                    if (Objects.nonNull(container)) {

                        ItemStack hopperStack = HopperBlockEntity.addItem(pumpBlockEntity, container, ejectedStack.copy().split(1), facingDirection.getOpposite());
                        if (hopperStack.isEmpty()) {

                            ejectedStack.shrink(1);

                        }

                        itemStack = ejectedStack;

                    } else {

                        itemStack = DISPENSE_BEHAVIOUR.dispense(blockSource, ejectedStack);

                    }

                    serverLevel.setBlockAndUpdate(behindBlockPos, Blocks.CAULDRON.defaultBlockState());
                    serverLevel.playSound(null, behindBlockPos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);

                    if (!itemStack.isEmpty()) {

                        itemStack = new ItemStack(Items.BUCKET, itemStack.getCount());

                    }

                    pumpBlockEntity.setItem(slotIndex, itemStack);

                    return;

                }

            }

        }

        serverLevel.playSound(null, blockPos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);

    }

}

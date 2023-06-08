package com.gapherd.gapherdutil.block.pump;

import com.gapherd.gapherdutil.block.pump.entity.PumpBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.LavaCauldronBlock;
import net.minecraft.world.level.block.PowderSnowCauldronBlock;
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

        if (behindBlock instanceof AbstractCauldronBlock cauldronBlock && cauldronBlock.isFull(serverLevel.getBlockState(behindBlockPos))) {

            BlockSourceImpl blockSource = new BlockSourceImpl(serverLevel, blockPos);
            PumpBlockEntity pumpBlockEntity = blockSource.getEntity();

            int slotIndex = pumpBlockEntity.getRandomSlot(serverLevel.random);
            if (slotIndex < 0) {

                return;

            }

            ItemStack slotStack = pumpBlockEntity.getItem(slotIndex);
            if (slotStack.is(Items.BUCKET)) {

                ItemStack ejectedStack = new ItemStack(getLiquidItem(cauldronBlock), slotStack.getCount());

                Container container = HopperBlockEntity.getContainerAt(serverLevel, blockPos.relative(facingDirection));
                if (Objects.nonNull(container)) {

                    slotStack = addToHopper(facingDirection, pumpBlockEntity, ejectedStack, container);

                } else {

                    slotStack = DISPENSE_BEHAVIOUR.dispense(blockSource, ejectedStack);

                }

                emptyCauldron(serverLevel, behindBlockPos, cauldronBlock);

                if (!slotStack.isEmpty()) {

                    slotStack = new ItemStack(Items.BUCKET, slotStack.getCount());

                }

                pumpBlockEntity.setItem(slotIndex, slotStack);

                return;

            }

        }

        serverLevel.playSound(null, blockPos, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);

    }

    private void emptyCauldron(ServerLevel serverLevel, BlockPos behindBlockPos, AbstractCauldronBlock cauldronBlock) {
        serverLevel.setBlockAndUpdate(behindBlockPos, Blocks.CAULDRON.defaultBlockState());
        serverLevel.playSound(null, behindBlockPos, getSoundEvent(cauldronBlock), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static ItemStack addToHopper(Direction facingDirection, PumpBlockEntity pumpBlockEntity, ItemStack ejectedStack, Container container) {

        ItemStack hopperStack = HopperBlockEntity.addItem(pumpBlockEntity, container, ejectedStack.copy().split(1), facingDirection.getOpposite());
        if (hopperStack.isEmpty()) {

            ejectedStack.shrink(1);

        }

        return ejectedStack;

    }

    private SoundEvent getSoundEvent(AbstractCauldronBlock cauldronBlock) {

        if (cauldronBlock instanceof LavaCauldronBlock) {

            return SoundEvents.BUCKET_FILL_LAVA;

        } else if (cauldronBlock instanceof PowderSnowCauldronBlock) {

            return SoundEvents.BUCKET_FILL_POWDER_SNOW;

        } else {

            return SoundEvents.BUCKET_FILL;

        }

    }

    private Item getLiquidItem(AbstractCauldronBlock cauldronBlock) {

        if (cauldronBlock instanceof LavaCauldronBlock) {

            return Items.LAVA_BUCKET;

        } else if (cauldronBlock instanceof PowderSnowCauldronBlock) {

            return Items.POWDER_SNOW_BUCKET;

        } else {

            return Items.WATER_BUCKET;

        }

    }

}

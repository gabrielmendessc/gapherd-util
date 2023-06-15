package com.gapherd.gapherdutil.block.battery;

import com.gapherd.gapherdutil.item.ModItems;
import com.gapherd.gapherdutil.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopperFullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.ToIntFunction;

public class BatteryBlock extends WeatheringCopperFullBlock {
    public static final IntegerProperty CHARGING_LEVEL = IntegerProperty.create("charging_level", 0, 3);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BatteryBlock(Properties properties) {

        super(WeatherState.UNAFFECTED, properties);

        this.registerDefaultState(getStateDefinition().any().setValue(CHARGING_LEVEL, 0).setValue(POWERED, false));

    }

    public static ToIntFunction<BlockState> getLightEmission() {

        return (blockState) -> {

            if (blockState.getValue(POWERED)) {

                return 20;

            }

            return switch (blockState.getValue(CHARGING_LEVEL)) {
                case 1 -> 3;
                case 2 -> 6;
                case 3 -> 9;
                default -> 0;
            };

        };

    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        if (!level.isClientSide()) {

            Item chargedBatteryShellItem = ModItems.CHARGED_BATTERY_SHELL.get();
            Item emptyBatteryShellItem = ModItems.EMPTY_BATTERY_SHELL.get();

            ItemStack itemStack = player.getItemInHand(interactionHand);
            if (emptyBatteryShellItem.equals(itemStack.getItem())) {

                if (blockState.getValue(CHARGING_LEVEL) > 0) {

                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(chargedBatteryShellItem)));

                    updateChargingLevel(level, blockPos, blockState, false);

                }

            } else if (chargedBatteryShellItem.equals(itemStack.getItem())) {

                if (blockState.getValue(CHARGING_LEVEL) < 3) {

                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(emptyBatteryShellItem)));

                    updateChargingLevel(level, blockPos, blockState, true);

                }

            } else {

                return InteractionResult.PASS;

            }

        }

        return InteractionResult.sidedSuccess(level.isClientSide());

    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPosChanged, boolean bool) {

        if (hasPoweredLightningRodChanged(blockState, level, blockPosChanged) && isInLightningRodOpposedFace(level, blockPos, blockPosChanged)) {

            onLightningStrike(level, blockPos, blockState);

        }

    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {

        updateChargingLevel(serverLevel, blockPos, blockState.setValue(POWERED, false), true);

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {

        builder.add(CHARGING_LEVEL, POWERED);

    }

    private static boolean isInLightningRodOpposedFace(Level level, BlockPos blockPos, BlockPos blockPosChanged) {

        return blockPosChanged.relative(level.getBlockState(blockPosChanged).getValue(BlockStateProperties.FACING).getOpposite()).equals(blockPos);

    }

    private static boolean hasPoweredLightningRodChanged(BlockState blockState, Level level, BlockPos blockPosChanged) {

        return level.getBlockState(blockPosChanged).getBlock() instanceof LightningRodBlock && level.getBlockState(blockPosChanged).getValue(POWERED) && !blockState.getValue(POWERED);

    }

    private void onLightningStrike(Level level, BlockPos blockPos, BlockState blockState) {

        level.setBlock(blockPos, blockState.setValue(POWERED, true), 3);
        level.scheduleTick(blockPos, this, 8);

    }

    private void updateChargingLevel(Level level, BlockPos blockPos, BlockState blockState, boolean isIncrement) {

        int chargingLevelInt = blockState.getValue(CHARGING_LEVEL);
        if (isIncrement) {

            chargingLevelInt += chargingLevelInt < 3 ? 1 : 0;

        } else {

            chargingLevelInt -= chargingLevelInt > 0 ? 1 : 0;

        }

        level.playSound(null, blockPos, ModSounds.ELECTRICITY_SOUND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        level.setBlock(blockPos, blockState.setValue(CHARGING_LEVEL, chargingLevelInt), 3);

    }

}

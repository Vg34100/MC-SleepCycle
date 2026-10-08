package net.vg.sleepcycle.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//? if >=26.1 {
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributes;
//? }
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vg.sleepcycle.block.entity.ModBlockEntities;
import net.vg.sleepcycle.block.entity.SleepingBagBlockEntity;
import net.vg.sleepcycle.util.TimeProgressionHandler;
import org.jetbrains.annotations.NotNull;

public class SleepingBagBlock extends BedBlock implements EntityBlock {
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);

    public SleepingBagBlock(DyeColor dyeColor, Properties properties) {
        super(dyeColor, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(OCCUPIED, Boolean.FALSE));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SleepingBagBlockEntity(pos, state);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level.isClientSide()) {
            //? if >=26.1 {
            return InteractionResult.SUCCESS_SERVER;
            //? } else {
            /*return InteractionResult.CONSUME;
            *///? }
        } else {
            if (blockState.getValue(PART) != BedPart.HEAD) {
                blockPos = blockPos.relative(blockState.getValue(FACING));
                blockState = level.getBlockState(blockPos);
                if (!blockState.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            //? if >=26.1 {
            BedRule bedRule = level.environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, blockPos);
            if (bedRule.explodes()) {
            //? } else {
            /*if (!BedBlock.canSetSpawn(level)) {
            *///? }
                level.removeBlock(blockPos, false);
                BlockPos blockpos = blockPos.relative(blockState.getValue(FACING).getOpposite());
                if (level.getBlockState(blockpos).is(this)) {
                    level.removeBlock(blockpos, false);
                }

                level.explode(null, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, 5.0F, true, Level.ExplosionInteraction.BLOCK);
                return InteractionResult.SUCCESS;
            } else if (blockState.getValue(OCCUPIED)) {
                player.sendSystemMessage(net.minecraft.network.chat.Component.translatable("block.minecraft.bed.occupied"));
                return InteractionResult.SUCCESS;
            } else {
                player.startSleeping(blockPos);
                player.setSleepingPos(player.blockPosition());
                level.setBlock(blockPos, blockState.setValue(OCCUPIED, Boolean.TRUE), 3);
                if (player instanceof ServerPlayer) {
                    TimeProgressionHandler.addWorld((ServerLevel) level);
                }
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }
}

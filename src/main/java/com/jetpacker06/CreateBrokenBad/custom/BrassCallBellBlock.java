package com.jetpacker06.CreateBrokenBad.custom;

import com.jetpacker06.CreateBrokenBad.register.AllBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class BrassCallBellBlock extends BaseEntityBlock {
    public BrassCallBellBlock(Properties p_49795_) {
        super(p_49795_);
    }
    public static Logger LOGGER = LogManager.getLogger();
    public static BooleanProperty DOWN = BooleanProperty.create("down");

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(DOWN)) {
            return downShape;
        } else {
            return upShape;
        }
    }

    private static final VoxelShape upShape = Stream.of(
            Block.box(7.5, 2.25, 7.5, 8.5, 4, 8.5),
            Block.box(7, 1, 7, 9, 3, 9),
            Block.box(6, 0, 6, 10, 1, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape downShape = Stream.of(
            Block.box(7.5, 2.25, 7.5, 8.5, 3.25, 8.5),
            Block.box(7, 1, 7, 9, 3, 9),
            Block.box(6, 0, 6, 10, 1, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(DOWN, false);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(DOWN);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pState.getValue(DOWN)) {
            pState = pState.setValue(DOWN, true);
            pLevel.setBlock(pPos, pState, 3);
            pLevel.playSound(pPlayer,pPos, AllSoundEvents.BRASS_CALL_BELL_DING.get(), SoundSource.BLOCKS, 2f, 1f);
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BrassCallBellBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, AllBlockEntities.BRASS_CALL_BELL.get(), BrassCallBellBlockEntity::tick);
    }
}
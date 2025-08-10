package com.Infinity.Nexus.Utils.block.entity.terraform;

import com.Infinity.Nexus.Core.itemStackHandler.RestrictedItemStackHandler;
import com.Infinity.Nexus.Core.utils.ModUtils;
import com.Infinity.Nexus.Utils.block.entity.TerraformBlockEntity;
import com.Infinity.Nexus.Utils.component.UtilsDataComponents;
import com.Infinity.Nexus.Utils.item.ModItemsUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.FakePlayer;

public class TerraformPlacer {
    public static boolean placeBlock(TerraformBlockEntity entity, BlockPos target, int[] outputSlots, int[] UPGRADE_SLOTS, FakePlayer player) {
        Level level = entity.getLevel();
        if (level == null || !level.isLoaded(target) || !level.isEmptyBlock(target)) return false;
        RestrictedItemStackHandler handler = (RestrictedItemStackHandler) entity.getItemHandler(null);
        BlockState state = level.getBlockState(target);
        if (!level.isEmptyBlock(target) && !state.getFluidState().isEmpty()) {
            level.setBlock(target, Blocks.AIR.defaultBlockState(), 3);
            level.playSound(null, target, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        
        // Verifica se o tipo do cartão é "Walls" (tipo 1)
        boolean isWallMode = false;
        for (int slot : UPGRADE_SLOTS) {
            ItemStack upgrade = handler.getStackInSlot(slot);
            if (upgrade.is(ModItemsUtils.TERRAFORM_PLACER_UPGRADE.get()) &&
                upgrade.has(UtilsDataComponents.TERRAFORM_PLACER_CARD_TYPE.get()) &&
                upgrade.getOrDefault(UtilsDataComponents.TERRAFORM_PLACER_CARD_TYPE.get(), 0) == 1) {
                isWallMode = true;
                break;
            }
        }
        
        // Se estiver no modo "Walls", verifica se o bloco está nas extremidades
        if (isWallMode) {
            BlockPos min = entity.getAreaMin();
            BlockPos max = entity.getAreaMax();
            int x = target.getX();
            int y = target.getY();
            int z = target.getZ();
            
            // Verifica se está nas extremidades (paredes)
            boolean isWall = (x == min.getX() || x == max.getX()) || 
                           (y == min.getY() || y == max.getY()) || 
                           (z == min.getZ() || z == max.getZ());
            
            if (!isWall) {
                return false; // Não coloca blocos que não estão nas paredes
            }
        }
        
        // Verifica se o randomizador está ativado em algum placer card
        boolean randomizer = false;
        for (int slot : UPGRADE_SLOTS) {
            ItemStack upgrade = handler.getStackInSlot(slot);
            if (upgrade.has(UtilsDataComponents.TERRAFORM_PLACER_CARD_RANDOMIZER.get()) &&
                upgrade.getOrDefault(UtilsDataComponents.TERRAFORM_PLACER_CARD_RANDOMIZER.get(), false)) {
                randomizer = true;
                break;
            }
        }
        int[] slotsToUse = outputSlots;
        if (randomizer) {
            java.util.List<Integer> slotList = new java.util.ArrayList<>();
            for (int s : outputSlots) slotList.add(s);
            java.util.Collections.shuffle(slotList, new java.util.Random(level.getGameTime()));
            slotsToUse = slotList.stream().mapToInt(Integer::intValue).toArray();
        }
        for (int outSlot : slotsToUse) {
            ItemStack stack = handler.getStackInSlot(outSlot);
            if (!stack.isEmpty()) {
                Block block = Block.byItem(stack.getItem());
                BlockState placeState = block.defaultBlockState();
                if (placeState != null && !placeState.isAir()) {
                    ItemStack fakeStack = stack.copy();
                    fakeStack.setCount(1);
                    player.setItemInHand(InteractionHand.MAIN_HAND, fakeStack);
                    InteractionHand hand = InteractionHand.MAIN_HAND;
                    BlockPlaceContext ctx =
                        new BlockPlaceContext(
                            player, hand, fakeStack, new BlockHitResult(
                                target.getCenter(),
                                net.minecraft.core.Direction.UP,
                                target,
                                false
                            )
                        );
                    boolean success = fakeStack.useOn(ctx).consumesAction();
                    BlockState afterState = level.getBlockState(target);
                    if (success && afterState.getBlock() == block) {
                        stack.shrink(1);
                        handler.setStackInSlot(outSlot, stack);
                        if(ModUtils.getMuffler(handler, UPGRADE_SLOTS) <= 0) {
                            level.playSound(null, entity.getBlockPos(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS, 0.1f, 1.0f);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean placeBlockAt(Level level, BlockPos target, BlockState blockState, FakePlayer player, ItemStack stack) {
        System.out.println("placeBlockAt" + blockState);
        if (level == null || !level.isLoaded(target) || !level.isEmptyBlock(target))
            return false;

        BlockState currentState = level.getBlockState(target);

        // Se há algum bloco que não seja ar, substitui por ar antes de colocar o novo
        if (!currentState.getFluidState().isEmpty()) {
            level.setBlock(target, Blocks.AIR.defaultBlockState(), 3);
            level.playSound(null, target, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
        }

        // Cria o estado do bloco a ser colocado

        if (blockState != null && !blockState.isAir()) {
            player.setItemInHand(InteractionHand.MAIN_HAND, stack);

            BlockPlaceContext ctx = new BlockPlaceContext(
                    player,
                    InteractionHand.MAIN_HAND,
                    stack,
                    new BlockHitResult(target.getCenter(), Direction.UP, target, false)
            );

            boolean success = stack.useOn(ctx).consumesAction();
            BlockState afterState = level.getBlockState(target);

            if (success && afterState.getBlock() == blockState.getBlock()) {
                return true;
            }
        }
        return false;
    }
}

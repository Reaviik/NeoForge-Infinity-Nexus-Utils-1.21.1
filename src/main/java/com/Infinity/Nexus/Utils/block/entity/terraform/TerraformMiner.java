package com.Infinity.Nexus.Utils.block.entity.terraform;

import com.Infinity.Nexus.Core.itemStackHandler.RestrictedItemStackHandler;
import com.Infinity.Nexus.Core.utils.ItemStackHandlerUtils;
import com.Infinity.Nexus.Core.utils.ModUtils;
import com.Infinity.Nexus.Utils.block.entity.TerraformBlockEntity;
import com.Infinity.Nexus.Utils.item.ModItemsUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.List;

public class TerraformMiner {
    public static boolean mineBlock(TerraformBlockEntity entity, BlockPos target, int[] outputSlots, int[] upgradeSlots, FakePlayer player) {
        Level level = entity.getLevel();
        if (level == null || !level.isLoaded(target) || level.isEmptyBlock(target)) return false;
        RestrictedItemStackHandler handler = (RestrictedItemStackHandler) entity.getItemHandler(null);
        // Só minera se houver slot livre para guardar drops
        boolean hasFreeSlot = false;
        for (int outSlot : outputSlots) {
            if (handler.getStackInSlot(outSlot).isEmpty()) {
                hasFreeSlot = true;
                break;
            }
        }
        if (!hasFreeSlot) return false;
        // Aplica os encantamentos do card de miner na picareta do fakeplayer
        ItemStack minerCard = ItemStack.EMPTY;
        for (int slot : upgradeSlots) {
            ItemStack s = handler.getStackInSlot(slot);
            if (s.is(ModItemsUtils.TERRAFORM_MINER_UPGRADE.get()) && s.isEnchanted()) {
                minerCard = s;
                break;
            }
        }
        ItemStack pickaxe = Items.NETHERITE_PICKAXE.getDefaultInstance();
        if (!minerCard.isEmpty() && minerCard.isEnchanted()) {
            if (minerCard.has(DataComponents.ENCHANTMENTS)) {
                pickaxe.set(DataComponents.ENCHANTMENTS, minerCard.get(DataComponents.ENCHANTMENTS));
            }
        }
        // Garante modo survival
        try {
            player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
        } catch (Throwable ignored) {}
        player.setItemInHand(InteractionHand.MAIN_HAND, pickaxe);
        BlockState state = level.getBlockState(target);
        if (!level.isEmptyBlock(target) && !state.getFluidState().isEmpty()) {
            level.setBlock(target, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            level.playSound(null, target, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY, net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
            return true;
        }
        if (player.gameMode.destroyBlock(target)) {
            List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, target, level.getBlockEntity(target), player, pickaxe);
            for (ItemStack drop : drops) {
                insertItemOnSelfInventory(entity, drop.copy(), outputSlots);
            }
            List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, new AABB(target).inflate(0.5));
            for (ItemEntity item : entities) {
                item.discard();
            }
            if(ModUtils.getMuffler(handler, upgradeSlots) <= 0) {
                level.playSound(null, entity.getBlockPos(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS, 0.1f, 1.0f);
            }
            return true;
        }
        return false;
    }

    private static void insertItemOnSelfInventory(TerraformBlockEntity entity, ItemStack itemStack, int[] outputSlots) {
        RestrictedItemStackHandler handler = (RestrictedItemStackHandler) entity.getItemHandler(null);
        for (int slot : outputSlots) {
            if (ModUtils.canPlaceItemInContainer(itemStack, slot, handler)) {
                ItemStackHandlerUtils.insertItem(slot, itemStack, false, handler);
                break;
            }
        }
    }
} 
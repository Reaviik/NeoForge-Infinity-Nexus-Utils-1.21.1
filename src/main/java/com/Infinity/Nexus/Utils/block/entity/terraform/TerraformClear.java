package com.Infinity.Nexus.Utils.block.entity.terraform;

import com.Infinity.Nexus.Core.itemStackHandler.RestrictedItemStackHandler;
import com.Infinity.Nexus.Core.utils.ModUtils;
import com.Infinity.Nexus.Utils.block.entity.TerraformBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayer;

public class TerraformClear {
    public static boolean clearBlock(TerraformBlockEntity entity, BlockPos target, int[] upgradeSlots, FakePlayer player) {
        Level level = entity.getLevel();
        if (level == null || !level.isLoaded(target) || level.isEmptyBlock(target)) return false;
        RestrictedItemStackHandler handler = (RestrictedItemStackHandler) entity.getItemHandler(null);
        player.setItemInHand(InteractionHand.MAIN_HAND, Items.NETHERITE_SHOVEL.getDefaultInstance());
        BlockState state = level.getBlockState(target);

        // Se for fluido, remove com balde
        if (!level.isEmptyBlock(target) && !state.getFluidState().isEmpty()) {
            level.setBlock(target, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            level.playSound(null, target, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY, net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
            return true;
        }

        if (!level.isEmptyBlock(target)) {
            if (player.gameMode.destroyBlock(target)) {
                java.util.List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, new AABB(target).inflate(0.5));
                for (ItemEntity item : entities) {
                    item.discard();
                }
                if (ModUtils.getMuffler(handler, upgradeSlots) <= 0) {
                    level.playSound(null, entity.getBlockPos(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.BLOCKS, 0.1f, 1.0f);
                }
                return true;
            }
        }
        return false;
    }
}
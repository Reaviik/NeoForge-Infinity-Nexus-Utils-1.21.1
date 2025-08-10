package com.Infinity.Nexus.Utils.screen.terraform;

import com.Infinity.Nexus.Core.itemStackHandler.RestrictedItemStackHandler;
import com.Infinity.Nexus.Core.screen.BaseAbstractContainerMenu;
import com.Infinity.Nexus.Core.slots.*;
import com.Infinity.Nexus.Utils.block.ModBlocksUtils;
import com.Infinity.Nexus.Utils.block.entity.TerraformBlockEntity;
import com.Infinity.Nexus.Utils.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class TerraformMenu extends BaseAbstractContainerMenu {
    public final TerraformBlockEntity blockEntity;
    private final Level level;
    private IEnergyStorage energyStorage;
    private final ContainerData data;
    private static final int slots = 13;

    public TerraformMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, (TerraformBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(9), new RestrictedItemStackHandler(slots));
    }

    public TerraformMenu(int pContainerId, Inventory inv, TerraformBlockEntity entity, ContainerData data, RestrictedItemStackHandler iItemHandler) {
        super(ModMenuTypes.TERRAFORM_MENU.get(), pContainerId, slots);
        checkContainerSize(inv, slots);
        blockEntity = entity;
        energyStorage = blockEntity.getEnergyStorage();
        level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.addSlot(new InputSlot(iItemHandler, 0, 116, 11));
        this.addSlot(new InputSlot(iItemHandler, 1, 134, 11));
        this.addSlot(new InputSlot(iItemHandler, 2, 116, 29));
        this.addSlot(new InputSlot(iItemHandler, 3, 134, 29));
        this.addSlot(new InputSlot(iItemHandler, 4, 116, 47));
        this.addSlot(new InputSlot(iItemHandler, 5, 134, 47));

        this.addSlot(new UpgradeSlot(iItemHandler, 6, -11, 11));
        this.addSlot(new UpgradeSlot(iItemHandler, 7, -11, 23));
        this.addSlot(new UpgradeSlot(iItemHandler, 8, -11, 35));
        this.addSlot(new UpgradeSlot(iItemHandler, 9, -11, 47));

        this.addSlot(new ComponentSlot(iItemHandler, 10, 9, 29));
        this.addSlot(new FuelSlot(iItemHandler, 11, 9, 47));
        this.addSlot(new LinkSlot(iItemHandler, 12, 9, 11));


        addDataSlots(data);
    }

    public TerraformBlockEntity getBlockEntity(){
        return blockEntity;
    }
    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public boolean hasRangeCard() {
        return blockEntity.hasRangeCard();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(this.getBlockEntity().getLevel(), this.getBlockEntity().getBlockPos()),
                pPlayer, ModBlocksUtils.TERRAFORM.get());
    }
}
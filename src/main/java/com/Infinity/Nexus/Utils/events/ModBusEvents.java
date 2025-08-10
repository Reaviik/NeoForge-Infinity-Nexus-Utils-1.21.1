package com.Infinity.Nexus.Utils.events;

import com.Infinity.Nexus.Utils.InfinityNexusUtils;
import com.Infinity.Nexus.Utils.block.entity.ModBlockEntities;
import com.Infinity.Nexus.Utils.block.entity.TerraformBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;


@EventBusSubscriber(modid = InfinityNexusUtils.MOD_ID)
public class ModBusEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntities.TERRAFORM_BE.get(), TerraformBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.TERRAFORM_BE.get(), TerraformBlockEntity::getEnergyStorage);
    }
}

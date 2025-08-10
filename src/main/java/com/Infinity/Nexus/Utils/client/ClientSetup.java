package com.Infinity.Nexus.Utils.client;

import com.Infinity.Nexus.Core.renderer.area.GenericAreaRenderer;
import com.Infinity.Nexus.Utils.InfinityNexusUtils;
import com.Infinity.Nexus.Utils.block.entity.TerraformBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Configuração do lado cliente para o mod Infinity Nexus Utils
 */
@EventBusSubscriber(modid = InfinityNexusUtils.MOD_ID, value = Dist.CLIENT)
public class ClientSetup {
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Registra o TerraformBlockEntity para renderização de área usando o sistema genérico
        GenericAreaRenderer.registerEntityForAreaRendering(TerraformBlockEntity.class);
        
        InfinityNexusUtils.LOGGER.info("Sistema de renderização de área registrado para TerraformBlockEntity");
    }
} 
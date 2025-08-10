package com.Infinity.Nexus.Utils;

import com.Infinity.Nexus.Utils.block.ModBlocksUtils;
import com.Infinity.Nexus.Utils.block.entity.ModBlockEntities;
import com.Infinity.Nexus.Utils.command.Commands;
import com.Infinity.Nexus.Utils.component.UtilsDataComponents;
import com.Infinity.Nexus.Utils.config.ModConfigs;
import com.Infinity.Nexus.Utils.item.ModItemsUtils;
import com.Infinity.Nexus.Utils.networking.ModMessages;
import com.Infinity.Nexus.Utils.screen.ModMenuTypes;
import com.Infinity.Nexus.Utils.screen.terraform.TerraformScreen;
import com.Infinity.Nexus.Utils.tab.ModTab;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(InfinityNexusUtils.MOD_ID)
public class InfinityNexusUtils {
    long time = System.currentTimeMillis();
    public static final String MOD_ID = "infinity_nexus_utils";
    public static final Logger LOGGER = LogUtils.getLogger();

    public InfinityNexusUtils(IEventBus modEventBus, ModContainer modContainer){
        NeoForge.EVENT_BUS.register(this);

        ModTab.register(modEventBus);

        ModItemsUtils.register(modEventBus);
        ModBlocksUtils.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        UtilsDataComponents.register(modEventBus);

        modEventBus.register(ModMessages.class);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::registerScreens);

        modContainer.registerConfig(ModConfig.Type.SERVER, ModConfigs.SPEC);
    }


    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        Commands.register(event.getDispatcher(), event.getBuildContext());
    }
    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.TERRAFORM_MENU.get(), TerraformScreen::new);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("   §4_____§5_   __§9__________§3_   ______§b_______  __");
        LOGGER.info("  §4/_  _§5/ | / §9/ ____/  _§3/ | / /  _§b/_  __| \\/ /");
        LOGGER.info("   §4/ /§5/  |/ §9/ /_   / /§3/  |/ // /  §b/ /   \\  / ");
        LOGGER.info(" §4_/ /§5/ /|  §9/ __/ _/ /§3/ /|  // /  §b/ /    / /  ");
        LOGGER.info("§4/___§5/_/ |_§9/_/   /___§3/_/ |_/___/ §b/_/    /_/   ");
        LOGGER.info("§b             Infinty Nexus Utils");
        LOGGER.info("§cTempo de carregamento: {} ms", System.currentTimeMillis() - time);
    }
}

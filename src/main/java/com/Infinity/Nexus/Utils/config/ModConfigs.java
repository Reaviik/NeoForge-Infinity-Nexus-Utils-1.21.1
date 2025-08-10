package com.Infinity.Nexus.Utils.config;

import com.Infinity.Nexus.Utils.InfinityNexusUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = InfinityNexusUtils.MOD_ID)
public class ModConfigs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    //-----------------------------------------------Terraform-----------------------------------------------//
    //ENERGY
    private static final ModConfigSpec.IntValue TERRAFORM_ENERGY = BUILDER.comment("Defines the amount of energy that the Terraform will store").defineInRange("terraform_energy_capacity", 150000, 1, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue TERRAFORM_ENERGY_TRANSFER = BUILDER.comment("Defines the amount of energy that the Terraform will transfer").defineInRange("terraform_energy_transfer", 100000, 1, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue TERRAFORM_ENERGY_COST_PER_OPERATION = BUILDER.comment("Defines the amount of energy that the Terraform will consume per operation").defineInRange("terraform_energy_cost_per_operation", 100, 1, Integer.MAX_VALUE);
    //TICK
    private static final ModConfigSpec.IntValue TERRAFORM_OPERATION_TICK = BUILDER.comment("Defines the number of ticks that the Terraform will take to process each block").defineInRange("terraform_operation_tick_count", 5, 1, Integer.MAX_VALUE);
    //RANGE
    private static final ModConfigSpec.ConfigValue<Boolean> TERRAFORM_COST_CARD_DURABILITY = BUILDER
            .comment("Define if Range Card use durability when remove block, include air")
            .define("terraform_cost_range_card_durability", true);
    private static final ModConfigSpec.ConfigValue<Boolean> TERRAFORM_COST_CARD_DURABILITY_LIMIT = BUILDER
            .comment("Define if Range Card use more than default durability when remove block, include air")
            .define("terraform_cost_range_card_extra_durability", true);
    private static final ModConfigSpec.ConfigValue<Integer> TERRAFORM_DEFAULT_RANGE = BUILDER
            .comment("Range card default range³, put 0 to disable")
            .comment("To custom range you can give a custom Range Card giving /give <playerName> infinity_nexus_utils:terraform_range_card[infinity_nexus_utils.range=<value>]")
            .define("terraform_default_area", 4096);
    private static final ModConfigSpec.ConfigValue<Integer> TERRAFORM_MAX_RANGE = BUILDER
            .comment("Range card max range³, put 0 to disable")
            .comment("To custom range you can give a custom Range Card giving /give <playerName> infinity_nexus_utils:terraform_range_card[infinity_nexus_utils.range=<value>]")
            .comment("Custom range above this limit not applied")
            .define("terraform_max_area", 1000000);
    //MISC
    private static final ModConfigSpec.ConfigValue<Boolean> TERRAFORM_SKIP_BLOCK_ENTITIES = BUILDER
            .comment("Defines if the Terraform will ignore BlockEntities (ex: Furnace, Dispenser, etc.)")
            .define("terraform_skip_block_entities", true);
    private static final ModConfigSpec.ConfigValue<Boolean> TERRAFORM_AIR_BLOCKS_COST_DURABILITY = BUILDER
            .comment("Defines if the Terraform will consume upgrade area when placing blocks in empty spaces")
            .define("terraform_air_blocks_cost_durability", true);

    //BlackList
    private static final ModConfigSpec.ConfigValue<List<? extends String>> TERRAFORM_BLOCKS_BLACKLIST = BUILDER
            .comment("List of blocks not breakable from terraform")
            .defineList("terraform_blocks_blacklist", List.of(
                            "minecraft:bedrock"),
                    o -> o instanceof String
            );
    //Deny Dimensions
    private static final ModConfigSpec.ConfigValue<List<? extends String>> TERRAFORM_DIMENSIONS_BLACKLIST = BUILDER
            .comment("List of denied dimensions for place terraform")
            .defineList("terraform_dimensions_blacklist", List.of(),
                    o -> o instanceof String
            );

    public static final ModConfigSpec SPEC = BUILDER.build();

    //-----------------------------------------------Terraform-----------------------------------------------//
    public static int terraformEnergyCapacity;
    public static int terraformEnergyTransfer;
    public static int terraformEnergyCostPerOperation;
    public static int terraformDefaultArea;
    public static int terraformMaxArea;
    public static int terraformOperationTicks;
    public static boolean terraformSkipBlockEntities;
    public static boolean terraformAirBlocksCostDurability;
    public static boolean terraformCostCardExtraDurability;
    public static boolean terraformCostCardDurability;
    public static List<String> terraformBlocksBlacklist = new ArrayList<>();
    public static List<String> terraformDimensionsBlacklist = new ArrayList<>();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        //-----------------------------------------------Terraform-----------------------------------------------//
        terraformEnergyCapacity = TERRAFORM_ENERGY.get();
        terraformEnergyTransfer = TERRAFORM_ENERGY_TRANSFER.get();
        terraformEnergyCostPerOperation = TERRAFORM_ENERGY_COST_PER_OPERATION.get();
        terraformDefaultArea = TERRAFORM_DEFAULT_RANGE.get();
        terraformMaxArea = TERRAFORM_MAX_RANGE.get();
        terraformOperationTicks = TERRAFORM_OPERATION_TICK.get();
        terraformSkipBlockEntities = TERRAFORM_SKIP_BLOCK_ENTITIES.get();
        terraformAirBlocksCostDurability = TERRAFORM_AIR_BLOCKS_COST_DURABILITY.get();
        terraformCostCardExtraDurability = TERRAFORM_COST_CARD_DURABILITY_LIMIT.get();
        terraformCostCardDurability = TERRAFORM_COST_CARD_DURABILITY.get();
        terraformBlocksBlacklist = new ArrayList<>(TERRAFORM_BLOCKS_BLACKLIST.get());
        terraformDimensionsBlacklist = new ArrayList<>(TERRAFORM_DIMENSIONS_BLACKLIST.get());
    }
}
package com.Infinity.Nexus.Utils.component;

import com.Infinity.Nexus.Utils.InfinityNexusUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagNetworkSerialization;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

/*
*
* Credits
* https://github.com/InnovativeOnlineIndustries/Industrial-Foregoing/blob/1.21/src/main/java/com/buuz135/industrial/api/IMachineSettings.java#L25
*
*
*/
public class UtilsDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, InfinityNexusUtils.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TERRAFORM_RANGE_CARD_RANGE = register("terraform_range_card_range", op -> op.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TERRAFORM_RANGE_CARD_RANGE_PERSISTENT = register("terraform_range_card_range_persistent", op -> op.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> TERRAFORM_RANGE_CARD_PERSISTENT = register("terraform_range_card_persistent",  op -> op.persistent(Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TERRAFORM_RANGE_CARD_TYPE = register("terraform_range_card_type", op -> op.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> TERRAFORM_PLACER_CARD_RANDOMIZER = register("terraform_placer_card_randomizer",  op -> op.persistent(Codec.BOOL));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TERRAFORM_PLACER_CARD_TYPE = register("terraform_placer_card_type", op -> op.persistent(Codec.INT));


    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
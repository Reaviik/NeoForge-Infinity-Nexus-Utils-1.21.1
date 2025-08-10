package com.Infinity.Nexus.Utils.networking.packet;

import com.Infinity.Nexus.Core.utils.GetResourceLocation;
import com.Infinity.Nexus.Utils.InfinityNexusUtils;
import com.Infinity.Nexus.Utils.block.entity.TerraformBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleTerraformingC2SPacket(BlockPos pos, boolean enabled) implements CustomPacketPayload {
    public static final Type<ToggleTerraformingC2SPacket> TYPE =
            new Type<>(GetResourceLocation.withNamespaceAndPath(InfinityNexusUtils.MOD_ID, "toggle_terraforming"));

    public static final StreamCodec<FriendlyByteBuf, ToggleTerraformingC2SPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ToggleTerraformingC2SPacket::pos,
            ByteBufCodecs.BOOL,
            ToggleTerraformingC2SPacket::enabled,
            ToggleTerraformingC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ToggleTerraformingC2SPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            if (level.getBlockEntity(packet.pos()) instanceof TerraformBlockEntity blockEntity) {
                blockEntity.setTerraformingEnabled(packet.enabled());
                blockEntity.setChanged();

                level.sendBlockUpdated(packet.pos(),
                        blockEntity.getBlockState(),
                        blockEntity.getBlockState(),
                        3);
            }
        });
    }
} 
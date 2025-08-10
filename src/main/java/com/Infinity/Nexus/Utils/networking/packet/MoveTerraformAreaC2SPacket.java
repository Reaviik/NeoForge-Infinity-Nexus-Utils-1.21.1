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

public record MoveTerraformAreaC2SPacket(BlockPos pos, String axis, int delta) implements CustomPacketPayload {
    public static final Type<MoveTerraformAreaC2SPacket> TYPE =
            new Type<>(GetResourceLocation.withNamespaceAndPath(InfinityNexusUtils.MOD_ID, "move_terraform_area"));

    public static final StreamCodec<FriendlyByteBuf, MoveTerraformAreaC2SPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            MoveTerraformAreaC2SPacket::pos,
            ByteBufCodecs.STRING_UTF8,
            MoveTerraformAreaC2SPacket::axis,
            ByteBufCodecs.INT,
            MoveTerraformAreaC2SPacket::delta,
            MoveTerraformAreaC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MoveTerraformAreaC2SPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player == null) return;

            ServerLevel level = player.serverLevel();
            if (level.getBlockEntity(packet.pos()) instanceof TerraformBlockEntity blockEntity) {
                blockEntity.moveArea(packet.axis(), packet.delta());
                blockEntity.setChanged();
                level.sendBlockUpdated(packet.pos(),
                        blockEntity.getBlockState(),
                        blockEntity.getBlockState(),
                        3);
            }
        });
    }
} 
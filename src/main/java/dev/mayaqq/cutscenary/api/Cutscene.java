package dev.mayaqq.cutscenary.api;

import dev.mayaqq.cutscenary.Cutscenary;
import dev.mayaqq.cutscenary.client.CutsceneRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public record Cutscene(Identifier id, Text[] lines) {

    @Environment(EnvType.CLIENT)
    public void play() {
        CutsceneRenderer.renderTextCutScene(this);
    }

    public void play(ServerPlayerEntity player) {
        play(id, player);
    }

    @Deprecated
    public static void playTextCutscene(ServerPlayerEntity player, String line) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(line);
        ServerPlayNetworking.send(player, Cutscenary.id("cutscenewithtext"), buf);
    }

    public static void play(Identifier id, ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(id);
        ServerPlayNetworking.send(player, Cutscenary.id("cutscene"), buf);
    }
}

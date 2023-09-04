package dev.mayaqq.cutscenary.client;

import dev.mayaqq.cutscenary.Cutscenary;
import dev.mayaqq.cutscenary.api.CutscenaryRegistries;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class CutscenaryS2CPackets {

    public static final Identifier CUTSCENE = Cutscenary.id("cutscene");
    public static final Identifier CUTSCENE_WITH_TEXT = Cutscenary.id("cutscenewithtext");

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(CUTSCENE_WITH_TEXT, (client, handler, buf, responseSender) -> {
            String message = buf.readString();
            client.execute(() -> {
                CutsceneRenderer.renderTextCutScene(message);
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(CUTSCENE, (client, handler, buf, responseSender) -> {
            Identifier id = buf.readIdentifier();
            client.execute(() -> {
                CutscenaryRegistries.CUTSCENE.get(id).play();
            });
        });
    }
}

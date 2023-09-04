package dev.mayaqq.cutscenary.client;

import net.fabricmc.api.ClientModInitializer;

public class CutscenaryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CutsceneRenderer.init();
        CutscenaryKeybinds.register();
        CutscenaryS2CPackets.register();
    }
}

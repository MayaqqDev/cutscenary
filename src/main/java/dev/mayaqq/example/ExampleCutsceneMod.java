package dev.mayaqq.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class ExampleCutsceneMod implements ModInitializer {
    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ExampleCutsceneRegistry.register();
        }
    }
}

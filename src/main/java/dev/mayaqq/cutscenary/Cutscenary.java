package dev.mayaqq.cutscenary;

import dev.mayaqq.cutscenary.commands.CutscenaryCommands;
import dev.mayaqq.cutscenary.config.CutscenaryConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cutscenary implements ModInitializer {
    public static final String MOD_ID = "cutscenary";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Cutscenary is initializing...");
        CutscenaryCommands.register();
        CutscenaryConfig.INSTANCE.load();
    }
}

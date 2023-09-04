package dev.mayaqq.cutscenary.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.mayaqq.cutscenary.config.CutscenaryConfig;

public class ModMenuIntegration implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CutscenaryConfig.INSTANCE::makeScreen;
    }
}
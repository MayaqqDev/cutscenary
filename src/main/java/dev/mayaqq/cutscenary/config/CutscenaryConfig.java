package dev.mayaqq.cutscenary.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.impl.controller.TickBoxControllerBuilderImpl;
import dev.mayaqq.cutscenary.Cutscenary;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.nio.file.Files;
import java.nio.file.Path;

public class CutscenaryConfig {
    public static final CutscenaryConfig INSTANCE = new CutscenaryConfig();

    public final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("cutscenary.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean cutSceneTextCentered = false;

    public void save() {
        try {
            Files.deleteIfExists(configFile);

            JsonObject json = new JsonObject();
            json.addProperty("cutSceneTextCentered", cutSceneTextCentered);

            Files.writeString(configFile, gson.toJson(json));
        } catch (Exception e) {
            Cutscenary.LOGGER.error("Failed to load config file!");
        }
    }

    public void load() {
        try {
            if (Files.notExists(configFile)) {
                save();
                return;
            }

            JsonObject json = gson.fromJson(Files.readString(configFile), JsonObject.class);

            if (json.has("cutSceneTextCentered"))
                cutSceneTextCentered = json.getAsJsonPrimitive("cutSceneTextCentered").getAsBoolean();
        } catch (Exception e) {
            Cutscenary.LOGGER.error("Failed to load config file!");
        }
    }

    public Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("Cutscenary Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Text"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.of("Center Text"))
                                .binding(
                                        false,
                                        () -> cutSceneTextCentered,
                                        value -> cutSceneTextCentered = value
                                )
                                .controller(TickBoxControllerBuilderImpl::new)
                                .build())
                        .build())
                .save(this::save)
                .build()
                .generateScreen(parent);
    }
}

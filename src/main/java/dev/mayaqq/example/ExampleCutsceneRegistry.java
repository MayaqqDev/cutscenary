package dev.mayaqq.example;

import dev.mayaqq.cutscenary.api.CutscenaryRegistries;
import dev.mayaqq.cutscenary.api.Cutscene;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ExampleCutsceneRegistry {
    public static final Cutscene EXAMPLE_CUTSCENE = Registry.register(CutscenaryRegistries.CUTSCENE, new Identifier("example", "example_cutscene"), new Cutscene(
            new Identifier("example", "example_cutscene"),
            new Text[]{Text.of("example cutscene is here! you can summon this cutscene using §a/cutscene example:example_cutscene")}
    ));

    public static void register() {}
}

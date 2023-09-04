package dev.mayaqq.cutscenary.api;

import com.mojang.serialization.Lifecycle;
import dev.mayaqq.cutscenary.Cutscenary;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;

public class CutscenaryRegistries {
    public static final RegistryKey<Registry<Cutscene>> CUTSCENE_KEY = RegistryKey.ofRegistry(Cutscenary.id("cutscene"));
    public static final Registry<Cutscene> CUTSCENE = new SimpleRegistry<>(CUTSCENE_KEY, Lifecycle.stable(), false);
}
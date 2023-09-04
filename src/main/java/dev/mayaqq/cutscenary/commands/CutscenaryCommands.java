package dev.mayaqq.cutscenary.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.mayaqq.cutscenary.api.CutscenaryRegistries;
import dev.mayaqq.cutscenary.api.Cutscene;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class CutscenaryCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            LiteralCommandNode<ServerCommandSource> cutscene = CommandManager.literal("cutscene").requires(source -> source.hasPermissionLevel(4)).build();

            ArgumentCommandNode<ServerCommandSource, Identifier> cutsceneId = CommandManager.argument("cutsceneId", IdentifierArgumentType.identifier()).suggests(new CutsceneSuggestionProvider()).executes(CutscenaryCommands::cutsceneCommand).build();

            dispatcher.getRoot().addChild(cutscene);
            cutscene.addChild(cutsceneId);
        });
    }

    public static int cutsceneCommand(CommandContext<ServerCommandSource> context) {
        Cutscene cutscene = CutscenaryRegistries.CUTSCENE.get(IdentifierArgumentType.getIdentifier(context, "cutsceneId"));
        cutscene.play(context.getSource().getPlayer());
        return 0;
    }
}
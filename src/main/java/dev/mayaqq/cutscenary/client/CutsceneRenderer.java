package dev.mayaqq.cutscenary.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.mayaqq.cutscenary.api.Cutscene;
import dev.mayaqq.cutscenary.config.CutscenaryConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

public class CutsceneRenderer {

    static boolean cutSceneInProgress = false;
    static int cutScenePhase = 0;

    static int renderTick = 0;
    static int duration = 0;
    static ArrayList<String> cutSceneBuffer = new ArrayList<>();
    static String text = "";
    static int textPhase = 0;
    static boolean shouldContinue = true;
    static String fullText = "";

    private static boolean escPressed(MinecraftClient client) {
        return GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS;
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (escPressed(client) && cutSceneInProgress) {
                cutSceneBuffer.clear();
                cutSceneInProgress = false;
                cutScenePhase = 0;
                text = "";
                textPhase = 0;
                shouldContinue = true;
            }
            if (client.options.hudHidden && cutSceneInProgress) {
                client.options.hudHidden = false;
            }
            if (CutscenaryKeybinds.cutscene.wasPressed() && cutSceneInProgress) {
                cutScenePhase++;
            }
        });

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            if (!cutSceneBuffer.isEmpty()) {
                fullText = cutSceneBuffer.get(0);
                cutSceneInProgress = true;
            }
            if (cutSceneInProgress) {
                duration++;
                renderTick++;
                if (renderTick == 5) renderTick = 0;
                MinecraftClient client = MinecraftClient.getInstance();
                client.options.hudHidden = false;

                int windowWidth = client.getWindow().getScaledWidth();
                int windowHeight = client.getWindow().getScaledHeight();

                int white = 0xFFFFFFFF;
                int black = 0x80000000;

                client.getFramebuffer().beginWrite(false);
                RenderSystem.enableDepthTest();

                TextRenderer textRenderer = client.textRenderer;
                MatrixStack matrixStack = matrices.getMatrices();
                matrixStack.push();
                matrixStack.translate(0, 0, 1000);

                matrices.fill(0, 0, windowWidth, windowHeight, black);
                if (renderTick == 0 && shouldContinue) {
                    if (textPhase == fullText.length()) {
                        shouldContinue = false;
                    } else {
                        char c = fullText.charAt(textPhase);
                        if (c == '<' && fullText.substring(textPhase).contains(">")) {
                            while (fullText.charAt(textPhase) != '>' && textPhase < fullText.length()) {
                                text += fullText.charAt(textPhase);
                                textPhase++;
                            }
                            for (int i = 0; i < 2; i++) {
                                text += fullText.charAt(textPhase);
                                textPhase++;
                            }
                        } else if (c == '§') {
                            for (int i = 0; i < 2; i++) {
                                text += fullText.charAt(textPhase);
                                textPhase++;
                            }
                        } else {
                            text += fullText.charAt(textPhase);
                            textPhase++;
                        }
                    }
                }

                int x = CutscenaryConfig.INSTANCE.cutSceneTextCentered ? windowWidth / 2 - (textRenderer.getWidth(text) / 2) : windowWidth / 2 - textRenderer.getWidth(fullText) / 2;
                matrices.drawTextWithShadow(textRenderer, text, x, windowHeight / 2 - 20, white);

                String pressText = "Press " + CutscenaryKeybinds.cutscene.getBoundKeyLocalizedText().getString() + " to continue";
                matrices.drawTextWithShadow(textRenderer, pressText, windowWidth - textRenderer.getWidth(pressText) - 5, windowHeight - textRenderer.fontHeight - 5, white);

                matrixStack.pop();
                RenderSystem.disableDepthTest();

                client.getFramebuffer().endWrite();
                if (cutScenePhase == 1 || duration > 1000) {
                    duration = 0;
                    cutSceneBuffer.remove(0);
                    cutSceneInProgress = false;
                    cutScenePhase = 0;
                    text = "";
                    textPhase = 0;
                    shouldContinue = true;
                }
            }
        });
    }

    public static void renderTextCutScene(String text) {
        cutSceneBuffer.add(text);
    }

    public static void renderTextCutScene(Cutscene cutsceneInstance) {
        for (Text line : cutsceneInstance.lines()) {
            cutSceneBuffer.add(line.getString());
        }
    }
}
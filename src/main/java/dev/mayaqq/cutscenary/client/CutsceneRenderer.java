package dev.mayaqq.cutscenary.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.mayaqq.cutscenary.api.Cutscene;
import dev.mayaqq.cutscenary.config.CutscenaryConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

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

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            handleEscapeKey(client);
            handleHudHidden(client);
            handleCutsceneKeybind(client);
        });

        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            if (!cutSceneBuffer.isEmpty()) {
                startCutscene();
            }

            if (cutSceneInProgress) {
                renderCutscene(matrices);
            }
        });
    }

    private static void handleEscapeKey(MinecraftClient client) {
        if (GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS && cutSceneInProgress) {
            resetCutscene();
        }
    }

    private static void handleHudHidden(MinecraftClient client) {
        if (client.options.hudHidden && cutSceneInProgress) {
            client.options.hudHidden = false;
        }
    }

    private static void handleCutsceneKeybind(MinecraftClient client) {
        if (CutscenaryKeybinds.cutscene.wasPressed() && cutSceneInProgress) {
            cutScenePhase++;
        }
    }

    private static void startCutscene() {
        fullText = cutSceneBuffer.get(0);
        cutSceneInProgress = true;
    }

    private static void resetCutscene() {
        cutSceneBuffer.clear();
        cutSceneInProgress = false;
        cutScenePhase = 0;
        text = "";
        textPhase = 0;
        shouldContinue = true;
    }

    private static void renderCutscene(DrawContext matrices) {
        duration++;
        renderTick++;

        if (renderTick == 5) {
            renderTick = 0;
        }

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
                text += fullText.charAt(textPhase);
                textPhase++;
            }
        }

        int textX, textY;
        if (CutscenaryConfig.INSTANCE.cutSceneTextCentered) {
            textX = windowWidth / 2;
            textY = windowHeight / 2 - 20;
        } else {
            int center = windowWidth / 2 - textRenderer.getWidth(fullText) / 2;
            textX = center;
            textY = windowHeight / 2 - 20;
        }

        matrices.drawCenteredTextWithShadow(textRenderer, text, textX, textY, white);

        String pressText = "Press " + CutscenaryKeybinds.cutscene.getBoundKeyLocalizedText().getString() + " to continue";
        matrices.drawText(textRenderer, pressText, windowWidth - textRenderer.getWidth(pressText) - 5, windowHeight - textRenderer.fontHeight - 5, white, true);

        matrixStack.pop();
        RenderSystem.disableDepthTest();

        client.getFramebuffer().endWrite();

        if (cutScenePhase == 1 || duration > 1000) {
            resetCutscene();
        }
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
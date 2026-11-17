package pl.purpleclient.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.FilterMode;
import net.dimaskama.mcef.api.MCEFApi;
import net.dimaskama.mcef.api.MCEFBrowser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3x2f;

/**
 * YouTube Music rendered by an embedded Chromium browser inside Minecraft.
 * Google credentials are entered only on Google's own sign-in page.
 */
public final class YouTubeMusicBrowserScreen extends Screen {
    private final Screen parent;
    private MCEFBrowser browser;
    private boolean initializationRequested;
    private boolean removed;

    public YouTubeMusicBrowserScreen(Screen parent) {
        super(Component.literal("YouTube Music"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        if (browser != null) {
            browser.resize(width, height);
            browser.setFocus(true);
            return;
        }
        if (initializationRequested) return;

        initializationRequested = true;
        MCEFApi.getInstanceFuture().whenComplete((api, error) -> {
            Minecraft client = Minecraft.getInstance();
            client.execute(() -> {
                if (removed || client.screen != this) return;
                if (error != null) return;

                try {
                    browser = api.createBrowser("https://music.youtube.com/", false);
                    browser.resize(width, height);
                    browser.setFocus(true);
                } catch (RuntimeException ignored) {
                    browser = null;
                }
            });
        });
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        if (browser == null) return;

        var texture = browser.getTextureView();
        if (texture != null) {
            graphics.guiRenderState.addGuiElement(new BlitRenderState(
                RenderPipelines.GUI_TEXTURED,
                TextureSetup.singleTexture(
                    texture,
                    RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
                ),
                new Matrix3x2f(graphics.pose()),
                0, 0, width, height,
                0.0F, 1.0F, 0.0F, 1.0F,
                0xFFFFFFFF,
                graphics.scissorStack.peek()
            ));
        }
        graphics.requestCursor(browser.getCursorType());
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubled) {
        if (browser != null) browser.onMouseClicked(event, doubled);
        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (browser != null) browser.onMouseReleased(event);
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (browser != null) browser.onMouseScrolled((int) mouseX, (int) mouseY, verticalAmount);
        return true;
    }

    @Override
    public void mouseMoved(double x, double y) {
        if (browser != null) browser.onMouseMoved((int) x, (int) y);
        super.mouseMoved(x, y);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (browser != null) browser.onKeyPressed(event);
        return true;
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        if (browser != null) browser.onKeyReleased(event);
        return true;
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (browser != null) browser.onCharTyped(event);
        return true;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().gui.setScreen(parent);
    }

    @Override
    public void removed() {
        removed = true;
        if (browser != null) {
            browser.close();
            browser = null;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

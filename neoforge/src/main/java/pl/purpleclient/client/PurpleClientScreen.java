package pl.purpleclient.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class PurpleClientScreen extends Screen {
    public PurpleClientScreen() {
        super(Component.literal("Purple Client"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.addRenderableWidget(Button.builder(Component.literal("Ustawienia"), button ->
                Minecraft.getInstance().setScreen(new PurpleClientSettingsScreen(this)))
            .bounds(centerX - 100, centerY - 4, 200, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("Zamknij"), button ->
                Minecraft.getInstance().setScreen(null))
            .bounds(centerX - 100, centerY + 24, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        int left = this.width / 2 - 132;
        int top = this.height / 2 - 88;
        graphics.fill(left, top, left + 264, top + 174, 0xF010101B);
        graphics.fill(left, top, left + 264, top + 3, 0xFF8B50E8);
        graphics.drawCenteredString(this.font, "PURPLE CLIENT", this.width / 2, top + 24, 0xFFE9D9FF);
        graphics.drawCenteredString(this.font, "Menu klienta", this.width / 2, top + 48, 0xFFB9A5D8);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}

package pl.purpleclient.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class PurpleClientSettingsScreen extends Screen {
    private final Screen parent;

    public PurpleClientSettingsScreen(Screen parent) {
        super(Component.literal("Purple Client - Ustawienia"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.literal("Powrót"), button ->
                Minecraft.getInstance().setScreen(this.parent))
            .bounds(this.width / 2 - 100, this.height / 2 + 42, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        int left = this.width / 2 - 132;
        int top = this.height / 2 - 72;
        graphics.fill(left, top, left + 264, top + 144, 0xF010101B);
        graphics.fill(left, top, left + 264, top + 3, 0xFF8B50E8);
        graphics.drawCenteredString(this.font, "USTAWIENIA PURPLE CLIENT", this.width / 2, top + 22, 0xFFE9D9FF);
        graphics.drawCenteredString(this.font, "Ustawienia moda będą dostępne tutaj.", this.width / 2, top + 55, 0xFFB9A5D8);
        super.render(graphics, mouseX, mouseY, partialTick);
    }
}

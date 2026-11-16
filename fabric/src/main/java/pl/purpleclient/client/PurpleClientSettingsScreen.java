package pl.purpleclient.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class PurpleClientSettingsScreen extends Screen {
    private final Screen parent;
    private int panelLeft;
    private int panelTop;
    private int panelWidth;
    private int panelHeight;

    public PurpleClientSettingsScreen(Screen parent) {
        super(Component.literal("Purple Client - Ustawienia"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        panelWidth = Math.min(520, this.width - 32);
        panelHeight = Math.min(290, this.height - 32);
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = (this.height - panelHeight) / 2;
        this.addRenderableWidget(Button.builder(Component.literal("←  Powrót"), b ->
                Minecraft.getInstance().setScreen(this.parent))
            .bounds(panelLeft + 18, panelTop + panelHeight - 48, 112, 27).build());
        this.addRenderableWidget(Button.builder(Component.literal("Zamknij"), b ->
                Minecraft.getInstance().setScreen(null))
            .bounds(panelLeft + panelWidth - 112, panelTop + panelHeight - 48, 94, 27).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        panelWidth = Math.min(520, this.width - 32);
        panelHeight = Math.min(290, this.height - 32);
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = (this.height - panelHeight) / 2;
        int right = panelLeft + panelWidth - 18;

        graphics.fill(panelLeft - 2, panelTop - 2, panelLeft + panelWidth + 2, panelTop + panelHeight + 2, 0x805E35A0);
        graphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + panelHeight, 0xF0151420);
        graphics.fillGradient(panelLeft, panelTop, panelLeft + panelWidth, panelTop + 4, 0xFFB47CFF, 0xFF7040D8);
        graphics.drawString(this.font, "PURPLE CLIENT  /  PREFERENCJE", panelLeft + 20, panelTop + 22, 0xFFBFA6E8, false);
        graphics.drawString(this.font, "Ustawienia", panelLeft + 20, panelTop + 45, 0xFFF4EDFF, true);
        graphics.drawString(this.font, "Personalizacja klienta", panelLeft + 20, panelTop + 63, 0xFFB6A8C9, false);

        int rowTop = panelTop + 94;
        graphics.fill(panelLeft + 18, rowTop, right, rowTop + 62, 0xFF211C2C);
        graphics.drawString(this.font, "Skrót klawiszowy", panelLeft + 32, rowTop + 14, 0xFFF4EDFF, true);
        graphics.drawString(this.font, "Right Shift — zmień go w Opcje → Sterowanie.", panelLeft + 32, rowTop + 34, 0xFFB6A8C9, false);

        graphics.fill(panelLeft + 18, rowTop + 74, right, rowTop + 136, 0xFF211C2C);
        graphics.drawString(this.font, "Wygląd", panelLeft + 32, rowTop + 88, 0xFFF4EDFF, true);
        graphics.drawString(this.font, "Ciemny motyw z fioletowym akcentem.", panelLeft + 32, rowTop + 108, 0xFFB6A8C9, false);

        graphics.drawString(this.font, "Więcej przełączników dodamy wraz z funkcjami klienta.", panelLeft + 20,
            panelTop + panelHeight - 69, 0xFF8F829F, false);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
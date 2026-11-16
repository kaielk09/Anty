package pl.purpleclient.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class PurpleClientScreen extends Screen {
    private static final int PURPLE = 0xFF9B65FF;
    private static final int TEXT = 0xFFF4EDFF;
    private static final int MUTED = 0xFFB6A8C9;
    private static final int PANEL = 0xF0151420;
    private int panelLeft;
    private int panelTop;
    private int panelWidth;
    private int panelHeight;

    public PurpleClientScreen() {
        super(Component.literal("Purple Client"));
    }

    @Override
    protected void init() {
        panelWidth = Math.min(560, this.width - 32);
        panelHeight = Math.min(330, this.height - 32);
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = (this.height - panelHeight) / 2;

        int sidebarWidth = Math.max(118, Math.min(142, panelWidth / 4));
        int buttonX = panelLeft + 14;
        int buttonWidth = sidebarWidth - 28;
        this.addRenderableWidget(Button.builder(Component.literal("⌂  Przegląd"), b -> {})
            .bounds(buttonX, panelTop + 91, buttonWidth, 25).build());
        this.addRenderableWidget(Button.builder(Component.literal("⚙  Ustawienia"), b ->
                Minecraft.getInstance().setScreen(new PurpleClientSettingsScreen(this)))
            .bounds(buttonX, panelTop + 122, buttonWidth, 25).build());
        this.addRenderableWidget(Button.builder(Component.literal("Zamknij menu"), b ->
                Minecraft.getInstance().setScreen(null))
            .bounds(panelLeft + panelWidth - 126, panelTop + panelHeight - 42, 112, 26).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);

        panelWidth = Math.min(560, this.width - 32);
        panelHeight = Math.min(330, this.height - 32);
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = (this.height - panelHeight) / 2;
        int sidebarWidth = Math.max(118, Math.min(142, panelWidth / 4));
        int contentLeft = panelLeft + sidebarWidth + 22;

        graphics.fill(panelLeft - 2, panelTop - 2, panelLeft + panelWidth + 2, panelTop + panelHeight + 2, 0x805E35A0);
        graphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + panelHeight, PANEL);
        graphics.fillGradient(panelLeft, panelTop, panelLeft + panelWidth, panelTop + 4, 0xFFB47CFF, 0xFF7040D8);
        graphics.fill(panelLeft, panelTop + 4, panelLeft + sidebarWidth, panelTop + panelHeight, 0xF0191725);
        graphics.fill(panelLeft + sidebarWidth, panelTop + 4, panelLeft + sidebarWidth + 1, panelTop + panelHeight, 0xFF30283D);

        graphics.drawString(this.font, "PURPLE", panelLeft + 17, panelTop + 22, 0xFFB987FF, true);
        graphics.drawString(this.font, "CLIENT", panelLeft + 17, panelTop + 35, TEXT, true);
        graphics.fill(panelLeft + 17, panelTop + 59, panelLeft + sidebarWidth - 17, panelTop + 60, 0xFF40344F);
        graphics.drawString(this.font, "NAWIGACJA", panelLeft + 17, panelTop + 72, 0xFF8F829F, false);

        graphics.drawString(this.font, "CENTRUM KLIENTA", contentLeft, panelTop + 25, MUTED, false);
        graphics.drawString(this.font, "Witaj w Purple Client", contentLeft, panelTop + 49, TEXT, true);
        graphics.drawString(this.font, "Twoje menu. Jeden skrót, szybki dostęp.", contentLeft, panelTop + 66, MUTED, false);

        int cardTop = panelTop + 105;
        int cardRight = panelLeft + panelWidth - 18;
        graphics.fill(contentLeft, cardTop, cardRight, cardTop + 66, 0xFF211C2C);
        graphics.fill(contentLeft, cardTop, contentLeft + 3, cardTop + 66, PURPLE);
        graphics.drawString(this.font, "PURPLE CLIENT", contentLeft + 14, cardTop + 13, TEXT, true);
        graphics.drawString(this.font, "Interfejs jest uruchomiony i gotowy.", contentLeft + 14, cardTop + 31, MUTED, false);
        graphics.fill(contentLeft + 14, cardTop + 49, contentLeft + 21, cardTop + 56, 0xFF72D69A);
        graphics.drawString(this.font, "MENU AKTYWNE", contentLeft + 27, cardTop + 48, 0xFF9BE5B6, false);

        int infoTop = cardTop + 78;
        graphics.drawString(this.font, "SZYBKIE INFORMACJE", contentLeft, infoTop, 0xFFBFA6E8, true);
        graphics.fill(contentLeft, infoTop + 17, cardRight, infoTop + 18, 0xFF393044);
        graphics.drawString(this.font, "Otwieranie menu", contentLeft, infoTop + 29, TEXT, false);
        graphics.drawString(this.font, "Right Shift  •  zmienisz w Opcje → Sterowanie", contentLeft, infoTop + 44, MUTED, false);

        graphics.drawString(this.font, "v0.1.0  •  CLIENT", panelLeft + 17, panelTop + panelHeight - 23, 0xFF857991, false);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
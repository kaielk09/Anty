package pl.purpleclient.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PurpleClientScreen extends Screen {
    private static final int PURPLE = 0xFF9B65FF;
    private static final int TEXT = 0xFFF4EDFF;
    private static final int MUTED = 0xFFB6A8C9;
    private static final int PANEL = 0xF0151420;

    private static final Map<String, Boolean> ENABLED = new HashMap<>();
    private static final Map<String, Integer> PRESETS = new HashMap<>();
    private static boolean YOUTUBE_MUSIC_HOLO_SHOW_TRACK = true;
    private static int YOUTUBE_MUSIC_HOLO_POSITION = 0;
    private static int YOUTUBE_MUSIC_HOLO_MODE = 0;
    private EditBox youtubeMusicSearchBox;

    private final String category;
    private int panelLeft;
    private int panelTop;
    private int panelWidth;
    private int panelHeight;

    private static final List<String> MOVEMENT = List.of(
        "Sprint HUD", "Speedometer", "Direction HUD"
    );
    private static final List<String> VISUALS = List.of(
        "Fullbright", "Zoom", "Coordinates", "FPS Counter",
        "Ping Display", "Armor HUD", "Potion HUD", "Keystrokes",
        "Clock", "Compass", "Crosshair Editor", "Block Outline", "Armor Durability", "Custom Sky", "Weather Changer", "Custom Fog", "Ambient Particles", "Item Physics", "Totem Animation", "Jump Circle", "YouTube Music Holo"
    );

    public PurpleClientScreen() {
        this("Visuals");
    }

    private PurpleClientScreen(String category) {
        super(Component.literal("Purple Client"));
        this.category = category;
    }

    @Override
    protected void init() {
        panelWidth = Math.min(700, this.width - 24);
        panelHeight = Math.min(470, this.height - 24);
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = (this.height - panelHeight) / 2;

        int sidebarWidth = 142;
        int tabX = panelLeft + 12;
        int tabWidth = sidebarWidth - 24;
        String[] categories = {"Visuals", "Config"};
        for (int i = 0; i < categories.length; i++) {
            String selected = categories[i];
            this.addRenderableWidget(Button.builder(Component.literal(
                    (selected.equals(category) ? "•  " : "   ") + selected), b ->
                    Minecraft.getInstance().gui.setScreen(new PurpleClientScreen(selected)))
                .bounds(tabX, panelTop + 94 + i * 34, tabWidth, 27).build());
        }

        this.addRenderableWidget(Button.builder(Component.literal("Zamknij"), b ->
                Minecraft.getInstance().gui.setScreen(null))
            .bounds(panelLeft + panelWidth - 102, panelTop + panelHeight - 38, 86, 25).build());

        List<String> modules = getModules();
        if ("Config".equals(category)) {
            this.addRenderableWidget(Button.builder(Component.literal("Show track title: " + (YOUTUBE_MUSIC_HOLO_SHOW_TRACK ? "ON" : "OFF")), b -> {
                    YOUTUBE_MUSIC_HOLO_SHOW_TRACK = !YOUTUBE_MUSIC_HOLO_SHOW_TRACK;
                    Minecraft.getInstance().gui.setScreen(new PurpleClientScreen("Config"));
                })
                .bounds(panelLeft + sidebarWidth + 18, panelTop + 112, 250, 26).build());
            this.addRenderableWidget(Button.builder(Component.literal("Holo position: " + youtubeMusicPositionLabel()), b -> {
                    YOUTUBE_MUSIC_HOLO_POSITION = (YOUTUBE_MUSIC_HOLO_POSITION + 1) % 3;
                    Minecraft.getInstance().gui.setScreen(new PurpleClientScreen("Config"));
                })
                .bounds(panelLeft + sidebarWidth + 18, panelTop + 148, 250, 26).build());
        }
        if ("YouTube Music Holo".equals(category)) {
            int sx = panelLeft + sidebarWidth + 18;
            this.addRenderableWidget(Button.builder(Component.literal("Otwórz YouTube Music w grze"), b ->
                    Minecraft.getInstance().gui.setScreen(new YouTubeMusicBrowserScreen(this)))
                .bounds(sx, panelTop + 104, 240, 28).build());
            this.addRenderableWidget(Button.builder(Component.literal("Tryb: " + youtubeMusicModeLabel()), b -> {
                    YOUTUBE_MUSIC_HOLO_MODE = (YOUTUBE_MUSIC_HOLO_MODE + 1) % 2;
                    Minecraft.getInstance().gui.setScreen(new PurpleClientScreen("YouTube Music Holo"));
                }).bounds(sx, panelTop + 142, 240, 26).build());
            this.addRenderableWidget(Button.builder(Component.literal("Wróć"), b ->
                    Minecraft.getInstance().gui.setScreen(new PurpleClientScreen("Visuals")))
                .bounds(sx, panelTop + 180, 90, 24).build());
        }
        if (!"Config".equals(category) && !"YouTube Music Holo".equals(category)) {
            int contentX = panelLeft + sidebarWidth + 18;
            int contentWidth = panelWidth - sidebarWidth - 36;
            int columnWidth = (contentWidth - 10) / 2;
            for (int i = 0; i < modules.size(); i++) {
                String module = modules.get(i);
                int column = i % 2;
                int row = i / 2;
                int x = contentX + column * (columnWidth + 10);
                int y = panelTop + 112 + row * 29;
                int toggleWidth = Math.max(86, columnWidth - 63);
                this.addRenderableWidget(Button.builder(Component.literal(toggleLabel(module)), b -> {
                        ENABLED.put(module, !ENABLED.getOrDefault(module, false));
                        Minecraft.getInstance().gui.setScreen(new PurpleClientScreen(category));
                    })
                    .bounds(x, y, toggleWidth, 24).build());
                this.addRenderableWidget(Button.builder(Component.literal("CFG"), b -> {
                        if ("YouTube Music Holo".equals(module)) {
                            Minecraft.getInstance().gui.setScreen(new PurpleClientScreen("YouTube Music Holo"));
                        } else {
                            PRESETS.put(module, (PRESETS.getOrDefault(module, 0) + 1) % 4);
                            Minecraft.getInstance().gui.setScreen(new PurpleClientScreen(category));
                        }
                    })
                    .bounds(x + toggleWidth + 4, y, 54, 24).build());
            }
        }
    }

    private List<String> getModules() {
        List<String> modules = new ArrayList<>();
        if ("Visuals".equals(category)) {
            modules.addAll(VISUALS);
        }
        return modules;
    }

    private static String toggleLabel(String module) {
        return (ENABLED.getOrDefault(module, false) ? "ON  " : "OFF  ") + module;
    }

    private static String youtubeMusicModeLabel() {
        return YOUTUBE_MUSIC_HOLO_MODE == 1 ? "Fly (floating in world)" : "Stick (on-screen HUD)";
    }

    private static String youtubeMusicPositionLabel() {
        return switch (YOUTUBE_MUSIC_HOLO_POSITION) {
            case 1 -> "Bottom left";
            case 2 -> "Bottom right";
            default -> "Top center";
        };
    }

    public static void refreshYouTubeMusicScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.execute(() -> {
            if (minecraft.screen instanceof PurpleClientScreen screen && "YouTube Music Holo".equals(screen.category)) {
                minecraft.gui.setScreen(new PurpleClientScreen("YouTube Music Holo"));
            }
        });
    }

    private static String presetLabel(String module) {
        return switch (PRESETS.getOrDefault(module, 0)) {
            case 1 -> "Compact";
            case 2 -> "Minimal";
            case 3 -> "Detailed";
            default -> "Default";
        };
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);

        panelWidth = Math.min(700, this.width - 24);
        panelHeight = Math.min(470, this.height - 24);
        panelLeft = (this.width - panelWidth) / 2;
        panelTop = (this.height - panelHeight) / 2;
        int sidebarWidth = 142;
        int contentX = panelLeft + sidebarWidth + 20;
        int contentRight = panelLeft + panelWidth - 18;

        graphics.fill(panelLeft - 2, panelTop - 2, panelLeft + panelWidth + 2, panelTop + panelHeight + 2, 0x805E35A0);
        graphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + panelHeight, PANEL);
        graphics.fillGradient(panelLeft, panelTop, panelLeft + panelWidth, panelTop + 4, 0xFFB47CFF, 0xFF7040D8);
        graphics.fill(panelLeft, panelTop + 4, panelLeft + sidebarWidth, panelTop + panelHeight, 0xF0191725);
        graphics.fill(panelLeft + sidebarWidth, panelTop + 4, panelLeft + sidebarWidth + 1, panelTop + panelHeight, 0xFF30283D);

        graphics.drawString(this.font, "PURPLE", panelLeft + 17, panelTop + 22, 0xFFB987FF, true);
        graphics.drawString(this.font, "CLIENT", panelLeft + 17, panelTop + 35, TEXT, true);
        graphics.fill(panelLeft + 16, panelTop + 59, panelLeft + sidebarWidth - 16, panelTop + 60, 0xFF40344F);
        graphics.drawString(this.font, "CATEGORIES", panelLeft + 16, panelTop + 76, 0xFF8F829F, false);

        graphics.drawString(this.font, "PURPLE CLIENT  /  MODULE MANAGER", contentX, panelTop + 24, MUTED, false);
        graphics.drawString(this.font, category, contentX, panelTop + 48, TEXT, true);

        if ("YouTube Music Holo".equals(category)) {
            graphics.fill(contentX, panelTop + 91, contentRight, panelTop + 245, 0xFF211C2C);
            graphics.fill(contentX, panelTop + 91, contentX + 3, panelTop + 245, PURPLE);
            graphics.drawString(this.font, "YOUTUBE MUSIC", contentX + 16, panelTop + 112, TEXT, true);
            graphics.drawString(this.font, "Wbudowany odtwarzacz Chromium działa wewnątrz Minecrafta.", contentX + 16, panelTop + 142, MUTED, false);
            graphics.drawString(this.font, "Kliknij przycisk, aby zalogować się i używać YouTube Music.", contentX + 16, panelTop + 162, MUTED, false);
            graphics.drawString(this.font, "Logowanie odbywa się bezpośrednio na stronie Google.", contentX + 16, panelTop + 182, MUTED, false);
        } else if ("Config".equals(category)) {
            graphics.fill(contentX, panelTop + 91, contentRight, panelTop + 220, 0xFF211C2C);
            graphics.fill(contentX, panelTop + 91, contentX + 3, panelTop + 220, PURPLE);
            graphics.drawString(this.font, "YOUTUBE MUSIC HOLO", contentX + 16, panelTop + 110, TEXT, true);
            graphics.drawString(this.font, "Track title: " + (YOUTUBE_MUSIC_HOLO_SHOW_TRACK ? "enabled" : "disabled"), contentX + 16, panelTop + 135, MUTED, false);
            graphics.drawString(this.font, "Position: " + youtubeMusicPositionLabel(), contentX + 16, panelTop + 154, MUTED, false);
            graphics.drawString(this.font, "YouTube Music status: " + YouTubeMusicService.status(), contentX + 16, panelTop + 184, 0xFFFFC878, false);
        } else {
            graphics.drawString(this.font, "Kliknij moduł, aby zmienić jego stan. CFG zmienia profil ustawień.", contentX,
                panelTop + 68, MUTED, false);
            List<String> modules = getModules();
            for (int i = 0; i < modules.size(); i++) {
                String module = modules.get(i);
                int column = i % 2;
                int row = i / 2;
                int columnWidth = (panelWidth - sidebarWidth - 46) / 2;
                int x = contentX + column * (columnWidth + 10);
                int y = panelTop + 105 + row * 29;
                graphics.fill(x, y, x + columnWidth, y + 27, 0xFF211C2C);
                graphics.fill(x, y, x + 2, y + 27, ENABLED.getOrDefault(module, false) ? 0xFF72D69A : 0xFF574B63);
                graphics.drawString(this.font, module, x + 9, y + 6, TEXT, false);
                graphics.drawString(this.font, "CFG: " + presetLabel(module), x + 9, y + 19, 0xFF9F91B2, false);
            }
        }

        graphics.drawString(this.font, "UI PROTOTYPE  •  RIGHT SHIFT", panelLeft + 17,
            panelTop + panelHeight - 20, 0xFF857991, false);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

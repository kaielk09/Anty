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
                    Minecraft.getInstance().setScreen(new PurpleClientScreen(selected)))
                .bounds(tabX, panelTop + 94 + i * 34, tabWidth, 27).build());
        }

        this.addRenderableWidget(Button.builder(Component.literal("Zamknij"), b ->
                Minecraft.getInstance().setScreen(null))
            .bounds(panelLeft + panelWidth - 102, panelTop + panelHeight - 38, 86, 25).build());

        List<String> modules = getModules();
        if ("Config".equals(category)) {
            this.addRenderableWidget(Button.builder(Component.literal("Show track title: " + (YOUTUBE_MUSIC_HOLO_SHOW_TRACK ? "ON" : "OFF")), b -> {
                    YOUTUBE_MUSIC_HOLO_SHOW_TRACK = !YOUTUBE_MUSIC_HOLO_SHOW_TRACK;
                    Minecraft.getInstance().setScreen(new PurpleClientScreen("Config"));
                })
                .bounds(panelLeft + sidebarWidth + 18, panelTop + 112, 250, 26).build());
            this.addRenderableWidget(Button.builder(Component.literal("Holo position: " + youtubeMusicPositionLabel()), b -> {
                    YOUTUBE_MUSIC_HOLO_POSITION = (YOUTUBE_MUSIC_HOLO_POSITION + 1) % 3;
                    Minecraft.getInstance().setScreen(new PurpleClientScreen("Config"));
                })
                .bounds(panelLeft + sidebarWidth + 18, panelTop + 148, 250, 26).build());
        }
        if ("YouTube Music Holo".equals(category)) {
            int sx = panelLeft + sidebarWidth + 18;
            this.addRenderableWidget(Button.builder(Component.literal("Tryb: " + youtubeMusicModeLabel()), b -> {
                    YOUTUBE_MUSIC_HOLO_MODE = (YOUTUBE_MUSIC_HOLO_MODE + 1) % 2;
                    Minecraft.getInstance().setScreen(new PurpleClientScreen("YouTube Music Holo"));
                }).bounds(sx + 270, panelTop + 104, 150, 24).build());
            this.addRenderableWidget(Button.builder(Component.literal("Otwórz YouTube Music"), b -> YouTubeMusicService.connect())
                .bounds(sx, panelTop + 104, 150, 24).build());
            this.addRenderableWidget(Button.builder(Component.literal("Odśwież status"), b -> YouTubeMusicService.refreshPlayback())
                .bounds(sx + 158, panelTop + 104, 105, 24).build());

            youtubeMusicSearchBox = new EditBox(this.font, sx, panelTop + 138, 250, 24, Component.literal("Szukaj utworu"));
            youtubeMusicSearchBox.setMaxLength(120);
            youtubeMusicSearchBox.setValue("");
            this.addRenderableWidget(youtubeMusicSearchBox);
            this.addRenderableWidget(Button.builder(Component.literal("Szukaj"), b -> {
                    if (youtubeMusicSearchBox != null && !youtubeMusicSearchBox.getValue().isBlank())
                        YouTubeMusicService.search(youtubeMusicSearchBox.getValue().trim());
                }).bounds(sx + 258, panelTop + 138, 90, 24).build());

            int resultIndex = 0;
            for (YouTubeMusicService.Track track : YouTubeMusicService.results()) {
                if (resultIndex >= 5) break;
                int rowY = panelTop + 174 + resultIndex * 27;
                this.addRenderableWidget(Button.builder(Component.literal("▶  " + track.toString()), b -> YouTubeMusicService.play(track.uri()))
                    .bounds(sx, rowY, Math.min(430, panelWidth - sidebarWidth - 38), 23).build());
                resultIndex++;
            }
            int controlsY = panelTop + 315;
            this.addRenderableWidget(Button.builder(Component.literal("⏮"), b -> YouTubeMusicService.previous())
                .bounds(sx, controlsY, 62, 25).build());
            this.addRenderableWidget(Button.builder(Component.literal("▶ Start"), b -> YouTubeMusicService.resume())
                .bounds(sx + 70, controlsY, 72, 25).build());
            this.addRenderableWidget(Button.builder(Component.literal("⏸ Pauza"), b -> YouTubeMusicService.pause())
                .bounds(sx + 148, controlsY, 82, 25).build());
            this.addRenderableWidget(Button.builder(Component.literal("⏭"), b -> YouTubeMusicService.next())
                .bounds(sx + 236, controlsY, 62, 25).build());
            this.addRenderableWidget(Button.builder(Component.literal("Odśwież"), b -> YouTubeMusicService.refreshPlayback())
                .bounds(sx + 304, controlsY, 90, 25).build());
            this.addRenderableWidget(Button.builder(Component.literal("Wróć"), b ->
                    Minecraft.getInstance().setScreen(new PurpleClientScreen("Visuals")))
                .bounds(sx, panelTop + 354, 90, 24).build());
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
                        Minecraft.getInstance().setScreen(new PurpleClientScreen(category));
                    })
                    .bounds(x, y, toggleWidth, 24).build());
                this.addRenderableWidget(Button.builder(Component.literal("CFG"), b -> {
                        if ("YouTube Music Holo".equals(module)) {
                            Minecraft.getInstance().setScreen(new PurpleClientScreen("YouTube Music Holo"));
                        } else {
                            PRESETS.put(module, (PRESETS.getOrDefault(module, 0) + 1) % 4);
                            Minecraft.getInstance().setScreen(new PurpleClientScreen(category));
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
                minecraft.setScreen(new PurpleClientScreen("YouTube Music Holo"));
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
            graphics.fill(contentX, panelTop + 91, contentRight, panelTop + 400, 0xFF211C2C);
            graphics.fill(contentX, panelTop + 91, contentX + 3, panelTop + 400, PURPLE);
            graphics.drawString(this.font, "YOUTUBE MUSIC", contentX + 16, panelTop + 94, TEXT, true);
            graphics.drawString(this.font, "Tryb: " + youtubeMusicModeLabel(), contentX + 16, panelTop + 124, MUTED, false);
            graphics.drawString(this.font, "Status: " + YouTubeMusicService.status(), contentX + 16, panelTop + 296, 0xFFB9E7C6, false);
            graphics.drawString(this.font, "Teraz: " + YouTubeMusicService.currentTrack(), contentX + 16, panelTop + 345, TEXT, false);
            graphics.drawString(this.font, "Wybierz wynik wyszukiwania, aby rozpocząć odtwarzanie.", contentX + 16, panelTop + 380, MUTED, false);
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

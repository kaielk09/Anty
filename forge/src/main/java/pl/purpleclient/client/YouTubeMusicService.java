package pl.purpleclient.client;

import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class YouTubeMusicService {
    private static volatile String status = "Gotowy — bez logowania";
    private static final String currentTrack = "Tytuł utworu nie jest dostępny bez integracji konta";

    public record Track(String name, String artist, String uri) {
        @Override public String toString() { return name + " — " + artist; }
    }

    private YouTubeMusicService() {}

    public static String status() { return status; }
    public static String currentTrack() { return currentTrack; }
    public static List<Track> results() { return List.of(); }

    public static void connect() { open("https://music.youtube.com/"); status = "Otwarto YouTube Music"; }

    public static void search(String text) {
        open("https://music.youtube.com/search?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8));
        status = "Otwarto wyszukiwanie YouTube Music";
    }

    public static void play(String uri) { search(uri); }
    public static void resume() { mediaKey(KeyEvent.VK_MEDIA_PLAY); }
    public static void pause() { mediaKey(KeyEvent.VK_MEDIA_PAUSE); }
    public static void next() { mediaKey(KeyEvent.VK_MEDIA_NEXT_TRACK); }
    public static void previous() { mediaKey(KeyEvent.VK_MEDIA_PREVIOUS_TRACK); }
    public static void refreshPlayback() {
        status = "Sterowanie przez klawisze multimedialne";
    }

    private static void mediaKey(int key) {
        try {
            Robot robot = new Robot();
            robot.keyPress(key);
            robot.keyRelease(key);
            status = "Wysłano polecenie multimedialne";
        } catch (Exception e) {
            status = "Nie udało się wysłać klawisza multimedialnego";
        }
    }

    private static void open(String url) {
        try {
            if (!Desktop.isDesktopSupported()) {
                status = "Otwórz ręcznie: music.youtube.com";
                return;
            }
            Desktop.getDesktop().browse(URI.create(url));
        } catch (Exception e) {
            status = "Nie udało się otworzyć YouTube Music";
        }
    }
}
package pl.purpleclient.client;

import com.google.gson.*;
import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public final class SpotifyService {
    private static final String REDIRECT = "http://127.0.0.1:43821/callback";
    private static final String SCOPES = "user-read-playback-state user-read-currently-playing user-modify-playback-state";
    private static final HttpClient HTTP = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
    private static final Gson GSON = new Gson();
    private static volatile JsonObject token;
    private static volatile String status = "Niepołączono";
    private static volatile String currentTrack = "Brak informacji o utworze";
    private static volatile List<Track> results = List.of();

    public record Track(String name, String artist, String uri) {
        @Override public String toString() { return name + " — " + artist; }
    }

    private SpotifyService() {}

    private static Path folder() {
        return net.minecraft.client.Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("purpleclient");
    }
    private static Path clientIdFile() { return folder().resolve("spotify-client-id.txt"); }
    private static Path tokenFile() { return folder().resolve("spotify-token.json"); }

    public static String status() { return status; }
    public static String currentTrack() { return currentTrack; }
    public static List<Track> results() { return results; }

    private static String clientId() throws IOException {
        Files.createDirectories(folder());
        if (!Files.exists(clientIdFile())) {
            Files.writeString(clientIdFile(), "WKLEJ_TUTAJ_SPOTIFY_CLIENT_ID", StandardCharsets.UTF_8);
            throw new IOException("Wpisz Spotify Client ID do config/purpleclient/spotify-client-id.txt");
        }
        String id = Files.readString(clientIdFile(), StandardCharsets.UTF_8).trim();
        if (id.isBlank() || id.startsWith("WKLEJ_")) throw new IOException("Uzupełnij spotify-client-id.txt");
        return id;
    }

    private static String random(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    private static String challenge(String verifier) throws Exception {
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(verifier.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
    private static String form(Map<String,String> values) {
        StringJoiner join = new StringJoiner("&");
        values.forEach((k,v) -> join.add(URLEncoder.encode(k, StandardCharsets.UTF_8) + "=" + URLEncoder.encode(v, StandardCharsets.UTF_8)));
        return join.toString();
    }

    public static void connect() {
        status = "Oczekiwanie na logowanie Spotify...";
        CompletableFuture.runAsync(() -> {
            try {
                String id = clientId();
                String verifier = random(64);
                String state = random(24);
                String url = "https://accounts.spotify.com/authorize?" + form(Map.of(
                    "client_id", id, "response_type", "code", "redirect_uri", REDIRECT,
                    "scope", SCOPES, "state", state, "code_challenge_method", "S256",
                    "code_challenge", challenge(verifier)
                ));
                try (ServerSocket server = new ServerSocket()) {
                    server.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 43821));
                    server.setSoTimeout(180000);
                    if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(URI.create(url));
                    status = "Zaloguj się w otwartej karcie przeglądarki...";
                    try (Socket socket = server.accept()) {
                        socket.setSoTimeout(10000);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                        String request = reader.readLine();
                        while (true) { String line = reader.readLine(); if (line == null || line.isEmpty()) break; }
                        String target = request == null ? "/" : request.split(" ")[1];
                        URI callback = URI.create("http://127.0.0.1" + target);
                        Map<String,String> params = query(callback.getRawQuery());
                        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\nConnection: close\r\n\r\n<html><body style='font-family:sans-serif;background:#171321;color:white'><h2>Purple Client</h2><p>Możesz wrócić do Minecrafta.</p></body></html>";
                        socket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
                        if (!Objects.equals(state, params.get("state")) || params.get("code") == null)
                            throw new IOException("Logowanie anulowane lub nieprawidłowy stan OAuth.");
                        HttpRequest tokenRequest = HttpRequest.newBuilder(URI.create("https://accounts.spotify.com/api/token"))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(form(Map.of(
                                "client_id", id, "grant_type", "authorization_code",
                                "code", params.get("code"), "redirect_uri", REDIRECT,
                                "code_verifier", verifier
                            )))).build();
                        JsonObject body = GSON.fromJson(HTTP.send(tokenRequest, HttpResponse.BodyHandlers.ofString()).body(), JsonObject.class);
                        if (body.has("error")) throw new IOException(body.toString());
                        body.addProperty("expires_at", System.currentTimeMillis() + body.get("expires_in").getAsLong() * 1000L);
                        token = body;
                        Files.writeString(tokenFile(), GSON.toJson(body), StandardCharsets.UTF_8);
                        status = "Połączono ze Spotify";
                    }
                }
            } catch (Exception e) { status = "Błąd Spotify: " + safe(e.getMessage()); }
        });
    }

    private static Map<String,String> query(String raw) {
        Map<String,String> map = new HashMap<>();
        if (raw == null) return map;
        for (String part : raw.split("&")) {
            String[] pair = part.split("=", 2);
            map.put(URLDecoder.decode(pair[0], StandardCharsets.UTF_8), pair.length > 1 ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8) : "");
        }
        return map;
    }
    private static String safe(String s) { return s == null ? "nieznany błąd" : s.substring(0, Math.min(110, s.length())); }

    private static synchronized String accessToken() throws Exception {
        if (token == null && Files.exists(tokenFile()))
            token = JsonParser.parseString(Files.readString(tokenFile(), StandardCharsets.UTF_8)).getAsJsonObject();
        if (token == null) throw new IOException("Najpierw połącz konto Spotify.");
        if (System.currentTimeMillis() < token.get("expires_at").getAsLong() - 60000)
            return token.get("access_token").getAsString();
        if (!token.has("refresh_token")) throw new IOException("Sesja wygasła. Połącz Spotify ponownie.");
        JsonObject refreshed = GSON.fromJson(HTTP.send(HttpRequest.newBuilder(URI.create("https://accounts.spotify.com/api/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(form(Map.of(
                "client_id", clientId(), "grant_type", "refresh_token",
                "refresh_token", token.get("refresh_token").getAsString()
            )))).build(), HttpResponse.BodyHandlers.ofString()).body(), JsonObject.class);
        if (refreshed.has("error")) throw new IOException(refreshed.toString());
        token.add("access_token", refreshed.get("access_token"));
        token.addProperty("expires_at", System.currentTimeMillis() + refreshed.get("expires_in").getAsLong() * 1000L);
        if (refreshed.has("refresh_token")) token.add("refresh_token", refreshed.get("refresh_token"));
        Files.writeString(tokenFile(), GSON.toJson(token), StandardCharsets.UTF_8);
        return token.get("access_token").getAsString();
    }

    private static String api(String method, String path, String body) throws Exception {
        String access = accessToken();
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("https://api.spotify.com/v1" + path))
            .timeout(Duration.ofSeconds(20)).header("Authorization", "Bearer " + access);
        if (body == null) builder.method(method, HttpRequest.BodyPublishers.noBody());
        else builder.header("Content-Type", "application/json").method(method, HttpRequest.BodyPublishers.ofString(body));
        HttpResponse<String> response = HTTP.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 204) return "";
        if (response.statusCode() < 200 || response.statusCode() >= 300)
            throw new IOException("Spotify HTTP " + response.statusCode() + ": " + safe(response.body()));
        return response.body();
    }

    public static void search(String text) {
        CompletableFuture.runAsync(() -> {
            try {
                String path = "/search?" + form(Map.of("q", text, "type", "track", "limit", "6"));
                JsonObject root = JsonParser.parseString(api("GET", path, null)).getAsJsonObject();
                JsonArray items = root.getAsJsonObject("tracks").getAsJsonArray("items");
                List<Track> found = new ArrayList<>();
                for (JsonElement item : items) {
                    JsonObject track = item.getAsJsonObject();
                    String artist = track.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
                    found.add(new Track(track.get("name").getAsString(), artist, track.get("uri").getAsString()));
                }
                results = List.copyOf(found);
                status = found.isEmpty() ? "Brak wyników" : "Wyniki: " + found.size();
                PurpleClientScreen.refreshSpotifyScreen();
            } catch (Exception e) { status = "Błąd wyszukiwania: " + safe(e.getMessage()); }
        });
    }

    public static void play(String uri) { command("PUT", "/me/player/play", GSON.toJson(Map.of("uris", List.of(uri)))); }
    public static void pause() { command("PUT", "/me/player/pause", "{}"); }
    public static void next() { command("POST", "/me/player/next", null); }
    public static void previous() { command("POST", "/me/player/previous", null); }

    private static void command(String method, String path, String body) {
        CompletableFuture.runAsync(() -> {
            try { api(method, path, body); status = "Polecenie wysłane do Spotify"; refreshPlayback(); }
            catch (Exception e) { status = "Błąd odtwarzania: " + safe(e.getMessage()); }
        });
    }
    public static void refreshPlayback() {
        CompletableFuture.runAsync(() -> {
            try {
                JsonObject root = JsonParser.parseString(api("GET", "/me/player/currently-playing", null)).getAsJsonObject();
                if (root.has("item") && root.get("item").isJsonObject()) {
                    JsonObject item = root.getAsJsonObject("item");
                    String artist = item.getAsJsonArray("artists").get(0).getAsJsonObject().get("name").getAsString();
                    currentTrack = item.get("name").getAsString() + " — " + artist;
                } else currentTrack = "Nic nie jest odtwarzane";
            } catch (Exception e) { currentTrack = "Brak aktywnego odtwarzania"; status = safe(e.getMessage()); }
        });
    }
}

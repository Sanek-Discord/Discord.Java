package net.sascha123789.djava.core.discord.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sascha123789.djava.core.discord.std.DiscordAccount;
import net.sascha123789.djava.core.discord.std.GeneralConfig;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicReference;

public class Role {
    private String id;
    private String name;
    private int hexColor;
    private boolean hoist;
    private String icon;
    private String unicodeEmoji;
    private int position;
    private boolean managed;
    private boolean mentionable;
    private static ObjectMapper mapper = new ObjectMapper();

    private Role(String id, String name, int hexColor, boolean hoist, String icon, String unicodeEmoji, int position, boolean managed, boolean mentionable) {
        this.id = id;
        this.name = name;
        this.icon = "https://cdn.discordapp.com/role-icons/" + id + "/" + icon + ".png";
        this.hoist = hoist;
        this.hexColor = hexColor;
        this.unicodeEmoji = unicodeEmoji;
        this.position = position;
        this.managed = managed;
        this.mentionable = mentionable;
    }

    /**
     * Returns role id as String**/
    public String getId() {
        return id;
    }

    /**
     * Returns role id as long**/
    public long getRoleIdAsLong() {
        return Long.parseLong(id);
    }

    /**
     * Returns role name**/
    public String getName() {
        return name;
    }

    /**
     * If this role is pinned in the user listing**/
    public boolean isHoist() {
        return hoist;
    }

    /**
     * Returns role icon url**/
    public String getIconUrl() {
        return icon;
    }

    /**
     * Returns hexadecimal color code**/
    public int getHexColor() {
        return hexColor;
    }

    /**
     * Returns role color**/
    public Color getColor() {
        System.out.println(String.valueOf(hexColor));
        return Color.decode(String.valueOf(hexColor));
    }

    /**
     * Returns role unicode emoji**/
    public String getUnicodeEmoji() {
        return unicodeEmoji;
    }

    /**
     * Returns role position**/
    public int getPosition() {
        return position;
    }

    /**
     * Returns true if role managed by integration**/
    public boolean isManaged() {
        return managed;
    }

    /**
     * Returns true if role mentionable**/
    public boolean isMentionable() {
        return mentionable;
    }

    public static Role fromJson(String json) {
        try {
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            String id = node.get("id").asText();
            String name = node.get("name").asText();
            int hexColor = node.get("color").asInt();
            boolean hoist = node.get("hoist").asBoolean();
            String icon = node.get("icon").asText();
            String unicodeEmoji = node.get("unicode_emoji").asText();
            int position = node.get("position").asInt();
            boolean managed = node.get("managed").asBoolean();
            boolean mentionable = node.get("mentionable").asBoolean();

            return new Role(id, name, hexColor, hoist, icon, unicodeEmoji, position, managed, mentionable);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Role getById(String guildId, String roleId) {
        try {
            AtomicReference<String> json = new AtomicReference<>();

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(GeneralConfig.API_STRING + "guilds/" + guildId + "/roles"))
                    .header("User-Agent", "DiscordBot (" + GeneralConfig.API_STRING + "guilds/" + guildId + "/roles" + ", 10)")
                    .header("Authorization", "Bot " + GeneralConfig.getToken())
                    .build();

            DiscordAccount.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(content -> {
                        json.set(content);
                    }).join();
            ArrayNode arr = mapper.readValue(json.get(), ArrayNode.class);
            JsonNode node = null;

            for(JsonNode n: arr) {
                if(n.get("id").asText().equals(roleId)) {
                    node = n;
                    break;
                }
            }

            String id = node.get("id").asText();
            String name = node.get("name").asText();
            int hexColor = node.get("color").asInt();
            boolean hoist = node.get("hoist").asBoolean();
            String icon = node.get("icon").asText();
            String unicodeEmoji = node.get("unicode_emoji").asText();
            int position = node.get("position").asInt();
            boolean managed = node.get("managed").asBoolean();
            boolean mentionable = node.get("mentionable").asBoolean();

            return new Role(id, name, hexColor, hoist, icon, unicodeEmoji, position, managed, mentionable);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

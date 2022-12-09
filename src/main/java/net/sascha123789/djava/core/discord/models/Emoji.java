package net.sascha123789.djava.core.discord.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.charset.StandardCharsets;

public class Emoji {
    private String id;
    private String name;
    private DiscordUser user;
    private boolean requireColons;
    private boolean managed;
    private boolean animated;
    private boolean available;
    private static ObjectMapper mapper = new ObjectMapper();

    private Emoji(String id, String name, DiscordUser user, boolean requireColons, boolean managed, boolean animated, boolean available) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.requireColons = requireColons;
        this.managed = managed;
        this.animated = animated;
        this.available = available;
    }

    /**
     * Returns emoji id as String**/
    public String getId() {
        return id;
    }

    /**
     * Returns emoji id as long**/
    public long getIdAsLong() {
        return Long.parseLong(id);
    }

    /**
     * Returns emoji name**/
    public String getName() {
        return new String(name.getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * If possible, returns emoji author**/
    public DiscordUser getAuthor() {
        return user;
    }

    /**
     * Returns true if emoji is requireColons**/
    public boolean isRequireColons() {
        return requireColons;
    }

    /**
     * Returns true if emoji is managed**/
    public boolean isManaged() {
        return managed;
    }

    /**
     * Returns true if emoji is animated**/
    public boolean isAnimated() {
        return animated;
    }

    /**
     * Returns true if emoji available**/
    public boolean isAvailable() {
        return available;
    }

    /**
     * Returns emoji object from json**/
    public static Emoji fromJson(String json) {
        try {
            ObjectNode node = mapper.readValue(json, ObjectNode.class);

            String id = node.get("id").asText();
            String name = node.get("name").asText();
            JsonNode author = node.get("user");
            boolean requireColons = node.get("require_colons").asBoolean();
            boolean managed = node.get("managed").asBoolean();
            boolean animated = node.get("animated").asBoolean();
            boolean available = true;

            if(node.has("available")) {
                available = node.get("available").asBoolean();
            }

            return new Emoji(id, name, DiscordUser.getById(author.get("id").asText()), requireColons, managed, animated, available);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package net.sascha123789.djava.core.discord.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sascha123789.djava.core.discord.types.StickerFormatType;
import net.sascha123789.djava.core.discord.types.StickerType;

import java.util.ArrayList;
import java.util.List;

public class Sticker {
    private String id;
    private String name;
    private String packId;
    private String description;
    private String tags;
    private int type;
    private StickerType stickerType;
    private int formatType;
    private StickerFormatType stickerFormatType;
    private boolean available;
    private int authorGuildId;
    private DiscordUser author;
    private int sortedValue;
    private static ObjectMapper mapper = new ObjectMapper();

    private Sticker(String id, String name, String packId, String description, String tags, int type, int formatType, boolean available, int authorGuildId, DiscordUser author, int sortedValue) {
        this.id = id;
        this.name = name;
        this.packId = packId;
        this.description = description;
        this.tags = tags;
        this.type = type;

        switch (type) {
            case 1:
                stickerType = StickerType.STANDARD;
                break;
            case 2:
                stickerType = StickerType.GUILD;
                break;
        }

        this.formatType = formatType;

        switch(formatType) {
            case 1:
                stickerFormatType = StickerFormatType.PNG;
                break;
            case 2:
                stickerFormatType = StickerFormatType.APNG;
                break;
            case 3:
                stickerFormatType = StickerFormatType.LOTTIE;
                break;
        }

        this.available = available;
        this.authorGuildId = authorGuildId;
        this.author = author;
        this.sortedValue = sortedValue;
    }

    /**
     * Returns sticker id as String**/
    public String getId() {
        return id;
    }

    /**
     * Returns sticker id as long**/
    public long getIdAsLong() {
        return Long.parseLong(id);
    }

    /**
     * Returns sticker name**/
    public String getName() {
        return name;
    }

    /**
     * If sticker in standard pack, returns sticker pack id as String**/
    public String getPackId() {
        return packId;
    }

    /**
     * If sticker in standard pack, returns sticker pack id as long**/
    public long getPackIdAsLong() {
        return Long.parseLong(packId);
    }

    /**
     * Returns sticker description**/
    public String getDescription() {
        return description;
    }

    /**
     * Returns sticker tags as String**/
    public String getTagsAsString() {
        return tags;
    }

    /**
     * Returns sticker tags as String list**/
    public List<String> getTags() {
        List<String> list = new ArrayList<>();

        this.tags = tags.replaceAll(" ", "");

        StringBuilder word = new StringBuilder();

        for(Character c: tags.toCharArray()) {

            if(!c.equals(",")) {
                word.append(c.toString());
            } else {
                this.tags = tags.replaceFirst(",", "");
                list.add(word.toString());
                word = new StringBuilder();
            }
        }

        return list;
    }

    /**
     * Returns sticker type as integer**/
    public int getIntType() {
        return type;
    }

    /**
     * Returns sticker type**/
    public StickerType getType() {
        return stickerType;
    }

    /**
     * Returns sticker format type as integer**/
    public int getFormatIntType() {
        return formatType;
    }

    /**
     * Returns sticker format type**/
    public StickerFormatType getFormatType() {
        return stickerFormatType;
    }

    /**
     * Returns true if sticker is available**/
    public boolean isAvailable() {
        return available;
    }

    /**
     * Returns author guild id**/
    public int getAuthorGuildId() {
        return authorGuildId;
    }

    /**
     * Returns sticker author**/
    public DiscordUser getAuthor() {
        return author;
    }

    /**
     * Returns sticker sorted value in pack**/
    public int getSortedValue() {
        return sortedValue;
    }

    public static Sticker fromJson(String json) {
        try {
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            String id = node.get("id").asText();
            String packId = node.get("pack_id").asText();
            String name = node.get("name").asText();
            String description = node.get("description").asText();
            String tags = node.get("tags").asText();
            int type = node.get("type").asInt();
            int formatType = node.get("format_type").asInt();
            boolean available = node.get("available").asBoolean();
            int authorGuildId = node.get("guild_id").asInt();
            JsonNode author = null;

            try {
                author = node.get("user");
            } catch(Exception  e) {
                author = null;
            }

            int sortedValue = node.get("sort_value").asInt();

            return new Sticker(id, name, packId, description, tags, type, formatType, available, authorGuildId, DiscordUser.getById(author.get("id").asText()), sortedValue);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

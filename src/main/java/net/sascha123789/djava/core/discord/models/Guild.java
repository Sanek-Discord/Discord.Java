package net.sascha123789.djava.core.discord.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sascha123789.djava.core.discord.caching.EmojiCacheManager;
import net.sascha123789.djava.core.discord.caching.StickerCacheManager;
import net.sascha123789.djava.core.discord.std.DiscordAccount;
import net.sascha123789.djava.core.discord.std.GeneralConfig;
import net.sascha123789.djava.core.discord.types.guilds.*;
import net.sascha123789.djava.core.discord.types.std.DiscordCache;
import net.sascha123789.djava.core.discord.types.std.DiscordIntent;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Guild {
    private ObjectNode node;
    private Guild(String guildId) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(GeneralConfig.API_STRING + "guilds/" + guildId + "?with_counts=true"))
                    .header("Authorization", "Bot " + GeneralConfig.getToken())
                    .header("User-Agent", "DiscordBot (" + GeneralConfig.API_STRING + "guilds/" + guildId + "?with_counts=true" + ", 10)")
                    .build();

            DiscordAccount.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(content -> {
                        try {
                            this.node = mapper.readValue(content, ObjectNode.class);

                            if(node.has("message")) {
                                String message = node.get("message").asText();
                                String reason = "";

                                if(message.equals("Missing Access")) {
                                    reason = "The bot is not in a guild";
                                }

                                throw new RuntimeException("Message: " + message + "\nReason: " + reason);
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }).join();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns Guild id as String**/
    public String getId() {
        return node.get("id").asText();
    }

    /**
     * Returns Guild id as Long**/
    public long getIdAsLong() {
        return Long.parseLong(node.get("id").asText());
    }

    /**
     * Returns Guild name**/
    public String getName() {
        return new String(node.get("name").asText().getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Returns Guild icon hash**/
    public String getIconHash() {
        return node.get("icon").asText();
    }

    /**
     * Returns Guild icon url**/
    public String getIconUrl() {
        return "https://cdn.discordapp.com/icons/" + this.getId() + "/" + this.getIconHash() + ".png";
    }

    /**
     * Returns Guild description**/
    public String getDescription() {
        return node.get("description").asText();
    }

    /**
     * Returns Guild splash hash**/
    public String getSplashHash() {
        return node.get("splash").asText();
    }

    /**
     * Returns Guild splash url**/
    public String getSplashUrl() {
        return "https://cdn.discordapp.com/splashes/" + this.getId() + "/" + this.getSplashHash() + ".png";
    }

    /**
     * Returns Guild features as String list**/
    public List<String> getStringFeatures() {
        List<String> features = new ArrayList<>();
        for(JsonNode el: node.get("features")) {
            features.add(el.asText());
        }

        return features;
    }

    /**
     * Returns list of GuildFeatures**/
    public List<GuildFeature> getFeatures() {
        List<GuildFeature> features = new ArrayList<>();

        for(JsonNode el: node.get("features")) {
            String name = el.asText();

            switch (name) {
                case "ANIMATED_BANNER":
                    features.add(GuildFeature.ANIMATED_BANNER);
                    break;
                case "ANIMATED_ICON":
                    features.add(GuildFeature.ANIMATED_ICON);
                    break;
                case "APPLICATION_COMMAND_PERMISSIONS_V2":
                    features.add(GuildFeature.APPLICATION_COMMAND_PERMISSIONS_V2);
                    break;
                case "AUTO_MODERATION":
                    features.add(GuildFeature.AUTO_MODERATION);
                    break;
                case "BANNER":
                    features.add(GuildFeature.BANNER);
                    break;
                case "COMMUNITY":
                    features.add(GuildFeature.COMMUNITY);
                    break;
                case "DEVELOPER_SUPPORT_SERVER":
                    features.add(GuildFeature.DEVELOPER_SUPPORT_SERVER);
                    break;
                case "DISCOVERABLE":
                    features.add(GuildFeature.DISCOVERABLE);
                    break;
                case "FEATURABLE":
                    features.add(GuildFeature.FEATURABLE);
                    break;
                case "INVITES_DISABLED":
                    features.add(GuildFeature.INVITES_DISABLED);
                    break;
                case "INVITE_SPLASH":
                    features.add(GuildFeature.INVITE_SPLASH);
                    break;
                case "MEMBER_VERIFICATION_GATE_ENABLED":
                    features.add(GuildFeature.MEMBER_VERIFICATION_GATE_ENABLED);
                    break;
                case "MONETIZATION_ENABLED":
                    features.add(GuildFeature.MONETIZATION_ENABLED);
                    break;
                case "MORE_STICKERS":
                    features.add(GuildFeature.MORE_STICKERS);
                    break;
                case "NEWS":
                    features.add(GuildFeature.NEWS);
                    break;
                case "PARTNERED":
                    features.add(GuildFeature.PARTNERED);
                    break;
                case "PREVIEW_ENABLED":
                    features.add(GuildFeature.PREVIEW_ENABLED);
                    break;
                case "ROLE_ICONS":
                    features.add(GuildFeature.ROLE_ICONS);
                    break;
                case "TICKETED_EVENTS_ENABLED":
                    features.add(GuildFeature.TICKETED_EVENTS_ENABLED);
                    break;
                case "VANITY_URL":
                    features.add(GuildFeature.VANITY_URL);
                    break;
                case "VERIFIED":
                    features.add(GuildFeature.VERIFIED);
                    break;
                case "VIP_REGIONS":
                    features.add(GuildFeature.VIP_REGIONS);
                    break;
                case "WELCOME_SCREEN_ENABLED":
                    features.add(GuildFeature.WELCOME_SCREEN_ENABLED);
                    break;
            }
        }

        return features;
    }

    /**
     * Returns Guild members count**/
    public int getMembersCount() {
        return node.get("approximate_member_count").asInt();
    }

    /**
     * Returns Guild custom emojis list**/
    public List<Emoji> getEmojis() {
        if(!GeneralConfig.getIntents().contains(DiscordIntent.GUILD_EMOJIS_AND_STICKERS)) {
            throw new RuntimeException("The bot must have a stickers and emojis intent!");
        }

        if(node.get("emojis").isEmpty()) {
            return null;
        }

        if(GeneralConfig.getCaches().contains(DiscordCache.EMOJIS)) {
            try {
                if(EmojiCacheManager.CACHE.get(this).size() == 0) {
                    EmojiCacheManager.CACHE.put(this, this.getEmojisWithoutCaching());
                } else if(node.get("emojis").size() != EmojiCacheManager.CACHE.get(this).size()) {
                    EmojiCacheManager.CACHE.asMap().clear();

                    EmojiCacheManager.CACHE.put(this, this.getEmojisWithoutCaching());
                }

                return EmojiCacheManager.CACHE.get(this);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return this.getEmojisWithoutCaching();
        }
    }



    /**
     * Returns Guild custom emojis list without if-else condition
     * Not recommended to use with enabled caching, better usable method for caching getEmojis**/
    public List<Emoji> getEmojisWithoutCaching() {
        if(!GeneralConfig.getIntents().contains(DiscordIntent.GUILD_EMOJIS_AND_STICKERS)) {
            throw new RuntimeException("The bot must have a stickers and emojis intent!");
        }

        if(node.get("emojis").isEmpty()) {
            return null;
        }

            List<Emoji> emojis = new ArrayList<>();
            AtomicReference<String> json = new AtomicReference<>("");
            ObjectMapper mapper = new ObjectMapper();

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        HttpRequest request = HttpRequest.newBuilder()
                                .GET()
                                .uri(new URI(GeneralConfig.API_STRING + "guilds/" + getId() + "/emojis"))
                                .header("Authorization", "Bot " + GeneralConfig.getToken())
                                .header("User-Agent", "DiscordBot (" + GeneralConfig.API_STRING + "guilds/" + getId() + "/emojis" + ", 10)")
                                .build();

                        DiscordAccount.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                .thenApply(HttpResponse::body)
                                .thenAccept(content -> {
                                    json.set(content);
                                }).join();

                        ArrayNode node = mapper.readValue(json.get(), ArrayNode.class);
                        int size = node.size();

                        for (int i = 0; i < size; i++) {
                            JsonNode el = node.get(i);
                            emojis.add(Emoji.fromJson(el.toString()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "Getting guild emojis");

            thread.start();

            try {
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return emojis;
    }

    /**
     * Returns discovery banner hash if guild has DISCOVERABLE Guild Feature**/
    public String getDiscoveryBannerHash() {
        if(this.getFeatures().contains(GuildFeature.DISCOVERABLE)) {
            return node.get("discovery_splash").asText();
        } else {
            return null;
        }
    }

    /**
     * Returns Guild discovery banner url**/
    public String getDiscoveryBannerUrl() {
        return "https://cdn.discordapp.com/discovery-splashes/" + this.getId() + "/" + this.getDiscoveryBannerHash() + ".png";
    }

    /**
     * Returns banner hash if Guild has BANNER or ANIMATED_BANNER Guild features**/
    public String getBannerHash() {
        if(this.getFeatures().contains(GuildFeature.BANNER) || this.getFeatures().contains(GuildFeature.ANIMATED_BANNER)) {
            return node.get("banner").asText();
        } else {
            return null;
        }
    }

    /**
     * Returns Guild banner url**/
    public String getBannerUrl() {
        return "https://cdn.discordapp.com/banners/" + this.getId() + "/" + this.getBannerHash() + ".png";
    }

    /**
     * Returns Guild online members count**/
    public int getOnlineMembersCount() {
        return node.get("approximate_presence_count").asInt();
    }

    /**
     * Returns Guild stickers list without if-else condition
     * Not recommended to use with enabled caching, better usable method for caching getStickers**/
    public List<Sticker> getStickersWithoutCaching() {
        if(!GeneralConfig.getIntents().contains(DiscordIntent.GUILD_EMOJIS_AND_STICKERS)) {
            throw new RuntimeException("The bot must have a stickers and emojis intent!");
        }

        if(node.get("stickers").isEmpty()) {
            return null;
        }

        List<Sticker> stickers = new ArrayList<>();
        AtomicReference<String> json = new AtomicReference<>("");
        ObjectMapper mapper = new ObjectMapper();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .GET()
                            .uri(new URI(GeneralConfig.API_STRING + "guilds/" + getId() + "/emojis"))
                            .header("Authorization", "Bot " + GeneralConfig.getToken())
                            .header("User-Agent", "DiscordBot (" + GeneralConfig.API_STRING + "guilds/" + getId() + "/stickers" + ", 10)")
                            .build();

                    DiscordAccount.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body)
                            .thenAccept(content -> {
                                json.set(content);
                            }).join();

                    ArrayNode node = mapper.readValue(json.get(), ArrayNode.class);
                    int size = node.size();

                    for (int i = 0; i < size; i++) {
                        JsonNode el = node.get(i);
                        stickers.add(Sticker.fromJson(el.asText()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Getting guild stickers");

        thread.start();

        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return stickers;
    }

    /**
     * Returns guild stickers**/
    public List<Sticker> getStickers() {
        if(!GeneralConfig.getIntents().contains(DiscordIntent.GUILD_EMOJIS_AND_STICKERS)) {
            throw new RuntimeException("The bot must have a stickers and emojis intent!");
        }

        if(node.get("stickers").isEmpty()) {
            return null;
        }

        if(GeneralConfig.getCaches().contains(DiscordCache.STICKERS)) {
            try {
                if(StickerCacheManager.CACHE.get(this).size() == 0) {
                    StickerCacheManager.CACHE.put(this, this.getStickersWithoutCaching());
                } else if(node.get("stickers").size() != StickerCacheManager.CACHE.get(this).size()) {
                    StickerCacheManager.CACHE.asMap().clear();

                    StickerCacheManager.CACHE.put(this, this.getStickersWithoutCaching());
                }

                return StickerCacheManager.CACHE.get(this);
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return this.getStickersWithoutCaching();
        }
    }

    /**
     * Returns guild owner as DiscordUser object**/
    public DiscordUser getOwnerAsUser() {
        return DiscordUser.getById(node.get("owner_id").asText());
    } // TODO: add method getOwnerAsMember

    /**
     * Returns guild voice region**/
    public String getVoiceRegion() {
        return node.get("region").asText();
    }

    /**
     * Returns guild afk timeout in seconds**/
    public int getAfkTimeout() {
        return node.get("afk_timeout").asInt();
    } // TODO: add method getAfkChannel

    /**
     * Returns true if guild widget enabled**/
    public boolean isWidgetEnabled() {
        return node.get("widget_enabled").asBoolean();
    } // TODO: add method getWidgetChannel


    /**
     * Returns guild verification level as integer**/
    public int getIntVerificationLevel() {
        return node.get("verification_level").asInt();
    }

    /**
     * Returns guild verification level**/
    public GuildVerificationLevel getVerificationLevel() {
        GuildVerificationLevel level = null;

        switch (this.getIntVerificationLevel()) {
            case 0:
                level = GuildVerificationLevel.NONE;
                break;
            case 1:
                level = GuildVerificationLevel.LOW;
                break;
            case 2:
                level = GuildVerificationLevel.MEDIUM;
                break;
            case 3:
                level = GuildVerificationLevel.HIGH;
                break;
            case 4:
                level = GuildVerificationLevel.VERY_HIGH;
                break;
        }

        return level;
    }

    /**
     * Returns guild default notifications level as integer**/
    public int getIntDefaultNotificationsLevel() {
        return node.get("default_message_notifications").asInt();
    }

    /**
     * Returns guild default notifications level**/
    public GuildDefaultNotificationsLevel getDefaultNotificationsLevel() {
        GuildDefaultNotificationsLevel level = null;

        switch(this.getIntDefaultNotificationsLevel()) {
            case 0:
                level = GuildDefaultNotificationsLevel.ALL_MESSAGES;
                break;
            case 1:
                level = GuildDefaultNotificationsLevel.ONLY_MENTIONS;
                break;
        }

        return level;
    }

    /**
     * Returns guild mfa level as integer**/
    public int getIntMfaLevel() {
        return node.get("mfa_level").asInt();
    }

    /**
     * Returns guild mfa level**/
    public GuildMfaLevel getMfaLevel() {
        GuildMfaLevel level = null;

        switch(this.getIntMfaLevel()) {
            case 0:
                level = GuildMfaLevel.NONE;
                break;
            case 1:
                level = GuildMfaLevel.ELEVATED;
                break;
        }

        return level;
    }

    /**
     * Returns guild explicit content filter level as integer**/
    public int getIntExplicitContentFilterLevel() {
        return node.get("explicit_content_filter").asInt();
    }

    /**
     * Returns guild explicit content filter level**/
    public GuildExplicitContentFilterLevel getExplicitContentFilterLevel() {
        GuildExplicitContentFilterLevel level = null;

        switch(this.getIntExplicitContentFilterLevel()) {
            case 0:
                level = GuildExplicitContentFilterLevel.DISABLED;
                break;
            case 1:
                level = GuildExplicitContentFilterLevel.MEMBERS_WITHOUT_ROLES;
                break;
            case 2:
                level = GuildExplicitContentFilterLevel.ALL_MEMBERS;
                break;
        }

        return level;
    }

    /**
     * Returns guild maximum members limit**/
    public int getMaximumMembersLimit() {
        return node.get("max_members").asInt();
    }

    /**
     * Returns guild vanity url code**/
    public String getVanityUrlCode() {
        return node.get("vanity_url_code").asText();
    }

    /**
     * Returns guild vanity url invite**/
    public String getVanityUrl() {
        return "https://discord.gg/" + this.getVanityUrlCode();
    }

    /**
     * Returns guild boost level as integer**/
    public int getIntBoostLevel() {
        return node.get("premium_tier").asInt();
    }

    /**
     * Returns guild boost level**/
    public GuildBoostLevel getBoostLevel() {
        GuildBoostLevel level = null;

        switch(this.getIntBoostLevel()) {
            case 0:
                level = GuildBoostLevel.NONE;
                break;
            case 1:
                level = GuildBoostLevel.LEVEL_ONE;
                break;
            case 2:
                level = GuildBoostLevel.LEVEL_TWO;
                break;
            case 3:
                level = GuildBoostLevel.LEVEL_THREE;
                break;
        }

        return level;
    }

    /**
     * Returns guild boost count**/
    public int getBoostCount() {
        return node.get("premium_subscription_count").asInt();
    }

    /**
     * Returns guild system channel flags**/
    public int getSystemChannelFlagsCode() {
        return node.get("system_channel_flags").asInt();
    }

    /**
     * Returns guild community locale as String**/
    public String getCommunityLocaleAsString() {
        return node.get("preferred_locale").asText();
    }

    /**
     * Returns guild community locale**/
    public GuildLocale getCommunityLocale() {
        GuildLocale locale = null;

        switch (this.getCommunityLocaleAsString()) {
            case "da":
                locale = GuildLocale.DANISH;
                break;
            case "de":
                locale = GuildLocale.GERMAN;
                break;
            case "en-GB":
                locale = GuildLocale.ENGLISH_UK;
                break;
            case "en-US":
                locale = GuildLocale.ENGLISH_US;
                break;
            case "es-ES":
                locale = GuildLocale.SPANISH;
                break;
            case "fr":
                locale = GuildLocale.FRENCH;
                break;
            case "hr":
                locale = GuildLocale.CROATIAN;
                break;
            case "it":
                locale = GuildLocale.ITALIAN;
                break;
            case "lt":
                locale = GuildLocale.LITHUANIAN;
                break;
            case "hu":
                locale = GuildLocale.HUNGARIAN;
                break;
            case "nl":
                locale = GuildLocale.DUTCH;
                break;
            case "no":
                locale = GuildLocale.NORWEGIAN;
                break;
            case "pl":
                locale = GuildLocale.POLISH;
                break;
            case "pt-BR":
                locale = GuildLocale.PORTUGUESE;
                break;
            case "ro":
                locale = GuildLocale.ROMANIAN;
                break;
            case "fi":
                locale = GuildLocale.FINNISH;
                break;
            case "sv-SE":
                locale = GuildLocale.SWEDISH;
                break;
            case "vi":
                locale = GuildLocale.VIETNAMESE;
                break;
            case "tr":
                locale = GuildLocale.TURKISH;
                break;
            case "cs":
                locale = GuildLocale.CZECH;
                break;
            case "el":
                locale = GuildLocale.GREEK;
                break;
            case "bg":
                locale = GuildLocale.BULGARIAN;
                break;
            case "ru":
                locale = GuildLocale.RUSSIAN;
                break;
            case "uk":
                locale = GuildLocale.UKRAINIAN;
                break;
            case "hi":
                locale = GuildLocale.HINDI;
                break;
            case "th":
                locale = GuildLocale.THAI;
                break;
            case "zh-CN":
                locale = GuildLocale.CHINESE_CHINA;
                break;
            case "ja":
                locale = GuildLocale.JAPANESE;
                break;
            case "zh-TW":
                locale = GuildLocale.CHINESE_TAIWAN;
                break;
            case "ko":
                locale = GuildLocale.KOREAN;
                break;
        }

        return locale;
    }

    /**
     * Returns true if boost progress bar enabled in guild**/
    public boolean isBoostProgressBarEnabled() {
        return node.get("premium_progress_bar_enabled").asBoolean();
    }

    /**
     * Returns guild nsfw level as integer**/
    public int getIntNsfwLevel() {
        return node.get("nsfw_level").asInt();
    }

    /**
     * Returns guild nsfw level**/
    public GuildNsfwLevel getNsfwLevel() {
        GuildNsfwLevel level = null;

        switch(this.getIntNsfwLevel()) {
            case 0:
                level = GuildNsfwLevel.DEFAULT;
                break;
            case 1:
                level = GuildNsfwLevel.EXPLICIT;
                break;
            case 2:
                level = GuildNsfwLevel.SAFE;
                break;
            case 3:
                level = GuildNsfwLevel.AGE_RESTRICTED;
                break;
        }

        return level;
    }

    /**
     * Returns true if guild is nsfw**/
    public boolean isNsfw() {
        return node.get("nsfw").asBoolean();
    }

    /**
     * Returns role object by id**/
    public Role getRoleById(String id) {
        return Role.getById(this.getId(), id);
    }

    /**
     * Get Guild object by id**/
    public static Guild getGuild(String id) {
        //{"id": "922404056599781397", "name": "\u0421\u0435\u0440\u0432\u0435\u0440 Gleb(\u0430)", "icon": "697b6b27027f9a9beff511291086d3dd", "description": null, "splash": null, "discovery_splash": null, "features": ["COMMUNITY", "NEWS", "APPLICATION_COMMAND_PERMISSIONS_V2"], "approximate_member_count": 6, "approximate_presence_count": 3, "emojis": [{"name": "gay_down", "roles": [], "id": "926368017942716496", "require_colons": true, "managed": false, "animated": false, "available": true}, {"name": "gay_up", "roles": [], "id": "934009153784008705", "require_colons": true, "managed": false, "animated": false, "available": true}, {"name": "uno1", "roles": [], "id": "934010912115937290", "require_colons": true, "managed": false, "animated": false, "available": true}, {"name": "uno2", "roles": [], "id": "934010970928459836", "require_colons": true, "managed": false, "animated": false, "available": true}, {"name": "uno3", "roles": [], "id": "934011155670765638", "require_colons": true, "managed": false, "animated": false, "available": true}, {"name": "uno4", "roles": [], "id": "934011921789755392", "require_colons": true, "managed": false, "animated": false, "available": true}, {"name": "uno5", "roles": [], "id": "934011974143074314", "require_colons": true, "managed": false, "animated": false, "available": true}, {"name": "emoji_8", "roles": [], "id": "948563723008757810", "require_colons": true, "managed": false, "animated": true, "available": true}, {"name": "emoji_9", "roles": [], "id": "949334032976576552", "require_colons": true, "managed": false, "animated": false, "available": true}, {"name": "booms", "roles": [], "id": "967684261291393055", "require_colons": true, "managed": false, "animated": true, "available": true}, {"name": "pngtransparentverifiedbadgesymbo", "roles": [], "id": "1022071133157204029", "require_colons": true, "managed": false, "animated": false, "available": true}, {"name": "pngtransparentverifiedbadgesymbo", "roles": [], "id": "1022071532899553280", "require_colons": true, "managed": false, "animated": false, "available": true}, {"name": "anime_tyanochka", "roles": [], "id": "1046394376991023175", "require_colons": true, "managed": false, "animated": false, "available": true}], "stickers": [], "banner": null, "owner_id": "919960814465740830", "application_id": null, "region": "japan", "afk_channel_id": null, "afk_timeout": 300, "system_channel_id": null, "widget_enabled": false, "widget_channel_id": null, "verification_level": 1, "roles": [{"id": "922404056599781397", "name": "@everyone", "description": null, "permissions": "1071698529857", "position": 0, "color": 0, "hoist": false, "managed": false, "mentionable": false, "icon": null, "unicode_emoji": null, "flags": 0}, {"id": "1010460076366114859", "name": "Super Fish Bot", "description": null, "permissions": "8", "position": 3, "color": 0, "hoist": false, "managed": true, "mentionable": false, "icon": null, "unicode_emoji": null, "flags": 0, "tags": {"bot_id": "1010459034341613578"}}, {"id": "1023968647003242559", "name": "Seizer", "description": null, "permissions": "274877910024", "position": 2, "color": 0, "hoist": false, "managed": true, "mentionable": false, "icon": null, "unicode_emoji": null, "flags": 0, "tags": {"bot_id": "1013130193923211314"}}, {"id": "1023968752821342290", "name": "\u043d\u043e\u0432\u0430\u044f \u0440\u043e\u043b\u044c", "description": null, "permissions": "1071698660937", "position": 4, "color": 10181046, "hoist": false, "managed": false, "mentionable": false, "icon": null, "unicode_emoji": null, "flags": 0}, {"id": "1023973707460399104", "name": "ban", "description": null, "permissions": "1071698660933", "position": 1, "color": 2067276, "hoist": false, "managed": false, "mentionable": false, "icon": null, "unicode_emoji": null, "flags": 0}, {"id": "1047119220883849249", "name": "Kobrex", "description": null, "permissions": "8", "position": 1, "color": 0, "hoist": false, "managed": true, "mentionable": false, "icon": null, "unicode_emoji": null, "flags": 0, "tags": {"bot_id": "947406604192587787"}}], "default_message_notifications": 1, "mfa_level": 0, "explicit_content_filter": 2, "max_presences": null, "max_members": 500000, "max_stage_video_channel_users": 0, "max_video_channel_users": 25, "vanity_url_code": null, "premium_tier": 0, "premium_subscription_count": 0, "system_channel_flags": 0, "preferred_locale": "ru", "rules_channel_id": "1043447776320696431", "safety_alerts_channel_id": null, "public_updates_channel_id": "1043447776849174538", "hub_type": null, "premium_progress_bar_enabled": false, "nsfw": false, "nsfw_level": 0}
        return new Guild(id);
    }
}

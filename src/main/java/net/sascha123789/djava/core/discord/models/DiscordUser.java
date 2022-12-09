package net.sascha123789.djava.core.discord.models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sascha123789.djava.core.discord.std.DiscordAccount;
import net.sascha123789.djava.core.discord.std.GeneralConfig;
import net.sascha123789.djava.core.discord.types.std.NitroType;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DiscordUser extends User{
    protected ObjectNode node;
    private String id;
    private DiscordUser(String id) {
        super();
        this.id = id;

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://discord.com/api/v10/users/" + id))
                .header("Authorization", "Bot " + GeneralConfig.getToken())
                .header("User-Agent", "DiscordBot (" + GeneralConfig.API_STRING + "users/" + id + ", 10)")
                .build();

        DiscordAccount.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    this.json = content;
                }).join();

        try {
            this.node = mapper.readValue(this.json, ObjectNode.class);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns user id as string**/
    @Override
    public String getId() {
        return node.get("id").asText();
    }

    /**
     * Returns user id as long**/
    @Override
    public long getIdAsLong() {
        long id = Long.parseLong(node.get("id").asText());
        return id;
    }

    /**
     * Returns user username**/
    @Override
    public String getUsername() {
        return node.get("username").asText();
    }

    /**
     * Returns user discriminator(tag/#xxxx)**/
    @Override
    public String getDiscriminator() {
        return node.get("discriminator").asText();
    }

    /**
     * Returns user discriminator as integer**/
    @Override
    public int getDiscriminatorAsInt() {
        int discriminator = Integer.parseInt(node.get("discriminator").asText());
        return discriminator;
    }

    /**
     * Returns user avatar hash
     * Not avatar url!!!**/
    @Override
    public String getAvatarHash() {
        return node.get("avatar").asText();
    }

    /**
     * Returns user avatar url**/
    @Override
    public String getAvatarUrl() {
        return "https://cdn.discordapp.com/avatars/" + this.getId() + "/" + this.getAvatarHash() + ".png";
    }

    /**
     * Returns true if user is bot**/
    @Override
    public boolean isBot() {
        return node.get("bot").asBoolean();
    }

    /**
     * Returns true if on user account enabled 2fa**/
    @Override
    public boolean isMfaEnabled() {
        return node.get("mfa_enabled").asBoolean();
    }

    /**
     * Returns null if user not have an banner, else user banner hash
     * Not banner url!!!**/
    @Override
    public String getBannerHash() {
        return node.get("banner").asText();
    }

    /**
     * Returns null if user not have an banner, else banner url**/
    @Override
    public String getBannerUrl() {
        return this.getBannerHash() != null ? "https://cdn.discordapp.com/banners/" + this.getId() + "/" + this.getBannerHash() + ".png" : null;
    }

    /**
     * Returns user selected language
     * Example: "en-US"**/
    @Override
    public String getLocaleAsString() {
        return node.get("locale").asText();
    }

    /**
     * Returns user flags(badges) code
     * Example: 1 (DISCORD_STAFF)**/
    @Override
    public int getUserFlagsCode() {
        return node.get("flags").asInt();
    }

    /**
     * Returns user nitro type
     * 0 - none
     * 1 - classic
     * 2 - nitro
     * 3 - basic**/
    @Override
    public int getNitroCodeType() {
        return node.get("premium_type").asInt();
    }

    /**
     * Returns nitro type(Not code!)
     * Example: NitroType.BASIC**/
    @Override
    public NitroType getNitroType() {
        NitroType nitroType = NitroType.NONE;
        int nitroCode = node.get("premium_type").asInt();

        switch(nitroCode) {
            case 0:
                nitroType = NitroType.NONE;
                break;
            case 1:
                nitroType = NitroType.CLASSIC;
                break;
            case 2:
                nitroType = NitroType.NITRO;
                break;
            case 3:
                nitroType = NitroType.BASIC;
                break;
        }
        return nitroType;
    }

    /**
     * Returns user description/biography**/
    @Override
    public String getBio() {
        return node.get("bio").asText();
    }

    /**
     * Returns user object from provided json**/
    public static DiscordUser getById(String id) {
        return new DiscordUser(id);
    }

    /**
     * Returns user object from provided json**/
    public static DiscordUser getById(long id) {
        return new DiscordUser(String.valueOf(id));
    }
}

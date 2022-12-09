package net.sascha123789.djava.core.discord.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sascha123789.djava.core.discord.types.std.NitroType;

public abstract class User {
    protected String json;
    protected ObjectMapper mapper;

    protected User() {
        this.mapper = new ObjectMapper();
    }

    public abstract String getId();

    public abstract long getIdAsLong();

    public abstract String getUsername();

    public abstract String getDiscriminator();

    public abstract int getDiscriminatorAsInt();

    public abstract String getAvatarHash();

    public abstract String getAvatarUrl();

    public abstract boolean isBot();

    public abstract boolean isMfaEnabled();

    public abstract String getBannerHash();

    public abstract String getBannerUrl();

    public abstract String getLocaleAsString();

    public abstract int getUserFlagsCode();

    public abstract int getNitroCodeType();

    public abstract NitroType getNitroType();

    public abstract String getBio();
}

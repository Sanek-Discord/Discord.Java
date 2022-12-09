package net.sascha123789.djava.core.discord.types;

public enum StickerType {STANDARD(1), GUILD(2);
    private int code = 0;

    private StickerType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

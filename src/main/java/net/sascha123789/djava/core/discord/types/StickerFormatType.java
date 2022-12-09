package net.sascha123789.djava.core.discord.types;

public enum StickerFormatType {PNG(1), APNG(2), LOTTIE(3);
    private int code = 0;

    private StickerFormatType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

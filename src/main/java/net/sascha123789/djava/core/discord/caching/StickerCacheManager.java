package net.sascha123789.djava.core.discord.caching;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.sascha123789.djava.core.discord.models.Guild;
import net.sascha123789.djava.core.discord.models.Sticker;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StickerCacheManager {
    public static CacheLoader<Guild, List<Sticker>> CACHE_LOADER = new CacheLoader<Guild, List<Sticker>>() {
        @Override
        public List<Sticker> load(Guild key) throws Exception {
            return key.getStickersWithoutCaching();
        }
    };

    public static LoadingCache<Guild, List<Sticker>> CACHE = CacheBuilder.newBuilder()
            .refreshAfterWrite(1, TimeUnit.MINUTES)
            .build(CACHE_LOADER);
}

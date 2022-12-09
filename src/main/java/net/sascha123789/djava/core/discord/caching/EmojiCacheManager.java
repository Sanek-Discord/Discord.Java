package net.sascha123789.djava.core.discord.caching;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.sascha123789.djava.core.discord.models.Emoji;
import net.sascha123789.djava.core.discord.models.Guild;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EmojiCacheManager {
    public static CacheLoader<Guild, List<Emoji>> CACHE_LOADER = new CacheLoader<Guild, List<Emoji>>() {
        @Override
        public List<Emoji> load(Guild key) throws Exception {
            return key.getEmojisWithoutCaching();
        }
    };

    public static LoadingCache<Guild, List<Emoji>> CACHE = CacheBuilder.newBuilder()
            .refreshAfterWrite(1, TimeUnit.MINUTES)
            .build(CACHE_LOADER);
}

package org.broadleafcommerce.presentation.thymeleaf3.cache;

import org.broadleafcommerce.presentation.cache.BroadleafTemplateCacheContext;

public class BroadleafThymeleaf3CacheContext<K, V> implements BroadleafTemplateCacheContext<K, V> {

    protected BroadleafThymeleaf3ICache<K, V> blcCache;
    
    public BroadleafThymeleaf3CacheContext(BroadleafThymeleaf3ICache blcCache) {
        this.blcCache = blcCache;
    }
    
    @Override
    public V defaultGet(K key) {
        return blcCache.defaultGet(key);
    }

    @Override
    public void defaultPut(K key, V value) {
        blcCache.defaultPut(key, value);
    }

}

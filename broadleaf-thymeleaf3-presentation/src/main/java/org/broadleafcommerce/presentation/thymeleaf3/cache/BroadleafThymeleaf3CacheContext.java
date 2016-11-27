/*
 * #%L
 * broadleaf-thymeleaf3-presentation
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
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

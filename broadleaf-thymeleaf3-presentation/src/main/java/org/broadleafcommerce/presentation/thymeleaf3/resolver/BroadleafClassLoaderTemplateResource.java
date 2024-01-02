/*-
 * #%L
 * BroadleafCommerce Thymeleaf3 Presentation
 * %%
 * Copyright (C) 2009 - 2024 Broadleaf Commerce
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
package org.broadleafcommerce.presentation.thymeleaf3.resolver;

import org.broadleafcommerce.common.util.EfficientLRUMap;
import org.thymeleaf.templateresource.ClassLoaderTemplateResource;
import org.thymeleaf.templateresource.ITemplateResource;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public class BroadleafClassLoaderTemplateResource implements ITemplateResource {

    private final ClassLoaderTemplateResource delegateTemplateResource;

    protected static final Map<String, Boolean> RESOURCE_PATH_CACHE = new EfficientLRUMap<>(10000);

    public BroadleafClassLoaderTemplateResource(final ClassLoader classLoader, final String path, final String characterEncoding) {
        delegateTemplateResource = new ClassLoaderTemplateResource(classLoader, path, characterEncoding);
    }

    @Override
    public String getDescription() {
        return delegateTemplateResource.getDescription();
    }

    @Override
    public String getBaseName() {
        return delegateTemplateResource.getBaseName();
    }

    @Override
    public boolean exists() {
        Boolean fromCache = getFromCache(getDescription());
        if (fromCache != null) {
            return fromCache;
        }
        boolean resourcePresent = delegateTemplateResource.exists();
        addToCache(getDescription(), resourcePresent);

        return resourcePresent;
    }

    public Boolean getFromCache(String cacheKey) {
        return RESOURCE_PATH_CACHE.get(cacheKey);
    }

    public void addToCache(String cacheKey, Boolean isExist) {
        RESOURCE_PATH_CACHE.put(cacheKey, isExist);
    }

    @Override
    public Reader reader() throws IOException {
        return delegateTemplateResource.reader();
    }

    @Override
    public ITemplateResource relative(String relativeLocation) {
        return delegateTemplateResource.relative(relativeLocation);
    }

}

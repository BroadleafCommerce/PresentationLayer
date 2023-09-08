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

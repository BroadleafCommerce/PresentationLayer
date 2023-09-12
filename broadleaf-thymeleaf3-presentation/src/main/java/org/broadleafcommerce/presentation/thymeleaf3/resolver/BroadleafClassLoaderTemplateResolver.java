package org.broadleafcommerce.presentation.thymeleaf3.resolver;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

public class BroadleafClassLoaderTemplateResolver extends ClassLoaderTemplateResolver {

    private final ClassLoader classLoader;

    public BroadleafClassLoaderTemplateResolver() {
        this(null);
    }

    public BroadleafClassLoaderTemplateResolver(final ClassLoader classLoader) {
        super();
        // Class Loader might be null if we want to apply the default one
        this.classLoader = classLoader;
    }

    @Override
    protected ITemplateResource computeTemplateResource(
            final IEngineConfiguration configuration, final String ownerTemplate, final String template, final String resourceName, final String characterEncoding, final Map<String, Object> templateResolutionAttributes) {
        return new BroadleafClassLoaderTemplateResource(this.classLoader, resourceName, characterEncoding);
    }

}

/*-
 * #%L
 * BroadleafCommerce Thymeleaf3 Presentation
 * %%
 * Copyright (C) 2009 - 2023 Broadleaf Commerce
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

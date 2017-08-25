/*
 * #%L
 * broadleaf-theme
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
package org.broadleafcommerce.presentation.thymeleaf3.resolver;

import org.broadleafcommerce.common.web.resource.BroadleafContextUtil;
import org.broadleafcommerce.core.web.resolver.DatabaseResourceResolverExtensionManager;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

/**
 * The injection happens in XML configuration.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class BroadleafThymeleaf3DatabaseTemplateResolver extends AbstractConfigurableTemplateResolver {
    
    protected DatabaseResourceResolverExtensionManager resourceResolverExtensionManager;

    protected BroadleafContextUtil blcContextUtil;
    
    public BroadleafThymeleaf3DatabaseTemplateResolver() {
        setCheckExistence(true);
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, String resourceName, String characterEncoding, Map<String, Object> templateResolutionAttributes) {
        return new BroadleafThymeleaf3DatabaseResourceResolver(resourceResolverExtensionManager, blcContextUtil, template);
    }

    public DatabaseResourceResolverExtensionManager getResourceResolverExtensionManager() {
        return resourceResolverExtensionManager;
    }
    
    public void setResourceResolverExtensionManager(DatabaseResourceResolverExtensionManager resourceResolverExtensionManager) {
        this.resourceResolverExtensionManager = resourceResolverExtensionManager;
    }

    public void setBroadleafContextUtil(BroadleafContextUtil blcContextUtil) {
        this.blcContextUtil = blcContextUtil;
    }

}

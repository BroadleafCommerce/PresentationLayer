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

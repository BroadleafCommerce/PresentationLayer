package org.broadleafcommerce.presentation.thymeleaf3.resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.web.resource.BroadleafContextUtil;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @author Jon Fleschler (jfleschler)
 */
public class DelegatingThymeleaf3TemplateResolver extends SpringResourceTemplateResolver {

    private static final Log LOG = LogFactory.getLog(DelegatingThymeleaf3TemplateResolver.class);

    @Resource(name = "blBroadleafContextUtil")
    protected BroadleafContextUtil blcContextUtil;

    protected BroadleafTemplateResolver templateResolver;

    public DelegatingThymeleaf3TemplateResolver() {
        super();
        setCheckExistence(true);
    }

    @Override
    protected ITemplateResource computeTemplateResource(final IEngineConfiguration configuration, final String ownerTemplate,
                                                        final String template, final String resourceName, final String characterEncoding,
                                                        final Map<String, Object> templateResolutionAttributes) {
        blcContextUtil.establishThinRequestContextWithoutSandBox();
        InputStream resolvedResource = templateResolver.resolveResource(template, resourceName);
        return new BroadleafThymeleaf3ITemplateResource(resourceName, resolvedResource);
    }

    public BroadleafTemplateResolver getTemplateResolver() {
        return templateResolver;
    }

    public void setTemplateResolver(BroadleafTemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }
}

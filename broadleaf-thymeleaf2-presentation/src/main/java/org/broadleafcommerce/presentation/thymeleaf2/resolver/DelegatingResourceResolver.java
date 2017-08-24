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
package org.broadleafcommerce.presentation.thymeleaf2.resolver;

import org.broadleafcommerce.common.web.resource.BroadleafContextUtil;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;

import java.io.InputStream;

import javax.annotation.Resource;


/**
 * An implementation of {@link IResourceResolver} that provides an extension point for retrieving
 * templates from the database.
 * 
 * @author Jon Fleschler (jfleschler)
 */
public class DelegatingResourceResolver implements IResourceResolver {

    @Resource(name = "blBroadleafContextUtil")
    protected BroadleafContextUtil blcContextUtil;

    protected BroadleafTemplateResolver templateResolver;

    public DelegatingResourceResolver(BroadleafTemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }

    @Override
    public String getName() {
        return "BL_CUSTOM";
    }

    @Override
    public InputStream getResourceAsStream(TemplateProcessingParameters params, String resourceName) {
        blcContextUtil.establishThinRequestContext();
        return templateResolver.resolveResource(params.getTemplateName(), resourceName);
    }
}

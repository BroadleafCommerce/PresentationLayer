/*
 * #%L
 * BroadleafCommerce Common Libraries
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
package org.broadleafcommerce.presentation.thymeleaf3;

import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.site.domain.Theme;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.common.web.resource.BroadleafContextUtil;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.util.Validate;

import java.util.Map;

import javax.annotation.Resource;

/**
 * Overrides the Thymeleaf ContextTemplateResolver and appends the org.broadleafcommerce.presentation.thymeleaf3.Theme path to the url
 * if it exists.
 */
public class BroadleafThymeleaf3ThemeAwareTemplateResolver extends SpringResourceTemplateResolver {

    @Resource(name = "blBroadleafContextUtil")
    protected BroadleafContextUtil blcContextUtil;

    protected String templateFolder = "";
    
    public BroadleafThymeleaf3ThemeAwareTemplateResolver() {
        super();
        setCheckExistence(true);
    }

    @Override
    protected ITemplateResource computeTemplateResource(final IEngineConfiguration configuration, final String ownerTemplate,
            final String template, final String resourceName, final String characterEncoding, final Map<String, Object> templateResolutionAttributes) {

        blcContextUtil.establishThinRequestContextWithoutSandBox();

        String themePath = getThemePath();

        Validate.notNull(template, "Template name cannot be null");

        String prefix = this.getPrefix();
        String themeAwareResourceName = resourceName;
        String actualPath = templateFolder == null ? "" : templateFolder;
        if (themePath != null && !themePath.trim().equals("")) {
            actualPath = themePath + "/" + actualPath;
        }
        if (prefix != null && !prefix.trim().equals("")) {
            // Using replaceOnce from StringUtils instead of normal replace because I want to prevent against the possibility of
            // special regex characters existing in prefix and screwing up the replacement
            // Intentionally using resourceName because it already has prefix/suffix/alias transformations applied to it
            themeAwareResourceName = StringUtils.replaceOnce(resourceName, prefix, prefix + actualPath);
        } else {
            themeAwareResourceName = resourceName + actualPath;
        }

        return super.computeTemplateResource(configuration, ownerTemplate, template, themeAwareResourceName, characterEncoding, templateResolutionAttributes);
    }

    protected String getThemePath() {
        Theme theme = BroadleafRequestContext.getBroadleafRequestContext().getTheme();

        return (theme == null) ? null : theme.getPath();
    }

    public String getTemplateFolder() {
        return templateFolder;
    }

    public void setTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder;
    }
    
}



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
package org.broadleafcommerce.presentation.thymeleaf2;

import org.broadleafcommerce.common.site.domain.Theme;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.common.web.resource.BroadleafContextUtil;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.util.Validate;

import javax.annotation.Resource;

/**
 * Overrides the Thymeleaf ContextTemplateResolver and appends the org.broadleafcommerce.presentation.thymeleaf2.Theme path to the url
 * if it exists.
 */
public class BroadleafThymeleafThemeAwareTemplateResolver extends SpringResourceTemplateResolver {

    @Resource(name = "blBroadleafContextUtil")
    protected BroadleafContextUtil blcContextUtil;
    
    protected String templateFolder = "";

    @Override
    protected String computeResourceName(final TemplateProcessingParameters templateProcessingParameters) {
        blcContextUtil.establishThinRequestContext();

        String themePath = getThemePath();

        checkInitialized();

        final String templateName = templateProcessingParameters.getTemplateName();

        Validate.notNull(templateName, "Template name cannot be null");

        String unaliasedName = this.getTemplateAliases().get(templateName);
        if (unaliasedName == null) {
            unaliasedName = templateName;
        }

        final StringBuilder resourceName = new StringBuilder();
        String prefix = this.getPrefix();
        if (prefix != null && ! prefix.trim().equals("")) {
            resourceName.append(prefix);
        } else {
            resourceName.append('/');
        }
        if (themePath != null) {
            resourceName.append(themePath).append('/');
        }
        if (templateName != null) {
            resourceName.append(templateFolder);
        }
        resourceName.append(unaliasedName);
        String suffix = this.getSuffix();
        if (suffix != null && ! suffix.trim().equals("")) {
            resourceName.append(suffix);
        }

        return resourceName.toString();
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



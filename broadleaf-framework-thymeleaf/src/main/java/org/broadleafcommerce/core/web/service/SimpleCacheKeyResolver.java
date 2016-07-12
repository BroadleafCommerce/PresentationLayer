/*
 * #%L
 * BroadleafCommerce Framework Web
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

package org.broadleafcommerce.core.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

/**
 * Default implementation of {@link TemplateCacheKeyResolverService} that returns a concatenation of a 
 * templateName and cacheKey.   If the cacheKey is set to none, null is returned resulting in no cache.
 * 
 * @author Brian Polster (bpolster)
 */
@Service("blTemplateCacheKeyResolver")
public class SimpleCacheKeyResolver implements TemplateCacheKeyResolverService {
    
    /**
     * Returns a concatenation of the templateName and cacheKey separated by an "_".    
     * If cacheKey is null, only the templateName is returned.
     * 
     * If cacheKey is "none" then null will be returned causing the template not to be cached.
     * 
     * @param templateName - Name of the template that is subject to being cached. 
     * @param cacheKey - Value of the parameter passed in from the template
     * @return
     */
    @Override
    public String resolveCacheKey(Arguments arguments, Element element) {
        StringBuilder sb = new StringBuilder();
        sb.append(getStringValue(arguments, element, "cacheKey", true));
        sb.append(resolveTemplateName(arguments, element));
        sb.append(resolveLineNumber(arguments, element));
        return sb.toString();
    }
    

    protected String resolveTemplateName(Arguments arguments, Element element) {
        String templateName = getStringValue(arguments, element, "templateName", true);

        if (StringUtils.isEmpty(templateName)) {
            templateName = (String) element.getNodeProperty("templateName");
        }

        if (StringUtils.isEmpty(templateName)) {
            templateName = element.getDocumentName();
        }
        
        return templateName;
    }
    
    protected Integer resolveLineNumber(Arguments arguments, Element element) {
        Integer line = element.getLineNumber();
        return line == null ? 0 : line;
    }

    protected String getStringValue(Arguments arguments, Element element, String attrName, boolean removeAttribute) {
        if (element.hasAttribute(attrName)) {
            String cacheKeyParam = element.getAttributeValue(attrName);
            Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                    .parseExpression(arguments.getConfiguration(), arguments, cacheKeyParam);
            if (removeAttribute) {
                element.removeAttribute(attrName);
            }
            return expression.execute(arguments.getConfiguration(), arguments).toString();

        }
        return "";
    }
}

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

package org.broadleafcommerce.common.web.cache.service;

import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    public String resolveCacheKey(String tagName, Map<String, String> tagAttributes, String documentName, Integer lineNumber, BroadleafThymeleafContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append(getStringValue("cacheKey", tagAttributes, true, context));
        String attributeDocName = getStringValue("templateName", tagAttributes, true, context);
        sb.append(attributeDocName == null ? documentName : attributeDocName);
        sb.append(lineNumber == null ? 0 : lineNumber);
        return sb.toString();
    }

    protected String getStringValue(String attrName, Map<String, String> tagAttributes, boolean removeAttribute, BroadleafThymeleafContext context) {
        if (tagAttributes.containsKey(attrName)) {
            String cacheKeyParam = tagAttributes.get(attrName);
            if (removeAttribute) {
                tagAttributes.remove(attrName);
            }
            return context.parseExpression(cacheKeyParam).toString();
        }
        return "";
    }

}

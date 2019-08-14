/*
 * #%L
 * broadleaf-thymeleaf3-presentation
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
package org.broadleafcommerce.presentation.thymeleaf3.cache;

import org.broadleafcommerce.presentation.cache.BroadleafTemplateCacheInvalidationContext;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;

public class BroadleafThymeleaf3CacheInvalidationContext implements BroadleafTemplateCacheInvalidationContext {

    protected ITemplateEngine templateEngine;
    
    @Override
    public void clearTemplateCacheFor(String path) {
        if (TemplateEngine.class.isAssignableFrom(templateEngine.getClass())) {
            ((TemplateEngine) templateEngine).clearTemplateCacheFor(path);
        } else {
            throw new UnsupportedOperationException("Unable to invalidate cache for template engine because it's of type " + templateEngine.getClass().getName() 
                + " which doesn't extend " + TemplateEngine.class.getName() + ". To fix this extend " + this.getClass().getName() 
                + " and implement the clearTemplateCacheFor method for your template engine");
        }
    }

    public ITemplateEngine getTemplateEngine() {
        return templateEngine;
    }
    
    public void setTemplateEngine(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

}

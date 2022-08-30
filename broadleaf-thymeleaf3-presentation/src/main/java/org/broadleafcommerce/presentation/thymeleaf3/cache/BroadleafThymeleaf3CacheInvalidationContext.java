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

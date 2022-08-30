package org.broadleafcommerce.presentation.thymeleaf3.extension;

import org.broadleafcommerce.common.extension.ExtensionResultHolder;
import org.broadleafcommerce.common.extension.ExtensionResultStatusType;
import org.broadleafcommerce.common.extension.TemplateCacheExtensionHandler;
import org.broadleafcommerce.common.extension.TemplateCacheExtensionManager;
import org.springframework.stereotype.Service;
import org.thymeleaf.cache.TemplateCacheKey;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service("blThymeleafTemplateCacheExtensionHandler")
public class ThymeleafTemplateCacheExtensionHandler implements TemplateCacheExtensionHandler {

    @Resource(name = "blTemplateCacheExtensionManager")
    protected TemplateCacheExtensionManager extensionManager;

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PostConstruct
    public void init(){
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }

    @Override
    public ExtensionResultStatusType getTemplateCacheKey(Object key, String template, ExtensionResultHolder<Object> extensionResultHolder) {
        if(key instanceof TemplateCacheKey) {
            TemplateCacheKey templateCacheKey = ((TemplateCacheKey) key);
            extensionResultHolder.setResult(new TemplateCacheKey(templateCacheKey.getOwnerTemplate(), template, templateCacheKey.getTemplateSelectors(), templateCacheKey.getLineOffset(), templateCacheKey.getColOffset(), templateCacheKey.getTemplateMode(), templateCacheKey.getTemplateResolutionAttributes()));
            return ExtensionResultStatusType.HANDLED;
        }
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType getTemplateName(Object key, ExtensionResultHolder<Object> extensionResultHolder) {
        if(key instanceof TemplateCacheKey) {
            TemplateCacheKey templateCacheKey = ((TemplateCacheKey) key);
            String template = templateCacheKey.getTemplate();
            extensionResultHolder.setResult(template);
            return ExtensionResultStatusType.HANDLED;
        }
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}

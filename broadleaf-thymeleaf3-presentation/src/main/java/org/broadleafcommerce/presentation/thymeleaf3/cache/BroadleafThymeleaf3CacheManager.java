package org.broadleafcommerce.presentation.thymeleaf3.cache;

import org.broadleafcommerce.common.web.cache.BLCICacheExtensionManager;
import org.thymeleaf.cache.AbstractCacheManager;
import org.thymeleaf.cache.ExpressionCacheKey;
import org.thymeleaf.cache.ICache;
import org.thymeleaf.cache.StandardCache;
import org.thymeleaf.cache.StandardCacheManager;
import org.thymeleaf.cache.TemplateCacheKey;
import org.thymeleaf.engine.TemplateModel;

import javax.annotation.Resource;

/**
 * Implementation of {@link org.thymeleaf.cache.AbstractCacheManager} to use {@link BroadleafThymeleaf3ICache} for templates.
 * This class heavily leverages {@link org.thymeleaf.cache.StandardCacheManager} functionality. Only the
 * initializeTemplateCache() method should behave differently by instantiating a BLCICache instead of a StandardCache.
 *
 * @author Chad Harchar (charchar)
 */
public class BroadleafThymeleaf3CacheManager extends AbstractCacheManager {

    @Resource(name = "blICacheExtensionManager")
    protected BLCICacheExtensionManager extensionManager;

    protected StandardCacheManager standardCacheManager = new StandardCacheManager();
    
    /**
     * This method was changed just to return a BLCICache, instead of a StandardCache
     *
     * @return
     */
    @Override
    protected ICache<TemplateCacheKey, TemplateModel> initializeTemplateCache() {
        final int maxSize = standardCacheManager.getTemplateCacheMaxSize();
        if (maxSize == 0) {
            return null;
        }
        return new BroadleafThymeleaf3ICache<TemplateCacheKey, TemplateModel>(
                standardCacheManager.getTemplateCacheName(), standardCacheManager.getTemplateCacheUseSoftReferences(),
                standardCacheManager.getTemplateCacheInitialSize(), maxSize,
                standardCacheManager.getTemplateCacheValidityChecker(), standardCacheManager.getTemplateCacheLogger(),
                extensionManager);
    }

    /**
     * This method was changed just to use StandardCacheManager methods and should function the same
     *
     * @return
     */
    @Override
    protected ICache<ExpressionCacheKey, Object> initializeExpressionCache() {
        final int maxSize = standardCacheManager.getExpressionCacheMaxSize();
        if (maxSize == 0) {
            return null;
        }
        return new StandardCache<ExpressionCacheKey, Object>(
                standardCacheManager.getExpressionCacheName(), standardCacheManager.getExpressionCacheUseSoftReferences(),
                standardCacheManager.getExpressionCacheInitialSize(), maxSize,
                standardCacheManager.getExpressionCacheValidityChecker(), standardCacheManager.getExpressionCacheLogger());
    }
}

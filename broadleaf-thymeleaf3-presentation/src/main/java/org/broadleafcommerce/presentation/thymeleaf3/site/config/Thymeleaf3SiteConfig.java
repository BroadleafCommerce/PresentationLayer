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
package org.broadleafcommerce.presentation.thymeleaf3.site.config;

import org.broadleafcommerce.common.logging.LifeCycleEvent;
import org.broadleafcommerce.common.logging.ModuleLifecycleLoggingBean;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleaf3MessageResolver;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleaf3TemplateEngine;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleafViewResolver;
import org.broadleafcommerce.presentation.thymeleaf3.cache.BroadleafThymeleaf3CacheInvalidationContext;
import org.broadleafcommerce.presentation.thymeleaf3.cache.BroadleafThymeleaf3CacheManager;
import org.broadleafcommerce.presentation.thymeleaf3.config.AbstractThymeleaf3DialectConfig;
import org.broadleafcommerce.presentation.thymeleaf3.config.AbstractThymeleaf3EngineConfig;
import org.broadleafcommerce.presentation.thymeleaf3.config.Thymeleaf3CommonConfig;
import org.broadleafcommerce.presentation.thymeleaf3.config.Thymeleaf3ModuleRegistration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.cache.ICacheManager;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.LinkedHashSet;
import java.util.Set;

@Configuration
public class Thymeleaf3SiteConfig extends Thymeleaf3CommonConfig {
    
    @Bean
    public ModuleLifecycleLoggingBean blThymeleaf3Lifecycle() {
        return new ModuleLifecycleLoggingBean(Thymeleaf3ModuleRegistration.MODULE_NAME, LifeCycleEvent.LOADING);
    }
    
    @Configuration
    static class Thymeleaf3SiteDialectConfig extends AbstractThymeleaf3DialectConfig {
        
        @Bean
        public Set<IDialect> blWebDialects() {
            Set<IDialect> dialects = new LinkedHashSet<>();
            dialects.add(thymeleafSpringStandardDialect());
            dialects.add(blDialect());
            return dialects;
        }
    }
    
    @Configuration
    static class Thymeleaf3SiteEngineConfig extends AbstractThymeleaf3EngineConfig {
        
        protected Set<IMessageResolver> messageResolvers;
        
        protected ICacheManager cacheManager;

        public Thymeleaf3SiteEngineConfig(Set<IMessageResolver> messageResolvers,
                                          ICacheManager cacheManager) {
            this.messageResolvers = messageResolvers;
            this.cacheManager = cacheManager;
        }
        
        @Bean
        @Primary
        public BroadleafThymeleaf3TemplateEngine blWebTemplateEngine() {
            BroadleafThymeleaf3TemplateEngine engine = new BroadleafThymeleaf3TemplateEngine();
            engine.setMessageResolvers(messageResolvers);
            Set<ITemplateResolver> allResolvers = new LinkedHashSet<>();
            allResolvers.addAll(iTemplateResolvers);
            allResolvers.addAll(blWebTemplateResolvers());
            engine.setTemplateResolvers(allResolvers);
            engine.setCacheManager(cacheManager);
            engine.setDialects(dialects);
            return engine;
        }
        
        @Configuration
        protected static class Thymeleaf3TemplateResolverConfig extends Thymeleaf3SiteTemplateConfig {}
    }
    
    @Configuration
    static class Thymeleaf3SiteViewConfig {
        
        protected ISpringTemplateEngine templateEngine;
        
        protected Environment environment;
        
        public Thymeleaf3SiteViewConfig(ISpringTemplateEngine templateEngine, Environment environment) {
            this.templateEngine = templateEngine;
            this.environment = environment;
        }
        
        @Bean(name = {"blThymeleafViewResolver", "thymeleafViewResolver"})
        public BroadleafThymeleafViewResolver blThymeleafViewResolver() {
            BroadleafThymeleafViewResolver view = new BroadleafThymeleafViewResolver();
            view.setTemplateEngine(templateEngine);
            view.setOrder(1);
            view.setCache(environment.getProperty("thymeleaf.view.resolver.cache", Boolean.class, true));
            view.setCharacterEncoding("UTF-8");
            return view;
        }
    }
    
    @Configuration
    static class Thymeleaf3CacheInvalidationConfig {
        
        protected ITemplateEngine templateEngine;
        
        public Thymeleaf3CacheInvalidationConfig(ITemplateEngine templateEngine) {
            this.templateEngine = templateEngine;
        }
        
        @Bean
        public BroadleafThymeleaf3CacheInvalidationContext blTemplateCacheInvalidationContext() {
            BroadleafThymeleaf3CacheInvalidationContext context = new BroadleafThymeleaf3CacheInvalidationContext();
            context.setTemplateEngine(templateEngine);
            return context;
        }
        
    }
    
    @Bean
    public IMessageResolver blMessageResolver() {
        BroadleafThymeleaf3MessageResolver resolver = new BroadleafThymeleaf3MessageResolver();
        resolver.setOrder(100);
        return resolver;
    }
    
    @Bean
    @ConditionalOnMissingBean(ICacheManager.class)
    public BroadleafThymeleaf3CacheManager blICacheManager() {
        return new BroadleafThymeleaf3CacheManager();
    }
    
}

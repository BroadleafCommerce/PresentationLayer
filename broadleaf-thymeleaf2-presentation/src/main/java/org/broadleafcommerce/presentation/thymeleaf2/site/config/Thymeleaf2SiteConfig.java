/*-
 * #%L
 * broadleaf-thymeleaf2-presentation
 * %%
 * Copyright (C) 2009 - 2022 Broadleaf Commerce
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
package org.broadleafcommerce.presentation.thymeleaf2.site.config;

import org.broadleafcommerce.common.logging.LifeCycleEvent;
import org.broadleafcommerce.common.logging.ModuleLifecycleLoggingBean;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafMessageResolver;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafStandardTemplateModeHandlers;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafViewResolver;
import org.broadleafcommerce.presentation.thymeleaf2.cache.BLCICacheManager;
import org.broadleafcommerce.presentation.thymeleaf2.cache.BroadleafThymeleaf2CacheInvalidationContext;
import org.broadleafcommerce.presentation.thymeleaf2.config.AbstractThymeleaf2DialectConfig;
import org.broadleafcommerce.presentation.thymeleaf2.config.AbstractThymeleaf2EngineConfig;
import org.broadleafcommerce.presentation.thymeleaf2.config.Thymeleaf2CommonConfig;
import org.broadleafcommerce.presentation.thymeleaf2.config.Thymeleaf2ModuleRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.ICacheManager;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Configuration
public class Thymeleaf2SiteConfig extends Thymeleaf2CommonConfig {
    
    @Bean
    public ModuleLifecycleLoggingBean blThymeleaf2Lifecycle() {
        return new ModuleLifecycleLoggingBean(Thymeleaf2ModuleRegistration.MODULE_NAME, LifeCycleEvent.LOADING);
    }

    @Configuration
    static class Thymeleaf2SiteDialectConfig extends AbstractThymeleaf2DialectConfig {
        
        @Bean
        public Set<IDialect> blWebDialects() {
            // In order for BLC's expression evaluator to be used this has to be a linked hashset
            // and the blDialect has to be last
            Set<IDialect> dialects = new LinkedHashSet<>();
            dialects.add(thymeleafSpringStandardDialect());
            dialects.add(blDialect());
            return dialects;
        }
    }
    
    @Configuration
    static class Thymeleaf2SiteEngineConfig extends AbstractThymeleaf2EngineConfig {
        
        protected Set<IMessageResolver> messageResolvers;
        
        protected ICacheManager cacheManager;

        public Thymeleaf2SiteEngineConfig(Set<IMessageResolver> messageResolvers,
                                          ICacheManager cacheManager) {
            this.messageResolvers = messageResolvers;
            this.cacheManager = cacheManager;
        }
        
        @Bean
        @Primary
        public SpringTemplateEngine blWebTemplateEngine() {
            SpringTemplateEngine engine = new SpringTemplateEngine();
            engine.setMessageResolvers(messageResolvers);
            Set<ITemplateResolver> allResolvers = new LinkedHashSet<>();
            allResolvers.addAll(iTemplateResolvers);
            allResolvers.addAll(blWebTemplateResolvers());
            engine.setTemplateResolvers(allResolvers);
            engine.setCacheManager(cacheManager);
            engine.setTemplateModeHandlers(blThymeleafStandardTemplateModeHandlers().getStandardTemplateModeHandlers());
            engine.setDialects(getDialects());
            return engine;
        }
        
        @Bean
        public BroadleafThymeleafStandardTemplateModeHandlers blThymeleafStandardTemplateModeHandlers() {
            return new BroadleafThymeleafStandardTemplateModeHandlers();
        }
        
        @Configuration
        protected static class Thymeleaf2TemplateResolverConfig extends Thymeleaf2SiteTemplateConfig {}
    }
    
    @Configuration
    static class Thymeleaf2SiteViewConfig {
        
        protected SpringTemplateEngine templateEngine;
        
        protected Environment environment;
        
        public Thymeleaf2SiteViewConfig(SpringTemplateEngine templateEngine, Environment environment) {
            this.templateEngine = templateEngine;
            this.environment = environment;
        }
        
        @Bean(name = {"blThymeleafViewResolver", "thymeleafViewResolver"})
        public BroadleafThymeleafViewResolver blThymeleafViewResolver() {
            BroadleafThymeleafViewResolver resolver = new BroadleafThymeleafViewResolver();
            resolver.setTemplateEngine(templateEngine);
            resolver.setOrder(1);
            resolver.setCache(environment.getProperty("thymeleaf.view.resolver.cache", Boolean.class, true));
            resolver.setCharacterEncoding("UTF-8");
            return resolver;
        }
    }
    
    @Configuration
    static class Thymeleaf2CacheInvalidationConfig {
        
        protected TemplateEngine templateEngine;
        
        public Thymeleaf2CacheInvalidationConfig(TemplateEngine templateEngine) {
            this.templateEngine = templateEngine;
        }
        
        @Bean
        public BroadleafThymeleaf2CacheInvalidationContext blTemplateCacheInvalidationContext() {
            BroadleafThymeleaf2CacheInvalidationContext context = new BroadleafThymeleaf2CacheInvalidationContext();
            context.setTemplateEngine(templateEngine);
            return context;
        }
    }
    
    @Bean
    public BroadleafThymeleafMessageResolver blMessageResolver() {
        BroadleafThymeleafMessageResolver resolver = new BroadleafThymeleafMessageResolver();
        resolver.setOrder(100);
        return resolver;
    }
    
    @Bean
    public Set<IMessageResolver> blWebMessageResolvers() {
        Set<IMessageResolver> resolvers = new HashSet<>();
        resolvers.add(blMessageResolver());
        resolvers.add(springMessageResolver());
        return resolvers;
    }
    
    @Bean
    public BLCICacheManager blICacheManager() {
        return new BLCICacheManager();
    }
    
}

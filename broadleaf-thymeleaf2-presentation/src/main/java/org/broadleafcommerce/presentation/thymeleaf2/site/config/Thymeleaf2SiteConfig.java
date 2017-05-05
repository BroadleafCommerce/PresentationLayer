/*
 * #%L
 * broadleaf-thymeleaf2-presentation
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
package org.broadleafcommerce.presentation.thymeleaf2.site.config;

import org.broadleafcommerce.common.logging.LifeCycleEvent;
import org.broadleafcommerce.common.logging.ModuleLifecycleLoggingBean;
import org.broadleafcommerce.presentation.resolver.BroadleafClasspathTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafDatabaseTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateMode;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafThemeAwareTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafMessageResolver;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafStandardTemplateModeHandlers;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafViewResolver;
import org.broadleafcommerce.presentation.thymeleaf2.cache.BLCICacheManager;
import org.broadleafcommerce.presentation.thymeleaf2.cache.BroadleafThymeleaf2CacheInvalidationContext;
import org.broadleafcommerce.presentation.thymeleaf2.config.Thymeleaf2CommonConfig;
import org.broadleafcommerce.presentation.thymeleaf2.config.Thymeleaf2ModuleRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Configuration
public class Thymeleaf2SiteConfig extends Thymeleaf2CommonConfig {
    
    @Bean
    public ModuleLifecycleLoggingBean blThymeleaf2Lifecycle() {
        return new ModuleLifecycleLoggingBean(Thymeleaf2ModuleRegistration.MODULE_NAME, LifeCycleEvent.LOADING);
    }

    @Bean
    public BroadleafThymeleafMessageResolver blMessageResolver() {
        BroadleafThymeleafMessageResolver resolver = new BroadleafThymeleafMessageResolver();
        resolver.setOrder(100);
        return resolver;
    }
    
    @Bean
    public SpringMessageResolver springMessageResolver() {
        SpringMessageResolver springMessageResolver = new SpringMessageResolver();
        springMessageResolver.setOrder(200);
        return springMessageResolver;
    }
    
    @Bean
    public Set<IMessageResolver> blWebMessageResolvers() {
        Set<IMessageResolver> resolvers = new HashSet<>();
        resolvers.add(blMessageResolver());
        resolvers.add(springMessageResolver());
        return resolvers;
    }
    
    @Bean
    public BroadleafThymeleafStandardTemplateModeHandlers blThymeleafStandardTemplateModeHandlers() {
        return new BroadleafThymeleafStandardTemplateModeHandlers();
    }
    
    @Bean
    public BLCICacheManager blICacheManager() {
        return new BLCICacheManager();
    }
    
    @Bean
    public Set<IDialect> blWebDialects() {
        // In order for BLC's expression evaluator to be used this has to be a linked hashset
        // and the blDialect has to be last
        Set<IDialect> dialects = new LinkedHashSet<>();
        dialects.add(thymeleafSpringStandardDialect());
        dialects.add(blDialect());
        return dialects;
    }
    
    @Bean
    public SpringTemplateEngine blWebTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setMessageResolvers(blWebMessageResolvers());
        engine.setTemplateResolvers(blWebTemplateResolvers());
        engine.setCacheManager(blICacheManager());
        engine.setTemplateModeHandlers(blThymeleafStandardTemplateModeHandlers().getStandardTemplateModeHandlers());
        engine.setDialects(blWebDialects());
        return engine;
    }
    
    @Bean
    public BroadleafThymeleaf2CacheInvalidationContext blTemplateCacheInvalidationContext() {
        BroadleafThymeleaf2CacheInvalidationContext context = new BroadleafThymeleaf2CacheInvalidationContext();
        context.setTemplateEngine(blWebTemplateEngine());
        return context;
    }
    
    @Bean(name = {"blThymeleafViewResolver", "thymeleafViewResolver"})
    public BroadleafThymeleafViewResolver blThymeleafViewResolver() {
        BroadleafThymeleafViewResolver resolver = new BroadleafThymeleafViewResolver();
        resolver.setTemplateEngine(blWebTemplateEngine());
        resolver.setOrder(1);
        resolver.setCache(environment.getProperty("thymeleaf.view.resolver.cache", Boolean.class, false));
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }
    
    @Configuration
    protected static class Thymeleaf2SiteTemplateResolverConfig {
        
        @Autowired
        protected Environment environment;
        
        protected final String isCacheableProperty = "cache.page.templates";
        protected final String cacheableTTLProperty = "cache.page.templates.ttl";
        protected final String themeFolderProperty = "theme.templates.folder";
        
        @Bean
        public BroadleafTemplateResolver blWebTemplateResolver() {
            BroadleafThemeAwareTemplateResolver resolver = new BroadleafThemeAwareTemplateResolver();
            resolver.setPrefix("/WEB-INF/");
            resolver.setTemplateFolder(environment.getProperty(themeFolderProperty, String.class, ""));
            resolver.setSuffix(".html");
            resolver.setTemplateMode(BroadleafTemplateMode.HTML5);
            resolver.setCharacterEncoding("UTF-8");
            resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
            resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
            resolver.setOrder(200);
            return resolver;
        }
        
        @Bean
        public BroadleafTemplateResolver blWebDatabaseTemplateResolver() {
            BroadleafDatabaseTemplateResolver resolver = new BroadleafDatabaseTemplateResolver();
            resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
            resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
            resolver.setCharacterEncoding("UTF-8");
            resolver.setOrder(100);
            return resolver;
        }
        
        @Bean
        public BroadleafTemplateResolver blWebClasspathTemplateResolver() {
            BroadleafClasspathTemplateResolver resolver = new BroadleafClasspathTemplateResolver();
            resolver.setPrefix("webTemplates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode(BroadleafTemplateMode.HTML5);
            resolver.setCharacterEncoding("UTF-8");
            resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
            resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
            resolver.setOrder(300);
            return resolver;
        }
    }
}
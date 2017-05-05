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
import org.broadleafcommerce.presentation.resolver.BroadleafClasspathTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafDatabaseTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafThemeAwareTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleaf3MessageResolver;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleaf3TemplateEngine;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleafViewResolver;
import org.broadleafcommerce.presentation.thymeleaf3.cache.BroadleafThymeleaf3CacheInvalidationContext;
import org.broadleafcommerce.presentation.thymeleaf3.cache.BroadleafThymeleaf3CacheManager;
import org.broadleafcommerce.presentation.thymeleaf3.config.Thymeleaf3CommonConfig;
import org.broadleafcommerce.presentation.thymeleaf3.config.Thymeleaf3ModuleRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.messageresolver.IMessageResolver;

import java.util.LinkedHashSet;
import java.util.Set;

@Configuration
public class Thymeleaf3SiteConfig extends Thymeleaf3CommonConfig {
    
    @Bean
    public ModuleLifecycleLoggingBean blThymeleaf3Lifecycle() {
        return new ModuleLifecycleLoggingBean(Thymeleaf3ModuleRegistration.MODULE_NAME, LifeCycleEvent.LOADING);
    }
    
    @Bean
    public IMessageResolver blMessageResolver() {
        BroadleafThymeleaf3MessageResolver resolver = new BroadleafThymeleaf3MessageResolver();
        resolver.setOrder(100);
        return resolver;
    }
    
    @Bean
    public BroadleafThymeleaf3CacheManager blICacheManager() {
        return new BroadleafThymeleaf3CacheManager();
    }
    
    @Bean
    public Set<IDialect> blWebDialects() {
        Set<IDialect> dialects = new LinkedHashSet<>();
        dialects.add(thymeleafSpringStandardDialect());
        dialects.add(blDialect());
        return dialects;
    }
    
    @Bean
    @Primary
    public BroadleafThymeleaf3TemplateEngine blWebTemplateEngine() {
        BroadleafThymeleaf3TemplateEngine engine = new BroadleafThymeleaf3TemplateEngine();
        Set<IMessageResolver> messageResolvers = new LinkedHashSet<>();
        messageResolvers.add(blMessageResolver());
        messageResolvers.add(springMessageResolver());
        engine.setMessageResolvers(messageResolvers);
        engine.setTemplateResolvers(blWebTemplateResolvers());
        engine.setCacheManager(blICacheManager());
        engine.setDialects(blWebDialects());
        return engine;
    }
    
    @Bean(name = {"blThymeleafViewResolver", "thymeleafViewResolver"})
    public BroadleafThymeleafViewResolver blThymeleafViewResolver() {
        BroadleafThymeleafViewResolver view = new BroadleafThymeleafViewResolver();
        view.setTemplateEngine(blWebTemplateEngine());
        view.setOrder(1);
        view.setCache(environment.getProperty("thymeleaf.view.resolver.cache", Boolean.class, false));
        view.setCharacterEncoding("UTF-8");
        return view;
    }
    
    @Bean
    public BroadleafThymeleaf3CacheInvalidationContext blTemplateCacheInvalidationContext() {
        BroadleafThymeleaf3CacheInvalidationContext context = new BroadleafThymeleaf3CacheInvalidationContext();
        context.setTemplateEngine(blWebTemplateEngine());
        return context;
    }
    
    @Configuration
    protected static class Thymeleaf3SiteTemplateResolverConfig {
        
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
            resolver.setCharacterEncoding("UTF-8");
            resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
            resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
            resolver.setOrder(300);
            return resolver;
        }
    }
    
}

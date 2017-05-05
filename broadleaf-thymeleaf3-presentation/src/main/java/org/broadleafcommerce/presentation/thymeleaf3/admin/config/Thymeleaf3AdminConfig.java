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
package org.broadleafcommerce.presentation.thymeleaf3.admin.config;

import org.broadleafcommerce.presentation.dialect.BroadleafDialectPrefix;
import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.resolver.BroadleafClasspathTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateMode;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafThemeAwareTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleaf3MessageResolver;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleaf3TemplateEngine;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleafViewResolver;
import org.broadleafcommerce.presentation.thymeleaf3.config.Thymeleaf3CommonConfig;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.BroadleafThymeleaf3AdminDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class Thymeleaf3AdminConfig extends Thymeleaf3CommonConfig {

    @Bean
    public BroadleafThymeleaf3AdminDialect blAdminDialect() {
        BroadleafThymeleaf3AdminDialect dialect = new BroadleafThymeleaf3AdminDialect();
        dialect.setProcessors(blAdminDialectProcessors());
        return dialect;
    }
    
    @Bean 
    public IMessageResolver blAdminMessageResolver() {
        BroadleafThymeleaf3MessageResolver resolver = new BroadleafThymeleaf3MessageResolver();
        resolver.setOrder(100);
        return resolver;
    }
    
    @Bean
    public Set<ITemplateResolver> blAdminWebTemplateResolvers() {
        return configUtil.getWebResolvers(templateResolvers);
    }
    
    @Bean
    @Override
    public Set<IProcessor> blDialectProcessors() {
        Collection<BroadleafProcessor> commonProcessors = new ArrayList<>();
        for (BroadleafProcessor processor : processors) {
            if (BroadleafDialectPrefix.BLC.equals(processor.getPrefix())) {
                commonProcessors.add(processor);
            }
        }
        return configUtil.getDialectProcessors(commonProcessors);
    }
    
    @Bean
    public Set<IProcessor> blAdminDialectProcessors() {
        Collection<BroadleafProcessor> adminProcessors = new ArrayList<>();
        for (BroadleafProcessor processor : processors) {
            if (BroadleafDialectPrefix.BLC_ADMIN.equals(processor.getPrefix())) {
                adminProcessors.add(processor);
            }
        }
        return configUtil.getDialectProcessors(adminProcessors);
    }
    
    @Bean
    public Set<IDialect> blAdminDialects() {
        Set<IDialect> dialects = new LinkedHashSet<>();
        dialects.add(thymeleafSpringStandardDialect());
        dialects.add(blAdminDialect());
        dialects.add(blDialect());
        return dialects;
    }
    
    @Bean
    @Primary
    public BroadleafThymeleaf3TemplateEngine blAdminWebTemplateEngine() {
        BroadleafThymeleaf3TemplateEngine engine = new BroadleafThymeleaf3TemplateEngine();
        Set<IMessageResolver> messages = new LinkedHashSet<>();
        messages.add(blAdminMessageResolver());
        messages.add(springMessageResolver());
        engine.setMessageResolvers(messages);
        engine.setTemplateResolvers(blAdminWebTemplateResolvers());
        engine.setDialects(blAdminDialects());
        return engine;
    }
    
    @Bean(name = {"blAdminThymeleafViewResolver", "thymeleafViewResolver"})
    public BroadleafThymeleafViewResolver blAdminThymeleafViewResolver() {
        BroadleafThymeleafViewResolver view = new BroadleafThymeleafViewResolver();
        view.setTemplateEngine(blAdminWebTemplateEngine());
        view.setOrder(1);
        view.setCache(environment.getProperty("thymeleaf.view.resolver.cache", Boolean.class, false));
        view.setCharacterEncoding("UTF-8");
        view.setFullPageLayout("layout/fullPageLayout");
        Map<String, String> layoutMap = new HashMap<>();
        layoutMap.put("login/", "layout/loginLayout");
        layoutMap.put("views/", "NONE");
        layoutMap.put("modules/modalContainer", "NONE");
        view.setLayoutMap(layoutMap);
        return view;
    }
    
    @Configuration
    protected static class Thymeleaf3AdminTemplateResolverConfig {
        
        @Autowired
        protected Environment environment;
        
        protected final String isCacheableProperty = "cache.page.templates";
        protected final String cacheableTTLProperty = "cache.page.templates.ttl";
        protected final String themeFolderProperty = "theme.templates.folder";
        
        @Bean
        public BroadleafTemplateResolver blAdminWebTemplateResolver() {
            BroadleafThemeAwareTemplateResolver resolver = new BroadleafThemeAwareTemplateResolver();
            resolver.setPrefix("/WEB-INF/templates/admin/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode(BroadleafTemplateMode.HTML);
            resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
            resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
            resolver.setCharacterEncoding("UTF-8");
            resolver.setOrder(200);
            return resolver;
        }
        
        @Bean
        public BroadleafTemplateResolver blAdminWebClasspathTemplateResolver() {
            BroadleafClasspathTemplateResolver resolver = new BroadleafClasspathTemplateResolver();
            resolver.setPrefix("open_admin_style/templates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode(BroadleafTemplateMode.HTML);
            resolver.setCharacterEncoding("UTF-8");
            resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
            resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
            resolver.setOrder(300);
            return resolver;
        }
    }

}
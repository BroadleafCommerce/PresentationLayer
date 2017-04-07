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
package org.broadleafcommerce.presentation.thymeleaf2.admin.config;

import org.broadleafcommerce.presentation.dialect.BroadleafDialectPrefix;
import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafMessageResolver;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafViewResolver;
import org.broadleafcommerce.presentation.thymeleaf2.config.Thymeleaf2CommonConfig;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.BLCAdminDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class Thymeleaf2AdminConfig extends Thymeleaf2CommonConfig {
    
    @Bean
    public BroadleafThymeleafMessageResolver blAdminMessageResolver() {
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
    public Set<IMessageResolver> blAdminMessageResolvers() {
        Set<IMessageResolver> resolvers = new HashSet<>();
        resolvers.add(blAdminMessageResolver());
        resolvers.add(springMessageResolver());
        return resolvers;
    }
    
    @Bean
    public BLCAdminDialect blAdminDialect() {
        BLCAdminDialect dialect = new BLCAdminDialect();
        dialect.setProcessors(blAdminDialectProcessors());
        return dialect;
    }
    
    @Bean 
    public Set<IDialect> blAdminDialects() {
        Set<IDialect> dialects = new HashSet<>();
        dialects.add(blAdminDialect());
        dialects.add(blDialect());
        dialects.add(thymeleafSpringStandardDialect());
        return dialects;
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
    public SpringTemplateEngine blAdminWebTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setMessageResolvers(blAdminMessageResolvers());
        engine.setTemplateResolvers(blAdminWebTemplateResolvers());
        engine.setDialects(blAdminDialects());
        return engine;
    }
    
    @Bean
    public BroadleafThymeleafViewResolver blAdminThymeleafViewResolver() {
        BroadleafThymeleafViewResolver resolver = new BroadleafThymeleafViewResolver();
        resolver.setTemplateEngine(blAdminWebTemplateEngine());
        resolver.setOrder(1);
        resolver.setCache(environment.getProperty("thymeleaf.view.resolver.cache", Boolean.class, false));
        resolver.setCharacterEncoding("UTF-8");
        resolver.setFullPageLayout("layout/fullPageLayout");
        resolver.setLayoutMap(getLayoutMap());
        return resolver;
    }
    
    protected Map<String, String> getLayoutMap() {
        Map<String, String> layoutMap = new HashMap<>();
        layoutMap.put("login/", "layout/loginLayout");
        layoutMap.put("views/", "NONE");
        layoutMap.put("modules/modalContainer", "NONE");
        return layoutMap;
    }
}

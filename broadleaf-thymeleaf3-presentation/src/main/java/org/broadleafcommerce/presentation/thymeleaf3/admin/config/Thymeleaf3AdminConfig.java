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
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleaf3MessageResolver;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleaf3TemplateEngine;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleafViewResolver;
import org.broadleafcommerce.presentation.thymeleaf3.config.AbstractThymeleaf3DialectConfig;
import org.broadleafcommerce.presentation.thymeleaf3.config.AbstractThymeleaf3EngineConfig;
import org.broadleafcommerce.presentation.thymeleaf3.config.Thymeleaf3CommonConfig;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.BroadleafThymeleaf3AdminDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class Thymeleaf3AdminConfig extends Thymeleaf3CommonConfig {

    
    @Configuration
    static class Thymeleaf3AdminDialectConfig extends AbstractThymeleaf3DialectConfig {

        @Bean
        public BroadleafThymeleaf3AdminDialect blAdminDialect() {
            BroadleafThymeleaf3AdminDialect dialect = new BroadleafThymeleaf3AdminDialect();
            Set<IProcessor> allProcessors = new LinkedHashSet<>();
            allProcessors.addAll(iProcessors);
            allProcessors.addAll(blAdminDialectProcessors());
            dialect.setProcessors(allProcessors);
            return dialect;
        }
        
        @Bean
        @Override
        public Set<IProcessor> blDialectProcessors() {
            Collection<BroadleafProcessor> commonProcessors = new ArrayList<>();
            for (BroadleafProcessor processor : blcProcessors) {
                if (BroadleafDialectPrefix.BLC.equals(processor.getPrefix())) {
                    commonProcessors.add(processor);
                }
            }
            return configUtil.getDialectProcessors(commonProcessors);
        }
        
        @Bean
        public Set<IProcessor> blAdminDialectProcessors() {
            Collection<BroadleafProcessor> adminProcessors = new ArrayList<>();
            for (BroadleafProcessor processor : blcProcessors) {
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
        
    }
    
    @Configuration
    static class Thymeleaf3AdminEngineConfig extends AbstractThymeleaf3EngineConfig {

        protected Set<IMessageResolver> messageResolvers;
        
        public Thymeleaf3AdminEngineConfig(Set<IMessageResolver> messageResolvers) {
            this.messageResolvers = messageResolvers;
        }
        
        @Bean
        public Set<ITemplateResolver> blAdminWebTemplateResolvers() {
            return configUtil.getWebResolvers(blcTemplateResolvers);
        }
        
        @Bean
        @Primary
        public BroadleafThymeleaf3TemplateEngine blAdminWebTemplateEngine() {
            BroadleafThymeleaf3TemplateEngine engine = new BroadleafThymeleaf3TemplateEngine();
            engine.setMessageResolvers(messageResolvers);
            Set<ITemplateResolver> allResolvers = new LinkedHashSet<>();
            allResolvers.addAll(iTemplateResolvers);
            allResolvers.addAll(blAdminWebTemplateResolvers());
            engine.setTemplateResolvers(allResolvers);
            engine.setDialects(dialects);
            return engine;
        }
        
        @Configuration
        protected static class Thymeleaf3AdminTemplateResolverConfig extends Thymeleaf3AdminTemplateConfig {}
    }
    
    @Configuration
    static class Thymeleaf3AdminViewConfig {
        
        protected ISpringTemplateEngine templateEngine;
        
        protected Environment environment;
        
        public Thymeleaf3AdminViewConfig(ISpringTemplateEngine templateEngine, Environment environment) {
            this.templateEngine = templateEngine;
            this.environment = environment;
        }
        
        @Bean(name = {"blAdminThymeleafViewResolver", "thymeleafViewResolver"})
        public BroadleafThymeleafViewResolver blAdminThymeleafViewResolver() {
            BroadleafThymeleafViewResolver view = new BroadleafThymeleafViewResolver();
            view.setTemplateEngine(templateEngine);
            view.setOrder(1);
            view.setCache(environment.getProperty("thymeleaf.view.resolver.cache", Boolean.class, true));
            view.setCharacterEncoding("UTF-8");
            view.setFullPageLayout("layout/fullPageLayout");
            Map<String, String> layoutMap = new HashMap<>();
            layoutMap.put("login/", "layout/loginLayout");
            layoutMap.put("views/", "NONE");
            layoutMap.put("modules/modalContainer", "NONE");
            view.setLayoutMap(layoutMap);
            return view;
        }
    }
    
    @Bean 
    public IMessageResolver blAdminMessageResolver() {
        BroadleafThymeleaf3MessageResolver resolver = new BroadleafThymeleaf3MessageResolver();
        resolver.setOrder(100);
        return resolver;
    }
    
}
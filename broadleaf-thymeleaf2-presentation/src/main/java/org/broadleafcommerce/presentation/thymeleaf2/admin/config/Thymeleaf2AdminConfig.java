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
package org.broadleafcommerce.presentation.thymeleaf2.admin.config;

import org.broadleafcommerce.presentation.dialect.BroadleafDialectPrefix;
import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafMessageResolver;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafViewResolver;
import org.broadleafcommerce.presentation.thymeleaf2.config.AbstractThymeleaf2DialectConfig;
import org.broadleafcommerce.presentation.thymeleaf2.config.AbstractThymeleaf2EngineConfig;
import org.broadleafcommerce.presentation.thymeleaf2.config.Thymeleaf2CommonConfig;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.BLCAdminDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class Thymeleaf2AdminConfig extends Thymeleaf2CommonConfig {
    
    @Configuration
    static class Thymeleaf2AdminDialectConfig extends AbstractThymeleaf2DialectConfig {

        @Bean
        public BLCAdminDialect blAdminDialect() {
            BLCAdminDialect dialect = new BLCAdminDialect();
            Set<IProcessor> allProcessors = new LinkedHashSet<>();
            allProcessors.addAll(iProcessors);
            allProcessors.addAll(blAdminDialectProcessors());
            dialect.setProcessors(allProcessors);
            return dialect;
        }
        
        @Bean 
        public Set<IDialect> blAdminDialects() {
            // In order for BLC's expression evaluator to be used this has to be a linked hashset
            // and the blDialect has to be last
            Set<IDialect> dialects = new LinkedHashSet<>();
            dialects.add(thymeleafSpringStandardDialect());
            dialects.add(blAdminDialect());
            dialects.add(blDialect());
            return dialects;
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
        
    }
    
    @Configuration
    static class Thymeleaf2AdminEngineConfig extends AbstractThymeleaf2EngineConfig {

        protected Set<IMessageResolver> messageResolvers;
        
        public Thymeleaf2AdminEngineConfig(Set<IMessageResolver> messageResolvers) {
            this.messageResolvers = messageResolvers;
        }
        
        @Bean
        public Set<ITemplateResolver> blAdminWebTemplateResolvers() {
            return configUtil.getWebResolvers(blcTemplateResolvers);
        }
        
        @Bean
        @Primary
        public SpringTemplateEngine blAdminWebTemplateEngine() {
            SpringTemplateEngine engine = new SpringTemplateEngine();
            engine.setMessageResolvers(messageResolvers);
            engine.setTemplateResolvers(blAdminWebTemplateResolvers());
            Set<ITemplateResolver> allResolvers = new LinkedHashSet<>();
            allResolvers.addAll(iTemplateResolvers);
            allResolvers.addAll(blAdminWebTemplateResolvers());
            engine.setTemplateResolvers(allResolvers);
            engine.setDialects(getDialects());
            return engine;
        }
        
        @Configuration
        protected static class Thymeleaf2AdminTemplateResolverConfig extends Thymeleaf2AdminTemplateConfig {}
    }
    
    @Configuration
    static class Thymeleaf2AdminViewConfig {
        
        protected SpringTemplateEngine templateEngine;
        
        protected Environment environment;
        
        public Thymeleaf2AdminViewConfig(SpringTemplateEngine templateEngine, Environment environment) {
            this.templateEngine = templateEngine;
            this.environment = environment;
        }
        
        @Bean(name = {"blAdminThymeleafViewResolver", "thymeleafViewResolver"})
        public BroadleafThymeleafViewResolver blAdminThymeleafViewResolver() {
            BroadleafThymeleafViewResolver resolver = new BroadleafThymeleafViewResolver();
            resolver.setTemplateEngine(templateEngine);
            resolver.setOrder(1);
            resolver.setCache(environment.getProperty("thymeleaf.view.resolver.cache", Boolean.class, true));
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
    
    @Bean
    public BroadleafThymeleafMessageResolver blAdminMessageResolver() {
        BroadleafThymeleafMessageResolver resolver = new BroadleafThymeleafMessageResolver();
        resolver.setOrder(100);
        return resolver;
    }
    
    @Bean
    public Set<IMessageResolver> blAdminMessageResolvers() {
        Set<IMessageResolver> resolvers = new HashSet<>();
        resolvers.add(blAdminMessageResolver());
        resolvers.add(springMessageResolver());
        return resolvers;
    }
}

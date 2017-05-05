/*-
 * #%L
 * broadleaf-thymeleaf3-presentation
 * %%
 * Copyright (C) 2009 - 2017 Broadleaf Commerce
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
package org.broadleafcommerce.presentation.thymeleaf3.config;

import org.broadleafcommerce.presentation.cache.service.SimpleCacheKeyResolver;
import org.broadleafcommerce.presentation.cache.service.TemplateCacheKeyResolverService;
import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.resolver.BroadleafClasspathTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateMode;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.BroadleafThymeleaf3Dialect;
import org.broadleafcommerce.presentation.thymeleaf3.expression.BroadleafVariableExpressionObjectFactory;
import org.broadleafcommerce.presentation.thymeleaf3.processor.ArbitraryHtmlInsertionProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.processor.BroadleafThymeleaf3CacheProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

@Configuration
public class Thymeleaf3CommonConfig {

    @Autowired
    protected Environment environment;
    
    @Autowired
    protected List<BroadleafProcessor> processors;
    
    @Autowired
    protected List<BroadleafTemplateResolver> templateResolvers;
    
    @Resource
    protected Thymeleaf3ConfigUtils configUtil;
    
    @Bean
    public SpringStandardDialect thymeleafSpringStandardDialect() {
        return new SpringStandardDialect();
    }
    
    @Bean
    public SpringMessageResolver springMessageResolver() {
        SpringMessageResolver resolver = new SpringMessageResolver();
        resolver.setOrder(200);
        return resolver;
    }
    
    @Bean
    public Set<IDialect> blEmailDialects() {
        Set<IDialect> dialects = new LinkedHashSet<>();
        dialects.add(thymeleafSpringStandardDialect());
        dialects.add(blDialect());
        return dialects;
    }
    
    @Bean
    public BroadleafThymeleaf3Dialect blDialect() {
        BroadleafThymeleaf3Dialect dialect = new BroadleafThymeleaf3Dialect();
        Set<IProcessor> iProcessors = new HashSet<>();
        iProcessors.addAll(blDialectProcessors());
        iProcessors.add(blArbitraryHtmlInjectionProcessor());
        iProcessors.add(blCacheProcessor());
        dialect.setProcessors(iProcessors);
        return dialect;
    }
    
    @Bean
    public IProcessor blArbitraryHtmlInjectionProcessor() {
        return new ArbitraryHtmlInsertionProcessor();
    }
    
    @Bean
    public IProcessor blCacheProcessor() {
        return new BroadleafThymeleaf3CacheProcessor();
    }
    
    @Bean
    public Set<IProcessor> blDialectProcessors() {
        return configUtil.getDialectProcessors(processors);
    }
    
    @Bean
    public Set<ITemplateResolver> blEmailTemplateResolvers() {
        return configUtil.getEmailResolvers(templateResolvers);
    }
    
    @Bean
    @Primary
    public Set<ITemplateResolver> blWebTemplateResolvers() {
        return configUtil.getWebResolvers(templateResolvers);
    }
    
    @Bean
    public SpringTemplateEngine blEmailTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolvers(blEmailTemplateResolvers());
        engine.setDialects(blEmailDialects());
        return engine;
    }
    
    @Bean
    @ConditionalOnMissingBean(TemplateCacheKeyResolverService.class)
    public SimpleCacheKeyResolver blTemplateCacheKeyResolver() {
        return new SimpleCacheKeyResolver();
    }
    
    @Bean
    @ConditionalOnMissingBean(IExpressionObjectFactory.class)
    public BroadleafVariableExpressionObjectFactory blVariableExpressionObjectFactory() {
        return new BroadleafVariableExpressionObjectFactory();
    }
    
    @Configuration
    protected static class Thymeleaf3CommonTemplateResolverConfig {
        
        @Autowired
        protected Environment environment;
        
        protected final String isCacheableProperty = "cache.page.templates";
        protected final String cacheableTTLProperty = "cache.page.templates.ttl";
        
        @Bean(name = {"blWebCommonClasspathTemplateResolver", "defaultTemplateResolver"})
        public BroadleafTemplateResolver blWebCommonClasspathTemplateResolver() {
            BroadleafClasspathTemplateResolver resolver = new BroadleafClasspathTemplateResolver();
            resolver.setPrefix("/common-style/templates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode(BroadleafTemplateMode.HTML);
            resolver.setCharacterEncoding("UTF-8");
            resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
            resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
            resolver.setOrder(500);
            return resolver;
        }
        
        @Bean
        public BroadleafTemplateResolver blEmailClasspathTemplateResolver() {
            BroadleafClasspathTemplateResolver resolver = new BroadleafClasspathTemplateResolver();
            resolver.setPrefix("emailTemplates/");
            resolver.setSuffix(".html");
            resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
            resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
            resolver.setCharacterEncoding("UTF-8");
            resolver.setEmailResolver(true);
            return resolver;
        }
    }
    
}

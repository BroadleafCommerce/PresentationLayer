/*-
 * #%L
 * broadleaf-thymeleaf2-presentation
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
package org.broadleafcommerce.presentation.thymeleaf2.config;

import org.broadleafcommerce.presentation.cache.service.SimpleCacheKeyResolver;
import org.broadleafcommerce.presentation.cache.service.TemplateCacheKeyResolverService;
import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.BLCDialect;
import org.broadleafcommerce.presentation.thymeleaf2.expression.BroadleafVariableExpressionEvaluator;
import org.broadleafcommerce.presentation.thymeleaf2.processor.ArbitraryHtmlInsertionProcessor;
import org.broadleafcommerce.presentation.thymeleaf2.processor.BroadleafCacheProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;
import org.thymeleaf.spring4.expression.SpelVariableExpressionEvaluator;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

@Configuration
public class Thymeleaf2CommonConfig {

    @Autowired
    protected Environment environment;
    
    @Autowired
    protected ApplicationContext context;
    
    @Resource
    protected Thymeleaf2ConfigUtils configUtil;
    
    @Autowired
    protected List<BroadleafProcessor> processors;
    
    @Autowired
    protected List<BroadleafTemplateResolver> templateResolvers;
    
    protected final String isCacheableProperty = "cache.page.templates";
    protected final String cacheableTTLProperty = "cache.page.templates.ttl";
    
    @Bean
    @Primary
    public Set<ITemplateResolver> blWebTemplateResolvers() {
        return configUtil.getWebResolvers(templateResolvers);
    }
    
    @Bean
    public Set<ITemplateResolver> blEmailTemplateResolvers() {
        return configUtil.getEmailResolvers(templateResolvers);
    }
    
    @Bean
    @ConditionalOnMissingBean(SpelVariableExpressionEvaluator.class)
    public BroadleafVariableExpressionEvaluator blVariableExpressionEvaluator() {
        return new BroadleafVariableExpressionEvaluator();
    }
    
    @Bean
    @ConditionalOnMissingBean(TemplateCacheKeyResolverService.class)
    public SimpleCacheKeyResolver blTemplateCacheKeyResolver() {
        return new SimpleCacheKeyResolver();
    }
    
    @Bean
    public Set<IDialect> blEmailDialects() {
        // In order for BLC's expression evaluator to be used this has to be a linked hashset
        // and the blDialect has to be last
        Set<IDialect> emailDialects = new LinkedHashSet<>();
        emailDialects.add(thymeleafSpringStandardDialect());
        emailDialects.add(blDialect());
        return emailDialects;
    }
    
    @Bean
    public SpringStandardDialect thymeleafSpringStandardDialect() {
        return new SpringStandardDialect();
    }
    
    @Bean
    public IProcessor blArbitraryHtmlInjectionProcessor() {
        return new ArbitraryHtmlInsertionProcessor();
    }
    
    @Bean
    public IProcessor blCacheProcessor() {
        return new BroadleafCacheProcessor();
    }
    
    @Bean
    public Set<IProcessor> blDialectProcessors() {
        return configUtil.getDialectProcessors(processors);
    }
    
    @Bean
    public BLCDialect blDialect() {
        BLCDialect dialect = new BLCDialect();
        Set<IProcessor> iProcessors = new HashSet<>();
        iProcessors.addAll(blDialectProcessors());
        iProcessors.add(blArbitraryHtmlInjectionProcessor());
        iProcessors.add(blCacheProcessor());
        dialect.setProcessors(iProcessors);
        return dialect;
    }
    
    @Bean
    public SpringTemplateEngine blEmailTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolvers(blEmailTemplateResolvers());
        engine.setDialects(blEmailDialects());
        return engine;
    }
    
}

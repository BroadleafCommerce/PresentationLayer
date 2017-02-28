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

import org.broadleafcommerce.common.config.FrameworkCommonPropertySource;
import org.broadleafcommerce.common.logging.LifeCycleEvent;
import org.broadleafcommerce.common.logging.ModuleLifecycleLoggingBean;
import org.broadleafcommerce.presentation.cache.service.SimpleCacheKeyResolver;
import org.broadleafcommerce.presentation.cache.service.TemplateCacheKeyResolverService;
import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf2.config.Thymeleaf2ConfigUtils;
import org.broadleafcommerce.presentation.thymeleaf2.config.Thymeleaf2ModuleRegistration;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.BLCDialect;
import org.broadleafcommerce.presentation.thymeleaf2.expression.BroadleafVariableExpressionEvaluator;
import org.broadleafcommerce.presentation.thymeleaf2.processor.ArbitraryHtmlInsertionProcessor;
import org.broadleafcommerce.presentation.thymeleaf2.processor.BroadleafCacheProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring4.expression.SpelVariableExpressionEvaluator;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

@Configuration
public class Thymeleaf2SiteConfig {
    
    @Resource
    protected ApplicationContext applicationContext;
    
    @Resource
    protected Thymeleaf2ConfigUtils configUtil;
    
    @Autowired
    protected List<BroadleafTemplateResolver> templateResolvers;
    
    @Autowired
    protected List<BroadleafProcessor> processors;
    
    @Bean
    public static FrameworkCommonPropertySource blThymeleafProperties() {
        return new FrameworkCommonPropertySource("config/bc/thymeleaf/");
    }
    
    @Bean
    public ModuleLifecycleLoggingBean blThymeleaf2Lifecycle() {
        return new ModuleLifecycleLoggingBean(Thymeleaf2ModuleRegistration.MODULE_NAME, LifeCycleEvent.LOADING);
    }
    
    @Bean
    public Set<ITemplateResolver> blWebTemplateResolvers() {
        return configUtil.getWebResolvers(templateResolvers);
    }
    
    @Bean
    public Set<ITemplateResolver> blEmailTemplateResolvers() {
        return configUtil.getEmailResolvers(templateResolvers);
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
    public BLCDialect blDialect() {
        BLCDialect dialect = new BLCDialect();
        Set<IProcessor> iProcessors = new HashSet<>();
        iProcessors.addAll(blDialectProcessors());
        iProcessors.add(blArbitraryHtmlInjectionProcessor());
        iProcessors.add(blCacheProcessor());
        dialect.setProcessors(iProcessors);
        return dialect;
    }
    
}

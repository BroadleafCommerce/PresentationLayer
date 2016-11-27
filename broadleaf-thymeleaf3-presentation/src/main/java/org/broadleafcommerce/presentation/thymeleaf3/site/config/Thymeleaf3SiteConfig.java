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

import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf3.config.Thymeleaf3ConfigUtils;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.BroadleafThymeleaf3Dialect;
import org.broadleafcommerce.presentation.thymeleaf3.processor.ArbitraryHtmlInsertionProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.processor.BroadleafThymeleaf3CacheProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

@Configuration
public class Thymeleaf3SiteConfig {
    
    @Resource
    protected ApplicationContext applicationContext;
    
    @Bean
    public BroadleafThymeleaf3Dialect blDialect() {
        BroadleafThymeleaf3Dialect dialect = new BroadleafThymeleaf3Dialect();
        Set<IProcessor> iProcessors = blDialectProcessors();
        iProcessors.add(blCacheProcessor());
        iProcessors.add(blHtmlInsertionProcessor());
        dialect.setProcessors(iProcessors);
        return dialect;
    }
    
    @Bean
    public Set<ITemplateResolver> blWebTemplateResolvers() {
        Collection<BroadleafTemplateResolver> resolvers = applicationContext.getBeansOfType(BroadleafTemplateResolver.class).values();
        return Thymeleaf3ConfigUtils.getWebResovlers(resolvers, applicationContext);
    }
    
    @Bean 
    public Set<ITemplateResolver> blEmailTemplateResolvers() {
        Collection<BroadleafTemplateResolver> resolvers = applicationContext.getBeansOfType(BroadleafTemplateResolver.class).values();
        return Thymeleaf3ConfigUtils.getEmailResolvers(resolvers, applicationContext);
    }
    
    @Bean
    public IProcessor blCacheProcessor() {
        return new BroadleafThymeleaf3CacheProcessor();
    }
    
    @Bean
    public IProcessor blHtmlInsertionProcessor() {
        return new ArbitraryHtmlInsertionProcessor();
    }
    
    @Bean
    public Set<IProcessor> blDialectProcessors() {
        Collection<BroadleafProcessor> blcProcessors = applicationContext.getBeansOfType(BroadleafProcessor.class).values();
        return Thymeleaf3ConfigUtils.getDialectProcessors(blcProcessors);
    }

}

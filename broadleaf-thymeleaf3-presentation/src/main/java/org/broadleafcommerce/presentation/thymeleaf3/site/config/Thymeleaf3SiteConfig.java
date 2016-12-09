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

import org.broadleafcommerce.common.config.FrameworkCommonPropertySource;
import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf3.config.Thymeleaf3ConfigUtils;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.BroadleafThymeleaf3Dialect;
import org.broadleafcommerce.presentation.thymeleaf3.processor.ArbitraryHtmlInsertionProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.processor.BroadleafThymeleaf3CacheProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

@Configuration
public class Thymeleaf3SiteConfig {
    
    @Resource
    protected ApplicationContext applicationContext;
    
    @Resource
    protected Thymeleaf3ConfigUtils configUtil;
    
    @Autowired
    protected List<BroadleafTemplateResolver> templateResolvers;
    
    @Autowired
    protected List<BroadleafProcessor> processors;
    
    @Bean
    public static FrameworkCommonPropertySource blThymeleafProperties() {
        return new FrameworkCommonPropertySource("config/bc/thymeleaf/");
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
        return new BroadleafThymeleaf3CacheProcessor();
    }
    
    @Bean
    public Set<IProcessor> blDialectProcessors() {
        return configUtil.getDialectProcessors(processors);
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
    
}

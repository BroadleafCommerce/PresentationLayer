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
package org.broadleafcommerce.admin.config;

import org.broadleafcommerce.common.config.Thymeleaf2ConfigUtils;
import org.broadleafcommerce.common.web.dialect.BLCAdminDialect;
import org.broadleafcommerce.common.web.dialect.BLCDialect;
import org.broadleafcommerce.common.web.dialect.BroadleafDialectPrefix;
import org.broadleafcommerce.common.web.dialect.BroadleafProcessor;
import org.broadleafcommerce.common.web.resolver.BroadleafTemplateResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

@Configuration
public class Thymeleaf2AdminConfig {

    @Resource
    protected ApplicationContext applicationContext;
    
    @Bean
    public BLCAdminDialect blAdminDialect() {
        BLCAdminDialect dialect = new BLCAdminDialect();
        dialect.setProcessors(blAdminDialectProcessors());
        return dialect;
    }
    
    @Bean 
    public BLCDialect blDialect() {
        BLCDialect dialect = new BLCDialect();
        dialect.setProcessors(blDialectProcessors());
        return dialect;
    }
    
    @Bean
    public Set<ITemplateResolver> blAdminWebTemplateResolvers() {
        Collection<BroadleafTemplateResolver> resolvers = applicationContext.getBeansOfType(BroadleafTemplateResolver.class).values();
        return Thymeleaf2ConfigUtils.getWebResovlers(resolvers, applicationContext);
    }
    
    @Bean 
    public Set<ITemplateResolver> blEmailTemplateResolvers() {
        Collection<BroadleafTemplateResolver> resolvers = applicationContext.getBeansOfType(BroadleafTemplateResolver.class).values();
        return Thymeleaf2ConfigUtils.getEmailResolvers(resolvers, applicationContext);
    }
    
    @Bean
    public Set<IProcessor> blDialectProcessors() {
        Collection<BroadleafProcessor> blcProcessors = applicationContext.getBeansOfType(BroadleafProcessor.class).values();
        Collection<BroadleafProcessor> commonProcessors = new ArrayList<>();
        for (BroadleafProcessor processor : blcProcessors) {
            if (BroadleafDialectPrefix.BLC.equals(processor.getPrefix())) {
                commonProcessors.add(processor);
            }
        }
        return Thymeleaf2ConfigUtils.getDialectProcessors(commonProcessors);
    }
    
    @Bean
    public Set<IProcessor> blAdminDialectProcessors() {
        Collection<BroadleafProcessor> blcProcessors = applicationContext.getBeansOfType(BroadleafProcessor.class).values();
        Collection<BroadleafProcessor> adminProcessors = new ArrayList<>();
        for (BroadleafProcessor processor : blcProcessors) {
            if (BroadleafDialectPrefix.BLC_ADMIN.equals(processor.getPrefix())) {
                adminProcessors.add(processor);
            }
        }
        return Thymeleaf2ConfigUtils.getDialectProcessors(adminProcessors);
    }
}

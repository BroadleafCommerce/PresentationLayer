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
package org.broadleafcommerce.site.config;

import org.broadleafcommerce.common.config.Thymeleaf3ConfigUtils;
import org.broadleafcommerce.common.web.dialect.BLCDialect;
import org.broadleafcommerce.common.web.dialect.BroadleafProcessor;
import org.broadleafcommerce.common.web.resolver.BroadleafThymeleafTemplateResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Resource;

@Configuration
public class Thymeleaf3SiteConfig {
    
    @Resource
    protected ApplicationContext applicationContext;
    
    @Bean
    public BLCDialect blDialect() {
        BLCDialect dialect = new BLCDialect();
        Collection<BroadleafProcessor> blcProcessors = applicationContext.getBeansOfType(BroadleafProcessor.class).values();
        dialect.setProcessors(Thymeleaf3ConfigUtils.getDialectProcessors(blcProcessors));
        return dialect;
    }
    
    @Bean
    public Set<ITemplateResolver> blWebTemplateResolvers() {
        Collection<BroadleafThymeleafTemplateResolver> resolvers = applicationContext.getBeansOfType(BroadleafThymeleafTemplateResolver.class).values();
        return Thymeleaf3ConfigUtils.getWebResovlers(resolvers, applicationContext);
    }
    
    @Bean 
    public Set<ITemplateResolver> blEmailTemplateResolvers() {
        Collection<BroadleafThymeleafTemplateResolver> resolvers = applicationContext.getBeansOfType(BroadleafThymeleafTemplateResolver.class).values();
        return Thymeleaf3ConfigUtils.getEmailResolvers(resolvers, applicationContext);
    }

}

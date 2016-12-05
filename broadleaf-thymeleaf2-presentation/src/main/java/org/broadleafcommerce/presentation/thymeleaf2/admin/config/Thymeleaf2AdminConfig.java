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
import org.broadleafcommerce.presentation.thymeleaf2.config.Thymeleaf2ConfigUtils;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.BLCAdminDialect;
import org.broadleafcommerce.presentation.thymeleaf2.site.config.Thymeleaf2SiteConfig;
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
public class Thymeleaf2AdminConfig extends Thymeleaf2SiteConfig {

    @Resource
    protected ApplicationContext applicationContext;
    
    @Resource
    protected Thymeleaf2ConfigUtils configUtil;
    
    @Bean
    public BLCAdminDialect blAdminDialect() {
        BLCAdminDialect dialect = new BLCAdminDialect();
        dialect.setProcessors(blAdminDialectProcessors());
        return dialect;
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
}

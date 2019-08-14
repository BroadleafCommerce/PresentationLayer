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
package org.broadleafcommerce.presentation.thymeleaf3.site.config;

import org.broadleafcommerce.presentation.resolver.BroadleafClasspathTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafDatabaseTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafThemeAwareTemplateResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class Thymeleaf3SiteTemplateConfig {
    
    @Autowired
    protected Environment environment;
    
    protected final String isCacheableProperty = "cache.page.templates";
    protected final String cacheableTTLProperty = "cache.page.templates.ttl";
    protected final String themeFolderProperty = "theme.templates.folder";
    
    @Bean
    public BroadleafTemplateResolver blWebTemplateResolver() {
        BroadleafThemeAwareTemplateResolver resolver = new BroadleafThemeAwareTemplateResolver();
        resolver.setPrefix("/WEB-INF/");
        resolver.setTemplateFolder(environment.getProperty(themeFolderProperty, String.class, "templates/"));
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
        resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
        resolver.setOrder(200);
        return resolver;
    }
    
    @Bean
    public BroadleafTemplateResolver blWebDatabaseTemplateResolver() {
        BroadleafDatabaseTemplateResolver resolver = new BroadleafDatabaseTemplateResolver();
        resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
        resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(100);
        return resolver;
    }
    
    @Bean
    public BroadleafTemplateResolver blWebClasspathTemplateResolver() {
        BroadleafClasspathTemplateResolver resolver = new BroadleafClasspathTemplateResolver();
        resolver.setPrefix("webTemplates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
        resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
        resolver.setOrder(300);
        return resolver;
    }
    
    @Bean
    public BroadleafTemplateResolver springDefaultTemplateResolver() {
        BroadleafThemeAwareTemplateResolver resolver = new BroadleafThemeAwareTemplateResolver();
        resolver.setPrefix("classpath:/");
        resolver.setTemplateFolder(environment.getProperty(themeFolderProperty, String.class, "templates/"));
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
        resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
        resolver.setOrder(400);
        return resolver;
    }
}

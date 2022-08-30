package org.broadleafcommerce.presentation.thymeleaf3.admin.config;

import org.broadleafcommerce.presentation.resolver.BroadleafClasspathTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateMode;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafThemeAwareTemplateResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class Thymeleaf3AdminTemplateConfig {

    @Autowired
    protected Environment environment;
    
    protected final String isCacheableProperty = "cache.page.templates";
    protected final String cacheableTTLProperty = "cache.page.templates.ttl";
    
    @Bean
    public BroadleafTemplateResolver blAdminWebTemplateResolver() {
        BroadleafThemeAwareTemplateResolver resolver = new BroadleafThemeAwareTemplateResolver();
        resolver.setPrefix("/WEB-INF/templates/admin/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(BroadleafTemplateMode.HTML);
        resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
        resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(200);
        return resolver;
    }
    
    @Bean
    public BroadleafTemplateResolver blAdminWebClasspathTemplateResolver() {
        BroadleafClasspathTemplateResolver resolver = new BroadleafClasspathTemplateResolver();
        resolver.setPrefix("open_admin_style/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(BroadleafTemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
        resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
        resolver.setOrder(300);
        return resolver;
    }
}

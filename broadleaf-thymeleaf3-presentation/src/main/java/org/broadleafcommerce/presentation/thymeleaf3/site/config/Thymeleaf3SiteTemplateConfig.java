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

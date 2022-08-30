package org.broadleafcommerce.presentation.thymeleaf3.config;

import org.broadleafcommerce.presentation.resolver.BroadleafClasspathTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateMode;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class Thymeleaf3CommonTemplateConfig {

    @Autowired
    protected Environment environment;
    
    protected final String isCacheableProperty = "cache.page.templates";
    protected final String cacheableTTLProperty = "cache.page.templates.ttl";
    
    @Bean(name = {"blWebCommonClasspathTemplateResolver", "defaultTemplateResolver"})
    public BroadleafTemplateResolver blWebCommonClasspathTemplateResolver() {
        BroadleafClasspathTemplateResolver resolver = new BroadleafClasspathTemplateResolver();
        resolver.setPrefix("/common_style/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(BroadleafTemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
        resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
        resolver.setOrder(500);
        return resolver;
    }
    
    @Bean
    public BroadleafTemplateResolver blEmailClasspathTemplateResolver() {
        BroadleafClasspathTemplateResolver resolver = new BroadleafClasspathTemplateResolver();
        resolver.setPrefix("emailTemplates/");
        resolver.setSuffix(".html");
        resolver.setCacheable(environment.getProperty(isCacheableProperty, Boolean.class, false));
        resolver.setCacheTTLMs(environment.getProperty(cacheableTTLProperty, Long.class, 0L));
        resolver.setCharacterEncoding("UTF-8");
        resolver.setEmailResolver(true);
        return resolver;
    }
}

package org.broadleafcommerce.presentation.thymeleaf3.config;

import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractThymeleaf3EngineConfig {

    @Autowired(required = false)
    protected Set<ITemplateResolver> iTemplateResolvers = new LinkedHashSet<>();
    
    @Autowired(required = false)
    protected Set<BroadleafTemplateResolver> blcTemplateResolvers = new LinkedHashSet<>();
    
    @Autowired
    protected Set<IDialect> dialects;
    
    @Autowired
    protected Thymeleaf3ConfigUtils configUtil;
    
    @Bean
    public SpringTemplateEngine blEmailTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        Set<ITemplateResolver> allResolvers = new LinkedHashSet<>();
        allResolvers.addAll(iTemplateResolvers);
        allResolvers.addAll(blEmailTemplateResolvers());
        engine.setTemplateResolvers(allResolvers);
        engine.setDialects(dialects);
        return engine;
    }
    
    @Bean
    public Set<ITemplateResolver> blEmailTemplateResolvers() {
        return configUtil.getEmailResolvers(blcTemplateResolvers);
    }
    
    @Bean
    @Primary
    public Set<ITemplateResolver> blWebTemplateResolvers() {
        return configUtil.getWebResolvers(blcTemplateResolvers);
    }
    
    @Configuration
    protected static class Thymeleaf3CommonTemplateResolverConfig extends Thymeleaf3CommonTemplateConfig {}
}

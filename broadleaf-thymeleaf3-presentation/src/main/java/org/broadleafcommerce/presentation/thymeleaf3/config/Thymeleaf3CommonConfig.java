package org.broadleafcommerce.presentation.thymeleaf3.config;

import org.broadleafcommerce.presentation.cache.service.SimpleCacheKeyResolver;
import org.broadleafcommerce.presentation.cache.service.TemplateCacheKeyResolverService;
import org.broadleafcommerce.presentation.thymeleaf3.expression.BroadleafVariableExpressionObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.spring5.messageresolver.SpringMessageResolver;

@Configuration
public class Thymeleaf3CommonConfig {

    @Bean
    public SpringMessageResolver springMessageResolver() {
        SpringMessageResolver resolver = new SpringMessageResolver();
        resolver.setOrder(200);
        return resolver;
    }
    
    @Bean
    @ConditionalOnMissingBean(TemplateCacheKeyResolverService.class)
    public SimpleCacheKeyResolver blTemplateCacheKeyResolver() {
        return new SimpleCacheKeyResolver();
    }
    
    @Bean
    @ConditionalOnMissingBean(IExpressionObjectFactory.class)
    public BroadleafVariableExpressionObjectFactory blVariableExpressionObjectFactory() {
        return new BroadleafVariableExpressionObjectFactory();
    }
    
}

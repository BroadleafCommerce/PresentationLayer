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

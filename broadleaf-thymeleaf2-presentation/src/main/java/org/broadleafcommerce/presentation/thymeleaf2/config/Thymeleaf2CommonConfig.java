/*-
 * #%L
 * broadleaf-thymeleaf2-presentation
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
package org.broadleafcommerce.presentation.thymeleaf2.config;

import org.broadleafcommerce.presentation.cache.service.SimpleCacheKeyResolver;
import org.broadleafcommerce.presentation.cache.service.TemplateCacheKeyResolverService;
import org.broadleafcommerce.presentation.thymeleaf2.expression.BroadleafVariableExpressionEvaluator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.expression.SpelVariableExpressionEvaluator;
import org.thymeleaf.spring4.messageresolver.SpringMessageResolver;

@Configuration
public class Thymeleaf2CommonConfig {

    @Bean
    @ConditionalOnMissingBean(SpelVariableExpressionEvaluator.class)
    public BroadleafVariableExpressionEvaluator blVariableExpressionEvaluator() {
        return new BroadleafVariableExpressionEvaluator();
    }
    
    @Bean
    @ConditionalOnMissingBean(TemplateCacheKeyResolverService.class)
    public SimpleCacheKeyResolver blTemplateCacheKeyResolver() {
        return new SimpleCacheKeyResolver();
    }
    
    @Bean
    public SpringMessageResolver springMessageResolver() {
        SpringMessageResolver springMessageResolver = new SpringMessageResolver();
        springMessageResolver.setOrder(200);
        return springMessageResolver;
    }
    
}

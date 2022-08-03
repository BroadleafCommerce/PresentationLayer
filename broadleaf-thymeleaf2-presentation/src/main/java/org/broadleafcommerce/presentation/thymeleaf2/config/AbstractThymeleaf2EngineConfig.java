/*-
 * #%L
 * broadleaf-thymeleaf2-presentation
 * %%
 * Copyright (C) 2009 - 2022 Broadleaf Commerce
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

import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.BLCDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractThymeleaf2EngineConfig {

    @Autowired(required = false)
    protected Set<ITemplateResolver> iTemplateResolvers = new LinkedHashSet<>();
    
    @Autowired(required = false)
    protected Set<BroadleafTemplateResolver> blcTemplateResolvers = new LinkedHashSet<>();
    
    /**
     * DO NOT USE THIS PROPETY DIRECTLY
     * The BLCDialect must be last for BroadleafVariableExpressions to work in TL2.
     * use the {@link #getDialects()} method if you need the dialects
     */
    @Autowired
    private Set<IDialect> dialects;
    
    @Autowired
    protected Thymeleaf2ConfigUtils configUtil;
    
    @Bean
    @Primary
    public Set<ITemplateResolver> blWebTemplateResolvers() {
        return configUtil.getWebResolvers(blcTemplateResolvers);
    }
    
    @Bean
    public Set<ITemplateResolver> blEmailTemplateResolvers() {
        return configUtil.getEmailResolvers(blcTemplateResolvers);
    }
    
    @Bean
    public SpringTemplateEngine blEmailTemplateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        Set<ITemplateResolver> allResolvers = new LinkedHashSet<>();
        allResolvers.addAll(iTemplateResolvers);
        allResolvers.addAll(blEmailTemplateResolvers());
        engine.setTemplateResolvers(allResolvers);
        engine.setDialects(getDialects());
        return engine;
    }
    
    public Set<IDialect> getDialects() {
        return orderDialects(dialects);
    }
    
    public void setDialects(Set<IDialect> unorderedDialects) {
        this.dialects = orderDialects(unorderedDialects);
    }
    
    private Set<IDialect> orderDialects(Set<IDialect> unorderedDialects) {
        Set<IDialect> correct = new LinkedHashSet<>();
        IDialect blcDialect = null;
        for (IDialect d : unorderedDialects) {
            if (d instanceof BLCDialect) {
                if (blcDialect != null) {
                    correct.add(blcDialect);
                }
                blcDialect = d;
            } else {
                correct.add(d);
            }
        }
        if (blcDialect != null) {
            correct.add(blcDialect);
        }
        return correct;
    }
    
    @Configuration
    static class Thymeleaf2CommonTemplateResolverConfig extends Thymeleaf2CommonTemplateConfig {}
}

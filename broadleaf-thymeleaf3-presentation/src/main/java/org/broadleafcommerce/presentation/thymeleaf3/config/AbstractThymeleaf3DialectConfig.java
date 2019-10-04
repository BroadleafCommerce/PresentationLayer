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

import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.BroadleafThymeleaf3Dialect;
import org.broadleafcommerce.presentation.thymeleaf3.processor.ArbitraryHtmlInsertionProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.processor.BroadleafThymeleaf3CacheProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.system.JavaVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;

import java.util.LinkedHashSet;
import java.util.Set;

import nz.net.ultraq.thymeleaf.LayoutDialect;

public abstract class AbstractThymeleaf3DialectConfig {

    @Autowired(required = false)
    protected Set<IProcessor> iProcessors = new LinkedHashSet<>();

    @Autowired(required = false)
    protected Set<BroadleafProcessor> blcProcessors = new LinkedHashSet<>();

    @Autowired
    protected Thymeleaf3ConfigUtils configUtil;

    @Bean
    public SpringStandardDialect thymeleafSpringStandardDialect() {
        return new SpringStandardDialect();
    }

    @Bean
    public Set<IDialect> blEmailDialects() {
        Set<IDialect> dialects = new LinkedHashSet<>();
        dialects.add(thymeleafSpringStandardDialect());
        dialects.add(blDialect());
        return dialects;
    }

    @Bean
    public BroadleafThymeleaf3Dialect blDialect() {
        BroadleafThymeleaf3Dialect dialect = new BroadleafThymeleaf3Dialect();
        Set<IProcessor> allProcessors = new LinkedHashSet<>();
        allProcessors.addAll(blDialectProcessors());
        allProcessors.addAll(iProcessors);
        dialect.setProcessors(allProcessors);
        return dialect;
    }

    @Bean
    public Set<IProcessor> blDialectProcessors() {
        return configUtil.getDialectProcessors(blcProcessors);
    }

    @Configuration
    @ConditionalOnClass(name = "nz.net.ultraq.thymeleaf.LayoutDialect")
    protected static class ThymeleafWebLayoutConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public LayoutDialect layoutDialect() {
            return new LayoutDialect();
        }

    }

    @Configuration
    @ConditionalOnClass(DataAttributeDialect.class)
    protected static class DataAttributeDialectConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public DataAttributeDialect dataAttributeDialect() {
            return new DataAttributeDialect();
        }

    }

    @Configuration
    @ConditionalOnClass({ SpringSecurityDialect.class })
    protected static class ThymeleafSecurityDialectConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public SpringSecurityDialect securityDialect() {
            return new SpringSecurityDialect();
        }

    }

    @Configuration
    @ConditionalOnJava(JavaVersion.EIGHT)
    @ConditionalOnClass(Java8TimeDialect.class)
    protected static class ThymeleafJava8TimeDialect {

        @Bean
        @ConditionalOnMissingBean
        public Java8TimeDialect java8TimeDialect() {
            return new Java8TimeDialect();
        }

    }

    @Configuration
    static class AbstractThymeleaf3ProcessorConfig {
        @Bean
        public IProcessor blArbitraryHtmlInjectionProcessor() {
            return new ArbitraryHtmlInsertionProcessor();
        }

        @Bean
        public IProcessor blCacheProcessor() {
            return new BroadleafThymeleaf3CacheProcessor();
        }

    }

}

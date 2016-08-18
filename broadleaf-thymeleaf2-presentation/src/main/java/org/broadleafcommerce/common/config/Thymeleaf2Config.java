/*
 * #%L
 * broadleaf-thymeleaf2-presentation
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
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
package org.broadleafcommerce.common.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.web.BroadleafThymeleafServletContextTemplateResolver;
import org.broadleafcommerce.common.web.dialect.BLCDialect;
import org.broadleafcommerce.common.web.dialect.BroadleafAttributeModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafAttributeModifierProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafFormReplacementProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafTagReplacementProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafTagTextModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafAttributeModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafAttributeModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafFormReplacementProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafTagReplacementProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafTagTextModifierProcessor;
import org.broadleafcommerce.common.web.resolver.BroadleafThymeleafTemplateResolver;
import org.broadleafcommerce.common.web.resolver.BroadleafThymeleafTemplateResolverType;
import org.broadleafcommerce.common.web.resolver.DatabaseResourceResolver;
import org.broadleafcommerce.common.web.resolver.DatabaseTemplateResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

@Configuration
public class Thymeleaf2Config {
    
    @Resource
    protected ApplicationContext applicationContext;
    
    protected static final Log LOG = LogFactory.getLog(Thymeleaf2Config.class);
            
    @Bean
    public BLCDialect blDialect() {
        BLCDialect dialect = new BLCDialect();
        Set<IProcessor> iProcessors = new HashSet<>();
        Collection<BroadleafProcessor> blcProcessors = applicationContext.getBeansOfType(BroadleafProcessor.class).values();
        for (BroadleafProcessor proc : blcProcessors) {
            if (BroadleafModelVariableModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingModelVariableModifierProcessor((BroadleafModelVariableModifierProcessor) proc));
            } else if (BroadleafTagTextModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingTagTextModifierProcessor((BroadleafTagTextModifierProcessor) proc));
            } else if (BroadleafTagReplacementProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingTagReplacementProcessor((BroadleafTagReplacementProcessor) proc));
            } else if (BroadleafFormReplacementProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingFormReplacementProcessor((BroadleafFormReplacementProcessor) proc));
            } else if (BroadleafAttributeModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingAttributeModifierProcessor((BroadleafAttributeModifierProcessor) proc));
            } else if (BroadleafAttributeModelVariableModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingAttributeModelVariableModifierProcessor((BroadleafAttributeModelVariableModifierProcessor) proc));
            } else {
                LOG.warn("No known delegating processor to instantiate processor " + proc);
            }
        }
        dialect.setProcessors(iProcessors);
        return dialect;
    }
    
    @Bean
    public Set<ITemplateResolver> blWebTemplateResolvers() {
        Collection<BroadleafThymeleafTemplateResolver> resolvers = applicationContext.getBeansOfType(BroadleafThymeleafTemplateResolver.class).values();
        Set<ITemplateResolver> webResolvers = new HashSet<>();
        for (BroadleafThymeleafTemplateResolver resolver : resolvers) {
            if (!resolver.isEmailResolver()) {
                ITemplateResolver iResolver = createCorrectTemplateResolver(resolver);
                if (iResolver != null) {
                    webResolvers.add(iResolver);
                }
            }

        }
        return webResolvers;
    }
    
    @Bean 
    public Set<ITemplateResolver> blEmailTemplateResolvers() {
        Collection<BroadleafThymeleafTemplateResolver> resolvers = applicationContext.getBeansOfType(BroadleafThymeleafTemplateResolver.class).values();
        Set<ITemplateResolver> emailResovlers = new HashSet<>();
        for (BroadleafThymeleafTemplateResolver resolver : resolvers) {
            if (resolver.isEmailResolver()) {
                ITemplateResolver iResolver = createCorrectTemplateResolver(resolver);
                if (iResolver != null) {
                    emailResovlers.add(iResolver);
                }
            }

        }
        return emailResovlers;
    }
    
    public DelegatingBroadleafModelVariableModifierProcessor createDelegatingModelVariableModifierProcessor(BroadleafModelVariableModifierProcessor processor) {
        return new DelegatingBroadleafModelVariableModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    public DelegatingBroadleafTagTextModifierProcessor createDelegatingTagTextModifierProcessor(BroadleafTagTextModifierProcessor processor) {
        return new DelegatingBroadleafTagTextModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    public DelegatingBroadleafTagReplacementProcessor createDelegatingTagReplacementProcessor(BroadleafTagReplacementProcessor processor) {
        return new DelegatingBroadleafTagReplacementProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    public DelegatingBroadleafFormReplacementProcessor createDelegatingFormReplacementProcessor(BroadleafFormReplacementProcessor processor) {
        return new DelegatingBroadleafFormReplacementProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    public DelegatingBroadleafAttributeModifierProcessor createDelegatingAttributeModifierProcessor(BroadleafAttributeModifierProcessor processor) {
        return new DelegatingBroadleafAttributeModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }

    public DelegatingBroadleafAttributeModelVariableModifierProcessor createDelegatingAttributeModelVariableModifierProcessor(BroadleafAttributeModelVariableModifierProcessor processor) {
        return new DelegatingBroadleafAttributeModelVariableModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    public ITemplateResolver createCorrectTemplateResolver(BroadleafThymeleafTemplateResolver resolver) {
        if (BroadleafThymeleafTemplateResolverType.CLASSPATH.equals(resolver.getResolverType())) {
            return createClassLoaderTemplateResolver(resolver);
        } else if (BroadleafThymeleafTemplateResolverType.DATABASE.equals(resolver.getResolverType())) {
            return createDatabaseTemplateResolver(resolver);
        } else if (BroadleafThymeleafTemplateResolverType.SERVLET.equals(resolver.getResolverType())) {
            return createServletTemplateResolver(resolver);
        } else {
            LOG.warn("No known Thmeleaf 3 template resolver can be mapped to BroadleafThymeleafTemplateResolverType " + resolver.getResolverType());
            return null;
        }
    }
    
    public ClassLoaderTemplateResolver createClassLoaderTemplateResolver(BroadleafThymeleafTemplateResolver resolver) {
        ClassLoaderTemplateResolver classpathResolver = new ClassLoaderTemplateResolver();
        classpathResolver.setCacheable(resolver.isCacheable());
        classpathResolver.setCacheTTLMs(resolver.getCacheTTLMs());
        classpathResolver.setCharacterEncoding(resolver.getCharacterEncoding());
        classpathResolver.setTemplateMode(resolver.getTemplateMode().toString());
        classpathResolver.setOrder(resolver.getOrder());
        classpathResolver.setPrefix(resolver.getPrefix() + resolver.getTemplateFolder());
        classpathResolver.setSuffix(resolver.getSuffix());
        return classpathResolver;
    }
    
    public DatabaseTemplateResolver createDatabaseTemplateResolver(BroadleafThymeleafTemplateResolver resolver) {
        DatabaseTemplateResolver databaseResolver = new DatabaseTemplateResolver();
        databaseResolver.setCacheable(resolver.isCacheable());
        databaseResolver.setCacheTTLMs(resolver.getCacheTTLMs());
        databaseResolver.setCharacterEncoding(resolver.getCharacterEncoding());
        databaseResolver.setTemplateMode(resolver.getTemplateMode().toString());
        databaseResolver.setOrder(resolver.getOrder());
        databaseResolver.setPrefix(resolver.getPrefix() + resolver.getTemplateFolder());
        databaseResolver.setSuffix(resolver.getSuffix());
        databaseResolver.setResourceResolver(applicationContext.getBean("blDatabaseResourceResolver", DatabaseResourceResolver.class));
        return databaseResolver;
    }
    
    public BroadleafThymeleafServletContextTemplateResolver createServletTemplateResolver(BroadleafThymeleafTemplateResolver resolver) {
        BroadleafThymeleafServletContextTemplateResolver servletResolver = new BroadleafThymeleafServletContextTemplateResolver();
        servletResolver.setCacheable(resolver.isCacheable());
        servletResolver.setCacheTTLMs(resolver.getCacheTTLMs());
        servletResolver.setCharacterEncoding(resolver.getCharacterEncoding());
        servletResolver.setTemplateMode(resolver.getTemplateMode().toString());
        servletResolver.setOrder(resolver.getOrder());
        servletResolver.setPrefix(resolver.getPrefix());
        servletResolver.setTemplateFolder(resolver.getTemplateFolder());
        servletResolver.setSuffix(resolver.getSuffix());
        return servletResolver;
    }
    
}

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
package org.broadleafcommerce.presentation.thymeleaf2.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.presentation.dialect.BroadleafAttributeModifierProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafModelModifierProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafTagReplacementProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafTagTextModifierProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafVariableModifierAttrProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafVariableModifierProcessor;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolverType;
import org.broadleafcommerce.presentation.thymeleaf2.BroadleafThymeleafThemeAwareTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.DelegatingThymeleaf2AttributeModifierProcessor;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.DelegatingThymeleaf2ModelModifierProcessor;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.DelegatingThymeleaf2TagReplacementProcessor;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.DelegatingThymeleaf2TagTextModifierProcessor;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.DelegatingThymeleaf2VariableModifierAttrProcessor;
import org.broadleafcommerce.presentation.thymeleaf2.dialect.DelegatingThymeleaf2VariableModifierProcessor;
import org.broadleafcommerce.presentation.thymeleaf2.resolver.DatabaseResourceResolver;
import org.broadleafcommerce.presentation.thymeleaf2.resolver.DatabaseTemplateResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

@Component("blThymeleaf2ConfigUtils")
public class Thymeleaf2ConfigUtils {
    
    @Resource
    protected ApplicationContext applicationContext;

    protected static final Log LOG = LogFactory.getLog(Thymeleaf2ConfigUtils.class);
    
    public Set<IProcessor> getDialectProcessors(Collection<BroadleafProcessor> blcProcessors) {
        Set<IProcessor> iProcessors = new HashSet<>();
        for (BroadleafProcessor proc : blcProcessors) {
            if (BroadleafVariableModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingModelVariableModifierProcessor((BroadleafVariableModifierProcessor) proc));
            } else if (BroadleafTagTextModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingTagTextModifierProcessor((BroadleafTagTextModifierProcessor) proc));
            } else if (BroadleafTagReplacementProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingTagReplacementProcessor((BroadleafTagReplacementProcessor) proc));
            } else if (BroadleafModelModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingFormReplacementProcessor((BroadleafModelModifierProcessor) proc));
            } else if (BroadleafAttributeModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingAttributeModifierProcessor((BroadleafAttributeModifierProcessor) proc));
            } else if (BroadleafVariableModifierAttrProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingAttributeModelVariableModifierProcessor((BroadleafVariableModifierAttrProcessor) proc));
            } else {
                LOG.warn("No known delegating processor to instantiate processor " + proc);
            }
        }
        return iProcessors;
    }
    
    public Set<ITemplateResolver> getWebResolvers(Collection<BroadleafTemplateResolver> resolvers) {
        Set<ITemplateResolver> webResolvers = new HashSet<>();
        for (BroadleafTemplateResolver resolver : resolvers) {
            if (!resolver.isEmailResolver()) {
                ITemplateResolver iResolver = createCorrectTemplateResolver(resolver);
                if (iResolver != null) {
                    webResolvers.add(iResolver);
                }
            }

        }
        return webResolvers;
    }
    
    public Set<ITemplateResolver> getEmailResolvers(Collection<BroadleafTemplateResolver> resolvers) {
        Set<ITemplateResolver> emailResovlers = new HashSet<>();
        for (BroadleafTemplateResolver resolver : resolvers) {
            if (resolver.isEmailResolver()) {
                ITemplateResolver iResolver = createCorrectTemplateResolver(resolver);
                if (iResolver != null) {
                    emailResovlers.add(iResolver);
                }
            }

        }
        return emailResovlers;
    }
    
    protected DelegatingThymeleaf2VariableModifierProcessor createDelegatingModelVariableModifierProcessor(BroadleafVariableModifierProcessor processor) {
        return new DelegatingThymeleaf2VariableModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected DelegatingThymeleaf2TagTextModifierProcessor createDelegatingTagTextModifierProcessor(BroadleafTagTextModifierProcessor processor) {
        return new DelegatingThymeleaf2TagTextModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected DelegatingThymeleaf2TagReplacementProcessor createDelegatingTagReplacementProcessor(BroadleafTagReplacementProcessor processor) {
        return new DelegatingThymeleaf2TagReplacementProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected DelegatingThymeleaf2ModelModifierProcessor createDelegatingFormReplacementProcessor(BroadleafModelModifierProcessor processor) {
        return new DelegatingThymeleaf2ModelModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected DelegatingThymeleaf2AttributeModifierProcessor createDelegatingAttributeModifierProcessor(BroadleafAttributeModifierProcessor processor) {
        return new DelegatingThymeleaf2AttributeModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }

    protected static DelegatingThymeleaf2VariableModifierAttrProcessor createDelegatingAttributeModelVariableModifierProcessor(BroadleafVariableModifierAttrProcessor processor) {
        return new DelegatingThymeleaf2VariableModifierAttrProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected ITemplateResolver createCorrectTemplateResolver(BroadleafTemplateResolver resolver) {
        if (BroadleafTemplateResolverType.CLASSPATH.equals(resolver.getResolverType())) {
            return createClassLoaderTemplateResolver(resolver);
        } else if (BroadleafTemplateResolverType.DATABASE.equals(resolver.getResolverType())) {
            return createDatabaseTemplateResolver(resolver);
        } else if (BroadleafTemplateResolverType.THEME_AWARE.equals(resolver.getResolverType())) {
            return createServletTemplateResolver(resolver);
        } else {
            LOG.warn("No known Thmeleaf 3 template resolver can be mapped to BroadleafThymeleafTemplateResolverType " + resolver.getResolverType());
            return null;
        }
    }
    
    protected ClassLoaderTemplateResolver createClassLoaderTemplateResolver(BroadleafTemplateResolver resolver) {
        ClassLoaderTemplateResolver classpathResolver = new ClassLoaderTemplateResolver();
        commonTemplateResolver(resolver, classpathResolver);
        classpathResolver.setPrefix(resolver.getPrefix() + resolver.getTemplateFolder());
        return classpathResolver;
    }
    
    protected DatabaseTemplateResolver createDatabaseTemplateResolver(BroadleafTemplateResolver resolver) {
        DatabaseTemplateResolver databaseResolver = new DatabaseTemplateResolver();
        commonTemplateResolver(resolver, databaseResolver);
        databaseResolver.setPrefix(resolver.getPrefix() + resolver.getTemplateFolder());
        databaseResolver.setResourceResolver(applicationContext.getBean("blDatabaseResourceResolver", DatabaseResourceResolver.class));
        return databaseResolver;
    }
    
    protected BroadleafThymeleafThemeAwareTemplateResolver createServletTemplateResolver(BroadleafTemplateResolver resolver) {
        BroadleafThymeleafThemeAwareTemplateResolver servletResolver = applicationContext.getAutowireCapableBeanFactory().createBean(BroadleafThymeleafThemeAwareTemplateResolver.class);
        commonTemplateResolver(resolver, servletResolver);
        servletResolver.setPrefix(resolver.getPrefix());
        servletResolver.setTemplateFolder(resolver.getTemplateFolder());
        return servletResolver;
    }
    
    protected void commonTemplateResolver(BroadleafTemplateResolver blResolver, TemplateResolver tlResolver) {
        tlResolver.setCacheable(blResolver.isCacheable());
        tlResolver.setCacheTTLMs(blResolver.getCacheTTLMs());
        tlResolver.setCharacterEncoding(blResolver.getCharacterEncoding());
        tlResolver.setTemplateMode(blResolver.getTemplateMode().toString());
        tlResolver.setOrder(blResolver.getOrder());
        tlResolver.setSuffix(blResolver.getSuffix());
    }
}

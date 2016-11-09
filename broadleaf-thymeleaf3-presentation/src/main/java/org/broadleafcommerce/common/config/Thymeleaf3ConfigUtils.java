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
import org.broadleafcommerce.common.web.BroadleafThymeleaf3ThemeAwareTemplateResolver;
import org.broadleafcommerce.common.web.dialect.BroadleafAttributeModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafAttributeModifierProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafModelModifierProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafTagReplacementProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafTagTextModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingThymeleaf3AttributeModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingThymeleaf3AttributeModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingThymeleaf3ModelModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingThymeleaf3ModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingThymeleaf3TagReplacementProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingThymeleaf3TagTextModifierProcessor;
import org.broadleafcommerce.common.web.resolver.BroadleafTemplateMode;
import org.broadleafcommerce.common.web.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.common.web.resolver.BroadleafTemplateResolverType;
import org.broadleafcommerce.common.web.resolver.BroadleafThymeleaf3DatabaseTemplateResolver;
import org.broadleafcommerce.core.web.resolver.DatabaseResourceResolverExtensionManager;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Thymeleaf3ConfigUtils {
    
    protected static final Log LOG = LogFactory.getLog(Thymeleaf3ConfigUtils.class);
            
    public static Set<IProcessor> getDialectProcessors(Collection<BroadleafProcessor> blcProcessors) {
        Set<IProcessor> iProcessors = new HashSet<>();
        for (BroadleafProcessor proc : blcProcessors) {
            if (BroadleafModelVariableModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingModelVariableModifierProcessor((BroadleafModelVariableModifierProcessor) proc));
            } else if (BroadleafTagTextModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingTagTextModifierProcessor((BroadleafTagTextModifierProcessor) proc));
            } else if (BroadleafTagReplacementProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingTagReplacementProcessor((BroadleafTagReplacementProcessor) proc));
            } else if (BroadleafModelModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingFormReplacementProcessor((BroadleafModelModifierProcessor) proc));
            } else if (BroadleafAttributeModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingAttributeModifierProcessor((BroadleafAttributeModifierProcessor) proc));
            } else if (BroadleafAttributeModelVariableModifierProcessor.class.isAssignableFrom(proc.getClass())) {
                iProcessors.add(createDelegatingAttributeModelVariableModifierProcessor((BroadleafAttributeModelVariableModifierProcessor) proc));
            } else {
                LOG.warn("No known delegating processor to instantiate processor " + proc);
            }
        }
        return iProcessors;
    }
    
    public static Set<ITemplateResolver> getWebResovlers(Collection<BroadleafTemplateResolver> resolvers, ApplicationContext applicationContext) {
        Set<ITemplateResolver> webResolvers = new HashSet<>();
        for (BroadleafTemplateResolver resolver : resolvers) {
            if (!resolver.isEmailResolver()) {
                ITemplateResolver iResolver = createCorrectTemplateResolver(resolver, applicationContext);
                if (iResolver != null) {
                    webResolvers.add(iResolver);
                }
            }

        }
        return webResolvers;
    }
    
    public static Set<ITemplateResolver> getEmailResolvers(Collection<BroadleafTemplateResolver> resolvers, ApplicationContext applicationContext) {
        Set<ITemplateResolver> emailResovlers = new HashSet<>();
        for (BroadleafTemplateResolver resolver : resolvers) {
            if (resolver.isEmailResolver()) {
                ITemplateResolver iResolver = createCorrectTemplateResolver(resolver, applicationContext);
                if (iResolver != null) {
                    emailResovlers.add(iResolver);
                }
            }

        }
        return emailResovlers;
    }

    protected static DelegatingThymeleaf3ModelVariableModifierProcessor createDelegatingModelVariableModifierProcessor(BroadleafModelVariableModifierProcessor processor) {
        return new DelegatingThymeleaf3ModelVariableModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected static DelegatingThymeleaf3TagTextModifierProcessor createDelegatingTagTextModifierProcessor(BroadleafTagTextModifierProcessor processor) {
        return new DelegatingThymeleaf3TagTextModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected static DelegatingThymeleaf3TagReplacementProcessor createDelegatingTagReplacementProcessor(BroadleafTagReplacementProcessor processor) {
        return new DelegatingThymeleaf3TagReplacementProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected static DelegatingThymeleaf3ModelModifierProcessor createDelegatingFormReplacementProcessor(BroadleafModelModifierProcessor processor) {
        return new DelegatingThymeleaf3ModelModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected static DelegatingThymeleaf3AttributeModifierProcessor createDelegatingAttributeModifierProcessor(BroadleafAttributeModifierProcessor processor) {
        return new DelegatingThymeleaf3AttributeModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }

    protected static DelegatingThymeleaf3AttributeModelVariableModifierProcessor createDelegatingAttributeModelVariableModifierProcessor(BroadleafAttributeModelVariableModifierProcessor processor) {
        return new DelegatingThymeleaf3AttributeModelVariableModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected static ITemplateResolver createCorrectTemplateResolver(BroadleafTemplateResolver resolver, ApplicationContext context) {
        if (BroadleafTemplateResolverType.CLASSPATH.equals(resolver.getResolverType())) {
            return createClassLoaderTemplateResolver(resolver);
        } else if (BroadleafTemplateResolverType.DATABASE.equals(resolver.getResolverType())) {
            return createDatabaseTemplateResolver(resolver, context);
        } else if (BroadleafTemplateResolverType.THEME_AWARE.equals(resolver.getResolverType())) {
            return createServletTemplateResolver(resolver, context);
        } else {
            LOG.warn("No known Thmeleaf 3 template resolver can be mapped to BroadleafThymeleafTemplateResolverType " + resolver.getResolverType());
            return null;
        }
    }
    
    protected static ClassLoaderTemplateResolver createClassLoaderTemplateResolver(BroadleafTemplateResolver resolver) {
        ClassLoaderTemplateResolver classpathResolver = new ClassLoaderTemplateResolver();
        classpathResolver.setCacheable(resolver.isCacheable());
        classpathResolver.setCacheTTLMs(resolver.getCacheTTLMs());
        classpathResolver.setCharacterEncoding(resolver.getCharacterEncoding());
        classpathResolver.setCheckExistence(true);
        classpathResolver.setTemplateMode(translateTemplateModeForThymeleaf3(resolver.getTemplateMode()).toString());
        classpathResolver.setOrder(resolver.getOrder());
        classpathResolver.setPrefix(resolver.getPrefix() + resolver.getTemplateFolder());
        classpathResolver.setSuffix(resolver.getSuffix());
        return classpathResolver;
    }
    
    protected static BroadleafThymeleaf3DatabaseTemplateResolver createDatabaseTemplateResolver(BroadleafTemplateResolver resolver, ApplicationContext applicationContext) {
        BroadleafThymeleaf3DatabaseTemplateResolver databaseResolver = new BroadleafThymeleaf3DatabaseTemplateResolver();
        databaseResolver.setCacheable(resolver.isCacheable());
        databaseResolver.setCacheTTLMs(resolver.getCacheTTLMs());
        databaseResolver.setCharacterEncoding(resolver.getCharacterEncoding());
        databaseResolver.setTemplateMode(translateTemplateModeForThymeleaf3(resolver.getTemplateMode()).toString());
        databaseResolver.setOrder(resolver.getOrder());
        databaseResolver.setPrefix(resolver.getPrefix() + resolver.getTemplateFolder());
        databaseResolver.setSuffix(resolver.getSuffix());
        databaseResolver.setResourceResolverExtensionManager(applicationContext.getBean("blDatabaseResourceResolverExtensionManager", DatabaseResourceResolverExtensionManager.class));
        return databaseResolver;
    }
    
    protected static BroadleafThymeleaf3ThemeAwareTemplateResolver createServletTemplateResolver(BroadleafTemplateResolver resolver, ApplicationContext applicationContext) {
        BroadleafThymeleaf3ThemeAwareTemplateResolver servletResolver = applicationContext.getAutowireCapableBeanFactory().createBean(BroadleafThymeleaf3ThemeAwareTemplateResolver.class);
        servletResolver.setCacheable(resolver.isCacheable());
        servletResolver.setCacheTTLMs(resolver.getCacheTTLMs());
        servletResolver.setCharacterEncoding(resolver.getCharacterEncoding());
        servletResolver.setTemplateMode(translateTemplateModeForThymeleaf3(resolver.getTemplateMode()).toString());
        servletResolver.setOrder(resolver.getOrder());
        servletResolver.setCheckExistence(true);
        servletResolver.setPrefix(resolver.getPrefix());
        servletResolver.setTemplateFolder(resolver.getTemplateFolder());
        servletResolver.setSuffix(resolver.getSuffix());
        return servletResolver;
    }
    
    /**
     * Utility method to convert all HTML5 template modes to HTML since the HTML
     * option in Thymeleaf 3 is HTML5 and the HTML5 option is deprecated 
     */
    protected static BroadleafTemplateMode translateTemplateModeForThymeleaf3(BroadleafTemplateMode mode) {
        if (BroadleafTemplateMode.HTML5.equals(mode) || BroadleafTemplateMode.LEGACYHTML5.equals(mode)) {
            return BroadleafTemplateMode.HTML;
        }
        return mode;
    }
    
}

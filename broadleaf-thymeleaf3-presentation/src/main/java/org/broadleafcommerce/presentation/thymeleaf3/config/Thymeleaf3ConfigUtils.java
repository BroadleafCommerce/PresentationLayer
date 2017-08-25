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
package org.broadleafcommerce.presentation.thymeleaf3.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.web.resource.BroadleafContextUtil;
import org.broadleafcommerce.core.web.resolver.DatabaseResourceResolverExtensionManager;
import org.broadleafcommerce.presentation.dialect.BroadleafAttributeModifierProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafModelModifierProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafTagReplacementProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafTagTextModifierProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafVariableModifierAttrProcessor;
import org.broadleafcommerce.presentation.dialect.BroadleafVariableModifierProcessor;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateMode;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolver;
import org.broadleafcommerce.presentation.resolver.BroadleafTemplateResolverType;
import org.broadleafcommerce.presentation.thymeleaf3.BroadleafThymeleaf3ThemeAwareTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.DelegatingThymeleaf3AttributeModelVariableModifierProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.DelegatingThymeleaf3AttributeModifierProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.DelegatingThymeleaf3ModelModifierProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.DelegatingThymeleaf3TagReplacementProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.DelegatingThymeleaf3TagTextModifierProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.dialect.DelegatingThymeleaf3VariableModifierProcessor;
import org.broadleafcommerce.presentation.thymeleaf3.resolver.BroadleafThymeleaf3DatabaseTemplateResolver;
import org.broadleafcommerce.presentation.thymeleaf3.resolver.DelegatingThymeleaf3TemplateResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

@Component("blThymeleaf3ConfigUtils")
public class Thymeleaf3ConfigUtils {
    
    protected static final Log LOG = LogFactory.getLog(Thymeleaf3ConfigUtils.class);
    
    @Resource
    protected ApplicationContext applicationContext;
            
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

    protected DelegatingThymeleaf3VariableModifierProcessor createDelegatingModelVariableModifierProcessor(BroadleafVariableModifierProcessor processor) {
        return new DelegatingThymeleaf3VariableModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected DelegatingThymeleaf3TagTextModifierProcessor createDelegatingTagTextModifierProcessor(BroadleafTagTextModifierProcessor processor) {
        return new DelegatingThymeleaf3TagTextModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected DelegatingThymeleaf3TagReplacementProcessor createDelegatingTagReplacementProcessor(BroadleafTagReplacementProcessor processor) {
        return new DelegatingThymeleaf3TagReplacementProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected DelegatingThymeleaf3ModelModifierProcessor createDelegatingFormReplacementProcessor(BroadleafModelModifierProcessor processor) {
        return new DelegatingThymeleaf3ModelModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected DelegatingThymeleaf3AttributeModifierProcessor createDelegatingAttributeModifierProcessor(BroadleafAttributeModifierProcessor processor) {
        return new DelegatingThymeleaf3AttributeModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }

    protected DelegatingThymeleaf3AttributeModelVariableModifierProcessor createDelegatingAttributeModelVariableModifierProcessor(BroadleafVariableModifierAttrProcessor processor) {
        return new DelegatingThymeleaf3AttributeModelVariableModifierProcessor(processor.getName(), processor, processor.getPrecedence());
    }
    
    protected ITemplateResolver createCorrectTemplateResolver(BroadleafTemplateResolver resolver) {
        if (BroadleafTemplateResolverType.CLASSPATH.equals(resolver.getResolverType())) {
            return createClassLoaderTemplateResolver(resolver);
        } else if (BroadleafTemplateResolverType.DATABASE.equals(resolver.getResolverType())) {
            return createDatabaseTemplateResolver(resolver);
        } else if (BroadleafTemplateResolverType.THEME_AWARE.equals(resolver.getResolverType())) {
            return createServletTemplateResolver(resolver);
        } else if (BroadleafTemplateResolverType.CUSTOM.equals(resolver.getResolverType())) {
            return createDelegatingThymeleaf3TemplateResolver(resolver);
        } else {
            LOG.warn("No known Thmeleaf 3 template resolver can be mapped to BroadleafThymeleafTemplateResolverType " + resolver.getResolverType());
            return null;
        }
    }
    
    protected ClassLoaderTemplateResolver createClassLoaderTemplateResolver(BroadleafTemplateResolver resolver) {
        ClassLoaderTemplateResolver classpathResolver = new ClassLoaderTemplateResolver();
        commonTemplateResolver(resolver, classpathResolver);
        classpathResolver.setCheckExistence(true);
        classpathResolver.setPrefix(resolver.getPrefix() + resolver.getTemplateFolder());
        return classpathResolver;
    }
    
    protected BroadleafThymeleaf3DatabaseTemplateResolver createDatabaseTemplateResolver(BroadleafTemplateResolver resolver) {
        BroadleafThymeleaf3DatabaseTemplateResolver databaseResolver = new BroadleafThymeleaf3DatabaseTemplateResolver();
        commonTemplateResolver(resolver, databaseResolver);
        databaseResolver.setPrefix(resolver.getPrefix() + resolver.getTemplateFolder());
        databaseResolver.setResourceResolverExtensionManager(applicationContext.getBean("blDatabaseResourceResolverExtensionManager", DatabaseResourceResolverExtensionManager.class));
        databaseResolver.setBroadleafContextUtil(applicationContext.getBean("blBroadleafContextUtil", BroadleafContextUtil.class));
        return databaseResolver;
    }

    protected BroadleafThymeleaf3ThemeAwareTemplateResolver createServletTemplateResolver(BroadleafTemplateResolver resolver) {
        BroadleafThymeleaf3ThemeAwareTemplateResolver servletResolver = applicationContext.getAutowireCapableBeanFactory().createBean(BroadleafThymeleaf3ThemeAwareTemplateResolver.class);
        commonTemplateResolver(resolver, servletResolver);
        servletResolver.setCheckExistence(true);
        servletResolver.setPrefix(resolver.getPrefix());
        servletResolver.setTemplateFolder(resolver.getTemplateFolder());
        return servletResolver;
    }

    protected DelegatingThymeleaf3TemplateResolver createDelegatingThymeleaf3TemplateResolver(BroadleafTemplateResolver resolver) {
        DelegatingThymeleaf3TemplateResolver delegatingResolver = applicationContext.getAutowireCapableBeanFactory().createBean(DelegatingThymeleaf3TemplateResolver.class);
        commonTemplateResolver(resolver, delegatingResolver);
        delegatingResolver.setTemplateResolver(resolver);
        delegatingResolver.setCheckExistence(true);
        delegatingResolver.setPrefix(resolver.getPrefix());
        return delegatingResolver;
    }

    /**
     * Utility method to convert all HTML5 template modes to HTML since the HTML
     * option in Thymeleaf 3 is HTML5 and the HTML5 option is deprecated 
     */
    protected BroadleafTemplateMode translateTemplateModeForThymeleaf3(BroadleafTemplateMode mode) {
        if (BroadleafTemplateMode.HTML5.equals(mode) || BroadleafTemplateMode.LEGACYHTML5.equals(mode)) {
            return BroadleafTemplateMode.HTML;
        }
        return mode;
    }
    
    protected void commonTemplateResolver(BroadleafTemplateResolver blResolver, AbstractConfigurableTemplateResolver tlResolver) {
        tlResolver.setCacheable(blResolver.isCacheable());
        tlResolver.setCacheTTLMs(blResolver.getCacheTTLMs());
        tlResolver.setCharacterEncoding(blResolver.getCharacterEncoding());
        tlResolver.setTemplateMode(translateTemplateModeForThymeleaf3(blResolver.getTemplateMode()).toString());
        tlResolver.setOrder(blResolver.getOrder());
        tlResolver.setSuffix(blResolver.getSuffix());
    }
}

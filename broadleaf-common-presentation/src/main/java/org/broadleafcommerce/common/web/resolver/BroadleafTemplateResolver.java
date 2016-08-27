/*
 * #%L
 * broadleaf-common-presentation
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
package org.broadleafcommerce.common.web.resolver;

/**
 * Class used to indicate a new Template Resolver to be used to resolve file names
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafTemplateResolver {
    
    /**
     * @return The string that should be automatically added to the beginning of given 
     * template name (i.e. for servlet template resolvers {@code /WEB-INF/})
     */
    public String getPrefix();
    
    /**
     * @return The string that should be automatically added to the end of given 
     * template name (i.e. {@code .html})
     */
    public String getSuffix();
    
    /**
     * @return The string that should be added after the prefix but before the given
     * template name to designate set of templates (i.e. {@code templates/})
     * 
     * note string should end in a {@code /}
     */
    public String getTemplateFolder();
    
    /**
     * @return A boolean indicating if the templates resolved through the template resolver
     * should be cached 
     */
    public Boolean isCacheable();
    
    /**
     * @return The amount of time (in minutes) for a template to live in cache
     */
    public Long getCacheTTLMs();
    
    /**
     * @return The character encoding of the templates that this resolver returns
     * (i.e. Usually {@code UTF-8})
     */
    public String getCharacterEncoding();
    
    /**
     * @return The order in the list of all {@code BroadleafThymeleafTemplateResolver} that this resolver should run
     */
    public Integer getOrder();
    
    /**
     * @return The type of templates this resolver resolves for
     */
    public BroadleafTemplateMode getTemplateMode();
    
    /**
     * @return The type of resolver this resolver is
     */
    public BroadleafTemplateResolverType getResolverType();
    
    /**
     * @return Indicates if this resolver should be in set of email resolvers or web resolvers
     */
    public Boolean isEmailResolver();
    
}


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

public abstract class AbstractBroadleafTemplateResolver implements BroadleafTemplateResolver {

    protected String prefix = "";
    protected String suffix = "";
    protected String templateFolder = "";
    protected Boolean cacheable = false;
    protected Long cacheTimeToLive = 0L;
    protected String characterEncoding = "UTF-8";
    protected Integer order = 1000;
    protected BroadleafTemplateMode templateMode = BroadleafTemplateMode.HTML5;
    protected Boolean emailResolver = false;
    
    @Override
    public String getPrefix() {
        return prefix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    @Override
    public String getSuffix() {
        return suffix;
    }
    
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    @Override
    public String getTemplateFolder() {
        return templateFolder;
    }
    
    public void setTemplateFolder(String templateFolder) {
        this.templateFolder = templateFolder;
    }

    @Override
    public Boolean isCacheable() {
        return cacheable;
    }
    
    public void setCacheable(Boolean cacheable) {
        this.cacheable = cacheable;
    }
    
    @Override
    public Long getCacheTTLMs() {
        return cacheTimeToLive;
    }
    
    public void setCacheTTLMs(Long cacheTimeToLive) {
        this.cacheTimeToLive = cacheTimeToLive;
    }
    
    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }
    
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
    
    @Override
    public Integer getOrder() {
        return order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
    }
    
    @Override
    public BroadleafTemplateMode getTemplateMode() {
        return templateMode;
    }
    
    public void setTemplateMode(BroadleafTemplateMode templateMode) {
        this.templateMode = templateMode;
    }

    @Override
    public Boolean isEmailResolver() {
        return emailResolver;
    }

    public void setEmailResolver(Boolean emailResolver) {
        this.emailResolver = emailResolver;
    }

}

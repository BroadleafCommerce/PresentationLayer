/*
 * #%L
 * BroadleafCommerce Common Libraries
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
package org.broadleafcommerce.common.web.processor;

import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.common.resource.service.ResourceBundlingService;
import org.broadleafcommerce.common.util.BLCSystemProperty;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.common.web.dialect.AbstractBroadleafTagReplacementProcessor;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


/**
 * <p>
 * Works with the blc:bundle tag.   
 * 
 * <p>
 * This processor does not do the actual bundling.   It merely changes the URL which causes the 
 * other bundling components to be invoked through the normal static resource handling processes.
 * 
 * <p>
 * This processor relies {@code bundle.enabled}.   If this property is false (typical for dev) then the list of
 * resources will be output as individual SCRIPT or LINK elements for each JavaScript or CSS file respectively.
 * 
 * <p>
 * To use this processor, supply a name, mapping prefix, and list of files.   
 * 
 * <pre>
 * {@code
 * <blc:bundle name="lib.js" 
 *             mapping-prefix="/js/"
 *             files="plugins.js,
 *                    libs/jquery.MetaData.js,
 *                    libs/jquery.rating.pack.js,
 *                    libs/jquery.dotdotdot-1.5.1.js" />
 *  }
 * </pre>                  
 * 
 * <p>
 * With bundling enabled this will turn into:
 * 
 * <pre>
 * 
 * {@code
 *  <script type="text/javascript" src="/js/lib-blbundle12345.js" />
 * }
 * </pre>
 * 
 * <p>
 * Where the <b>-blbundle12345</b> is used by the BundleUrlResourceResolver to determine the
 * actual bundle name.  
 * 
 * <p>
 * With bundling disabled this turns into:
 * 
 * <pre>
 * {@code
 *  <script type="text/javascript" src="/js/plugins.js" />
 *  <script type="text/javascript" src="/js/jquery.MetaData.js" />
 *  <script type="text/javascript" src="/js/jquery.rating.pack.js.js" />
 *  <script type="text/javascript" src="/js/jquery.dotdotdot-1.5.1.js" />
 * }
 * </pre>
 * 
 * <p>
 * This processor also supports producing the 'async' and 'defer' attributes for Javascript files. For instance:
 * 
 * <pre>
 * {@code
 * <blc:bundle name="lib.js" 
 *             async="true"
 *             defer="true"
 *             mapping-prefix="/js/"
 *             files="plugins.js,
 *                    libs/jquery.MetaData.js,
 *                    libs/jquery.rating.pack.js,
 *                    libs/jquery.dotdotdot-1.5.1.js" />
 *  }
 * </pre>
 * 
 * <p>
 * If bundling is turned on, the single output file contains the 'async' and 'defer' name-only attributes. When bundling is
 * turned off, then those name-only attributes are applied to each individual file reference.
 * 
 * <p>
 * This processor only supports files that end in <b>.js</b> and <b>.css</b>
 * 
 * @param <b>name</b>           (required) the final name prefix of the bundle
 * @param <b>mapping-prefix</b> (required) the prefix appended to the final tag output whether that be 
 *                              the list of files or the single minified file
 * @param <b>files</b>          (required) a comma-separated list of files that should be bundled together
 * 
 * @author apazzolini
 * @author bpolster
 * @see {@link ResourceBundlingService}
 */
public class ResourceBundleProcessor extends AbstractBroadleafTagReplacementProcessor {
    
    @Resource(name = "blResourceBundlingService")
    protected ResourceBundlingService bundlingService;
    
    protected boolean getBundleEnabled() {
        return BLCSystemProperty.resolveBooleanSystemProperty("bundle.enabled");
    }
    
    protected ResourceBundleProcessor() {
        super("bundle", 10000);
    }
    
    @Override
    protected BroadleafThymeleafModel getReplacementModel(String tagName, Map<String, String> tagAttributes, BroadleafThymeleafContext context) {
        String name = tagAttributes.get("name");
        String mappingPrefix = tagAttributes.get("mapping-prefix");
        boolean async = tagAttributes.containsKey("async");
        boolean defer = tagAttributes.containsKey("defer");
        
        List<String> files = new ArrayList<>();
        for (String file : tagAttributes.get("files").split(",")) {
            files.add(file.trim());
        }
        List<String> additionalBundleFiles = bundlingService.getAdditionalBundleFiles(name);
        if (additionalBundleFiles != null) {
            files.addAll(additionalBundleFiles);
        }
        BroadleafThymeleafModel model = context.createModel();
        if (getBundleEnabled()) {
            String bundleResourceName = bundlingService.resolveBundleResourceName(name, mappingPrefix, files);
            String bundleUrl = getBundleUrl(bundleResourceName);
            
            addElementToModel(bundleUrl, async, defer, context, model);
        } else {
            for (String fileName : files) {
                fileName = fileName.trim();
                String fullFileName = (String) context.parseExpression("@{'" + mappingPrefix + fileName + "'}");
                addElementToModel(fullFileName, async, defer, context, model);
            }
        }
        return model;
    }
    
    /**
     * Adds the context path to the bundleUrl.    We don't use the Thymeleaf "@" syntax or any other mechanism to 
     * encode this URL as the resolvers could have a conflict.   
     * 
     * For example, resolving a bundle named "style.css" that has a file also named "style.css" creates problems as
     * the TF or version resolvers both want to version this file.
     *  
     * @param arguments
     * @param bundleName
     * @return
     */
    protected String getBundleUrl(String bundleName) {
        String bundleUrl = bundleName;

        if (!StringUtils.startsWith(bundleUrl, "/")) {
            bundleUrl = "/" + bundleUrl;
        }
        
        String contextPath = BroadleafRequestContext.getBroadleafRequestContext().getRequest().getContextPath();

        if (StringUtils.isNotEmpty(contextPath)) {
            bundleUrl = contextPath + bundleUrl;
        }

        return bundleUrl;
    }
    
    protected void addElementToModel(String src, boolean async, boolean defer, BroadleafThymeleafContext context, BroadleafThymeleafModel model) {
        if (src.contains(";")) {
            src = src.substring(0, src.indexOf(';'));
        }
        
        if (src.endsWith(".js")) {
            model.addElement(context.createNonVoidElement("script", getScriptAttributes(src, async, defer), true));
        } else if (src.endsWith(".css")) {
            model.addElement(context.createNonVoidElement("link", getLinkAttributes(src), true));
        } else {
            throw new IllegalArgumentException("Unknown extension for: " + src + " - only .js and .css are supported");
        }
    }

    protected Map<String, String> getScriptAttributes(String src, boolean async, boolean defer) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("type", "text/javascript");
        attributes.put("src", src);
        if (async) {
            attributes.put("async", null);
        }
        if (defer) {
            attributes.put("defer", null);
        }
        return attributes;
    }
    
    protected Map<String, String> getLinkAttributes(String src) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("rel", "stylesheet");
        attributes.put("href", src);
        return attributes;
    }

}

/*
 * #%L
 * BroadleafCommerce Framework Web
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

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.security.handler.CsrfFilter;
import org.broadleafcommerce.common.security.service.ExploitProtectionService;
import org.broadleafcommerce.common.security.service.StaleStateProtectionService;
import org.broadleafcommerce.common.web.dialect.AbstractBroadleafFormReplacementProcessor;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafElement;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafFormReplacementDTO;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Used as a replacement to the HTML {@code <form>} element which adds a CSRF token input field to forms that are submitted
 * via anything but GET. This is required to properly bypass the {@link CsrfFilter}.
 * 
 * @author apazzolini
 * @see {@link CsrfFilter}
 */
@Component("blFormProcessor")
public class FormProcessor extends AbstractBroadleafFormReplacementProcessor {
    

    public FormProcessor(String elementName, int precedence) {
        super(elementName, precedence);
    }
    public FormProcessor() {
        this("form", 1001);
    }

    @Resource(name = "blExploitProtectionService")
    protected ExploitProtectionService eps;

    @Resource(name = "blStaleStateProtectionService")
    protected StaleStateProtectionService spps;
    
    @Override
    protected BroadleafThymeleafFormReplacementDTO getInjectedModelAndFormAttributes(String rootTagName, Map<String, String> rootTagAttributes, BroadleafThymeleafContext context) {
        Map<String, String> formAttributes = new HashMap<>();
        formAttributes.putAll(rootTagAttributes);
        BroadleafThymeleafModel model = context.createModel();
        BroadleafThymeleafFormReplacementDTO dto = new BroadleafThymeleafFormReplacementDTO();

        // If the form will be not be submitted with a GET, we must add the CSRF token
        // We do this instead of checking for a POST because post is default if nothing is specified
        if (!"GET".equalsIgnoreCase(formAttributes.get("method"))) {
            try {
                String csrfToken = eps.getCSRFToken();
                String stateVersionToken = null;
                if (spps.isEnabled()) {
                    stateVersionToken = spps.getStateVersionToken();
                }

                //detect multipart form
                if ("multipart/form-data".equalsIgnoreCase(formAttributes.get("enctype"))) {
                    String csrfQueryParameter = "?" + eps.getCsrfTokenParameter() + "=" + csrfToken;
                    if (stateVersionToken != null) {
                        csrfQueryParameter += "&" + spps.getStateVersionTokenParameter() + "=" + stateVersionToken;
                    }
                    
                    // Add this into the attribute map to be used for the new <form> tag. The expression has already
                    // been executed, don't need to treat the value as an expression
                    String actionValue = formAttributes.get("action");
                    actionValue += csrfQueryParameter;
                    formAttributes.put("action", actionValue);
                } else {
                    
                    Map<String, String> csrfAttributes = new HashMap<>();
                    csrfAttributes.put("type", "hidden");
                    csrfAttributes.put("name", eps.getCsrfTokenParameter());
                    csrfAttributes.put("value", csrfToken);
                    BroadleafThymeleafElement csrfTag = context.createStandaloneElement("input", csrfAttributes, true);
                    model.addElement(csrfTag);

                    if (stateVersionToken != null) {
                        
                        Map<String, String> stateVersionAttributes = new HashMap<>();
                        stateVersionAttributes.put("type", "hidden");
                        stateVersionAttributes.put("name", spps.getStateVersionTokenParameter());
                        stateVersionAttributes.put("value", stateVersionToken);
                        BroadleafThymeleafElement stateVersionTag = context.createStandaloneElement("input", stateVersionAttributes, true);
                        model.addElement(stateVersionTag);
                    }
                    dto.setModel(model);
                }
                
            } catch (ServiceException e) {
                throw new RuntimeException("Could not get a CSRF token for this session", e);
            }
        }
        dto.setFormParameters(formAttributes);
        return dto;
    }
    
    @Override
    protected boolean reprocessModel() {
        return true;
    }

}

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
import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import javax.annotation.Resource;

/**
 * Used as a replacement to the HTML {@code <form>} element which adds a CSRF token input field to forms that are submitted
 * via anything but GET. This is required to properly bypass the {@link CsrfFilter}.
 * 
 * @author apazzolini
 * @see {@link CsrfFilter}
 */
@Component("blFormProcessor")
public class FormProcessor extends AbstractElementProcessor {
    
    @Resource(name = "blExploitProtectionService")
    protected ExploitProtectionService eps;

    @Resource(name = "blStaleStateProtectionService")
    protected StaleStateProtectionService spps;
    
    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public FormProcessor() {
        super("form");
    }
    
    /**
     * We need this replacement to execute as early as possible to allow subsequent processors to act
     * on this element as if it were a normal form instead of a blc:form
     */
    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    protected ProcessorResult processElement(Arguments arguments, Element element) {
        // If the form will be not be submitted with a GET, we must add the CSRF token
        // We do this instead of checking for a POST because post is default if nothing is specified
        if (!"GET".equalsIgnoreCase(element.getAttributeValueFromNormalizedName("method"))) {
            try {
                String csrfToken = eps.getCSRFToken();
                String stateVersionToken = null;
                if (spps.isEnabled()) {
                    stateVersionToken = spps.getStateVersionToken();
                }

                //detect multipart form
                if ("multipart/form-data".equalsIgnoreCase(element.getAttributeValueFromNormalizedName("enctype"))) {
                    Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                            .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValueFromNormalizedName("th:action"));
                    String action = (String) expression.execute(arguments.getConfiguration(), arguments);
                    String csrfQueryParameter = "?" + eps.getCsrfTokenParameter() + "=" + csrfToken;
                    if (stateVersionToken != null) {
                        csrfQueryParameter += "&" + spps.getStateVersionTokenParameter() + "=" + stateVersionToken;
                    }
                    element.removeAttribute("th:action");
                    element.setAttribute("action", action + csrfQueryParameter);
                } else {
                    Element csrfNode = new Element("input");
                    csrfNode.setAttribute("type", "hidden");
                    csrfNode.setAttribute("name", eps.getCsrfTokenParameter());
                    csrfNode.setAttribute("value", csrfToken);
                    element.addChild(csrfNode);
                    if (stateVersionToken != null) {
                        Element versionNode = new Element("input");
                        versionNode.setAttribute("type", "hidden");
                        versionNode.setAttribute("name", spps.getStateVersionTokenParameter());
                        versionNode.setAttribute("value", stateVersionToken);
                        element.addChild(versionNode);
                    }
                }

            } catch (ServiceException e) {
                throw new RuntimeException("Could not get a CSRF token for this session", e);
            }
        }
        
        // Convert the <blc:form> node to a normal <form> node
        Element newElement = element.cloneElementNodeWithNewName(element.getParent(), "form", false);
        newElement.setRecomputeProcessorsImmediately(true);
        element.getParent().insertAfter(element, newElement);
        element.getParent().removeChild(element);
        
        return ProcessorResult.OK;
    }
    
}

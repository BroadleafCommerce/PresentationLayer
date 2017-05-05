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
package org.broadleafcommerce.presentation.thymeleaf3.dialect;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.presentation.dialect.BroadleafVariableModifierProcessor;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.broadleafcommerce.presentation.thymeleaf3.model.BroadleafThymeleaf3Context;
import org.springframework.web.context.request.WebRequest;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author apazzolini
 */
public class DelegatingThymeleaf3VariableModifierProcessor extends AbstractElementTagProcessor {
    
    private static final Log LOG = LogFactory.getLog(DelegatingThymeleaf3VariableModifierProcessor.class);
    protected BroadleafVariableModifierProcessor processor;
    
    public DelegatingThymeleaf3VariableModifierProcessor(String elementName, BroadleafVariableModifierProcessor processor, int precedence) {
        super(TemplateMode.HTML, processor.getPrefix(), elementName, true, null, false, precedence);
        this.processor = processor;
    }

    /**
     * This method will handle calling the modifyModelAttributes abstract method
     */
    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        Map<String, String> attributes = tag.getAttributeMap();
        BroadleafTemplateContext blcContext = new BroadleafThymeleaf3Context(context, structureHandler);
        Map<String, Object> newModelVariables = processor.populateModelVariables(tag.getElementCompleteName(), attributes, blcContext);
        
        if (MapUtils.isNotEmpty(newModelVariables)) {
            for (Map.Entry<String, Object> entry : newModelVariables.entrySet()) {
                addToModel(structureHandler, context, entry.getKey(), entry.getValue());
            }
        }
        
        // Remove the tag from the DOM
        structureHandler.removeTags();
    }

    private void addToModel(IElementTagStructureHandler structureHandler, ITemplateContext context, String key, Object value) {
        if (processor.getCollectionModelVariableNamesToAddTo() != null && processor.getCollectionModelVariableNamesToAddTo().contains(key)) {
            if (processor.useGlobalScope()) {
                addItemToExistingSet(key, value);
            } else {
                addToLocalExistingSet(context, structureHandler, key, value);
            }
        } else {
            if (processor.useGlobalScope()) {
                addToGlobalModel(key, value);
            } else {
                addToLocalModel(structureHandler, key, value);
            }
        }
    }
    
    /**
     * Helper method to add a value to the local variable state
     * 
     * @param structureHandler
     * @param key
     * @param value
     */
    private void addToLocalModel(IElementTagStructureHandler structureHandler, String key, Object value) {
        structureHandler.setLocalVariable(key, value);
    }
    
    @SuppressWarnings("unchecked")
    private <T> void addToLocalExistingSet(ITemplateContext context, IElementTagStructureHandler structureHandler, String key, Object value) {
        Set<T> items = new HashSet<>();
        if (context.containsVariable(key)) {
            items = (Set<T>) context.getVariable(key);
        }
        if (Collection.class.isAssignableFrom(value.getClass())) {
            items.addAll((Collection<T>) value);
        } else {
            items.add((T) value);
        }
        structureHandler.setLocalVariable(key, items);
    }
    
    /**
     * Helper method to add a value to the global variable state
     * 
     * @deprecated {@code addToLocalModel} should be used instead though the usage will not be the same.
     * Per TL3 suggestions modifying the global variable state should be avoided and only modifying the local state is preferred. 
     * @param key the key to add to the model
     * @param value the value represented by the key
     */
    @Deprecated
    private void addToGlobalModel(String key, Object value) {
        WebRequest request = BroadleafRequestContext.getBroadleafRequestContext()
            .getWebRequest();
        if (request != null) {
            request.setAttribute(key, value, WebRequest.SCOPE_REQUEST);
        } else {
            LOG.warn("Cannot add " + key + " to a global model since this is not inside of a web request");
        }
    }


    /**
     * Adds an item to an existing set on the global variable state
     * 
     * @param arguments
     * @param key
     * @param value
     * @return true if key, value pair was successfully added else false
     * @deprecated this method should no longer be used as it cannot provide the correct functionality in Thymeleaf 3+
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    protected <T> void addItemToExistingSet(String key, Object value) {
        WebRequest request = BroadleafRequestContext.getBroadleafRequestContext().getWebRequest();
        Set<T> items = (Set<T>) request.getAttribute(key, WebRequest.SCOPE_REQUEST);
        if (items == null) {
            items = new HashSet<>();
            addToGlobalModel(key, items);
        }
        
        if (Collection.class.isAssignableFrom(value.getClass())) {
            items.addAll((Collection<T>) value);
        } else {
            items.add((T) value);
        }
    }
}


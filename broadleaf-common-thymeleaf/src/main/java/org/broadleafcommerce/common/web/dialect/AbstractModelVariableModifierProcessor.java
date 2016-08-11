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
package org.broadleafcommerce.common.web.dialect;

import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContextImpl;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author apazzolini
 * 
 * Wrapper class around Thymeleaf's AbstractElementProcessor that facilitates adding Objects
 * to the current evaluation context (model) for processing in the remainder of the page.
 *
 */
public abstract class AbstractModelVariableModifierProcessor extends AbstractElementProcessor {
    
    private int precedence;

    public AbstractModelVariableModifierProcessor(String elementName) {
        this(elementName, 1000);
    }
    
    // In TL3 the precedence is declared in the constructor so this has to exist since the processors
    // in framework have to be TL version agnostic
    public AbstractModelVariableModifierProcessor(String elementName, int precedence) {
        super(elementName);
        this.precedence = precedence;
    }

    /**
     * This method will handle calling the modifyModelAttributes abstract method and return
     * an "OK" processor result
     */
    @Override
    protected ProcessorResult processElement(final Arguments arguments, final Element element) {
        BroadleafThymeleafContext context = new BroadleafThymeleafContextImpl(arguments);
        String tagName = element.getNormalizedName();
        Map<String, Attribute> attributeMap = element.getAttributeMap();
        Map<String, String> tagAttributes = new HashMap<>();
        for (String key : attributeMap.keySet()) {
            tagAttributes.put(element.getAttributeOriginalNameFromNormalizedName(key), attributeMap.get(key).getValue());
        }
        Map<String, Object> newModelVariables = new HashMap<>();
        populateModelVariables(tagName, tagAttributes, newModelVariables, context);
        if (!addToLocal()) {
            for (Map.Entry<String, Object> entry : newModelVariables.entrySet()) {
                addToModel(arguments, entry.getKey(), entry.getValue());
            }
            // Remove the tag from the DOM. Only done with global because local scope will be lost otherwise
            // Presumably this is a bug because this acts differently in Thymeleaf 3
            final NestableNode parent = element.getParent();
            parent.removeChild(element);
            return ProcessorResult.OK;
        }
        
        return ProcessorResult.setLocalVariables(newModelVariables);
    }
    
    /**
     * Helper method to add a value to the expression evaluation root (model) Map
     * @param key the key to add to the model
     * @param value the value represented by the key
     */
    @SuppressWarnings("unchecked")
    private void addToModel(Arguments arguments, String key, Object value) {
        ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).put(key, value);
    }
    
    @SuppressWarnings("unchecked")
    protected <T> void addCollectionToExistingSet(String key, Collection<T> value, BroadleafThymeleafContext context) {
        Arguments arguments = ((BroadleafThymeleafContextImpl) context).getThymeleafContext();
        Set<T> items = (Set<T>) ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).get(key);
        if (items == null) {
            items = new HashSet<T>();
            ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).put(key, items);
        }
        items.addAll(value);
    }

    @SuppressWarnings("unchecked")
    protected <T> void addItemToExistingSet(String key, Object value, BroadleafThymeleafContext context) {
        Arguments arguments = ((BroadleafThymeleafContextImpl) context).getThymeleafContext();
        Set<T> items = (Set<T>) ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).get(key);
        if (items == null) {
            items = new HashSet<T>();                         
            ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).put(key, items);
        }
        items.add((T) value);
    }
    
    protected boolean addToLocal() {
        return false;
    }

    protected abstract void populateModelVariables(String tagName, Map<String, String> tagAttributes, Map<String, Object> newModelVars, BroadleafThymeleafContext context);

    @Override
    public int getPrecedence() {
        return this.precedence;
    }
}

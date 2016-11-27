/*
 * #%L
 * broadleaf-common-thymeleaf
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
package org.broadleafcommerce.presentation.thymeleaf2.domain;

import org.broadleafcommerce.presentation.model.BroadleafAssignation;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.broadleafcommerce.presentation.model.BroadleafTemplateElement;
import org.broadleafcommerce.presentation.model.BroadleafTemplateModel;
import org.broadleafcommerce.presentation.model.BroadleafTemplateNonVoidElement;
import org.springframework.web.servlet.support.BindStatus;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.spring4.util.FieldUtils;
import org.thymeleaf.standard.expression.Assignation;
import org.thymeleaf.standard.expression.AssignationUtils;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation of utilities that can be done during execution of a processor.
 * The underlying encapsulated object is an {@code Arguments} object
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThymeleafContextImpl implements BroadleafTemplateContext { 

    protected Arguments arguments;
    
    public BroadleafThymeleafContextImpl(Arguments arguments) {
        this.arguments = arguments;
    }

    @Override
    public <T> T parseExpression(String value) {
        return (T) StandardExpressions.getExpressionParser(arguments.getConfiguration())
            .parseExpression(arguments.getConfiguration(), arguments, value)
            .execute(arguments.getConfiguration(), arguments);
    }

    @Override
    public List<BroadleafAssignation> getAssignationSequence(String value, boolean allowParametersWithoutValue) {
        List<BroadleafAssignation> assignations = new ArrayList<>();
        for (Assignation assignation : AssignationUtils.parseAssignationSequence(arguments.getConfiguration(), arguments, value, allowParametersWithoutValue)) {
            assignations.add(new BroadleafAssignationImpl(assignation));
        }
        return assignations;
    }

    public Arguments getThymeleafContext() {
        return this.arguments;
    }

    @Override
    public BroadleafTemplateNonVoidElement createNonVoidElement(String tagName, Map<String, String> attributes, boolean useDoubleQuotes) {
        return createNonTextElement(tagName, attributes);
    }

    @Override
    public BroadleafTemplateNonVoidElement createNonVoidElement(String tagName) {
        return createNonTextElement(tagName);
    }

    @Override
    public BroadleafTemplateElement createStandaloneElement(String tagName, Map<String, String> attributes, boolean useDoubleQuotes) {
        return createNonTextElement(tagName, attributes);
    }

    @Override
    public BroadleafTemplateElement createStandaloneElement(String tagName) {
        return createNonTextElement(tagName);
    }

    protected BroadleafThymeleafNonTextElementImpl createNonTextElement(String tagName) {
        return new BroadleafThymeleafNonTextElementImpl(new Element(tagName));
    }

    protected BroadleafThymeleafNonTextElementImpl createNonTextElement(String tagName, Map<String, String> attributes) {
        Element elem = new Element(tagName);
        elem.setAttributes(attributes);
        return new BroadleafThymeleafNonTextElementImpl(elem);
    }

    @Override
    public BroadleafTemplateElement createTextElement(String text) {
        return new BroadleafThymeleafTextElementImpl(new Macro(text));
    }

    @Override
    public BroadleafTemplateModel createModel() {
        return new BroadleafThymeleafModelImpl();
    }

    @Override
    public void setNodeLocalVariable(BroadleafTemplateElement element, String key, Object value) {
        ((BroadleafThymeleafTemplateEvent) element).getNode().setNodeLocalVariable(key, value);
        
    }

    @Override
    public void setNodeLocalVariables(BroadleafTemplateElement element, Map<String, Object> variableMap) {
        ((BroadleafThymeleafTemplateEvent) element).getNode().setAllNodeLocalVariables(variableMap);
    }

    @Override
    public Object getVariable(String name) {
        return arguments.getContext().getVariables().get(name);
    }

    @Override
    public BindStatus getBindStatus(String attributeValue) {
        return FieldUtils.getBindStatus(arguments.getConfiguration(), arguments, attributeValue);
    }

}

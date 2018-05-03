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
package org.broadleafcommerce.presentation.thymeleaf3.model;

import org.broadleafcommerce.presentation.model.BroadleafAssignation;
import org.broadleafcommerce.presentation.model.BroadleafBindStatus;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.broadleafcommerce.presentation.model.BroadleafTemplateElement;
import org.broadleafcommerce.presentation.model.BroadleafTemplateModel;
import org.broadleafcommerce.presentation.model.BroadleafTemplateNonVoidElement;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.WebEngineContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.ICloseElementTag;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.IStandaloneElementTag;
import org.thymeleaf.model.IText;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.util.FieldUtils;
import org.thymeleaf.standard.expression.Assignation;
import org.thymeleaf.standard.expression.AssignationUtils;
import org.thymeleaf.standard.expression.StandardExpressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Concrete implementation of utilities that can be done during execution of a processor.
 * The underlying encapsulated object is an {@code ITemplateContext}
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThymeleaf3Context implements BroadleafTemplateContext {

    protected ITemplateContext context;
    protected IElementModelStructureHandler modelHandler;
    protected IElementTagStructureHandler tagHandler;

    public BroadleafThymeleaf3Context(ITemplateContext context, IElementModelStructureHandler modelHandler) {
        this.context = context;
        this.modelHandler = modelHandler;
        this.tagHandler = null;
    }
    
    public BroadleafThymeleaf3Context(ITemplateContext context, IElementTagStructureHandler tagHandler) {
        this.context = context;
        this.tagHandler = tagHandler;
        this.modelHandler = null;
    }

    @Override
    public <T> T parseExpression(String value) {
        return (T) StandardExpressions.getExpressionParser(context.getConfiguration())
            .parseExpression(context, value)
            .execute(context);
    }
    
    @Override
    public List<BroadleafAssignation> getAssignationSequence(String value, boolean allowParametersWithoutValue) {
        List<BroadleafAssignation> assignations = new ArrayList<>();
        for (Assignation assignation : AssignationUtils.parseAssignationSequence(context, value, allowParametersWithoutValue)) {
            assignations.add(new BroadleafThymeleaf3Assignation(assignation));
        }
        return assignations;
    }

    public ITemplateContext getThymeleafContext() {
        return this.context;
    }

    @Override
    public BroadleafTemplateNonVoidElement createNonVoidElement(String tagName, Map<String, String> attributes, boolean useDoubleQuotes) {
        IOpenElementTag open = context.getModelFactory().createOpenElementTag(tagName, attributes, useDoubleQuotes ? AttributeValueQuotes.DOUBLE : AttributeValueQuotes.SINGLE, false);
        ICloseElementTag close = context.getModelFactory().createCloseElementTag(tagName, false, false);
        return new BroadleafThymeleaf3NonVoidElement(open, close);
    }

    @Override
    public BroadleafTemplateNonVoidElement createNonVoidElement(String tagName) {
        IOpenElementTag open = context.getModelFactory().createOpenElementTag(tagName);
        ICloseElementTag close = context.getModelFactory().createCloseElementTag(tagName, false, false);
        return new BroadleafThymeleaf3NonVoidElement(open, close);
    }

    @Override
    public BroadleafTemplateElement createStandaloneElement(String tagName, Map<String, String> attributes, boolean useDoubleQuotes) {
        IStandaloneElementTag standaloneTag = context.getModelFactory().createStandaloneElementTag(tagName, attributes, useDoubleQuotes ? AttributeValueQuotes.DOUBLE : AttributeValueQuotes.SINGLE, false, true);
        return new BroadleafThymeleaf3StandaloneElement(standaloneTag);
    }

    @Override
    public BroadleafTemplateElement createStandaloneElement(String tagName) {
        IStandaloneElementTag standaloneTag = context.getModelFactory().createStandaloneElementTag(tagName);
        return new BroadleafThymeleaf3StandaloneElement(standaloneTag);
    }

    @Override
    public BroadleafTemplateElement createTextElement(String text) {
        IText textNode = context.getModelFactory().createText(text);
        return new BroadleafThymeleaf3TextElement(textNode);
    }

    @Override
    public BroadleafTemplateModel createModel() {
        return new BroadleafThymeleaf3Model(context.getModelFactory().createModel());
    }

    @Override
    public void setNodeLocalVariable(BroadleafTemplateElement element, String key, Object value) {
        if (modelHandler != null) {
            modelHandler.setLocalVariable(key, value);
        } else if (tagHandler != null) {
            tagHandler.setLocalVariable(key, value);
        }
    }

    @Override
    public void setNodeLocalVariables(BroadleafTemplateElement element, Map<String, Object> variableMap) {
        if (modelHandler != null) {
            for (String key : variableMap.keySet()) {
                modelHandler.setLocalVariable(key, variableMap.get(key));
            }
        } else if (tagHandler != null) {
            for (String key : variableMap.keySet()) {
                tagHandler.setLocalVariable(key, variableMap.get(key));
            }
        }
    }

    @Override
    public Object getVariable(String name) {
        return context.getVariable(name);
    }

    @Override
    public BroadleafBindStatus getBindStatus(String attributeValue) {
        return new BroadleafThymeleaf3BindStatus(FieldUtils.getBindStatus(context, attributeValue));
    }

    @Override
    public HttpServletRequest getRequest() {
        if (context instanceof WebEngineContext) {
            return ((WebEngineContext) context).getRequest();
        }
        return null;
    }

}

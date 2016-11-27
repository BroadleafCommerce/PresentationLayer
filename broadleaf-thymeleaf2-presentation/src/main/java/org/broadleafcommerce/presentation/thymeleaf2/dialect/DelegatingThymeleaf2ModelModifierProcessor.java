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
package org.broadleafcommerce.presentation.thymeleaf2.dialect;

import org.apache.commons.collections.MapUtils;
import org.broadleafcommerce.presentation.dialect.BroadleafModelModifierProcessor;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.broadleafcommerce.presentation.model.BroadleafTemplateElement;
import org.broadleafcommerce.presentation.model.BroadleafTemplateModelModifierDTO;
import org.broadleafcommerce.presentation.thymeleaf2.model.BroadleafThymeleaf2Context;
import org.broadleafcommerce.presentation.thymeleaf2.model.BroadleafThymeleaf2Model;
import org.broadleafcommerce.presentation.thymeleaf2.model.BroadleafThymeleaf2TemplateEvent;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelegatingThymeleaf2ModelModifierProcessor extends AbstractElementProcessor {

    private int precedence;
    protected BroadleafModelModifierProcessor processor;
    
    public DelegatingThymeleaf2ModelModifierProcessor(String elementName, BroadleafModelModifierProcessor processor, int precedence) {
        super(elementName);
        this.precedence = precedence;
        this.processor = processor;
    }
    
    @Override
    protected ProcessorResult processElement(Arguments arguments, Element element) {
        BroadleafTemplateContext context = new BroadleafThymeleaf2Context(arguments);
        String tagName = element.getNormalizedName();
        Map<String, Attribute> attributeMap = element.getAttributeMap();
        Map<String, String> tagAttributes = new HashMap<>();
        for (String key : attributeMap.keySet()) {
            tagAttributes.put(element.getAttributeOriginalNameFromNormalizedName(key), attributeMap.get(key).getValue());
        }
        BroadleafTemplateModelModifierDTO dto = processor.getInjectedModelAndTagAttributes(tagName, tagAttributes, context);
        if (dto == null) {
            dto = new BroadleafTemplateModelModifierDTO();
        }
        Map<String, String> newParams = dto.getFormParameters();
        if (newParams == null) {
            newParams = new HashMap<>();
        }
        Element newForm = element.cloneElementNodeWithNewName(element.getParent(), dto.getReplacementTagName() != null ? dto.getReplacementTagName() : "form", false);
        if (!MapUtils.isEmpty(dto.getFormLocalVariables())) {
            newForm.setAllNodeLocalVariables(dto.getFormLocalVariables());
        }
        newForm.setAttributes(newParams);
        if (dto.getModel() != null) {
            List<BroadleafTemplateElement> elementsToAdd = ((BroadleafThymeleaf2Model) dto.getModel()).getElements();
            for (BroadleafTemplateElement elem : elementsToAdd) {
                newForm.addChild(((BroadleafThymeleaf2TemplateEvent) elem).getNode());
            }
        }
        if (processor.reprocessModel()) {
            newForm.setRecomputeProcessorsImmediately(true);
        }
        element.getParent().insertAfter(element, newForm);
        element.getParent().removeChild(element);
        return ProcessorResult.OK;
    }

    @Override
    public int getPrecedence() {
        return this.precedence;
    }
}

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
package org.broadleafcommerce.common.web.dialect;

import org.apache.commons.collections.MapUtils;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContextImpl;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafElement;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafFormReplacementDTO;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafModelImpl;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafTemplateEvent;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelegatingBroadleafFormReplacementProcessor extends AbstractElementProcessor {

    private int precedence;
    protected BroadleafFormReplacementProcessor processor;
    
    public DelegatingBroadleafFormReplacementProcessor(String elementName, BroadleafFormReplacementProcessor processor, int precedence) {
        super(elementName);
        this.precedence = precedence;
        this.processor = processor;
    }
    
    @Override
    protected ProcessorResult processElement(Arguments arguments, Element element) {
        BroadleafThymeleafContext context = new BroadleafThymeleafContextImpl(arguments);
        String tagName = element.getNormalizedName();
        Map<String, Attribute> attributeMap = element.getAttributeMap();
        Map<String, String> tagAttributes = new HashMap<>();
        for (String key : attributeMap.keySet()) {
            tagAttributes.put(element.getAttributeOriginalNameFromNormalizedName(key), attributeMap.get(key).getValue());
        }
        BroadleafThymeleafFormReplacementDTO dto = processor.getInjectedModelAndFormAttributes(tagName, tagAttributes, context);
        if (dto == null) {
            dto = new BroadleafThymeleafFormReplacementDTO();
        }
        Map<String, String> newParams = dto.getFormParameters();
        if (newParams == null) {
            newParams = new HashMap<>();
        }
        Element newForm = element.cloneElementNodeWithNewName(element.getParent(), "form", false);
        if (!MapUtils.isEmpty(dto.getFormLocalVariables())) {
            newForm.setAllNodeLocalVariables(dto.getFormLocalVariables());
        }
        newForm.setAttributes(newParams);
        if (dto.getModel() != null) {
            List<BroadleafThymeleafElement> elementsToAdd = ((BroadleafThymeleafModelImpl) dto.getModel()).getElements();
            for (BroadleafThymeleafElement elem : elementsToAdd) {
                newForm.addChild(((BroadleafThymeleafTemplateEvent) elem).getNode());
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

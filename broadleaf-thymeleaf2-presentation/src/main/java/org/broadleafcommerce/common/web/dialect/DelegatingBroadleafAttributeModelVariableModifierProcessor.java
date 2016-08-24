/*
 * #%L
 * broadleaf-thymeleaf2-presentation
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
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;

import java.util.HashMap;
import java.util.Map;

public class DelegatingBroadleafAttributeModelVariableModifierProcessor extends AbstractAttrProcessor {

    protected int precedence;
    protected BroadleafAttributeModelVariableModifierProcessor processor;
    
    public DelegatingBroadleafAttributeModelVariableModifierProcessor(String attributeName, BroadleafAttributeModelVariableModifierProcessor processor, int precedence) {
        super(attributeName);
        this.precedence = precedence;
        this.processor = processor;
    }

    protected ProcessorResult processAttribute(final Arguments arguments, final Element element, final String attributeName) {
        BroadleafThymeleafContext context = new BroadleafThymeleafContextImpl(arguments);
        Map<String, Attribute> attributeMap = element.getAttributeMap();
        Map<String, String> tagAttributes = new HashMap<>();
        for (String key : attributeMap.keySet()) {
            tagAttributes.put(element.getAttributeOriginalNameFromNormalizedName(key), attributeMap.get(key).getValue());
        }
        Map<String, Object> newModelVariables = new HashMap<>();
        processor.populateModelVariables(element.getNormalizedName(), tagAttributes, attributeName, element.getAttributeValueFromNormalizedName(attributeName), newModelVariables, context);
        return ProcessorResult.setLocalVariables(newModelVariables);
    }
    
    @Override
    public int getPrecedence() {
        return this.precedence;
    }
}

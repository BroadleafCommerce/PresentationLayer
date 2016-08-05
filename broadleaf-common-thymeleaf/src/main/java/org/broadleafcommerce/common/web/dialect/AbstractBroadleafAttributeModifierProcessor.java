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

import org.broadleafcommerce.common.web.domain.BroadleafAttributeModifier;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContextImpl;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBroadleafAttributeModifierProcessor extends AbstractAttributeModifierAttrProcessor {

    private int precedence;

    protected AbstractBroadleafAttributeModifierProcessor(String name, boolean removeAttribute, int precedence) {
        super(name);
        this.precedence = precedence;
    }

    protected AbstractBroadleafAttributeModifierProcessor(String name, int precedence) {
        this(name, true, precedence);
    }

    protected AbstractBroadleafAttributeModifierProcessor(String name) {
        this(name, 1000);
    }

    @Override
    protected Map<String, String> getModifiedAttributeValues(Arguments arguments, Element element, String attributeName) {
        BroadleafThymeleafContext context = new BroadleafThymeleafContextImpl(arguments);
        String tagName = element.getNormalizedName();
        Map<String, Attribute> attributeMap = element.getAttributeMap();
        Map<String, String> tagAttributes = new HashMap<>();
        for (String key : attributeMap.keySet()) {
            tagAttributes.put(element.getAttributeOriginalNameFromNormalizedName(key), attributeMap.get(key).getValue());
        }
        BroadleafAttributeModifier modifications = getModifiedAttributes(tagName, tagAttributes, attributeName, element.getAttributeValue(attributeName), context);
        for (String key : modifications.getRemoved()) {
            element.removeAttribute(key);
        }
        return modifications.getAdded();
    }

    protected boolean useSingleQuotes() {
        return false;
    }

    @Override
    protected ModificationType getModificationType(Arguments arguments, Element element, String attributeName, String newAttributeName) {
        return ModificationType.SUBSTITUTION;
    }

    @Override
    protected boolean removeAttributeIfEmpty(Arguments arguments, Element element, String attributeName, String newAttributeName) {
        return true;
    }

    @Override
    protected boolean recomputeProcessorsAfterExecution(Arguments arguments, Element element, String attributeName) {
        return false;
    }

    protected abstract BroadleafAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, BroadleafThymeleafContext context);

    @Override
    public int getPrecedence() {
        return this.precedence;
    }
}

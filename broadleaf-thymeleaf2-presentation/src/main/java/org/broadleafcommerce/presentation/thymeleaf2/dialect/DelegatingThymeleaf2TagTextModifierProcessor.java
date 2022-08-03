/*-
 * #%L
 * broadleaf-thymeleaf2-presentation
 * %%
 * Copyright (C) 2009 - 2022 Broadleaf Commerce
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

import org.broadleafcommerce.presentation.dialect.BroadleafTagTextModifierProcessor;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.broadleafcommerce.presentation.thymeleaf2.model.BroadleafThymeleaf2Context;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;

import java.util.HashMap;
import java.util.Map;

public class DelegatingThymeleaf2TagTextModifierProcessor extends AbstractTextChildModifierAttrProcessor {

    private int precedence;
    protected BroadleafTagTextModifierProcessor processor;

    public DelegatingThymeleaf2TagTextModifierProcessor(String name, BroadleafTagTextModifierProcessor processor, int precedence) {
        super(name);
        this.precedence = precedence;
        this.processor = processor;
    }

    @Override
    protected String getText(Arguments arguments, Element element, String attributeName) {
        BroadleafTemplateContext context = new BroadleafThymeleaf2Context(arguments);
        String tagName = element.getNormalizedName();
        Map<String, Attribute> attributeMap = element.getAttributeMap();
        Map<String, String> tagAttributes = new HashMap<>();
        for (String key : attributeMap.keySet()) {
            tagAttributes.put(element.getAttributeOriginalNameFromNormalizedName(key), attributeMap.get(key).getValue());
        }
        return processor.getTagText(tagName, tagAttributes, attributeName, element.getAttributeValue(attributeName), context);
    }

    @Override
    public int getPrecedence() {
        return this.precedence;
    }

}

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

import org.broadleafcommerce.presentation.dialect.BroadleafTagReplacementProcessor;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.broadleafcommerce.presentation.model.BroadleafTemplateElement;
import org.broadleafcommerce.presentation.model.BroadleafTemplateModel;
import org.broadleafcommerce.presentation.thymeleaf2.model.BroadleafThymeleaf2Context;
import org.broadleafcommerce.presentation.thymeleaf2.model.BroadleafThymeleaf2Model;
import org.broadleafcommerce.presentation.thymeleaf2.model.BroadleafThymeleaf2TemplateEvent;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DelegatingThymeleaf2TagReplacementProcessor extends AbstractElementProcessor {

    private int precedence;
    protected BroadleafTagReplacementProcessor processor;

    public DelegatingThymeleaf2TagReplacementProcessor(String tagName, BroadleafTagReplacementProcessor processor, int precedence) {
        super(tagName);
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
        BroadleafTemplateModel blcModel = processor.getReplacementModel(tagName, tagAttributes, context);
        Node lastNode = null;
        if (blcModel != null) {
            List<BroadleafTemplateElement> elements = ((BroadleafThymeleaf2Model) blcModel).getElements();
            for (BroadleafTemplateElement elem : elements) {
                Node currentNode = ((BroadleafThymeleaf2TemplateEvent) elem).getNode();
                if (lastNode != null) {
                    element.getParent().insertAfter(lastNode, currentNode);
                } else {
                    element.getParent().insertAfter(element, currentNode);
                }
                lastNode = currentNode;
            }
            element.getParent().removeChild(element);
        }
        return ProcessorResult.OK;
    }

    @Override
    public int getPrecedence() {
        return this.precedence;
    }
}

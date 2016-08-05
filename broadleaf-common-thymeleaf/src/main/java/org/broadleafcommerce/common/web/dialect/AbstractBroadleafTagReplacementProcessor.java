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

import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContextImpl;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafElement;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafModel;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafModelImpl;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafTemplateEvent;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractBroadleafTagReplacementProcessor extends AbstractElementProcessor {

    private int precedence;

    public AbstractBroadleafTagReplacementProcessor(String tagName, int precedence) {
        super(tagName);
        this.precedence = precedence;
    }

    public AbstractBroadleafTagReplacementProcessor(String tagName) {
        this(tagName, 10000);
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
        BroadleafThymeleafModel blcModel = getReplacementModel(tagName, tagAttributes, context);
        Node lastNode = null;
        if (blcModel != null) {
            List<BroadleafThymeleafElement> elements = ((BroadleafThymeleafModelImpl) blcModel).getElements();
            for (BroadleafThymeleafElement elem : elements) {
                Node currentNode = ((BroadleafThymeleafTemplateEvent) elem).getNode();
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

    protected boolean replacementNeedsProcessing() {
        return false;
    }
    protected abstract BroadleafThymeleafModel getReplacementModel(String tagName, Map<String, String> tagAttributes, BroadleafThymeleafContext context);

    @Override
    public int getPrecedence() {
        return this.precedence;
    }
}

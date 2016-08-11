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

public abstract class AbstractAttributeModelVariableModifierProcessor extends AbstractAttrProcessor {

    private int precedence;
    
    public AbstractAttributeModelVariableModifierProcessor(String attributeName, boolean removeAttribute, int precedence) {
        super(attributeName);
        this.precedence = precedence;
    }
    
    protected AbstractAttributeModelVariableModifierProcessor(String attributeName, int precedence) {
        this(attributeName, true, precedence);
    }
    
    protected AbstractAttributeModelVariableModifierProcessor(String attributeName) {
        this(attributeName, 1000);
    }

    protected ProcessorResult processAttribute(final Arguments arguments, final Element element, final String attributeName) {
        BroadleafThymeleafContext context = new BroadleafThymeleafContextImpl(arguments);
        Map<String, Attribute> attributeMap = element.getAttributeMap();
        Map<String, String> tagAttributes = new HashMap<>();
        for (String key : attributeMap.keySet()) {
            tagAttributes.put(element.getAttributeOriginalNameFromNormalizedName(key), attributeMap.get(key).getValue());
        }
        Map<String, Object> newModelVariables = new HashMap<>();
        populateModelVariables(element.getNormalizedName(), tagAttributes, attributeName, element.getAttributeOriginalNameFromNormalizedName(attributeName), newModelVariables, context);
        return ProcessorResult.setLocalVariables(newModelVariables);
    }
    
    protected abstract void populateModelVariables(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, Map<String, Object> newLocalVariables, BroadleafThymeleafContext context);
    
    @Override
    public int getPrecedence() {
        return this.precedence;
    }


}

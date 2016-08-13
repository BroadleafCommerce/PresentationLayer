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
        processor.populateModelVariables(element.getNormalizedName(), tagAttributes, attributeName, element.getAttributeOriginalNameFromNormalizedName(attributeName), newModelVariables, context);
        return ProcessorResult.setLocalVariables(newModelVariables);
    }
    
    @Override
    public int getPrecedence() {
        return this.precedence;
    }
}

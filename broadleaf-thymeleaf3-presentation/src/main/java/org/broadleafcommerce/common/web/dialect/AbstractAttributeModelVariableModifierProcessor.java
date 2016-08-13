package org.broadleafcommerce.common.web.dialect;

import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContextImpl;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractAttributeModelVariableModifierProcessor extends AbstractAttributeTagProcessor {

    
    protected AbstractAttributeModelVariableModifierProcessor(String name, boolean removeAttribute, int precedence) {
        super(TemplateMode.HTML, "blc", null, false, name, true, precedence, removeAttribute);
    }

    protected AbstractAttributeModelVariableModifierProcessor(String name, int precedence) {
        this(name, true, precedence);
    }

    protected AbstractAttributeModelVariableModifierProcessor(String name) {
        this(name, 1000);
    }
    
    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        Map<String, String> attributes = tag.getAttributeMap();
        Map<String, Object> newModelVariables = new HashMap<>();
        BroadleafThymeleafContext blcContext = new BroadleafThymeleafContextImpl(context, structureHandler);
        populateModelVariables(tag.getElementCompleteName(), attributes, attributeName.getAttributeName(), attributeValue, newModelVariables, blcContext);
        
        for (Map.Entry<String, Object> entry : newModelVariables.entrySet()) {
            structureHandler.setLocalVariable(entry.getKey(), entry.getValue());
        }
        
        // Remove the tag from the DOM
        structureHandler.removeTags();
    }
    
    protected abstract void populateModelVariables(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, Map<String, Object> newLocalVariables, BroadleafThymeleafContext context);

}

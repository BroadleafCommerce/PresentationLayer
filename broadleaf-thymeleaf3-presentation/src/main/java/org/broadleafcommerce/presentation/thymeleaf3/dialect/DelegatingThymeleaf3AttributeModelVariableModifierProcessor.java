package org.broadleafcommerce.presentation.thymeleaf3.dialect;

import org.apache.commons.collections.MapUtils;
import org.broadleafcommerce.presentation.dialect.BroadleafVariableModifierAttrProcessor;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.broadleafcommerce.presentation.thymeleaf3.model.BroadleafThymeleaf3Context;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Map;


public class DelegatingThymeleaf3AttributeModelVariableModifierProcessor extends AbstractAttributeTagProcessor {

    protected BroadleafVariableModifierAttrProcessor processor;
    
    public DelegatingThymeleaf3AttributeModelVariableModifierProcessor(String attributeName, BroadleafVariableModifierAttrProcessor processor, int precedence) {
        super(TemplateMode.HTML, processor.getPrefix(), null, false, attributeName, true, precedence, true);
        this.processor = processor;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        Map<String, String> attributes = tag.getAttributeMap();
        BroadleafTemplateContext blcContext = new BroadleafThymeleaf3Context(context, structureHandler);
        Map<String, Object> newModelVariables = processor.populateModelVariables(tag.getElementCompleteName(), attributes, attributeName.getAttributeName(), attributeValue, blcContext);
        
        if (MapUtils.isNotEmpty(newModelVariables)) {
            for (Map.Entry<String, Object> entry : newModelVariables.entrySet()) {
                structureHandler.setLocalVariable(entry.getKey(), entry.getValue());
            }
        }
    }
    
}

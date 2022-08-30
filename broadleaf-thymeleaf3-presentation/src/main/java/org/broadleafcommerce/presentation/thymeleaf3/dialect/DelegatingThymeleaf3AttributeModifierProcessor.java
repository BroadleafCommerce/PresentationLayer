package org.broadleafcommerce.presentation.thymeleaf3.dialect;

import org.broadleafcommerce.presentation.dialect.BroadleafAttributeModifierProcessor;
import org.broadleafcommerce.presentation.model.BroadleafAttributeModifier;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.broadleafcommerce.presentation.thymeleaf3.model.BroadleafThymeleaf3Context;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Map;

public class DelegatingThymeleaf3AttributeModifierProcessor extends AbstractAttributeTagProcessor {

    protected BroadleafAttributeModifierProcessor processor;
    
    public DelegatingThymeleaf3AttributeModifierProcessor(String name, BroadleafAttributeModifierProcessor processor, int precedence) {
        super(TemplateMode.HTML, processor.getPrefix(), null, false, name, true, precedence, true);
        this.processor = processor;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        BroadleafTemplateContext blcContext = new BroadleafThymeleaf3Context(context, structureHandler);
        String tagName = tag.getElementCompleteName();
        Map<String, String> tagAttributes = tag.getAttributeMap();
        BroadleafAttributeModifier modifications = processor.getModifiedAttributes(tagName, tagAttributes, attributeName.getAttributeName(), attributeValue, blcContext);
        AttributeValueQuotes quotes = processor.useSingleQuotes() ? AttributeValueQuotes.SINGLE : AttributeValueQuotes.DOUBLE;
        Map<String, String> added = modifications.getAdded();
        for (String key : added.keySet()) {
            structureHandler.setAttribute(key, added.get(key), quotes);
        }
        for (String key : modifications.getRemoved()) {
            structureHandler.removeAttribute(key);
        }
    }
}

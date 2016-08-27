/*
 * #%L
 * broadleaf-thymeleaf3-presentation
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

import org.broadleafcommerce.common.web.domain.BroadleafTemplateContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleaf3ContextImpl;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashMap;
import java.util.Map;


public class DelegatingThymeleaf3AttributeModelVariableModifierProcessor extends AbstractAttributeTagProcessor {

    protected BroadleafAttributeModelVariableModifierProcessor processor;
    
    public DelegatingThymeleaf3AttributeModelVariableModifierProcessor(String attributeName, BroadleafAttributeModelVariableModifierProcessor processor, int precedence) {
        super(TemplateMode.HTML, processor.getPrefix().toString(), null, false, attributeName, true, precedence, true);
        this.processor = processor;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        Map<String, String> attributes = tag.getAttributeMap();
        Map<String, Object> newModelVariables = new HashMap<>();
        BroadleafTemplateContext blcContext = new BroadleafThymeleaf3ContextImpl(context, structureHandler);
        processor.populateModelVariables(tag.getElementCompleteName(), attributes, attributeName.getAttributeName(), attributeValue, newModelVariables, blcContext);
        
        for (Map.Entry<String, Object> entry : newModelVariables.entrySet()) {
            structureHandler.setLocalVariable(entry.getKey(), entry.getValue());
        }
        
    }
    
}

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
package org.broadleafcommerce.presentation.thymeleaf3.dialect;

import org.apache.commons.collections.MapUtils;
import org.broadleafcommerce.presentation.dialect.BroadleafModelModifierProcessor;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.broadleafcommerce.presentation.model.BroadleafTemplateModelModifierDTO;
import org.broadleafcommerce.presentation.thymeleaf3.model.BroadleafThymeleaf3Context;
import org.broadleafcommerce.presentation.thymeleaf3.model.BroadleafThymeleaf3Model;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashMap;
import java.util.Map;

public class DelegatingThymeleaf3ModelModifierProcessor extends AbstractElementModelProcessor {
    
    protected BroadleafModelModifierProcessor processor;
    
    public DelegatingThymeleaf3ModelModifierProcessor(String elementName, BroadleafModelModifierProcessor processor, int precedence) {
        super(TemplateMode.HTML, processor.getPrefix(), elementName, true, null, false, precedence);
        this.processor = processor;
    }
    
    @Override
    protected void doProcess(ITemplateContext context, IModel model, IElementModelStructureHandler structureHandler) {
        IProcessableElementTag rootTag = (IProcessableElementTag) model.get(0);
        String rootTagName = rootTag.getElementCompleteName();
        Map<String, String> rootTagAttributes = rootTag.getAttributeMap();
        BroadleafTemplateContext blcContext = new BroadleafThymeleaf3Context(context, structureHandler);
        BroadleafTemplateModelModifierDTO dto = processor.getInjectedModelAndTagAttributes(rootTagName, rootTagAttributes, blcContext);
        if (dto.getModel() != null) {
            model.insertModel(model.size() - 1, ((BroadleafThymeleaf3Model) dto.getModel()).getModel());
        }
        Map<String, String> newParams = dto.getFormParameters();
        if (newParams == null) {
            newParams = new HashMap<>();
        }
        String tagName = dto.getReplacementTagName() != null ? dto.getReplacementTagName() : "form";
        model.replace(0, context.getModelFactory().createOpenElementTag(tagName, dto.getFormParameters(), processor.useSingleQuotes() ? AttributeValueQuotes.SINGLE : AttributeValueQuotes.DOUBLE, false));
        model.replace(model.size() - 1, context.getModelFactory().createCloseElementTag(tagName));
        if (!MapUtils.isEmpty(dto.getFormLocalVariables())) {
            for (String key : dto.getFormLocalVariables().keySet()) {
                structureHandler.setLocalVariable(key, dto.getFormLocalVariables().get(key));
            }
        }
    }
}

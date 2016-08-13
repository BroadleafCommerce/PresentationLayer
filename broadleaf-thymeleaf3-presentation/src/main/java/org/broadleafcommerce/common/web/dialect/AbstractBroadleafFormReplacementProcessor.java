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

import org.apache.commons.collections.MapUtils;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContextImpl;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafFormReplacementDTO;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafModelImpl;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBroadleafFormReplacementProcessor extends AbstractElementModelProcessor {

    public AbstractBroadleafFormReplacementProcessor(String elementName, int precedence) {
        super(TemplateMode.HTML, "blc", elementName, true, null, false, precedence);
    }
    
    @Override
    protected void doProcess(ITemplateContext context, IModel model, IElementModelStructureHandler structureHandler) {
        IProcessableElementTag rootTag = (IProcessableElementTag) model.get(0);
        String rootTagName = rootTag.getElementCompleteName();
        Map<String, String> rootTagAttributes = rootTag.getAttributeMap();
        BroadleafThymeleafContext blcContext = new BroadleafThymeleafContextImpl(context, structureHandler);
        BroadleafThymeleafFormReplacementDTO dto = getInjectedModelAndFormAttributes(rootTagName, rootTagAttributes, blcContext);
        if (dto.getModel() != null) {
            model.insertModel(model.size() - 1, ((BroadleafThymeleafModelImpl) dto.getModel()).getModel());
        }
        Map<String, String> newParams = dto.getFormParameters();
        if (newParams == null) {
            newParams = new HashMap<>();
        }
        model.replace(0, context.getModelFactory().createOpenElementTag("form", dto.getFormParameters(), useSingleQuotes() ? AttributeValueQuotes.SINGLE : AttributeValueQuotes.DOUBLE, false));
        model.replace(model.size() - 1, context.getModelFactory().createCloseElementTag("form"));
        if (!MapUtils.isEmpty(dto.getFormLocalVariables())) {
            for (String key : dto.getFormLocalVariables().keySet()) {
                structureHandler.setLocalVariable(key, dto.getFormLocalVariables().get(key));
            }
        }
    }

    protected boolean useSingleQuotes() {
        return false;
    }

    protected boolean reprocessModel() {
        return false;
    }

    protected abstract BroadleafThymeleafFormReplacementDTO getInjectedModelAndFormAttributes(String rootTagName, Map<String, String> rootTagAttributes, BroadleafThymeleafContext context);

}

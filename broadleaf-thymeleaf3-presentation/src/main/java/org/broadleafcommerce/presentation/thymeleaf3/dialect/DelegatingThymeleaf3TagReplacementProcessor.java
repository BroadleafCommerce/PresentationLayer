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

import org.broadleafcommerce.presentation.dialect.BroadleafTagReplacementProcessor;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.broadleafcommerce.presentation.model.BroadleafTemplateModel;
import org.broadleafcommerce.presentation.thymeleaf3.model.BroadleafThymeleaf3Context;
import org.broadleafcommerce.presentation.thymeleaf3.model.BroadleafThymeleaf3Model;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.Map;


public class DelegatingThymeleaf3TagReplacementProcessor extends AbstractElementTagProcessor {

    protected BroadleafTagReplacementProcessor processor;
    
    public DelegatingThymeleaf3TagReplacementProcessor(String tagName, BroadleafTagReplacementProcessor processor, int precedence) {
        super(TemplateMode.HTML, processor.getPrefix(), tagName, true, null, false, precedence);
        this.processor = processor;
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        BroadleafTemplateContext blcContext = new BroadleafThymeleaf3Context(context, structureHandler);
        String tagName = tag.getElementCompleteName();
        Map<String, String> tagAttributes = tag.getAttributeMap();
        BroadleafTemplateModel blcModel = processor.getReplacementModel(tagName, tagAttributes, blcContext);
        if (blcModel != null) {
            structureHandler.replaceWith(((BroadleafThymeleaf3Model) blcModel).getModel(), processor.replacementNeedsProcessing());
        } else {
            structureHandler.removeTags();
        }
    }
}

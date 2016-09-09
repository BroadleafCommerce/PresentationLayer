/*
 * #%L
 * broadleaf-common-presentation
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
import org.broadleafcommerce.common.web.domain.BroadleafTemplateModel;
import org.broadleafcommerce.common.web.domain.BroadleafTemplateModelModifierDTO;

import java.util.Map;

/**
 * A tag processor that changes the tag to the tagName specified in the {@link BroadleafTemplateModelModifierDTO}, adds attributes to that tag, adds local variables to that tag, and adds a 
 * {@link BroadleafTemplateModel} as the last child of the tag
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafModelModifierProcessor extends BroadleafProcessor {
    
    /**
     * @return true if the attribute values on the tag should be surrounded by single quotes or false if they should be surrounded by double quotes
     */
    public boolean useSingleQuotes();
    
    /**
     * @return true if the model inserted as the last child should be reprocessed because it contains template logic or false if it shoudn't
     */
    public boolean reprocessModel();

    /**
     * @param rootTagName The name of the tag the event was triggered on
     * @param rootTagAttributes A map of String to String of all of the attributes on the tag
     * @param context The {@link BroadleafTemplateContext} that should be used to perform operations on the tag with
     * @return A {@link BroadleafTemplateModelModifierDTO} that has the parameters that should be added to the tag, the model variables
     * that should be added to the local model and the {@link BroadleafTemplateModel} that should be added as the last child of the tag
     */
    public BroadleafTemplateModelModifierDTO getInjectedModelAndTagAttributes(String rootTagName, Map<String, String> rootTagAttributes, BroadleafTemplateContext context);
}

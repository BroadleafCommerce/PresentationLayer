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

import java.util.List;
import java.util.Map;

/**
 * Tag processor that modifies the the global variable model if {@link #addToLocal()} is false and local if it's true
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafModelVariableModifierProcessor extends BroadleafProcessor {

    /**
     * @return true if the newModelVars should be added to the local model or false if they should be added to global
     */
    public boolean addToLocal();
    
    /**
     * @return The list of variables on the model that, if they exist, should have additional values added to the existing list instead of replacing their values
     */
    public List<String> getCollectionModelVariableNamesToAddTo();

    /**
     * @param tagName The name of the tag the event was triggered on
     * @param tagAttributes A map of String to String of all of the attributes on the tag
     * @param newModelVars A map of String to Object of the new variables that should be added to the model. They'll be add to the local model if {@link #addToLocal()} is true, else they'll be added to the global model
     * @param context The {@link BroadleafTemplateContext} that should be used to perform operations on the tag with
     */
    public void populateModelVariables(String tagName, Map<String, String> tagAttributes, Map<String, Object> newModelVars, BroadleafTemplateContext context);
}

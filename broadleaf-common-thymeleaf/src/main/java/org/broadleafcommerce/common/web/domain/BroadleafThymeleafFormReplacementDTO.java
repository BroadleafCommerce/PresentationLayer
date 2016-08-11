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
package org.broadleafcommerce.common.web.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Holder object for passing around a {@code BroadleafThymeleafModel} and a Map that represents the parameters
 * that should be used when creating a form. See {@code AbstractBroadleafFormReplacementProcessor}
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThymeleafFormReplacementDTO {

    protected BroadleafThymeleafModel model;
    protected Map<String, String> formParameters;
    protected Map<String, Object> formLocalVariables;

    public BroadleafThymeleafFormReplacementDTO(BroadleafThymeleafModel model, Map<String, String> formParameters) {
        this.model = model;
        this.formParameters = formParameters;
    }
    
    public BroadleafThymeleafFormReplacementDTO(BroadleafThymeleafModel model, Map<String, String> formParameters, Map<String, Object> formLocalVariables) {
        this.model = model;
        this.formParameters = formParameters;
        this.formLocalVariables = formLocalVariables;
    }

    public BroadleafThymeleafFormReplacementDTO() {
        model = null;
        formParameters = new HashMap<>();
        formLocalVariables = new HashMap<>();
    }

    public BroadleafThymeleafModel getModel() {
        return model;
    }

    public void setModel(BroadleafThymeleafModel model) {
        this.model = model;
    }

    public Map<String, String> getFormParameters() {
        return formParameters;
    }

    public void setFormParameters(Map<String, String> formParameters) {
        this.formParameters = formParameters;
    }

    public Map<String, Object> getFormLocalVariables() {
        return formLocalVariables;
    }

    public void setFormLocalVariables(Map<String, Object> formLocalVariables) {
        this.formLocalVariables = formLocalVariables;
    }
}

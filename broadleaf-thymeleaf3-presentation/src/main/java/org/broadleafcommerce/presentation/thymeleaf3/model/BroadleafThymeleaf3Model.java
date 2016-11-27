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
package org.broadleafcommerce.presentation.thymeleaf3.model;

import org.broadleafcommerce.presentation.model.BroadleafTemplateElement;
import org.broadleafcommerce.presentation.model.BroadleafTemplateModel;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.ITemplateEvent;

/**
 * A class used to encapsulate the underlying IModel for Thymeleaf 3. 
 * The model is modified using {@code BroadleafThymeleafTemplateEvent}s and then used to
 * modify the original model sent to the processor
 * 
 * Note that this is only for use inside of the Broadleaf common layer for Thymeleaf module
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThymeleaf3Model implements BroadleafTemplateModel {

    protected IModel model;

    public BroadleafThymeleaf3Model(IModel model) {
        this.model = model;
    }
    @Override
    public void addElement(BroadleafTemplateElement elem) {
        for (ITemplateEvent tag : ((BroadleafThymeleaf3TemplateEvent) elem).getAllTags()) {
            model.add(tag);
        }
    }

    public IModel getModel() {
        return model;
    }

}

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
package org.broadleafcommerce.presentation.thymeleaf2.model;

import org.broadleafcommerce.presentation.model.BroadleafTemplateElement;
import org.broadleafcommerce.presentation.model.BroadleafTemplateModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A class used to mimic Thymeleaf 3's model in order for processor code to be the same for Thymeleaf 2 and 3. 
 * The model is modified using {@code BroadleafThymeleaf2TemplateEvent}s and then used to
 * modify the original model sent to the processor
 * 
 * Note that this is only for use inside of the Broadleaf common layer for Thymeleaf module
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThymeleaf2Model implements BroadleafTemplateModel {

    // This is an ArrayList specifically so that the elements stay in order for when they're added to the actual DOM
    protected ArrayList<BroadleafTemplateElement> elements;

    public BroadleafThymeleaf2Model(ArrayList<BroadleafTemplateElement> elements) {
        this.elements = elements;
    }

    public BroadleafThymeleaf2Model() {
        this.elements = new ArrayList<>();
    }

    @Override
    public void addElement(BroadleafTemplateElement elem) {
        this.elements.add(elem);
    }

    public List<BroadleafTemplateElement> getElements() {
        return elements;
    }

}

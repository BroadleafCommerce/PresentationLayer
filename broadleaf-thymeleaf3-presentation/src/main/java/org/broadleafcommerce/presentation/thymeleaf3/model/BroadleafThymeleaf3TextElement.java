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

import org.thymeleaf.model.ITemplateEvent;
import org.thymeleaf.model.IText;

import java.util.ArrayList;

/**
 * Class that's used for encapsulating a Thymeleaf 3 text node
 * 
 * Note that this is only for use inside of the Broadleaf common layer for Thymeleaf module
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThymeleaf3TextElement implements BroadleafThymeleaf3TemplateEvent {

    protected IText text;

    public BroadleafThymeleaf3TextElement(IText text) {
        this.text = text;
    }

    @Override
    public ArrayList<ITemplateEvent> getAllTags() {
        ArrayList<ITemplateEvent> tags = new ArrayList<>();
        tags.add(text);
        return tags;
    }

}

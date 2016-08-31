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

/**
 * The base object for making a new piece of markup using {@link BroadleafTemplateElement}s.
 * see {@link BroadleafTempalateContext} on how to obtain one of these
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafTemplateModel {

    /**
     * Add a new child element to the model. Adding elements conserves order in which they were added
     */
    public void addElement(BroadleafTemplateElement elem);

}

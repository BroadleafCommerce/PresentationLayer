/*-
 * #%L
 * BroadleafCommerce Thymeleaf3 Presentation
 * %%
 * Copyright (C) 2009 - 2024 Broadleaf Commerce
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
import org.thymeleaf.model.ITemplateEvent;

import java.util.ArrayList;

/**
 * Interface that should be implemented for all {@code BroadleafThymeleafElement}s so that 
 * the module code can retrieve the underlying Thymeleaf 3 objects
 * 
 * Note that this is only for use inside of the Broadleaf common layer for Thymeleaf module
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafThymeleaf3TemplateEvent extends BroadleafTemplateElement {

    public ArrayList<ITemplateEvent> getAllTags();
}

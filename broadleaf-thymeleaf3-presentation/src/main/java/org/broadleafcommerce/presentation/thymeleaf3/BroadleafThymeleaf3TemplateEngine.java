/*
 * #%L
 * BroadleafCommerce Common Libraries
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
package org.broadleafcommerce.presentation.thymeleaf3;

import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Overrides the standard {@link SpringTemplateEngine} to allow for custom MessageResolvers to be added with
 * order values
 * Created on 1/13/17.
 * @author Chris Nail (ChrisNail)
 */
public class BroadleafThymeleaf3TemplateEngine extends SpringTemplateEngine {

    /**
     * Overrides the {@link SpringTemplateEngine#afterPropertiesSet()} so that the MessageResolvers set is not
     * cleared during setup
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        //Do nothing
    }

}

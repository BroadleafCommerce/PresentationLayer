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

/**
 * Used to specify which dialect a {@link BroadleafProcessor} should be in
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafDialectPrefix {
    /**
     * Specifies the {@link BroadleafProcessor} should use the "blc" prefix.
     * This means it'll be used in site, admin, or both depending on the context the bean is created
     */
    public static final String BLC = "blc";
    
    /**
     * Specifies the {@link BroadleafProcessor} should use the "blc_admin" prefix.
     * This means it'll be used in the admin context
     */
    public static final String BLC_ADMIN = "blc_admin";
    
}

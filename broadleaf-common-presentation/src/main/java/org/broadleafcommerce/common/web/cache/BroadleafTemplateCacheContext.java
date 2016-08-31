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
package org.broadleafcommerce.common.web.cache;


/**
 * Utility class to interact with the current template cache
 * The cache that is being interacted with is usually has entries where the key is the path and the value is the fully evaluated template
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafTemplateCacheContext<K, V>  {

    public V defaultGet(final K key);
    
    public void defaultPut(final K key, final V value);
    
    public static final String NOT_FOUND = "NOT_FOUND";
}


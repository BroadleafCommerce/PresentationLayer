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

import org.broadleafcommerce.common.web.dialect.AbstractBroadleafAttributeModifierProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holder class for passing around a {@link Map} of parameters that should be added to a tag and a {@link List} of parameters
 * that should be removed. See {@link AbstractBroadleafAttributeModifierProcessor}
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafAttributeModifier {
    
    protected Map<String, String> added;
    protected List<String> removed;

    public BroadleafAttributeModifier(Map<String, String> added, List<String> removed) {
        this.added = added;
        this.removed = removed;
    }

    public BroadleafAttributeModifier() {
        this(new HashMap<String, String>(), new ArrayList<String>());
    }

    public BroadleafAttributeModifier(Map<String, String> added) {
        this(added, new ArrayList<String>());
    }

    public BroadleafAttributeModifier(List<String> removed) {
        this(new HashMap<String, String>(), removed);
    }

    public Map<String, String> getAdded() {
        return added;
    }

    public void setAdded(Map<String, String> added) {
        this.added = added;
    }

    public List<String> getRemoved() {
        return removed;
    }

    public void setRemoved(List<String> removed) {
        this.removed = removed;
    }
}

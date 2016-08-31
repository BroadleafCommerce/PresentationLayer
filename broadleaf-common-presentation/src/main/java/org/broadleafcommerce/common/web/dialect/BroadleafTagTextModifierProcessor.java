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

import org.broadleafcommerce.common.web.domain.BroadleafTemplateContext;

import java.util.Map;

/**
 * An attribute processor that is used to modify (and replace) the text contents of the tag it's used on
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafTagTextModifierProcessor extends BroadleafProcessor {
    
    /**
     * @return true if the text needs to be processed by the templating framework else false 
     */
    public boolean textShouldBeProcessed();

    /**
     * 
     * @param tagName The name of the tag that the attribute was triggered on
     * @param tagAttributes A map of String to String of all of the attributes on the tag
     * @param attributeName The name of the attribute that triggered the event
     * @param attributeValue The value of the attribute that triggered the event
     * @param context The {@link BroadleafTemplateContext} that should be used to perform operations on the tag with
     * @return The text that should be inserted into the tag
     */
    public String getTagText(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, BroadleafTemplateContext context);
}

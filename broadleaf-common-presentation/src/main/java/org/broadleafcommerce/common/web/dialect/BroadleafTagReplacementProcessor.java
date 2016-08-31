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
import org.broadleafcommerce.common.web.domain.BroadleafTemplateModel;

import java.util.Map;

/**
 * A tag processor that's used to replace the tag that it was triggered on with a {@link BroadleafTemplateModel}
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafTagReplacementProcessor extends BroadleafProcessor {

    /**
     * @return true if the model returned has template logic that needs to reprocessed; else false
     */
    public boolean replacementNeedsProcessing();

    /**
     * 
     * @param tagName The name of the tag the event was triggered on
     * @param tagAttributes A map of String to String of all of the attributes on the tag
     * @param context The {@link BroadleafTemplateContext} that should be used to perform operations on the tag with
     * @return The {@link BroadleafTemplateModel} that should replace the tag that the event was triggered on
     */
    public BroadleafTemplateModel getReplacementModel(String tagName, Map<String, String> tagAttributes, BroadleafTemplateContext context);

}

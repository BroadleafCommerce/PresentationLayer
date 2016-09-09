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
package org.broadleafcommerce.common.web.resolver;

/**
 * Implementation of {@link AbstractBroadleafTempalateResolver} that is used if the template you are resolving are in the deployed
 * {@code webapp} folder. Generally the templates would be in {@code WEB-INF}
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThemeAwareTemplateResolver extends AbstractBroadleafTemplateResolver {

    @Override
    public BroadleafTemplateResolverType getResolverType() {
        return BroadleafTemplateResolverType.THEME_AWARE;
    }

}

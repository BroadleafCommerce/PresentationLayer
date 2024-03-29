/*-
 * #%L
 * broadleaf-thymeleaf3-presentation
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
package org.broadleafcommerce.presentation.thymeleaf3.resolver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.commons.io.FilenameUtils;
import org.thymeleaf.templateresource.ITemplateResource;

public class BroadleafThymeleaf3StringResourceResolver implements ITemplateResource {
    protected String path;

    public BroadleafThymeleaf3StringResourceResolver(String path) {

        this.path = path;
    }

    public String getDescription() {
        return "BL_STRING";
    }

    public String getBaseName() {
        return FilenameUtils.getBaseName(this.path);
    }

    public boolean exists() {
        return this.resolveResource() != null;
    }

    public Reader reader() {
        InputStream resourceStream = this.resolveResource();
        return resourceStream == null ? null : new BufferedReader(new InputStreamReader(resourceStream));
    }

    public ITemplateResource relative(String relativeLocation) {
        return null;
    }

    protected InputStream resolveResource() {
        return new ByteArrayInputStream(path.getBytes());
    }
}

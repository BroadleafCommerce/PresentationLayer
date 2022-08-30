package org.broadleafcommerce.presentation.thymeleaf3.resolver;

import org.thymeleaf.templateresource.ITemplateResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Jon Fleschler (jfleschler)
 */
public class BroadleafThymeleaf3ITemplateResource implements ITemplateResource {

    protected String resourceName;
    protected InputStream inputStream;

    public BroadleafThymeleaf3ITemplateResource(String resourceName, InputStream inputStream) {
        this.resourceName = resourceName;
        this.inputStream = inputStream;
    }

    @Override
    public String getDescription() {
        return "BL_CUSTOM";
    }

    @Override
    public String getBaseName() {
        return resourceName;
    }

    @Override
    public boolean exists() {
        return inputStream != null;
    }

    @Override
    public Reader reader() throws IOException {
        return inputStream == null ? null : new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public ITemplateResource relative(String s) {
        return null;
    }
}

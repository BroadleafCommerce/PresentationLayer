package org.broadleafcommerce.presentation.thymeleaf3.resolver;

import org.apache.commons.io.FilenameUtils;
import org.broadleafcommerce.common.extension.ExtensionResultHolder;
import org.broadleafcommerce.common.extension.ExtensionResultStatusType;
import org.broadleafcommerce.common.web.resource.BroadleafContextUtil;
import org.broadleafcommerce.core.web.resolver.DatabaseResourceResolverExtensionHandler;
import org.broadleafcommerce.core.web.resolver.DatabaseResourceResolverExtensionManager;
import org.thymeleaf.templateresource.ITemplateResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * An implementation of {@link IResourceResolver} that provides an extension point for retrieving
 * templates from the database.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class BroadleafThymeleaf3DatabaseResourceResolver implements ITemplateResource {

    protected BroadleafContextUtil blcContextUtil;
    protected DatabaseResourceResolverExtensionManager extensionManager;
    protected String path;
    
    public BroadleafThymeleaf3DatabaseResourceResolver(DatabaseResourceResolverExtensionManager extensionManager,
            BroadleafContextUtil blcContextUtil, String path) {
        this.extensionManager = extensionManager;
        this.blcContextUtil = blcContextUtil;
        this.path = path;
    }

    /* (non-Javadoc)
     * @see org.thymeleaf.templateresource.ITemplateResource#getDescription()
     */
    @Override
    public String getDescription() {
        return "BL_DATABASE";
    }

    /* (non-Javadoc)
     * @see org.thymeleaf.templateresource.ITemplateResource#getBaseName()
     */
    @Override
    public String getBaseName() {
        return FilenameUtils.getBaseName(path);
    }

    @Override
    public boolean exists() {
        return resolveResource() != null;
    }

    @Override
    public Reader reader() throws IOException {
        InputStream resourceStream = resolveResource();
        return resourceStream == null ? null : new BufferedReader(new InputStreamReader(resourceStream));
    }

    /* (non-Javadoc)
     * @see org.thymeleaf.templateresource.ITemplateResource#relative(java.lang.String)
     */
    @Override
    public ITemplateResource relative(String relativeLocation) {
        // Intentionally unimplemented
        return null;
    }
    
    protected InputStream resolveResource() {
        blcContextUtil.establishThinRequestContext();

        ExtensionResultHolder erh = new ExtensionResultHolder();
        ExtensionResultStatusType result = extensionManager.getProxy().resolveResource(erh, path);
        if (result ==  ExtensionResultStatusType.HANDLED) {
            return (InputStream) erh.getContextMap().get(DatabaseResourceResolverExtensionHandler.IS_KEY);
        }
        return null;
    }

}

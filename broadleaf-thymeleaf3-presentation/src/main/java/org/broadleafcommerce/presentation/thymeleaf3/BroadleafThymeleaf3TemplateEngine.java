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

package org.broadleafcommerce.presentation.thymeleaf3.model;

import org.broadleafcommerce.presentation.model.BroadleafTemplateElement;
import org.thymeleaf.model.ITemplateEvent;

import java.util.ArrayList;

/**
 * Interface that should be implemented for all {@code BroadleafThymeleafElement}s so that 
 * the module code can retrieve the underlying Thymeleaf 3 objects
 * 
 * Note that this is only for use inside of the Broadleaf common layer for Thymeleaf module
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafThymeleaf3TemplateEvent extends BroadleafTemplateElement {

    public ArrayList<ITemplateEvent> getAllTags();
}

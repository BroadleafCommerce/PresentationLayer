package org.broadleafcommerce.presentation.thymeleaf3.model;

import org.thymeleaf.model.IStandaloneElementTag;
import org.thymeleaf.model.ITemplateEvent;

import java.util.ArrayList;

/**
 * Class used to encapsulate the Thymeleaf 3 version of a standalone tag which is a void tag.
 * A void tag is any tag that does not have a body
 * 
 * Note that this is only for use inside of the Broadleaf common layer for Thymeleaf module
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThymeleaf3StandaloneElement implements BroadleafThymeleaf3TemplateEvent {

    protected IStandaloneElementTag standaloneTag;

    public BroadleafThymeleaf3StandaloneElement(IStandaloneElementTag standaloneTag) {
        this.standaloneTag = standaloneTag;
    }

    @Override
    public ArrayList<ITemplateEvent> getAllTags() {
        ArrayList<ITemplateEvent> tags = new ArrayList<>();
        tags.add(standaloneTag);
        return tags;
    }

}

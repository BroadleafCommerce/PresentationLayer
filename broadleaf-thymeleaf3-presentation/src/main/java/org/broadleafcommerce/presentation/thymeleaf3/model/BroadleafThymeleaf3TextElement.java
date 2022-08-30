package org.broadleafcommerce.presentation.thymeleaf3.model;

import org.thymeleaf.model.ITemplateEvent;
import org.thymeleaf.model.IText;

import java.util.ArrayList;

/**
 * Class that's used for encapsulating a Thymeleaf 3 text node
 * 
 * Note that this is only for use inside of the Broadleaf common layer for Thymeleaf module
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThymeleaf3TextElement implements BroadleafThymeleaf3TemplateEvent {

    protected IText text;

    public BroadleafThymeleaf3TextElement(IText text) {
        this.text = text;
    }

    @Override
    public ArrayList<ITemplateEvent> getAllTags() {
        ArrayList<ITemplateEvent> tags = new ArrayList<>();
        tags.add(text);
        return tags;
    }

}

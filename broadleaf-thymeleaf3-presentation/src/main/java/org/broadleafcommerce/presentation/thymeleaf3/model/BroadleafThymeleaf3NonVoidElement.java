package org.broadleafcommerce.presentation.thymeleaf3.model;

import org.broadleafcommerce.presentation.model.BroadleafTemplateElement;
import org.broadleafcommerce.presentation.model.BroadleafTemplateNonVoidElement;
import org.thymeleaf.model.ICloseElementTag;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.ITemplateEvent;

import java.util.ArrayList;

/**
 * Class used to encapsulate the Thymeleaf 3 version of a non void element which consists of an open tag, close tag, 
 * and children elements who can either be other non void elements, standalone elements, or text elements.
 * 
 * Note that this is only for use inside of the Broadleaf common layer for Thymeleaf module
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThymeleaf3NonVoidElement implements BroadleafTemplateNonVoidElement, BroadleafThymeleaf3TemplateEvent {

    protected IOpenElementTag openTag;
    protected ICloseElementTag closeTag;

    // This is an ArrayList specifically so that the elements stay in order for when they're added to the actual DOM 
    protected ArrayList<BroadleafTemplateElement> children;

    public BroadleafThymeleaf3NonVoidElement(IOpenElementTag openTag, ICloseElementTag closeTag) {
        this.openTag = openTag;
        this.closeTag = closeTag;
        this.children = new ArrayList<>();
    }

    @Override
    public void addChild(BroadleafTemplateElement child) {
        this.children.add(child);
    }

    @Override
    public ArrayList<ITemplateEvent> getAllTags() {
        ArrayList<ITemplateEvent> tags = new ArrayList<>();
        tags.add(this.openTag);
        for (BroadleafTemplateElement elem : this.children) {
            tags.addAll(((BroadleafThymeleaf3TemplateEvent) elem).getAllTags());
        }
        tags.add(this.closeTag);
        return tags;
    }

}

package org.broadleafcommerce.presentation.thymeleaf3.model;

import org.broadleafcommerce.presentation.model.BroadleafTemplateElement;
import org.broadleafcommerce.presentation.model.BroadleafTemplateModel;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.ITemplateEvent;

/**
 * A class used to encapsulate the underlying IModel for Thymeleaf 3. 
 * The model is modified using {@code BroadleafThymeleafTemplateEvent}s and then used to
 * modify the original model sent to the processor
 * 
 * Note that this is only for use inside of the Broadleaf common layer for Thymeleaf module
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public class BroadleafThymeleaf3Model implements BroadleafTemplateModel {

    protected IModel model;

    public BroadleafThymeleaf3Model(IModel model) {
        this.model = model;
    }
    @Override
    public void addElement(BroadleafTemplateElement elem) {
        for (ITemplateEvent tag : ((BroadleafThymeleaf3TemplateEvent) elem).getAllTags()) {
            model.add(tag);
        }
    }

    public IModel getModel() {
        return model;
    }

}

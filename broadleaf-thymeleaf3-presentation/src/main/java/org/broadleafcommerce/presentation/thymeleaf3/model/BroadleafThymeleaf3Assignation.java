package org.broadleafcommerce.presentation.thymeleaf3.model;

import org.broadleafcommerce.presentation.model.BroadleafAssignation;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.thymeleaf.standard.expression.Assignation;

public class BroadleafThymeleaf3Assignation implements BroadleafAssignation {

    protected Assignation assignation;

    public BroadleafThymeleaf3Assignation(Assignation assignation) {
        this.assignation = assignation;
    }

    @Override
    public Object parseLeft(BroadleafTemplateContext context) {
        return assignation.getLeft().execute(((BroadleafThymeleaf3Context) context).getThymeleafContext());
    }

    @Override
    public Object parseRight(BroadleafTemplateContext context) {
        return assignation.getRight().execute(((BroadleafThymeleaf3Context) context).getThymeleafContext());
    }

    @Override
    public String getLeftStringRepresentation(BroadleafTemplateContext context) {
        return assignation.getLeft().getStringRepresentation();
    }

    @Override
    public String getRightStringRepresentation(BroadleafTemplateContext context) {
        return assignation.getRight().getStringRepresentation();
    }

}

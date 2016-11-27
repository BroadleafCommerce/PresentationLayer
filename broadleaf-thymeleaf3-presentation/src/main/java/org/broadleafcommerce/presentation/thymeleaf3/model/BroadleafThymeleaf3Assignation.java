/*
 * #%L
 * broadleaf-common-thymeleaf
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
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

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
package org.broadleafcommerce.common.web.domain;

import org.thymeleaf.standard.expression.Assignation;

public class BroadleafAssignationImpl implements BroadleafAssignation {

    protected Assignation assignation;

    public BroadleafAssignationImpl(Assignation assignation) {
        this.assignation = assignation;
    }

    @Override
    public Object parseLeft(BroadleafThymeleafContext context) {
        return assignation.getLeft().execute(((BroadleafThymeleafContextImpl) context).getThymeleafContext());
    }

    @Override
    public Object parseRight(BroadleafThymeleafContext context) {
        return assignation.getRight().execute(((BroadleafThymeleafContextImpl) context).getThymeleafContext());
    }

    @Override
    public String getLeftStringRepresentation(BroadleafThymeleafContext context) {
        return assignation.getLeft().getStringRepresentation();
    }

    @Override
    public String getRightStringRepresentation(BroadleafThymeleafContext context) {
        return assignation.getRight().getStringRepresentation();
    }

}

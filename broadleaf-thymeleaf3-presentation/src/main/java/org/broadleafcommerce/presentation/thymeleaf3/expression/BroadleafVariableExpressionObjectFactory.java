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
package org.broadleafcommerce.presentation.thymeleaf3.expression;

import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

public class BroadleafVariableExpressionObjectFactory implements IExpressionObjectFactory {

    @Resource
    protected List<BroadleafVariableExpression> expressions = new ArrayList<>();

    @Override
    public Set<String> getAllExpressionObjectNames() {
        Set<String> expressionObjectNames = new HashSet<>();
        for (BroadleafVariableExpression expression : expressions) {
            expressionObjectNames.add(expression.getName());
        }
        return expressionObjectNames;
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        if (context instanceof IWebContext) {
            for (BroadleafVariableExpression expression : expressions) {
                if (expressionObjectName.equals(expression.getName())) {
                    return expression;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return true;
    }

}
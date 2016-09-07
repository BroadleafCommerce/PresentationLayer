/*
 * #%L
 * broadleaf-common-presentation
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
package org.broadleafcommerce.common.web.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;


public class TemplatingExistCondition implements Condition {
   
    public static final String[] CONDITION_CLASSES = new String[] {"org.thymeleaf.spring4.view.ThymeleafViewResolver"};
    
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        for (int i = 0; i < CONDITION_CLASSES.length; i++) {
            try {
                Class.forName(CONDITION_CLASSES[i]);
                return true;
            } catch (ClassNotFoundException e) {
            }
        }
        return false;
    }

}

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

import org.springframework.web.servlet.support.BindStatus;

import java.util.List;
import java.util.Map;

/**
 * Interface that lines out a set of utilities that should be able to be done during execution of a processor
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafThymeleafContext {

    public Object parseExpression(String value);

    public List<BroadleafAssignation> getAssignationSequence(String value, boolean allowParametersWithoutValue);

    public BroadleafThymeleafNonVoidElement createNonVoidElement(String tagName, Map<String, String> attributes, boolean useDoubleQuotes);

    public BroadleafThymeleafNonVoidElement createNonVoidElement(String tagName);

    public BroadleafThymeleafElement createStandaloneElement(String tagName, Map<String, String> attributes, boolean useDoubleQuotes);

    public BroadleafThymeleafElement createStandaloneElement(String tagName);

    public BroadleafThymeleafElement createTextElement(String text);

    public BroadleafThymeleafModel createModel();
    
    public void setNodeLocalVariable(BroadleafThymeleafElement element, String key, Object value);
    
    public void setNodeLocalVariables(BroadleafThymeleafElement element, Map<String, Object> variableMap);
    
    public Object getVariable(String name);
    
    public BindStatus getBindStatus(String attributeValue);
}
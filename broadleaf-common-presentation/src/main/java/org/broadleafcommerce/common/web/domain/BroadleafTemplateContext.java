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
 * Utility class to be used to do various functions on and around the template
 * 
 * @author Jay Aisenbrey (cja769)
 *
 */
public interface BroadleafTemplateContext {

    /**
     * @return The object that the {@code value} evaluates to using the underlying template language
     */
    public Object parseExpression(String value);

    /**
     * @param allowParametersWithoutValue true if assignations where the key has no value assigning to it is valid
     * @return The list of {@link BroadleafAssignation}s that were evaluated from {@code value}
     */
    public List<BroadleafAssignation> getAssignationSequence(String value, boolean allowParametersWithoutValue);

    /**
     * @param tagName The name of the tag that is being created
     * @param attributes The attributes that should be added to the tag
     * @param useDoubleQuotes True if the attribute values should be surrounded by double quotes and false if they should use single quotes
     */
    public BroadleafTemplateNonVoidElement createNonVoidElement(String tagName, Map<String, String> attributes, boolean useDoubleQuotes);

    /**
     * @param tagName The name of the tag that is being created
     */
    public BroadleafTemplateNonVoidElement createNonVoidElement(String tagName);

    /**
     * @param tagName The name of the tag that is being created
     * @param attributes The attributes that should be added to the tag
     * @param useDoubleQuotes True if the attribute values should be surrounded by double quotes and false if they should use single quotes
     */
    public BroadleafTemplateElement createStandaloneElement(String tagName, Map<String, String> attributes, boolean useDoubleQuotes);

    /**
     * @param tagName The name of the tag that is being created
     */
    public BroadleafTemplateElement createStandaloneElement(String tagName);

    /**
     * @param text The text that the {@link BroadleafTemplateElement} should represent
     */
    public BroadleafTemplateElement createTextElement(String text);

    /**
     * @return A new {@link BroadleafTemplateModel} that can have elements added to it to make a snippet of markup
     */
    public BroadleafTemplateModel createModel();
    
    /**
     * Sets a variable on the given {@code element} 
     */
    public void setNodeLocalVariable(BroadleafTemplateElement element, String key, Object value);
    
    /**
     * Sets a map of variables on the give {@code element}
     */
    public void setNodeLocalVariables(BroadleafTemplateElement element, Map<String, Object> variableMap);
    
    /**
     * @return The object on the current variable model whose key was {@code name}
     */
    public Object getVariable(String name);
    
    /**
     * @return Gets the current Spring {@link BindStatus} for {@code attributeValue}
     */
    public BindStatus getBindStatus(String attributeValue);
}
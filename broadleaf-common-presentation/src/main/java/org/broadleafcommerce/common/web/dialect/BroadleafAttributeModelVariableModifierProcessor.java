package org.broadleafcommerce.common.web.dialect;

import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;

import java.util.Map;

public interface BroadleafAttributeModelVariableModifierProcessor extends BroadleafProcessor {

    public void populateModelVariables(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, Map<String, Object> newLocalVariables, BroadleafThymeleafContext context);
}

package org.broadleafcommerce.common.web.dialect;

import org.broadleafcommerce.common.web.domain.BroadleafAttributeModifier;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;

import java.util.Map;

public interface BroadleafAttributeModifierProcessor extends BroadleafProcessor {

    public BroadleafAttributeModifier getModifiedAttributes(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, BroadleafThymeleafContext context);
    
    public boolean useSingleQuotes();
}

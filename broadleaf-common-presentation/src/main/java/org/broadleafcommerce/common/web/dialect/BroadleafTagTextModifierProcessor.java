package org.broadleafcommerce.common.web.dialect;

import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;

import java.util.Map;

public interface BroadleafTagTextModifierProcessor extends BroadleafProcessor {
    
    public boolean textShouldBeProcessed();

    public String getTagText(String tagName, Map<String, String> tagAttributes, String attributeName, String attributeValue, BroadleafThymeleafContext context);
}

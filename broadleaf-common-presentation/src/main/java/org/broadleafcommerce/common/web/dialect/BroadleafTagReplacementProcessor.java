package org.broadleafcommerce.common.web.dialect;

import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafModel;

import java.util.Map;

public interface BroadleafTagReplacementProcessor extends BroadleafProcessor {

    public boolean replacementNeedsProcessing();

    public BroadleafThymeleafModel getReplacementModel(String tagName, Map<String, String> tagAttributes, BroadleafThymeleafContext context);

}

package org.broadleafcommerce.common.web.dialect;

import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;
import org.broadleafcommerce.common.web.domain.BroadleafThymeleafFormReplacementDTO;

import java.util.Map;

public interface BroadleafFormReplacementProcessor extends BroadleafProcessor {

    public boolean useSingleQuotes();
    
    public boolean reprocessModel();

    public BroadleafThymeleafFormReplacementDTO getInjectedModelAndFormAttributes(String rootTagName, Map<String, String> rootTagAttributes, BroadleafThymeleafContext context);
}

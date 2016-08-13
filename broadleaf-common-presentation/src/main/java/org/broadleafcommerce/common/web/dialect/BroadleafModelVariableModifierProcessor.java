package org.broadleafcommerce.common.web.dialect;

import org.broadleafcommerce.common.web.domain.BroadleafThymeleafContext;

import java.util.List;
import java.util.Map;

public interface BroadleafModelVariableModifierProcessor extends BroadleafProcessor {

    public boolean addToLocal();
    
    /**
     * Returns the list of variables on the model that, if they exist, should have additional values added to the existing list instead of replacing their values
     */
    public List<String> getCollectionModelVariableNamesToAddTo();

    public void populateModelVariables(String tagName, Map<String, String> tagAttributes, Map<String, Object> newModelVars, BroadleafThymeleafContext context);
}

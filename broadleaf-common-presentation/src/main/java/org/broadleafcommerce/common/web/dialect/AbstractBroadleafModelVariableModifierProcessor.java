package org.broadleafcommerce.common.web.dialect;

import java.util.List;

public abstract class AbstractBroadleafModelVariableModifierProcessor implements BroadleafModelVariableModifierProcessor {

    @Override
    public boolean addToLocal() {
        return false;
    }
    
    @Override
    public int getPrecedence() {
        return BroadleafProcessor.DEFAULT_PRECEDENCE;
    }
    
    @Override
    public List<String> getCollectionModelVariableNamesToAddTo() {
        return null;
    }
}

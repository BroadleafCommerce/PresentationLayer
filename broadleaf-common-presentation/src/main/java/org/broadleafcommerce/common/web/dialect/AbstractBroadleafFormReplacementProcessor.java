package org.broadleafcommerce.common.web.dialect;

public abstract class AbstractBroadleafFormReplacementProcessor implements BroadleafFormReplacementProcessor {
    
    @Override
    public boolean useSingleQuotes() {
        return false;
    }
    
    @Override
    public boolean reprocessModel() {
        return false;
    }
    
    @Override
    public int getPrecedence() {
        return BroadleafProcessor.DEFAULT_PRECEDENCE;
    }
}

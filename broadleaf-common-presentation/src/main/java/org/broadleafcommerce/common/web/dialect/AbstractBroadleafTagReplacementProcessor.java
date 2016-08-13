package org.broadleafcommerce.common.web.dialect;

public abstract class AbstractBroadleafTagReplacementProcessor implements BroadleafTagReplacementProcessor {

    @Override
    public boolean replacementNeedsProcessing() {
        return false;
    }
    
    @Override
    public int getPrecedence() {
        return BroadleafProcessor.DEFAULT_PRECEDENCE;
    }

}

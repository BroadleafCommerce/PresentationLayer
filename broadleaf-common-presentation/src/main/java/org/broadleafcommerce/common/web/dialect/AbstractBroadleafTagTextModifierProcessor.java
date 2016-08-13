package org.broadleafcommerce.common.web.dialect;

public abstract class AbstractBroadleafTagTextModifierProcessor implements BroadleafTagTextModifierProcessor {

    @Override
    public boolean textShouldBeProcessed() {
        return false;
    }
    
    @Override
    public int getPrecedence() {
        return BroadleafProcessor.DEFAULT_PRECEDENCE;
    }

}

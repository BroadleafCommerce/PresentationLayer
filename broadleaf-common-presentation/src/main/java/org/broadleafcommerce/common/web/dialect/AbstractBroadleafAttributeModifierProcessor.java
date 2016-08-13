package org.broadleafcommerce.common.web.dialect;

public abstract class AbstractBroadleafAttributeModifierProcessor implements BroadleafAttributeModifierProcessor {

    @Override
    public boolean useSingleQuotes() {
        return false;
    }
    
    @Override
    public int getPrecedence() {
        return BroadleafProcessor.DEFAULT_PRECEDENCE;
    }
}

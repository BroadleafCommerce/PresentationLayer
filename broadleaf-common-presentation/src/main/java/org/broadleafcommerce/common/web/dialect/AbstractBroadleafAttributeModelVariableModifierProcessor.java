package org.broadleafcommerce.common.web.dialect;

public abstract class AbstractBroadleafAttributeModelVariableModifierProcessor implements BroadleafAttributeModelVariableModifierProcessor {

    @Override
    public int getPrecedence() {
        return BroadleafProcessor.DEFAULT_PRECEDENCE;
    }
}

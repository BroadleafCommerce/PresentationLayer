package org.broadleafcommerce.presentation.thymeleaf3.model;

import org.broadleafcommerce.presentation.model.BroadleafBindStatus;
import org.springframework.validation.Errors;
import org.thymeleaf.spring5.context.IThymeleafBindStatus;

/**
 * @author Jeff Fischer
 */
public class BroadleafThymeleaf3BindStatus implements BroadleafBindStatus {

    protected IThymeleafBindStatus bindStatus;

    public BroadleafThymeleaf3BindStatus(IThymeleafBindStatus bindStatus) {
        this.bindStatus = bindStatus;
    }

    @Override
    public String getPath() {
        return bindStatus.getPath();
    }

    @Override
    public String getExpression() {
        return bindStatus.getExpression();
    }

    @Override
    public Object getValue() {
        return bindStatus.getValue();
    }

    @Override
    public Class<?> getValueType() {
        return bindStatus.getValueType();
    }

    @Override
    public Object getActualValue() {
        return bindStatus.getActualValue();
    }

    @Override
    public String getDisplayValue() {
        return bindStatus.getDisplayValue();
    }

    @Override
    public boolean isError() {
        return bindStatus.isError();
    }

    @Override
    public String[] getErrorCodes() {
        return bindStatus.getErrorCodes();
    }

    @Override
    public String getErrorCode() {
        return bindStatus.getErrorCode();
    }

    @Override
    public String[] getErrorMessages() {
        return bindStatus.getErrorMessages();
    }

    @Override
    public String getErrorMessage() {
        return bindStatus.getErrorMessage();
    }

    @Override
    public String getErrorMessagesAsString(String delimiter) {
        return bindStatus.getErrorMessagesAsString(delimiter);
    }

    @Override
    public Errors getErrors() {
        return bindStatus.getErrors();
    }
}

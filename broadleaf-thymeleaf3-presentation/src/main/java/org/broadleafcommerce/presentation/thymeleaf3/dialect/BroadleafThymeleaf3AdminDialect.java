package org.broadleafcommerce.presentation.thymeleaf3.dialect;

import org.broadleafcommerce.presentation.dialect.BroadleafDialectPrefix;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

public class BroadleafThymeleaf3AdminDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    @Resource(name = "blVariableExpressionObjectFactory")
    protected IExpressionObjectFactory expressionObjectFactory;

    private Set<IProcessor> processors = new HashSet<>();

    public BroadleafThymeleaf3AdminDialect() {
        super("Broadleaf Admin Dialect", BroadleafDialectPrefix.BLC_ADMIN.toString(), SpringStandardDialect.PROCESSOR_PRECEDENCE);
    }

    public void setProcessors(Set<IProcessor> processors) {
        this.processors = processors;
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return processors;
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return expressionObjectFactory;
    }


}

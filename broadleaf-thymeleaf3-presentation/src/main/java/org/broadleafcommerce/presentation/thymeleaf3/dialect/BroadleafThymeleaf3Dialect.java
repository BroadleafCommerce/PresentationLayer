
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

public class BroadleafThymeleaf3Dialect extends AbstractProcessorDialect implements IExpressionObjectDialect {

    @Resource(name = "blVariableExpressionObjectFactory")
    protected IExpressionObjectFactory expressionObjectFactory;

    private Set<IProcessor> processors = new HashSet<>();

    public BroadleafThymeleaf3Dialect() {
        super("Broadleaf Common Dialect", BroadleafDialectPrefix.BLC.toString(), SpringStandardDialect.PROCESSOR_PRECEDENCE);
    }
    
    public BroadleafThymeleaf3Dialect(final String name, final String prefix, final int processorPrecedence) {
        super(name, prefix, processorPrecedence);
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

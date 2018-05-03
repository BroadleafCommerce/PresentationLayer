/*
 * #%L
 * BroadleafCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */

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

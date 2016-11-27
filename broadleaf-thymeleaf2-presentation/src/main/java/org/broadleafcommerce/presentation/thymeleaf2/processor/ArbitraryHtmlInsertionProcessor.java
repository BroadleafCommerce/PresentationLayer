/*
 * #%L
 * broadleaf-thymeleaf2-presentation
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
package org.broadleafcommerce.presentation.thymeleaf2.processor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.util.DOMUtils;

import java.io.Reader;
import java.io.StringReader;


public class ArbitraryHtmlInsertionProcessor extends AbstractAttrProcessor {

    public ArbitraryHtmlInsertionProcessor() {
        super("html");
    }

    @Override
    protected ProcessorResult processAttribute(Arguments arguments, Element element, String attributeName) {
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue(attributeName));
        Object result = expression.execute(arguments.getConfiguration(), arguments);
        if (result != null) {
            Reader reader = new StringReader(result.toString());
            Document doc = DOMUtils.getHtml5DOMFor(reader);
            doc.setProcessable(true);
            element.clearChildren();
            element.addChild(doc);
            element.removeAttribute(attributeName);
        }
        return ProcessorResult.OK;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

}
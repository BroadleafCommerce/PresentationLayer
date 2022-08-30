package org.broadleafcommerce.presentation.thymeleaf3.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class ArbitraryHtmlInsertionProcessor extends AbstractAttributeTagProcessor {

    public ArbitraryHtmlInsertionProcessor() {
        super(TemplateMode.HTML, "blc", null, false, "html", true, 100, true);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
        Object result = StandardExpressions.getExpressionParser(context.getConfiguration())
                .parseExpression(context, attributeValue)
                .execute(context);
        if (result != null) {
            structureHandler.setBody(context.getConfiguration().getTemplateManager()
                                        .parseString(context.getTemplateData(), result.toString(), 0, 0, getTemplateMode(), false), true);
        }
    }


}

package org.broadleafcommerce.common.config;

import org.broadleafcommerce.common.web.dialect.BLCDialect;
import org.broadleafcommerce.common.web.dialect.BroadleafAttributeModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafAttributeModifierProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafFormReplacementProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafTagReplacementProcessor;
import org.broadleafcommerce.common.web.dialect.BroadleafTagTextModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafAttributeModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafAttributeModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafFormReplacementProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafModelVariableModifierProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafTagReplacementProcessor;
import org.broadleafcommerce.common.web.dialect.DelegatingBroadleafTagTextModifierProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.processor.IProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
@ComponentScan
public class Thymeleaf2Config {
    
    @Bean
    public List<DelegatingBroadleafAttributeModelVariableModifierProcessor> getAttributeModelVariableModifiers(List<BroadleafAttributeModelVariableModifierProcessor> processors) {
        List<DelegatingBroadleafAttributeModelVariableModifierProcessor> results = new ArrayList<>();
        for (BroadleafAttributeModelVariableModifierProcessor processor : processors) {
            results.add(new DelegatingBroadleafAttributeModelVariableModifierProcessor(processor.getName(), processor, processor.getPrecedence()));
        }
        return results;
    }
    
    @Bean
    public List<DelegatingBroadleafAttributeModifierProcessor> getAttributeModifiers(List<BroadleafAttributeModifierProcessor> processors) {
        List<DelegatingBroadleafAttributeModifierProcessor> results = new ArrayList<>();
        for (BroadleafAttributeModifierProcessor processor : processors) {
            results.add(new DelegatingBroadleafAttributeModifierProcessor(processor.getName(), processor, processor.getPrecedence()));
        }
        return results;
    }
    
    @Bean
    public List<DelegatingBroadleafFormReplacementProcessor> getFormReplacers(List<BroadleafFormReplacementProcessor> processors) {
        List<DelegatingBroadleafFormReplacementProcessor> results = new ArrayList<>();
        for (BroadleafFormReplacementProcessor processor : processors) {
            results.add(new DelegatingBroadleafFormReplacementProcessor(processor.getName(), processor, processor.getPrecedence()));
        }
        return results;
    }
    
    @Bean
    public List<DelegatingBroadleafTagReplacementProcessor> getTagReplacers(List<BroadleafTagReplacementProcessor> processors) {
        List<DelegatingBroadleafTagReplacementProcessor> results = new ArrayList<>();
        for (BroadleafTagReplacementProcessor processor : processors) {
            results.add(new DelegatingBroadleafTagReplacementProcessor(processor.getName(), processor, processor.getPrecedence()));
        }
        return results;
    }
    
    @Bean
    public List<DelegatingBroadleafTagTextModifierProcessor> getTagTextModifier(List<BroadleafTagTextModifierProcessor> processors) {
        List<DelegatingBroadleafTagTextModifierProcessor> results = new ArrayList<>();
        for (BroadleafTagTextModifierProcessor processor : processors) {
            results.add(new DelegatingBroadleafTagTextModifierProcessor(processor.getName(), processor, processor.getPrecedence()));
        }
        return results;
    }
    
    @Bean
    public List<DelegatingBroadleafModelVariableModifierProcessor> getModelVariableModifier(List<BroadleafModelVariableModifierProcessor> processors) {
        List<DelegatingBroadleafModelVariableModifierProcessor> results = new ArrayList<>();
        for (BroadleafModelVariableModifierProcessor processor : processors) {
            results.add(new DelegatingBroadleafModelVariableModifierProcessor(processor.getName(), processor, processor.getPrecedence()));
        }
        return results;
    }
    
    @Bean(name = "blDialect")
    public BLCDialect getDialect(Set<IProcessor> processors) {
        BLCDialect dialect = new BLCDialect();
        dialect.setProcessors(processors);
        return dialect;
    }
}

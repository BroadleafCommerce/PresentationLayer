package org.broadleafcommerce.common.web.dialect;


public interface BroadleafProcessor {
    
    public String getName();
    
    public int getPrecedence();
    
    public static final int DEFAULT_PRECEDENCE = 1000;
}

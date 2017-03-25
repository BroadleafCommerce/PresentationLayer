/**
 * 
 */
package org.broadleafcommerce.presentation.thymeleaf2.config;

import org.broadleafcommerce.common.config.DefaultOrderFrameworkCommonClasspathPropertySource;

/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class Thymeleaf2Properties extends DefaultOrderFrameworkCommonClasspathPropertySource {

    @Override
    public String getClasspathFolder() {
        return "config/bc/thymeleaf/";
    }

}

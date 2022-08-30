/**
 * 
 */
package org.broadleafcommerce.presentation.thymeleaf3.config;

import org.broadleafcommerce.common.config.DefaultOrderFrameworkCommonClasspathPropertySource;

/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class Thymeleaf3Properties extends DefaultOrderFrameworkCommonClasspathPropertySource {

    @Override
    public String getClasspathFolder() {
        return "config/bc/thymeleaf/";
    }

}

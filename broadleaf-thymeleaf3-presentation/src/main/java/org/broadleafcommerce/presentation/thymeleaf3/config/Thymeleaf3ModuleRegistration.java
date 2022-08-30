/**
 * 
 */
package org.broadleafcommerce.presentation.thymeleaf3.config;

import org.broadleafcommerce.common.module.BroadleafModuleRegistration;


/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class Thymeleaf3ModuleRegistration implements BroadleafModuleRegistration {

    public static final String MODULE_NAME = "Broadleaf Thymeleaf 3 Support";
    
    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

}

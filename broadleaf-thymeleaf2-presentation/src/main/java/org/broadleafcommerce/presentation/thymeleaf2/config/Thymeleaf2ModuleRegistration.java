/**
 * 
 */
package org.broadleafcommerce.presentation.thymeleaf2.config;

import org.broadleafcommerce.common.module.BroadleafModuleRegistration;


/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
public class Thymeleaf2ModuleRegistration implements BroadleafModuleRegistration {

    public static final String MODULE_NAME = "Broadleaf Thymeleaf 2 Support";
    
    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

}

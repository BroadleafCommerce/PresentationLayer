# Using Thymeleaf 2 with Broadleaf 5.1+

### What changed
- All Thymeleaf specific code has been refactored into a Presentation module so now everything in Broadleaf uses classes that reside in `broadleaf-common-presentation`
  - Thymeleaf specific code (for both Thymeleaf 2 and 3) has been refactored into submodules `broadleaf-thymeleaf2-presentation` and `broadleaf-thymeleaf3-presentation` which resides in the Presentation module
- All boilerplate setup has been moved to the new `broadleaf-thymeleaf2-presentation` submodule
  - This includes default template resolvers, email template resolvers, template engines and view resolvers

### What's there to do for the migration

##### Adding the new POM dependency
- In order for Thymeleaf to be included in the project you'll need to add the following dependency to both the site pom and the admin pom
```
<dependency>
    <groupId>org.broadleafcommerce</groupId>
    <artifactId>broadleaf-thymeleaf2-presentation</artifactId>
    <version>1.1.0-GA</version>
</dependency>
```

##### Migrating variable expressions
- These should all work just as they did before. The custom variable expressions will continue to extend the same class as they did before and the bean definition (whether it was done via component scanning or manual creation via xml) will be the exact same. The only difference is we no longer add variable expressions the bean `blVariableExpressions`. Simply creating the bean is enough.

##### Migrating processors
- Technically we did change how processors are written in Broadleaf, but support was still left to work which how client's added custom processors before the refactoring of Thymeleaf. Therefore any processors that were added to the `blWebDialect`, `blEmailDialect`, or the `blAdminWebDialect` can simply be created as a bean through XML or component scanning instead of having to be added explicitly to the list of processors.
  - If it is a goal to upgrade to Thymeleaf 3 in the future then it is an option to use the common code in `broadleaf-common-presentation` in order for the processors to not care what version of Thymeleaf is being used or the processors could be upgraded to use the Thymeleaf 3 api directly

##### Migrating template resolvers
- Email template resolvers
  - By default there was an email template resolver set up in `core/src/main/resources/applicationContext-email.xml` which was moved into the Broadleaf Thymeleaf modules so unless there was customizations the definition can be removed.
    - The declaration looks like
    ```
    <bean id="blEmailTemplateResolver" class="org.thymeleaf.templateresolver.ClassLoaderTemplateResolver">
        <property name="prefix" value="emailTemplates/" />
        <property name="suffix" value=".html" />
        <property name="cacheable" value="${cache.page.templates}"/>
        <property name="cacheTTLMs" value="${cache.page.templates.ttl}" />
    </bean>
    ```
  - If there was any modifications made then the only change needed is the bean needs to be add to the List bean `blEmailTemplateResolvers` or simply create the bean and the template resolver will be added for all template engine.
- Normal template resolvers
  - By default there was a servlet template resolver set up but that was done in `broadleaf-framework-web` so if there wasn't a template resolver added in any client application context's then there's no change.
  - If there was any modifications made then the only change needed is to add that template resolver to the correct list of template resolvers which, by default, is `blWebTemplateResolvers` for site templates and `blAdminWebTemplateResolvers` for admin. As of version 1.1.0-GA of presentation layer it is also supported so that the template resolver just needs to be created as a bean and it'll be added to the `blEmailTemplateResolvers`, `blWebTemplateResolvers`, and `blAdminWebTemplateResolvers`. There likely won't be any side affects of including the resolver in all three template engines.

##### Migrating template engines
- Email template engines
  - By default there was a template engine setup up for emails but it just added the template resolver mentioned in the last section so now that the template resolver was moved we don't need the template engine. So if in `core/src/main/resources/applicationContext-email` the bean named `blEmailTemplateEngine` looked like
  ```
  <bean id="blEmailTemplateEngine" class="org.thymeleaf.spring4.SpringTemplateEngine">
      <property name="templateResolvers">
          <set>
              <ref bean="blEmailTemplateResolver" />
          </set>
      </property>
      <property name="dialects">
          <set>
              <bean class="org.thymeleaf.spring4.dialect.SpringStandardDialect" />
              <ref bean="blDialect" />
          </set>
      </property>
  </bean>
  ```
  then it can be removed. If there was any customizations then it should be left and no changes should be needed
- By default the template engines were setup in Broadleaf so nothing has changed there.

##### Migrating view resolvers
- By default there's a declaration of a site view resolver and admin view resolver declared in `site/src/main/webapp/WEB-INF/applicationContext-servlet.xml` and `admin/src/main/webapp/WEB-INF/applicationContext-servlet-admin.xml` respectively. These have been moved out as well into the Broadleaf Thymeleaf modules. If the declarations for these look like this
```
<bean class="org.broadleafcommerce.common.web.BroadleafThymeleafViewResolver">
    <property name="templateEngine" ref="blWebTemplateEngine" />
    <property name="order" value="1" />
    <property name="cache" value="${thymeleaf.view.resolver.cache}" />
    <property name="fullPageLayout" value="layout/fullPageLayout" />
    <property name="characterEncoding" value="UTF-8" />
</bean>
```
in site and like this
```
<bean class="org.broadleafcommerce.common.web.BroadleafThymeleafViewResolver">
    <property name="templateEngine" ref="blAdminWebTemplateEngine" />
    <property name="order" value="1" />
    <property name="cache" value="${thymeleaf.view.resolver.cache}" />
    <property name="characterEncoding" value="UTF-8" />
    <property name="fullPageLayout" value="layout/fullPageLayout" />
    <property name="layoutMap">
        <map>
            <entry key="login/" value="layout/loginLayout" />
            <entry key="views/" value="NONE" />
            <entry key="modules/modalContainer" value="NONE" />
        </map>
    </property>
</bean>
```
in admin then the declarations can be removed. If there were changes then you simply need to add `blThymeleafViewResolver` as the id for the site resolver and `blAdminThymeleafViewResolver` as the id for the admin resolver.

# Using Thymeleaf 3 with Broadleaf 5.1

### What changed
- All Thymeleaf specific code has been refactored into a Presentation module so now everything in Broadleaf uses classes that reside in `broadleaf-common-presentation`
  - Thymeleaf specific code (for both Thymeleaf 2 and 3) has been refactored into submodules `broadleaf-thymeleaf2-presentation` and `broadleaf-thymeleaf3-presentation` which resides in the Presentation module
- All boilerplate setup has been moved to the new `broadleaf-thymeleaf3-presentation` submodule
  - This includes default template resolvers, email template resolvers, template engines and view resolvers

### What's there to do for the migration

###### Adding the new POM dependency
- In order for Thymeleaf to be included in the project you'll need to add the following dependency to both the site pom and the admin pom
```
<dependency>
    <groupId>org.broadleafcommerce</groupId>
    <artifactId>broadleaf-thymeleaf3-presentation</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

##### Ensuring that the HTML in templates is valid HTML5
- By default any HTML template resolvers/templates are HTML5
- All HTML needs to be valid HTML5 because unlike Thymeleaf 2, Thymeleaf 3 doesn't make valid XML into valid HTML
  - In Thymeleaf 2 all templates had to be valid XML.
    - This forced tags such as `input`s, `meta`s, and `br`s to have close tags even though most people don't add them.
    - This allowed users to self close tags that shouldn't be self closed such as `span`s, and `div`s because Thymeleaf would add the necessary close tags which would make the HTML valid when it was sent to the client.
  - In Thymeleaf 3 templates don't particularly have to be valid XHTML, HTML, or XML
    - This allows for more leniency in writing HTML
    - This also allows for malformed HTML to be sent to the client
- To reiterate on the information above, HTML written in Thymeleaf 3 should be valid HTML because Thymeleaf will not force it to be valid and will not make it valid therefore malformed HTML will be sent to the client whose browser then has to interpret the malformed HTML. This isn't good because different browsers interpret malformed HTML differently and almost all of them will format it in a way that was not originally intended.

##### Migrating variable expressions
- These should all work just as they did before. The custom variable expressions will continue to extend the same class as they did before and the bean definition (whether it was done via component scanning or manual creation via xml) will be the exact same.

##### Migrating processors
- The Thymeleaf API has changed a good amount between Thymeleaf 2 and 3 therefore if any custom processors were made that targeted Thymeleaf 2 then they will have to be rewritten to either target the Thymeleaf 3 API or the new common API that Broadleaf has written in `broadleaf-common-presentation`.
  - Targeting Broadleaf common layer
    - There is a Usage.md file in `broadleaf-common-presentation` that explains how create processors that use the Broadleaf common layer for templating. The advantage of targeting this API is now the processors are Thymeleaf version agnostic, therefore there's a good chance that when a new version of Thymeleaf is released, Broadleaf can make a `broadleaf-thymeleaf4-presentation` submodule and the processors will still work if the aforementioned dependency is updated to use that new submodule.
  - Targeting Thymeleaf 3 API
    - With this approach you would change the extended class to a Thymeleaf 3 class, implement the needed functionality for the processor and then add that bean to either the `blDialectProcessors` bean or the `blAdminDialectProcessors` bean.


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
  - If there was any modifications made then the only change needed is the bean needs to be add to the List bean `blEmailTemplateResolvers`
- Normal template resolvers
  - By default there was a servlet template resolver set up but that was done in `broadleaf-framework-web` so if there wasn't a template resolver added in any client application context's then there's no change.
  - If there was any modifications made then the only change needed is to add that template resolver to the correct list of template resolvers which, by default, is `blWebTemplateResolvers` for site templates and `blAdminWebTemplateResolvers` for admin

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

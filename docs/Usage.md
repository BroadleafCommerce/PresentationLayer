# Usage Docs

### Definitions
 - Variable Expressions
    - Java classes that implement a `getName` method which is used as an identifier and implement an arbitrary number of methods. Using the Spring expression language (SpEL) the methods are called anywhere in the templates and the result can be used throughout the templates.
 - Processors
    - Java classes that can modify the HTML in some way
    - These can be tag processors or attribute processors
      - Example of tag processor; `blc:form`
        - `<blc:form method="POST" action="/someurl"></blc:form>`
      - Example of attribute processor; `blc:price`
        - `<span blc:price="${amount}"></span>`
 - Template Resolver
    - The way a template language resolves templates
 - Dialects
    - Holds all of the variable expressions and processors
 - Template Engine
    - Contains and uses all template resolvers and dialects to process templates
 - View Resolver
    - Spring's way of knowing what to use to resolve a template path
      - By default we tell Spring to use Thymeleaf who then uses Template resolvers to decide how to resolve the template path. Then uses the built in processors that are in Thymeleaf along with all of the dialects' processors and variable expressions to evaluate the template file that resolved.

### Creating new variable expressions
1. Create class that implements BroadleafVariableExpression
2. Implement `getName`
  - This is how you'll scope when your variable expression will be used
3. Either annotate the class with `@Component` or create the bean in XML
4. Add the bean to the using
```
<bean id="blVariableExpressions" class="org.springframework.beans.factory.config.ListFactoryBean">
    <property name="sourceList">
        <list>
            <ref bean="yourVariableExpressionBean" />
        </list>
    </property>
</bean>
```

### Using variable expressions
1. Given a new variable expression whose `getName` returns `client` and has a method called `doStuff` that takes a parameter that's on the model named `thing`
2. `<span th:text="${#client.doStuff(thing)}"></span>`
3. This will evaluate the method `doStuff` with whatever `thing` is mapped to on the model and the result will appear inside the span

### Creating a new processor
1. Create a new class that extends either:
 - `AbstractBroadleafAttributeModelVariableModifier`
    - Using a custom attribute on any tag, add local variables to the model for the scope of that tag
        - It is generally better to create a variable expression and set that result to the local scope using the `th:with` attribute
 - `AbstractBroadleafAttributeModifierProcessor`
    - Using a custom attribute on any, tag add and/or remove attributes to/from that tag
 - `AbstractBroadleafFormReplacementProcessor`
    - Using a custom tag, create a custom model using the Broadleaf common layer template domain. This model gets inserted as the last child of this tag. Also set local variables for the form
      - Refer to the next section on how to create a model
 - `AbstractBroadleafModelVariableModifierProcessor`
    - Using a custom tag, add variables to the global or local model (adding to global will be deprecated)
      - It is generally better to create a variable expression and set that result to the local scope using the `th:with` attribute
 - `AbstractBroadleafTagReplacementProcessor`
    - Using a custom tag, replace that tag with (and it's contents) with a new model
       - Refer to the next section on how to create a model
 - `AbstractBroadleafTagTextModifierProcessor`
    - Using a custom attribute on any tag, replace contents of tag with text
      - It is generally better to create a variable expression and set the internal using `th:text` or `th:utext`
2. Implement all required methods and override all methods defined in the Abstract class that are needed to be different
3. Either annotate the class with `@Component` or create the bean in XML
4. No need to do anything else because the processor will automatically picked up
 - Note that if this is only suppose to run in admin then override the `getPrefix` method and return `BroadleafDialectPrefix.BLC_ADMIN`
 - Also note that depending on if the processor is an attribute processor or tag processor usage will vary

### Creating a model using the Broadleaf common template domain
1. Create a `BroadleafTemplateModel` using the `BroadleafTemplateContext` (usually named `context`) by doing `context.createModel`
2. Create elements that will go in the model by doing:
 - `context.createNonVoidElement`
    - Creates a normal element such as a `div`, `span`, or `a` tag
 - `context.createStandaloneElement`
    - Creates a self closing element such as a `br`, `input`, or `meta` tag
 - `context.createTextElement`
    - Create a text element using a given string
      - This is good for creating Javascript
3. Example - Create a model that has an input and inline javascript that adds an onclick listener
```
Map<String, String> attributes = new HashMap<>();
attributes.put("type", "button");
attributes("style", "color:green");
BroadleafTemplateModel model = context.createModel();
BroadleafTemplateNonVoidElement div = context.createNonVoidElement("div");
// Create input tag with attributes and use double quotes when adding the attributes
BroadleafTemplateElement input = context.createStandaloneElement("input", attributes, true);
BroadleafTemplateNonVoidElement script = context.createNonVoidElement("script");
BroadleafTemplateElement js = context.createTextElement("$('input').click() {\nalert('hi');\n}");
// Put the text inside the js block
script.addChild(js);
div.addChild(input);
div.addChild(script);
model.addElement(div);
```
The result will look like
```
  <div>
      <input type="button" style="color:green" />
      <script>
        $('input').click() {
          alert('hi');
        }
      </script
  </div>
```

### Adding a Template Resolver
1. Create a bean of class type:
  - `BroadleafClasspathTemplateResolver`
    - Used if the templates are on the classpath but not in `WEB-INF`
      - Example: `src/main/resources/module_style/templates`
  - `BroadleafDatabaseTemplateResolver`
    - Used if the templates are in the database
      - Example: CMS uses this since the templates live in the database
  - `BroadleafServletTemplateResolver`
    - Used if the templates are in `WEB-INF`
      - Example: Generally, if not exclusively, used to resolve client templates.
2. Set a prefix
  - This is the base path to prepend to the beginning of the returned template string. Must end in a `/`
    - Example: The default for client's servlet resolver is `/WEB-INF/`
3. Set a template folder
  - The folder that is, at some level, a child of the prefix. All files in this folder are template. This is used here instead of just adding it to the prefix because of the Theme module. Depending on the theme the folder between `prefix` and `templateFolder` will change. Must end in a `/`
    - Example: The default is `templates/`
      - Property name is `theme.templates.folder`
4. Set a suffix
  - The file extension to be appended to the end of the file named
    - Example: The default is `.html`
5. Set the cacheable flag
  - Determines if this template can be cached
  - Defaults to true in common-shared but is usually set to false in development
    - Property name is `cache.page.templates`
6. Set the cache time to live
  - Determines how long the template can live in cache if cacheable is true
  - This is in milliseconds and if null will be evicted by least recently used when room in the cache is needed
   - No default is set but the property name is `cache.page.templates.ttl`
7. Set the `emailResolver` flag if it resolves email specific templates
  - This is used because there's a separate template engine for email reosolvers
8. Set optional parameters
  - Character encoding - default is `UTF-8` but a different encoding can be used
  - Order - default is 1000 but if your resolver needs to be checked before a different resolver you'll need to set this to a lower number
    - Resolver run from smallest order to largest Order
  - Template mode - default is HTML5 but other options are listed in `BroadleafTemplateMode`
9. Note that by creating a bean with classes mentioned in 1. the bean will automatically be picked up and used in other submodules of ModulePresentation

### Info about Template Engines
- Template Engines in the context referred to in this doc are a Thymeleaf specific concept. There is pretty much no reason why additional engines would need to be created in any modules.
  - The only exception would be if a client has specific needs to alter the template engine setup in the submodules of ModulePresentation.
    - Examples : adding/removing message resolvers, changing/removing the default broadleaf cache manager, or adding/removing dialects.
    - For a reference on how the template engine is setup, they definitions are in the site and admin application contexts in the thymeleaf2-presentation and thymeleaf3-presentation submodules which, outside of common, are the only two implementations of the common presentation layer

### Info about the default view resolver
- There's a default view resolver setup using Thymeleaf in both site and admin for both thymeleaf2-presentation and thymeleaf3-presentation.
- The beans have ids `blThymeleafViewResolver` for the site and `blAdminThymeleafViewResolver` for the admin

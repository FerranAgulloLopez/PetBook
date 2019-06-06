package service.main.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import service.main.RestApiController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource("classpath:swagger.properties")
@ComponentScan(basePackageClasses = RestApiController.class)
@EnableSwagger2
public class SwaggerConfig {

    private static final String	LICENSE_TEXT	    = "Apache 2.0 License";
    private static final String	title		    = "Rest API Controller";
    private static final String	description	    = "";

    /**
     * API Documentation Generation.
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()))
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("service.main")).paths(PathSelectors.any())
                .build().tags(new Tag("Rest API Controller", ""));
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^((?!RegisterUser|ConfirmLogin).)*$")).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Bearer",
                authorizationScopes));
    }
    /**
     * Information that appear in the API Documentation Head.
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(title).description(description).license(LICENSE_TEXT)
                .contact(new Contact("", "", ""))
                .build();
    }
}
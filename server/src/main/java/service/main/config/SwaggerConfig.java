package service.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import service.main.RestApiController;

@Configuration
@PropertySource("classpath:swagger.properties")
@ComponentScan(basePackageClasses = RestApiController.class)
@EnableSwagger2
public class SwaggerConfig {

    private static final String	LICENSE_TEXT	    = "License";
    private static final String	title		    = "Rest API Controller";
    private static final String	description	    = "Just testing";

    /**
     * API Documentation Generation.
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("service.main")).paths(PathSelectors.any())
                .build().tags(new Tag("Rest API Controller", "API related to something"));
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
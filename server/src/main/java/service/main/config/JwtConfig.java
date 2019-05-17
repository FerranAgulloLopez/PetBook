package service.main.config;

public class JwtConfig {
    //@Value("${security.jwt.uri:/ServerRESTAPI/RegisterUser}")
    private String RegisterUri = "/ServerRESTAPI/RegisterUser";

    private String LoginUri = "/ServerRESTAPI/ConfirmLogin";

    //@Value("${security.jwt.header:Authorization}")
    private String header = "Authorization";

    //@Value("${security.jwt.prefix:Bearer }")
    private String prefix= "Bearer ";

    //@Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration = 24*60*60;

    //@Value("${security.jwt.secret:JwtSecretKey}")
    private String secret = "temporaryKEY"; //TODO change this in the future !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    private String swaggerUrl = "/swagger-ui.html";

    public String getRegisterUri() {
        return RegisterUri;
    }

    public String getLoginUri() {
        return LoginUri;
    }

    public String getHeader() {
        return header;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getExpiration() {
        return expiration;
    }

    public String getSecret() {
        return secret;
    }

    public String getSwaggerUrl() {
        return swaggerUrl;
    }
}

package service.main.config;

public class JwtConfig {

    private String RegisterUri = "/ServerRESTAPI/RegisterUser";

    private String LoginUri = "/ServerRESTAPI/ConfirmLogin";

    private String header = "Authorization";

    private String prefix= "Bearer ";

    private int expiration = 24*60*60;

    private String secret = "temporaryKEY";

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

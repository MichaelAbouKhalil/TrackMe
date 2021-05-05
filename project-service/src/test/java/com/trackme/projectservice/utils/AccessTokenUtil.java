package com.trackme.projectservice.utils;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AccessTokenUtil {

    private static final String OAUTH_API = "http://localhost:8080/auth-service/oauth/token";

    public String obtainAccessToken(String username, String password) throws Exception {

        ResourceOwnerPasswordResourceDetails resource = buildResource(username, password);

        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext(atr));

        OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();

        return accessToken.getValue();

    }

    private ResourceOwnerPasswordResourceDetails buildResource(String username, String password) {
        ResourceOwnerPasswordResourceDetails resource =
                new ResourceOwnerPasswordResourceDetails();

        resource.setAccessTokenUri(OAUTH_API);
        resource.setClientId("trackme-webapp");
        resource.setClientSecret("secret-trackme");
        resource.setGrantType("password");
        resource.setScope(Arrays.asList("webclient"));
        resource.setUsername(username);
        resource.setPassword(password);

        return resource;
    }
}

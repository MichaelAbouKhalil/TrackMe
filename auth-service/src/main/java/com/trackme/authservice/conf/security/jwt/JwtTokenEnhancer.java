package com.trackme.authservice.conf.security.jwt;

import com.trackme.models.security.UserDetail;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class JwtTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();

        /**
         * If we want to add additional info to the token
         */
        if(oAuth2Authentication.getPrincipal() != null &&
                oAuth2Authentication.getPrincipal() instanceof UserDetail){
            // Not used at the moment vvv
            UserDetail userDetail = (UserDetail) oAuth2Authentication.getPrincipal();
            additionalInfo.put("userId", userDetail.getUserId());
            additionalInfo.put("userEmail", userDetail.getEmail());
        }

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        return oAuth2AccessToken;
    }
}

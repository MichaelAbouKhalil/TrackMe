package com.trackme.authservice.conf.security.jwt;

import com.trackme.models.constants.JwtFields;
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
            UserDetail userDetail = (UserDetail) oAuth2Authentication.getPrincipal();
            additionalInfo.put(JwtFields.JWT_USER_ID, userDetail.getUserId());
            additionalInfo.put(JwtFields.JWT_EMAIL, userDetail.getEmail());
            additionalInfo.put(JwtFields.JWT_ORG_ID, userDetail.getOrgId());
        }

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        return oAuth2AccessToken;
    }
}

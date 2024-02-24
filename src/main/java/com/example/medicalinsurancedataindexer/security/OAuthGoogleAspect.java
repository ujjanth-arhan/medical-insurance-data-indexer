package com.example.medicalinsurancedataindexer.security;

import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Aspect
@Component
public class OAuthGoogleAspect {

    @Autowired
    private Environment environment;

    @Before("@annotation(Authorized)")
    public boolean validateAccessToken(String accessToken) {
        try {
            if (accessToken == null) {
                throw new AccessTokenException("Access token is required");
            }

            if (accessToken.startsWith("Bearer ")) {
                accessToken = accessToken.substring(7);
            }

            URL url = new URL("https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + accessToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println("Token Info Response: " + response.toString());
        } catch (Exception e) {
            throw new AccessTokenException("There was an error validating the access token: " + e.getMessage());
        }

        return true;
    }
}

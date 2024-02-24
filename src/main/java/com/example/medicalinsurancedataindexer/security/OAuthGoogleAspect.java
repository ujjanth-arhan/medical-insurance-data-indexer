package com.example.medicalinsurancedataindexer.security;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

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

    private HttpServletRequest findHttpServletRequest(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        return null;
    }

    @Around("@annotation(oAuthGoogle)")
    public void validateAccessToken(ProceedingJoinPoint joinPoint, OAuthGoogle oAuthGoogle) {
        try {
            HttpServletRequest request = findHttpServletRequest(joinPoint.getArgs());
            String accessToken = request.getHeader("Authorization");
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

    }
}

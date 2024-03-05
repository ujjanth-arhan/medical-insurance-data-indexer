package com.example.medicalinsurancedataindexer.security;

import com.example.medicalinsurancedataindexer.plans.PlanController;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthGoogleAspect.class);

    private HttpServletRequest findHttpServletRequest(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        return null;
    }

    @Around("@annotation(oAuthGoogle)")
    public Object validateAccessToken(ProceedingJoinPoint joinPoint, OAuthGoogle oAuthGoogle) {
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

            LOGGER.trace("OAuth Verification: " + response);
            return joinPoint.proceed();
        } catch (Exception e) {
            throw new AccessTokenException("There was an error validating the access token: " + e.getMessage());
        } catch (Throwable e) {
            throw new AccessTokenException("Join Point exception: " + e.getMessage());
        }
    }
}

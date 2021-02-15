package gov.faa.notam.developerportal.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Slf4j
public class ReCaptcha {
    private static final String RECAPTCHA_API_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Value("${recaptcha.secret.key}")
    private String reCaptchaSecretKey;

    public boolean verifyResponse(String response) {
        try {
            String params = "secret="
                    + URLEncoder.encode(reCaptchaSecretKey, StandardCharsets.UTF_8)
                    + "&response="
                    + URLEncoder.encode(response, StandardCharsets.UTF_8);
            URL url = new URL(RECAPTCHA_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            OutputStream out = connection.getOutputStream();
            out.write(params.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();

            InputStream in = connection.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode result = objectMapper.readTree(in);
            in.close();

            return result.get("success").asBoolean();
        } catch (IOException e) {
            log.error("Failed to verify recaptcha response.", e);
        }
        return false;
    }
}

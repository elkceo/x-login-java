package com.x.login.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.x.login.domain.AccessTokenResponse;
import com.x.login.domain.UserProfile;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class XAuthService {

    @Value("${x.api.key}")
    private String consumerKey;

    @Value("${x.api.secret}")
    private String consumerSecret;

    @Value("${x.callback.url}")
    private String callbackUrl;

    private OAuthProvider provider;

    @PostConstruct
    public void init() {
        // Initialize the provider after the properties have been set.
        provider = new CommonsHttpOAuthProvider(
                "https://api.x.com/oauth/request_token",
                "https://api.x.com/oauth/access_token",
                "https://api.x.com/oauth/authorize"
        );
    }

    public String getAuthUrl() throws Exception {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        return provider.retrieveRequestToken(consumer, callbackUrl);
    }

    public AccessTokenResponse getAccessToken(String oauthToken, String oauthVerifier) throws Exception {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(oauthToken, oauthToken); // Set both token & secret
        provider.retrieveAccessToken(consumer, oauthVerifier);
        return new AccessTokenResponse(consumer.getToken(), consumer.getTokenSecret());
    }

    public UserProfile getUserProfile(AccessTokenResponse accessToken) throws Exception {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken.getToken(), accessToken.getTokenSecret());

        String url = "https://api.x.com/2/users/me?user.fields=profile_image_url,description";
        HttpGet request = new HttpGet(url);

        consumer.sign(request);

        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new Exception("Failed to fetch user profile. HTTP error code: " + statusCode);
        }

        String responseBody = EntityUtils.toString(response.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        JsonNode data = root.path("data");
        UserProfile profile = mapper.treeToValue(data, UserProfile.class);
        log.info("Retrieved user profile: {}", profile);

        return profile;
    }
}

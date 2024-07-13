package training.ex.dto;

import java.util.HashMap;
import java.util.Map;

public class GithubResponse implements OAuth2Response {
    private final Map<String, Object> attributes = new HashMap<>();

    @Override
    public String getProvider() {
        return "github";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}

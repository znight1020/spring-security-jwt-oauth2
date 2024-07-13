package training.ex.dto;

public interface OAuth2Response {
    String getProvider(); // 제공자
    String getProviderId(); // 제공자 발급 ID
    String getEmail(); // 사용자 이메일
    String getName(); // 사용자 이름
}

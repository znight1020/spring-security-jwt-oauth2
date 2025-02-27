package training.ex.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import training.ex.auth.dto.*;
import training.ex.auth.model.User;
import training.ex.auth.repository.UserRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info(oAuth2User.toString());

        // 어떤 서비스(구글, 네이버 등)에서 받아왔는지
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info(registrationId);
        OAuth2Response oAuth2Response = null;
        if("naver".equals(registrationId)) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if("google".equals(registrationId)) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if("github".equals(registrationId)) {
            oAuth2Response = new GithubResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getName();
        User existUser = userRepository.findByUsername(username);

        // 첫 로그인인 경우
        if(existUser == null) {
            User user = new User();
            user.setUserUuid(UUID.randomUUID().toString());
            user.setSocialProvider(oAuth2Response.getProvider());
            user.setUsername(username);
            user.setUserEmail(oAuth2Response.getEmail());
            user.setRole("ROLE_USER");

            userRepository.save(user);
            return new CustomOAuth2User(user, oAuth2User.getAttributes());
        } else {
            existUser.setUsername(username);
            existUser.setUserEmail(oAuth2Response.getEmail());

            userRepository.save(existUser);
            return new CustomOAuth2User(existUser, oAuth2User.getAttributes());
        }
    }
}

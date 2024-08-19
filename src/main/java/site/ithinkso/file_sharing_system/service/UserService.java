package site.ithinkso.file_sharing_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ithinkso.file_sharing_system.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean login(String token) {
        return userRepository.existsByToken(token);
    }

}

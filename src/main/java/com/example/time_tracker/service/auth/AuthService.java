package com.example.time_tracker.service.auth;

import com.example.time_tracker.model.auth.AuthRequest;
import com.example.time_tracker.model.auth.RegistrationRequest;
import com.example.time_tracker.model.User;
import com.example.time_tracker.model.dto.UserDto;
import com.example.time_tracker.repository.UserRepository;
import com.example.time_tracker.model.auth.AuthResponse;
import com.example.time_tracker.util.convertor.RequestMapper;
import com.example.time_tracker.util.convertor.UserMapper;
import com.example.time_tracker.util.exception.EmailAlreadyExistException;
import com.example.time_tracker.util.exception.UsernameAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RequestMapper requestMapper;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /*
    Сохранение нового пользователя
    Перед этим происходят проверки на уникальность имени и почты
    Если проверки провалены выбрасывается соответствующее исключение
    Далее происходит шифровка пароля, и зашифрованный пароль назначается пользователю,
    который после сохраняется
     */
    @Transactional
    public UserDto create(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistException("Username already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistException("Email already taken");
        }

        User user = requestMapper.requestToModel(request);
        user.setPassword(encoder.encode(request.getPassword()));

        return userMapper.modelToDto(userRepository.save(user));
    }

//    Метод проверяет имя пользователя и пароль, и если все в порядке в ответ генерирует токен
    public AuthResponse authenticateAndGetToken(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        return new AuthResponse(jwtService.generateToken(request.getUsername()));
    }
}

package c.task.taskadil.servise.impl;

import c.task.taskadil.config.jwt.JwtService;
import c.task.taskadil.dto.reqest.AuthenticateRequest;
import c.task.taskadil.dto.reqest.ResetPasswordRequest;
import c.task.taskadil.dto.reqest.SignUpRequest;
import c.task.taskadil.dto.response.AuthenticationResponse;
import c.task.taskadil.dto.response.SimpleResponse;
import c.task.taskadil.entity.User;
import c.task.taskadil.entity.enums.Role;
import c.task.taskadil.exception.exceptions.AlreadyExistException;
import c.task.taskadil.exception.exceptions.BadCredentialException;
import c.task.taskadil.exception.exceptions.BadRequestException;
import c.task.taskadil.exception.exceptions.NotFoundException;
import c.task.taskadil.repository.UserRepository;
import c.task.taskadil.servise.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    private User getAuthenticate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        log.info("Токен взят!");
        return userRepository.findByUsername(login).orElseThrow(() -> {
            log.error("Пользователь не найден с токеном пожалуйста войдите или зарегистрируйтесь!");
            return new NotFoundException("пользователь не найден с токеном пожалуйста войдите или зарегистрируйтесь");
        });
    }
    @Override
    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        User user = userRepository.findByUsername(request.username()).orElseThrow(() -> {
            String message = "User with id: " + request.username() + "is not found";
            log.error(message);
            return new EntityNotFoundException(message);
        });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("wrong password please provide right credentials");
        }
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }

    @Override
    public SimpleResponse resetPassword(ResetPasswordRequest request) {
        User user = getAuthenticate();
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            log.error("Пароль не подходит");
            throw new BadRequestException("Пароль не подходит");
        }
        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new BadRequestException("Извините, но новый пароль совпадает со старым. Для повышения безопасности " +
                                          "вашей учетной записи, пожалуйста, выберите новый уникальный пароль.");
        }
        if (!request.newPassword().equals(request.password())){
            throw new BadRequestException("новый пароль не совпадает");
        }
        if (request.newPassword().length() <= 7) throw new BadRequestException("Извините, но длина вашего нового пароля меньше минимально допустимой (6 символов).");

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        return new SimpleResponse(HttpStatus.OK, "Пароль успешно обновлен. Теперь вы можете использовать новый пароль для входа в свою учетную запись. " +
                                                 "Пожалуйста, запомните новый пароль и не делитесь им с другими людьми.");
    }

    @Override
    public AuthenticationResponse signUp(SignUpRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            String message = "user with email: " + request.username() + " is already exists!!";
            log.error(message);
            throw new AlreadyExistException(message);
        }
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
//                .role(Role.valueOf(request.role().name()))
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}

package c.task.taskadil.api;

import c.task.taskadil.config.jwt.JwtTokenBlacklist;
import c.task.taskadil.dto.reqest.AuthenticateRequest;
import c.task.taskadil.dto.reqest.ResetPasswordRequest;
import c.task.taskadil.dto.reqest.SignUpRequest;
import c.task.taskadil.dto.response.AuthenticationResponse;
import c.task.taskadil.dto.response.SimpleResponse;
import c.task.taskadil.servise.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "The User Authentication API")
@RequestMapping("/api/auth")
public class AuthenticationApi {
    @Autowired
    private JwtTokenBlacklist tokenBlacklist;
    private final AuthenticationService service;

    @Operation(summary = "User authentication", description = "This method to authenticate the user")
    @PostMapping("/sign_in")
    public AuthenticationResponse authenticate (@RequestBody AuthenticateRequest authenticateRequest){
        return service.authenticate(authenticateRequest);
    }

    @Operation(summary = "Reset password", description = "This method reset password")
    @PostMapping("/reset_password")
    public SimpleResponse resetPassword (@RequestBody ResetPasswordRequest request){
        return service.resetPassword(request);
    }

    @PostMapping("/sign_up")
    @Operation(summary = "Sign up method")
    AuthenticationResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        return service.signUp(signUpRequest);
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = extractToken(request);
        tokenBlacklist.blacklistToken(token);
        return ResponseEntity.ok("Logged out successfully");
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

package c.task.taskadil.servise;

import c.task.taskadil.dto.reqest.AuthenticateRequest;
import c.task.taskadil.dto.reqest.ResetPasswordRequest;
import c.task.taskadil.dto.reqest.SignUpRequest;
import c.task.taskadil.dto.response.AuthenticationResponse;
import c.task.taskadil.dto.response.SimpleResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticateRequest request);
    SimpleResponse resetPassword(ResetPasswordRequest request);
    AuthenticationResponse signUp(SignUpRequest request);



}

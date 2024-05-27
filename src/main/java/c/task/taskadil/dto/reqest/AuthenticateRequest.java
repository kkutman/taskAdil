package c.task.taskadil.dto.reqest;

import jakarta.validation.constraints.NotBlank;

public record AuthenticateRequest(
        @NotBlank(message = "Имя пользователя не должна быть пустой!")
        String username,
        @NotBlank(message = "Пароль не должен быть пустым!")
        String password
) {
}

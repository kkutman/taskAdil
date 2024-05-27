package c.task.taskadil.dto.reqest;

import c.task.taskadil.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest (
        @NotBlank(message = "Имя пользователя не должна быть пустой!")
        String username,
        @NotBlank(message = "Пароль не должен быть пустым!")
        String password
) {
}

package c.task.taskadil.dto.reqest;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ResetPasswordRequest(

        @NotBlank(message = "Старый пароль не должен быть пустым!")
        String oldPassword,
        @NotBlank(message = "Новый пароль не должен быть пустым!")
        String newPassword,
        @NotBlank(message = "Новый пароль не должен быть пустым!")
        String password
) {
}

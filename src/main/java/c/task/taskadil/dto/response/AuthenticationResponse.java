package c.task.taskadil.dto.response;

import c.task.taskadil.entity.enums.Role;
import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String username,
        Role role,
        String token
) {
}

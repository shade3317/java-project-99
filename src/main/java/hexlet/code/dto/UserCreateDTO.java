package hexlet.code.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserCreateDTO {
    private String firstName;
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @Size(min = 3)
    @NotBlank
    private String password;
}

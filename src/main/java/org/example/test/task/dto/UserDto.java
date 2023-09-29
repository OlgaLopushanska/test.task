package org.example.test.task.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.test.task.utils.CustomDateConstraint;

import java.util.Objects;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(message = "User first name should not be null or empty")
    private String firstName;
    @NotEmpty(message = "User last name should not be null or empty")
    private String lastName;
    @Email(message = "Email address should be valid")
    @NotEmpty(message = "User email should not be null or empty")
    private String email;
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    //@DateTimeFormat(pattern = "\\d{1,2}\\-\\d{1,2}\\/\\d{2,4}$")
    @CustomDateConstraint
    private String birthDate;
    @NotEmpty(message = "User address should not be null or empty")
    private String address;
    @NotEmpty(message = "User phone number should not be null or empty")
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(email, userDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}



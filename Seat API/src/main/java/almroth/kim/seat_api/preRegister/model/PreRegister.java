package almroth.kim.seat_api.preRegister.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PreRegister {

    @Id
    @UuidGenerator
    private UUID uuid;
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Wrong email format, see correct example: test@domain.com")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;

    private String businessName;
    @NotBlank(message = "Role name is required")
    private String roleName;
}

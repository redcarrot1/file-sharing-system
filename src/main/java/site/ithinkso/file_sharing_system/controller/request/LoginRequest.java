package site.ithinkso.file_sharing_system.controller.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @Size(min = 5, max = 20, message = "토큰은 {min}자리 이상, {max}자리 이하입니다.")
    private String token;
}

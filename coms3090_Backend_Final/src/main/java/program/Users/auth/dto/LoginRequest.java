package program.Users.auth.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "Username/email is required")
    @Size(min = 3,max=40,message="Username/Email must be between 3 and 40 characters long")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 6,max=40,message="Password must be between 6 and 40 characters long")
    private String password;

    public LoginRequest() {}
    public LoginRequest(String usernameOrEmail, String password){
        this.password = password;
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getUsernameOrEmail(){
        return this.usernameOrEmail;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    public void setUsernameOrEmail(String newUoE){
        this.usernameOrEmail = newUoE;
    }
}

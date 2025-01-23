package program.Users.auth.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3,max=20,message="Username must be between 3 and 20 characters long")
    private String username;


    @NotBlank(message = "Password is required")
    @Size(min = 6,max=40,message="Password must be between 6 and 40 characters long")
    private String password;

    @NotBlank(message = "email is required")
    @Size(min = 3,max=20,message="email must be between 3 and 40 characters long")
    @Email
    private String email;

    @NotBlank(message = "university is required")
    @Size(max = 40, message = "University name must be under 40 characters")
    private String university;

    public SignupRequest() {}
    public SignupRequest(String username, String password,String email, String university){
        this.password = password;
        this.username = username;
        this.email = email;
        this.university = university;
    }

    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getEmail(){
        return this.email;
    }
    public String getUniversity() {
        return university;
    }
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    public void setUsername(String newUsername){
        this.username = newUsername;
    }
    public void setEmail(String newEmail){
        this.email = newEmail;
    }
    public void setUniversity(String university) {
        this.university = university;
    }
}

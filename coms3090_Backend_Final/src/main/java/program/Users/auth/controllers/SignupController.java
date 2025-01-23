package program.Users.auth.controllers;
import jakarta.validation.Valid;
import program.Users.auth.dto.SignupRequest;
import program.Users.auth.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {
    private final AuthService authService;
    


    public SignupController(AuthService authService){
        this.authService = authService;
    }


    @PostMapping("/api/auth/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest signupRequest){
        try{
            authService.signup(signupRequest);
            return ResponseEntity.ok("User registered successfully!");
            
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
}

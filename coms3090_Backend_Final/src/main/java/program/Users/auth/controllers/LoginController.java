package program.Users.auth.controllers;
import jakarta.validation.Valid;
import program.Users.User;
import program.Users.auth.dto.LoginRequest;
import program.Users.auth.service.AuthService;
import program.Users.dto.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private final AuthService authService;
    

    @Autowired
    public LoginController(AuthService authService){
        this.authService = authService;
    }

    //Needs to be changed to return an access token for future calls or something
    @PostMapping("api/auth/login")
    public ResponseEntity<UserDTO> login(@RequestBody @Valid LoginRequest loginRequest){
        try{
            User user = authService.login(loginRequest);
            return ResponseEntity.ok(UserDTO.turnToDTO(user));
            
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    
}

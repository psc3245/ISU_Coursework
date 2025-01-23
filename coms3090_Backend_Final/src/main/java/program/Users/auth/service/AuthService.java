package program.Users.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import program.Users.User;
import program.Users.UserRepository;
import program.Users.auth.dto.LoginRequest;
import program.Users.auth.dto.SignupRequest;

//Overlap between signup and login? signup need to check if email exists,username exists. login has to check if the email or username exists within the database and if so, check if the password matches the hashed password
/*
we need to implement hashing and such, I will leave that for later though
*/ 
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User signup(SignupRequest signupRequest){
        if (userRepository.existsByEmail(signupRequest.getEmail())){
            throw new IllegalArgumentException("Email already used by another account- try resetting your password");
        }
        if (userRepository.existsByUsername(signupRequest.getUsername())){
            throw new IllegalArgumentException("Username already taken by another account - try using a different username");
        }
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setUniversity(signupRequest.getUniversity());
        //before setting password we need to hash
        user.setPassword(signupRequest.getPassword());
        user.setUsername(signupRequest.getUsername());
        userRepository.save(user);
        return user;
    }


    /**
     * @param loginRequest 
     * @return User object
     */
    public User login(LoginRequest loginRequest){
        if(userRepository.existsByUsername(loginRequest.getUsernameOrEmail())){
            User user = userRepository.findByUsername(loginRequest.getUsernameOrEmail()).get();
            //before checking if passwords are equal we need to hash
            if(user.getPassword().equals(loginRequest.getPassword())){
                return user;
            }
            else throwPasswordInvalidException();
        }
        else if (userRepository.existsByEmail(loginRequest.getUsernameOrEmail())){
            User user = userRepository.findByEmail(loginRequest.getUsernameOrEmail()).get();
            if(user.getPassword().equals(loginRequest.getPassword())){
                return user;
            }
            else throwPasswordInvalidException();
        }
        else{
            System.out.println("USERNAME/EMAIL");
            throw new IllegalArgumentException("Username or Email is not mapped to an account");
        }
        return new User();
    }
    private void throwPasswordInvalidException(){
        System.out.println("Password Wrong");
        throw new IllegalArgumentException("Password is incorrect");
    }
}

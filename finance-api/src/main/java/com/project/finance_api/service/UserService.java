package com.project.finance_api.service;

import com.project.finance_api.component.JwtUtil;
import com.project.finance_api.dto.Login;
import com.project.finance_api.entity.User;
import com.project.finance_api.enums.UserRole;
import com.project.finance_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //get all user
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    //get Specific user by id
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found At id:"+id));
    }

    public User getUserByToken(String token) {
        String email = jwtUtil.extractEmail(token);
        User returnUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "+email));
        returnUser.setPassword("");
        return returnUser;
    }

    public Login addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        userRepository.save(user);
        return new Login("", jwtUtil.generateToken(user), user.getRole());
    }

    public String authUserFrequently(Login login) {
        String email = jwtUtil.extractEmail(login.getPassword());
        userRepository.findByEmail(login.getEmail())
            .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        return login.getPassword();
    }

    // Authenticate user by email and password
    public Login authExistingUser(Login login) throws EntityNotFoundException {
        User findUser = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "+login.getEmail()));
        if (passwordEncoder.matches(login.getPassword(), findUser.getPassword())) {
            return new Login("", jwtUtil.generateToken(findUser), findUser.getRole());
        }
        throw new IllegalArgumentException("Wrong credentials");
    }

    //Update password
    public User updatePassword(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    // Update an existing user
    public User updateUser(Long id, User updateUser){
        return userRepository.findById(id).map( user -> {
            user.setFirstName(updateUser.getFirstName());
            user.setEmail(updateUser.getEmail());
            user.setAge(updateUser.getAge());
            user.setRole(updateUser.getRole());
            return userRepository.save(user);
        }).orElseThrow(() -> new IllegalArgumentException("User not found with id:"+id));
    }

    //Delete an user
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException();
        }
        userRepository.deleteById(id);
    }
}

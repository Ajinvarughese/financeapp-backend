package com.project.finance_api.service;

import com.project.finance_api.component.JwtUtil;
import com.project.finance_api.dto.FullUser;
import com.project.finance_api.dto.Login;
import com.project.finance_api.entity.User;
import com.project.finance_api.enums.AccountStatus;
import com.project.finance_api.enums.UserRole;
import com.project.finance_api.exceptions.AccountSuspendedException;
import com.project.finance_api.exceptions.DuplicateResourceException;
import com.project.finance_api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AssetService assetService;
    private final LiabilityService liabilityService;


    //get all user
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public List<FullUser> fetchFullUser() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user -> user.getRole() == UserRole.USER)
                .map(user -> {

                    FullUser fullUser = new FullUser();

                    fullUser.setUser(user);
                    fullUser.setAssets(assetService.getAssetsByUser(user.getId()));
                    fullUser.setLiabilities(liabilityService.getLiabilitiesByUser(user.getId()));

                    return fullUser;
                })
                .toList();
    }

    //get Specific user by id
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found At id:"+id));
    }

    public User getUserByToken(String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "+email));
        if(user.getAccountStatus() == AccountStatus.SUSPENDED) {
            throw new AccountSuspendedException("Account is temporarily suspended");
        }

        return user;
    }

    public Login addUser(User user) {
        Optional<User> exists = userRepository.findByEmail(user.getEmail());
        if(exists.isPresent()){
            throw new DuplicateResourceException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.USER);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        return new Login("", jwtUtil.generateToken(user), user.getRole());
    }


    // Authenticate user by email and password
    public Login authExistingUser(Login login) throws EntityNotFoundException {
        User findUser = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "+login.getEmail()));
        if(findUser.getAccountStatus() == AccountStatus.SUSPENDED) {
            throw new AccountSuspendedException("Account is temporarily suspended");
        }
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

    public User updateUserStatus(Long id, AccountStatus status) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setAccountStatus(status);
                    return userRepository.save(user);
                })
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found with id: " + id)
                );
    }

    //Delete an user
    @Transactional
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException();
        }
        userRepository.deleteById(id);
    }
}

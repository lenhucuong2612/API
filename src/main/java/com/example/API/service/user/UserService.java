package com.example.API.service.user;

import com.example.API.exception.AlreadyExitsException;
import com.example.API.exception.ResourceNotFoundException;
import com.example.API.model.User;
import com.example.API.repository.UserRepository;
import com.example.API.request.UserCreateRequest;
import com.example.API.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(UserCreateRequest request) {
        return Optional.of(request).filter(user->!userRepository.exitsByEmail(request.getEmail()))
                .map(req->{
                    User user=new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(()->new AlreadyExitsException("oops! "+request.getEmail()+" already exits"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,()->{
            throw new ResourceNotFoundException("User not found");
        });
    }
}

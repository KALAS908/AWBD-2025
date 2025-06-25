package com.example.demo.service;

import com.example.demo.Models.User;
import com.example.demo.dto.user.CredentialsDto;
import com.example.demo.dto.user.SignUpDto;
import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService( UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        User existingEmailUser = userRepository.findByEmail(userRequestDto.getEmail());
        User existingUserNameUser = userRepository.findByUserName(userRequestDto.getUserName());
        if (existingEmailUser != null) {
            throw new ResourceNotFoundException("Email already exists: " + userRequestDto.getEmail());
        }
        if (existingUserNameUser != null) {
            throw new ResourceNotFoundException("Username already exists: " + userRequestDto.getUserName());
        }

        User user = UserMapper.toEntity(userRequestDto);
        return UserMapper.toDto(userRepository.save(user));
    }

    public UserResponseDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return UserMapper.toDto(user);
    }

    public UserResponseDto updateUser(UUID id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Update user fields
        if (userRequestDto.getFirstName() != null) user.setFirstName(userRequestDto.getFirstName());
        if (userRequestDto.getLastName() != null) user.setLastName(userRequestDto.getLastName());
        if (userRequestDto.getUserName() != null) user.setUserName(userRequestDto.getUserName());
        if (userRequestDto.getAge() > 0) user.setAge(userRequestDto.getAge());
        if (userRequestDto.getEmail() != null) user.setEmail(userRequestDto.getEmail());
        if (userRequestDto.getPassword() != null) user.setPassword(userRequestDto.getPassword());

        User updatedUser = userRepository.save(user);
        return UserMapper.toDto(updatedUser);
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
        // or alternatively: userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponseDto login(CredentialsDto credentialsDto)
    {
        User user =  userRepository.findByEmail(credentialsDto.getEmail());
        if (user != null && user.getPassword().equals(credentialsDto.getPassword())) {
            return UserMapper.toDto(user);
        }
        else {
            throw new ResourceNotFoundException("Invalid email or password");
        }

    }


}


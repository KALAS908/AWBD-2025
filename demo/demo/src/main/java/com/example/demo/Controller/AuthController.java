package com.example.demo.Controller;


import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.user.CredentialsDto;
import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;



    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        String UserName = userRequestDto.getUserName();
        try {
            UserResponseDto createdUser = userService.createUser(userRequestDto);
            if (createdUser == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            createdUser.setToken(userAuthProvider.createToken(createdUser));
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(org.springframework.http.MediaType.APPLICATION_JSON).body(
                    UserResponseDto.builder()
                            .message(e.getMessage())
                            .build()
            );
        }

    }


    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody CredentialsDto credentialsDto)
    {
        UserResponseDto userResponseDto = userService.login(credentialsDto);
        if (userResponseDto == null) {
            return ResponseEntity.status(401).build();
        }
        userResponseDto.setToken(userAuthProvider.createToken(userResponseDto));

        return ResponseEntity.ok(userResponseDto);
    }


}

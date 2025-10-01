package com.careerpath.controller;
// Hello again test
import com.careerpath.dto.UserDto;
import com.careerpath.mapper.UserMapper;
import com.careerpath.model.User;
import com.careerpath.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        User user = UserMapper.fromDto(userDto);
        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }
}

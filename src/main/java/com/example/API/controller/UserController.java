package com.example.API.controller;


import com.example.API.exception.AlreadyExitsException;
import com.example.API.exception.ResourceNotFoundException;
import com.example.API.model.User;
import com.example.API.request.UserCreateRequest;
import com.example.API.request.UserUpdateRequest;
import com.example.API.response.ApiResponse;
import com.example.API.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class UserController {

    private final IUserService userService;
    @GetMapping("{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            User user=userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("Success",user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateRequest request){
        try {
            User user=userService.createUser(request);
            return ResponseEntity.ok(new ApiResponse("Create User Success",user));
        } catch (AlreadyExitsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestParam UserUpdateRequest request,@PathVariable Long userId){
        try {
            User user=userService.updateUser(request,userId);
            return ResponseEntity.ok(new ApiResponse("Update User Success!",user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success!",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}

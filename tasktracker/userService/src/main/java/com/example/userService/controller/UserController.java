package com.example.userService.controller;


import com.example.userService.entity.User;
import com.example.userService.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;


public interface UserController {

    @PostMapping("/createUser")
    public ResponseEntity<UserResponse<User>> createUser(@RequestBody User user);

     @GetMapping("/getUsers")
     public ResponseEntity<UserResponse<List<User>>> getAllUsers(@RequestParam(defaultValue = "5", required = false) Integer pageSize,
             @RequestParam(defaultValue = "0", required = false) Integer page);

     @GetMapping("/getUsersById")
     public ResponseEntity<UserResponse<List<User>>> getUserById(@RequestParam Long id);
    @GetMapping("/getUserByDate")
    public ResponseEntity<UserResponse<List<User>>> getUsersBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate);

    @PostMapping("/updateUser")
    public ResponseEntity<UserResponse<List<User>>> updateUser(@RequestParam Long id, @RequestBody User updateUser) throws Exception;

    @GetMapping("/login")
    public ResponseEntity<String> sendEmail(@RequestParam("email") String email, @RequestParam("password") String password);

    @GetMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam("email") String email, @RequestParam("otp") Long otp);

    @PostMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam Long id);

    @PostMapping("/upload")
    public ResponseEntity<User> fileUpload(@RequestParam("image") MultipartFile image);

}
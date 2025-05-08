package com.example.userService.service;

import com.example.userService.entity.Task;
import com.example.userService.entity.User;
import com.example.userService.feignclient.TaskClient;
import com.example.userService.repository.UserRepository;
import com.example.userService.response.TaskResponse;
import com.example.userService.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserSeviceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskClient taskClient;

	@Autowired
	JavaMailSender javaMailSender;

	private static final Logger logger = LoggerFactory.getLogger(UserSeviceImpl.class);
	private static final String ERROR_MESSAGE = "Something went wrong";

	@Override
	public UserResponse<User> createUser(User user) {
		List<User> userList = new ArrayList<User>();

		try {
			if (!Objects.equals(user.getEmail(), "") && !Objects.equals(user.getName(), "")) {
				if (user.getEmail() != null && user.getName() != null) {
					Optional<User> existUser = userRepository.findByEmail(user.getEmail());

					if (existUser.isPresent()) {
						userList.add(existUser.get());
						return new UserResponse<>("User already exists", userList, false);
					} else {
						if (user.getPassword().matches("[a-zA-Z0-9@$]{8,}+")) {
							String encoded = new BCryptPasswordEncoder().encode(user.getPassword());
							user.setPassword(encoded);
						//	user.setCreationDate();
							userRepository.save(user);
							return new UserResponse<>("User saved successfully",user, true);
						} else {
							return new UserResponse<>(
									"Password should contain uppercase, lowercase, digit, and special character and length must be 8 characters",
									userList, false);
						}
					}
				} else {
					return new UserResponse<>("User cannot be null", userList, false);
				}
			} else {
				return new UserResponse<>("User cannot be empty", userList, false);
			}
		} catch (Exception e) {
			return new UserResponse<>(ERROR_MESSAGE, userList, false);
		}
	}

	@Override
	public UserResponse<List<User>> getAllUsers(Pageable paging) {
		List<User> userWithTasks = new ArrayList<>();

		try {
			Page<User> userPage = userRepository.findAll(paging);
			List<User> userList = userPage.getContent();

			userWithTasks = userList.stream().map(user -> {
				try {
					ResponseEntity<TaskResponse<List<Task>>> response = taskClient.getTaskByUserId(user.getId());
					TaskResponse<List<Task>> taskResponse = response.getBody();

					if (taskResponse != null && taskResponse.getData() != null) {
						user.setTask(taskResponse.getData());
					} else {
						user.setTask(Collections.emptyList()); // fallback
					}
				} catch (Exception e) {
					System.err.println("Error fetching tasks for user ID " + user.getId() + ": " + e.getMessage());
					user.setTask(Collections.emptyList());
				}
				return user;
			}).collect(Collectors.toList());

			return new UserResponse<>("Users fetched successfully", userWithTasks, true);

		} catch (Exception e) {
			System.err.println("Error fetching users: " + e.getMessage());
			return new UserResponse<>(ERROR_MESSAGE, userWithTasks, false);
		}
	}

	@Override
	public UserResponse<List<User>> getUserById(Long id) {
		List<User> userList = new ArrayList<User>();
		try {
			Optional<User> getUser = userRepository.findById(id);
			if (getUser.isPresent()) {
				User user = getUser.get();
				user.setOtp(null);
				// fetch task by userId
				ResponseEntity<TaskResponse<List<Task>>> taskByUserId = taskClient
						.getTaskByUserId(getUser.get().getId());
				TaskResponse<List<Task>> body = taskByUserId.getBody();
				List<Task> taskList = body.getData();
				System.out.println(taskList);
				user.setTask(taskList);
				userList.add(user);

			}

			return new UserResponse<List<User>>("Users fetched successfully", userList, true);
		} catch (Exception e) {
			return new UserResponse<List<User>>(ERROR_MESSAGE, userList, false);
		}
	}

	@Override
	public UserResponse<List<User>> getUsersBetweenDates(Date startDate, Date endDate) {
		List<User> userList = new ArrayList<User>();
		try {
			userList = userRepository.findByCreationDateBetween(startDate, endDate).orElse(null);
			if (userList != null && !userList.isEmpty()) {
				return new UserResponse<>("Users fetched successfully", userList, true);
			} else {
				return new UserResponse<>("No users found between the given dates", userList, false);
			}
		} catch (Exception e) {
			return new UserResponse<>(ERROR_MESSAGE, userList, false);
		}
	}

	@Override
	public UserResponse<List<User>> updateUser(Long id, User updateUser) {

		List<User> userList = new ArrayList<User>();
		try {
			if (id == null || updateUser == null) {
				throw new IllegalArgumentException("User id and updated user cannot be null");
			}

			User user = userRepository.findById(id).orElse(null);
			if (user == null) {
				return (new UserResponse<>("User not found with id: " + id, userList, false));
			}

			if (updateUser.getName() != null && !updateUser.getName().isEmpty()) {
				user.setName(updateUser.getName());
			}
			if (updateUser.getEmail() != null && !updateUser.getEmail().isEmpty()) {
				user.setEmail(updateUser.getEmail());
			}
			userList.add(user);
			userRepository.save(user);
			return new UserResponse<>("User updated successfully", userList, false);
		} catch (Exception e) {
			return (new UserResponse<>(ERROR_MESSAGE, userList, false));
		}
	}

	@Override
	public String deleteUser(Long id) {
		logger.info("finding the id of the User for getUserById");
		Optional<User> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			logger.error("User ID is  not present=" + id);
			return "user is not present";
		} else {
			userRepository.deleteById(id);
			logger.info("Uesr with ID " + id + " deleted successfully.");
			return "user deleted sucessfully";
		}
	}

	@Override
	public String uploadImage(String path, MultipartFile file) {
		// TODO Auto-generated method stub

		// File name

		String name = file.getOriginalFilename();
		// File Path

		String filePath = path + File.separator + name;

		// create folder if not created

		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}

		// file copy

		try {

			File files = new File("images/" + file.getOriginalFilename());
			if (files.exists()) {
				System.out.println("file already exist");
			} else {
				Files.copy(file.getInputStream(), Paths.get(filePath));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public String sendSimpleMessage(String email, String password) {
		try {
			Optional<User> user = userRepository.findByEmail(email);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			if (user.isPresent() && encoder.matches(password, user.get().getPassword())) {
				long otp = new Random().nextInt(1000, 9999);
				user.get().setOtp(otp);
				userRepository.save(user.get());

				String body = "Your OTP is " + otp;
				String subject = "OTP Verification";

				SimpleMailMessage message = new SimpleMailMessage();
				message.setFrom("karishma3349@gmail.com");
				message.setTo(email);
				message.setSubject(subject);
				message.setText(body);
				javaMailSender.send(message);

				return "Mail sent to: " + email;
			} else {
				return "Invalid email or password";
			}

		} catch (Exception e) {
			logger.error("UserServiceImpl {}", e);
			return "Error in sending the email";
		}
	}

	@Override
	public Boolean checkOtp(String email, Long otp) {

		Optional<User> user = userRepository.findByEmail(email);
		if (user.get().getOtp().equals(otp)) {
			user.get().setOtp(null);
			userRepository.save(user.get());
			return true;
		}
		user.get().setOtp(null);
		userRepository.save(user.get());
		return false;

	}

}

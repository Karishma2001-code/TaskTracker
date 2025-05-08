package com.example.userService.feignclient;

import java.util.List;

import com.example.userService.entity.Task;
import com.example.userService.response.TaskResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "todoService") // must match the spring.application.name of the Task service
public interface TaskClient {

//	@GetMapping("/task/user/{userId}")
//	List<Task> getTasksOfUser(@PathVariable Long userId);
//
	@GetMapping("/task/user/{userId}")
	public ResponseEntity<TaskResponse<List<Task>>> getTaskByUserId(@PathVariable Long userId);

}

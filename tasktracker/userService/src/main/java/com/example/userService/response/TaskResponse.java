package com.example.userService.response;

import java.util.List;

import com.example.userService.entity.Task;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskResponse<T> {
	
	private List<Task> data;
	
	private String message;
	
	private boolean status;

	public TaskResponse(String message, boolean status) {
		super();
		this.message = message;
		this.status = status;
	}

}

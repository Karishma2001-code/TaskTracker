package com.example.todoService.service;


import com.example.todoService.entity.Task;
import com.example.todoService.response.TaskResponse;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface TaskService {

	 TaskResponse<List<Task>> getByUserId(Long userId);

	 TaskResponse<Task> addTask(Task task);

	public TaskResponse<List<Task>> getAllTask(Pageable paging);

	public TaskResponse<Task> getTaskById(Integer id);

	public TaskResponse<List<Task>> getTaskByTitle(String title);

	public TaskResponse<List<Task>> getTaskByCompletionDate(LocalDateTime completionDate);

	public TaskResponse<List<Task>> getTaskByCreationDate(LocalDateTime creationDate);

	public TaskResponse<List<Task>> getAllRemainingTask();

	public TaskResponse<List<Task>> getIncompleteTask();

	public boolean deleteTaskById(Integer id);

	public TaskResponse<Task> updateTask(Task task, Integer id);


}

package com.example.todoService.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.todoService.PriorityEnum;
import com.example.todoService.StatusEnum;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Task.findIncomplteTask", query = "select t from Task t where t.status=0")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@CreationTimestamp
	@Column(name = "creation_date")
	private LocalDateTime creationDate;

	@Column(name = "completion_date")
	private LocalDateTime completionDate;

	@Column(name = "priority")
	private PriorityEnum priority;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "status")
	private StatusEnum status;

	@ElementCollection
	@CollectionTable(name = "CollectionHistory", joinColumns = @JoinColumn(name = "taskId"))
	private List<LocalDateTime> completionDateHistory;

}

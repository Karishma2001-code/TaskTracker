package com.example.userService.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import com.example.userService.enums.PriorityEnum;
import com.example.userService.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
	private Integer id;

	private String title;

	private String description;

	private LocalDate creationDate;

	private LocalDate completionDate;

	private PriorityEnum priority;

	private Integer userid;

	private StatusEnum status;

	private List<String> tags;

	List<LocalDate> completionDateHistory = new ArrayList<LocalDate>();

}

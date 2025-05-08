package com.example.userService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;


	@Column(name = "password")
	private String password;

	@Column(name = "otp")
	private Long otp;

	@Column(name = "creation_date")
	@CreationTimestamp
	private Date creationDate;

	@Column(name = "fileName")
	private String fileName;

	@Transient
	private List<Task> task;

}

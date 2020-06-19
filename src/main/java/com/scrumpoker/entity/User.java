package com.scrumpoker.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.scrumpoker.commons.StatusType;

import org.hibernate.annotations.DynamicUpdate;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "id")
@EqualsAndHashCode(callSuper = false)
public class User extends BaseAuditEntity<String>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true)
	@Getter
	@ApiModelProperty(hidden = true)
	private long id;

	@NotBlank(message = "First name cannot be empty.")
	@Size(min = 1, max = 256, message = "First name must be contain 1-256 character.")
	@Getter
	@Setter
	@ApiModelProperty(value = "User first name.")
	private String firstName;

	@NotBlank(message = "Last name cannot be empty.")
	@Size(min = 1, max = 256, message = "Last name must be contain 1-256 character.")
	@Getter
	@Setter
	@ApiModelProperty(value = "User last name.")
	private String lastName;

	@NotBlank(message = "Email cannot be empty.")
	@Email(message = "The email must be valid.")
	@Column(unique = true)
	@Getter
	@Setter
	@ApiModelProperty(value = "User email.")
	private String email;

	@NotBlank(message = "Password cannot be empty.")
	@Size(min = 1, max = 64, message = "Password must be contain 1-64 character.")
	@Getter
	@Setter
	@ApiModelProperty(value = "User password.")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String token;

	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	private LocalDateTime validationToken;

	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	private StatusType status;
}

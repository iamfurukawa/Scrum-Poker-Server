package com.scrumpoker.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.scrumpoker.commons.DeckType;
import com.scrumpoker.commons.SessionType;
import com.scrumpoker.commons.StatusType;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "id")
@EqualsAndHashCode(callSuper = false)
public class Session extends BaseAuditEntity<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true)
	@Getter
	@ApiModelProperty(hidden = true)
	private long id;

	@NotBlank(message = "Name cannot be empty.")
	@Size(min = 1, max = 256, message = "Name must be contain 1-256 character.")
	@Getter
	@Setter
	@ApiModelProperty(value = "Session name.")
	private String name;

	@Positive(message = "Max participants cannot be less than 1")
	@Getter
	@Setter
	@ApiModelProperty(value = "Max participants allowed in this session.")
	private int maxParticipants;

	@NotNull(message = "Session type cannot be undefined.")
	@Getter
	@Setter
	@ApiModelProperty(value = "Session type.")
	private SessionType sessionType;

	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	private StatusType status;

	@NotBlank(message = "Password cannot be empty.")
	@Size(min = 1, max = 64, message = "Password must be contain 1-64 character.")
	@Getter
	@Setter
	@ApiModelProperty(value = "Password session.")
	private String password;

	@NotNull(message = "Deck cannot be undefined.")
	@Getter
	@Setter
	@ApiModelProperty(value = "Deck type.")
	private DeckType deckType;

	@NotBlank(message = "URL cannot be empty.")
	@Size(min = 1, max = 64, message = "Link session must be contain 1-128 character.")
	@Getter
	@Setter
	@ApiModelProperty(value = "Session URL.")
	private String url;

	@OneToMany
	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	private List<Issue> issues;

	@ManyToMany
	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	private List<User> participants;

	@OneToOne
	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	private User owner;

	public void addIssue(final Issue issue) {
		this.issues.add(issue);
	}
}

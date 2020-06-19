package com.scrumpoker.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scrumpoker.commons.IssueType;

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
public class Issue extends BaseAuditEntity<String> {

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
	@ApiModelProperty(value = "Issue name.")
	private String name;

	@OneToMany
	@Getter
	@Setter
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	private List<Vote> votes;

	@NotBlank(message = "Link cannot be empty.")
	@Size(min = 1, max = 64, message = "Name must be contain 1-64 character.")
	@Getter
	@Setter
	@ApiModelProperty(value = "Issue link.")
	private String link;

	@PositiveOrZero(message = "Max participants cannot be less than 0")
	@Getter
	@Setter
	private int points;

	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	private IssueType status;

	public void addVote(final Vote vote) {
		this.votes.add(vote);
	}
}

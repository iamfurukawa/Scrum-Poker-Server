package com.scrumpoker.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.PositiveOrZero;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "issue_id" }) })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "id")
@EqualsAndHashCode(callSuper = false)
public class Vote extends BaseAuditEntity<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true)
	@Getter
	@ApiModelProperty(hidden = true)
	private long id;

	@OneToOne
	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	private User user;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	private Issue issue;

	@PositiveOrZero
	@Getter
	@Setter
	@ApiModelProperty(value = "Points given for an issue.")
	private int points;
}

package com.prgrms.ohouse.domain.community.model.post.hwpost;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
@Getter
public class WorkDuration {

	@Column
	private Integer duration;

	@Column(name = "duration_unit")
	private Unit unit;

	public enum Unit {
		WEEK, MONTH
	}
}

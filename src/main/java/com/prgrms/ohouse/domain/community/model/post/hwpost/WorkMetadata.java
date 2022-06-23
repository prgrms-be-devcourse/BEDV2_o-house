package com.prgrms.ohouse.domain.community.model.post.hwpost;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;

@Embeddable
@Getter
public class WorkMetadata {

	@Column(name = "work_duration")
	private Integer duration;

	@Column(name = "work_duration_unit")
	private Unit unit;

	@Column(name = "work_target")
	@Enumerated(EnumType.STRING)
	private WorkTarget workTarget;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "worker_type")
	private WorkerType workerType;

	@Column(name = "worker_desc")
	private String workerDescription;

	public enum Unit {
		WEEK, MONTH
	}
}

package com.prgrms.ohouse.domain.community.model.housewarming;

import static com.google.common.base.Preconditions.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	@Builder
	public WorkMetadata(Integer duration, Unit unit, WorkTarget workTarget, WorkerType workerType,
		String workerDescription) {
		checkArgument((duration != null) == (unit != null), "두 값이 전부 유효하거나, 전부 유효하지 않아야 합니다.");
		this.duration = duration;
		this.unit = unit;
		this.workTarget = workTarget;
		this.workerType = workerType;
		this.workerDescription = workerDescription;
	}

	public enum Unit {
		WEEK, MONTH
	}
}

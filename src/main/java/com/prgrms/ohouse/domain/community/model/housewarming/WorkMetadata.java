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

/**
 * 집들이 공사에 대한 정보들
 */

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkMetadata {

	/**
	 * 공사 기간
	 */
	@Column(name = "work_duration")
	private Integer duration;

	/**
	 * 공사 기간 단위(주, 월)
	 */
	@Column(name = "work_duration_unit")
	private Unit unit;

	/**
	 * 공사 대상
	 */
	@Column(name = "work_target")
	@Enumerated(EnumType.STRING)
	private WorkTarget workTarget;

	/**
	 * 작업자
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "worker_type")
	private WorkerType workerType;

	/**
	 * 시공 / 스타일링 업체 정보
	 * 반셀프, 전문가일 경우에 선택적으로 기입
	 */
	@Column(name = "worker_desc")
	private String workerDescription;

	@Builder
	public WorkMetadata(Integer duration, Unit unit, WorkTarget workTarget, WorkerType workerType,
		String workerDescription) {
		checkArgument((duration != null) == (unit != null),
			"공사 기간과 공사 단위는 동일한 상태여야 합니다.(null or notnull)");
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

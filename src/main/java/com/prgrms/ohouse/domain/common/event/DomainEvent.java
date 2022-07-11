package com.prgrms.ohouse.domain.common.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class DomainEvent implements Serializable {
	private static final long serialVersionUID = 8945128060450240352L;
	private LocalDateTime occurDate;

	protected DomainEvent() {
		occurDate = LocalDateTime.now();
	}
}

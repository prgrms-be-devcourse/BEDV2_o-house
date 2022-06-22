package com.prgrms.ohouse.domain.community.model.post.hwpost;

import static com.google.common.base.Preconditions.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Access(AccessType.FIELD)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Budget {

	@Column(nullable = false)
	private long constructionFee;
	@Column(nullable = false)
	private long stylingFee;
	@Column(nullable = false)
	private long total;
	public Budget(long constructionFee, long stylingFee) {
		checkArgument(constructionFee >= 0, "건축 비용은 0 이상이어야 합니다.");
		checkArgument( stylingFee >= 0, "홈 스타일링 비용은 0 이상이어야 합니다.");
		this.constructionFee = constructionFee;
		this.stylingFee = stylingFee;
		this.total = constructionFee + stylingFee;
	}
}

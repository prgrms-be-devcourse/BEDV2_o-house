package com.prgrms.ohouse.domain.community.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FollowingFeedService {
	Slice<FollowingFeedInfoResult> getFeedPosts(Long authId, Pageable pageable);
}

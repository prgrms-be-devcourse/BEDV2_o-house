package com.prgrms.ohouse.domain.community.model.post.hwpost;

import static javax.persistence.GenerationType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Link {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@Column(length = 2000)
	private String url;
	
	private String presentation;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private HousewarmingPost post;
}

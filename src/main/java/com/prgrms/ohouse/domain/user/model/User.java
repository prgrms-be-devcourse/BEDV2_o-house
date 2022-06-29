package com.prgrms.ohouse.domain.user.model;

import static com.google.common.base.Preconditions.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.URL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.prgrms.ohouse.domain.common.file.ImageAttachable;
import com.prgrms.ohouse.domain.common.file.StoredFile;
import com.prgrms.ohouse.domain.common.file.UserImage;

@Getter
@Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails, ImageAttachable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "nickname", nullable = false, unique = true, length = 10)
	private String nickname;

	@Column(name = "email", nullable = false, unique = true, length = 300)
	private String email;

	@Column(name = "password", nullable = false, length = 60)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", length = 10)
	private GenderType gender;

	@URL
	@Column(name = "personal_url", length = 500)
	private String personalUrl;

	@Column(name = "birth")
	private Date birth;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private UserImage image;

	@Column(name = "introductions")
	private String introductions;

	@Column(name = "following_count")
	private int followingCount = 0;

	@Column(name = "follower_count")
	private int followerCount = 0;

	@Embedded
	private transient Address defaultAddress;

	public void checkPassword(PasswordEncoder passwordEncoder, String password) {
		checkArgument(passwordEncoder.matches(password, this.password), "Bad credential. Login failed.");
	}

	@Builder
	public User(String nickname, String email, String password,
		GenderType gender, String personalUrl, Date birth, StoredFile image, String introductions,
		int followingCount, int followerCount,
		Address defaultAddress) {
		setNickname(nickname);
		setEmail(email);
		setPassword(password);

		setGender(gender);
		setPersonalUrl(personalUrl);
		setBirth(birth);
		setImage(image);
		setIntroductions(introductions);

		setFollowerCount(followerCount);
		setFollowingCount(followingCount);
		setDefaultAddress(defaultAddress);
	}

	public User update(String nickname, GenderType gender, String personalUrl, Date birth,
		String introductions) {

		setNickname(nickname);
		setGender(gender);
		setPersonalUrl(personalUrl);
		setBirth(birth);
		setIntroductions(introductions);
		return this;
	}

	private void setImage(StoredFile image) {
		this.image = (UserImage)image;
	}

	private void setFollowerCount(int followerCount) {
		checkArgument(followerCount >= 0, "Follower count can't be negative.");
		this.followerCount = followerCount;
	}

	private void setFollowingCount(int followingCount) {
		checkArgument(followingCount >= 0, "Following count can't be negative.");
		this.followingCount = followingCount;
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public StoredFile attach(String fileName, String fileUrl) {
		UserImage userImage = new UserImage(fileName, fileUrl, this);
		setImage(userImage);
		return userImage;
	}
}

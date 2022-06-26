package com.prgrms.ohouse.domain.user.application;

import com.prgrms.ohouse.domain.user.application.commands.UserCreateCommand;
import com.prgrms.ohouse.domain.user.application.commands.UserLoginCommand;
import com.prgrms.ohouse.domain.user.application.commands.UserUpdateCommand;
import com.prgrms.ohouse.domain.user.model.User;

public interface UserService {

	void signUp(UserCreateCommand command);

	User login(UserLoginCommand command);

	User updateUser(User user, UserUpdateCommand command);

}

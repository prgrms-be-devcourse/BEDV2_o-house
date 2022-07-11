package com.prgrms.ohouse.domain.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import net.minidev.json.JSONObject;

import com.prgrms.ohouse.web.user.results.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException e) throws IOException{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		log.warn("{} is denied", authentication.getPrincipal(), e);

		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("message", ErrorCode.ACCESS_DENIED.getMessage());
		jsonObject.put("code", ErrorCode.ACCESS_DENIED.getCode());

		response.getWriter().print(jsonObject);
	}
}

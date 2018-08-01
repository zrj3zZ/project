package com.iwork.webservice.text;

import javax.jws.WebService;

import com.iwork.webservice.text.model.User;

	@WebService
	public class UserDaoImpl implements UserDao {
	public String login(User user) {
	return "userName : "+user.getUserName()+" userPwd : "+user.getUserPwd();
	}
	}



package com.iwork.webservice.text;
import javax.jws.WebService;

import com.iwork.webservice.text.model.User;
	

	@WebService(targetNamespace="http://server.accuralink.com")
	public interface UserDao {

	public String login(User user);

	}

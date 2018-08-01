package com.iwork.webservice.text.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="User")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"userName","userPwd"})
public class User {

private String userName;

private String userPwd;

public String getUserName() {
return userName;
}

public void setUserName(String userName) {
this.userName = userName;
}

public String getUserPwd() {
return userPwd;
}

public void setUserPwd(String userPwd) {
this.userPwd = userPwd;
}
}
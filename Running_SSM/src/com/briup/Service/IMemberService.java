package com.briup.Service;


import com.briup.Bean.Memberinfo;
import com.briup.Common.exception.MemberServiceException;

public interface IMemberService {
	// 用户注册
	void registerMemberinfo(Memberinfo memberinfo) throws MemberServiceException;
	
	// 按照姓名查找用户
	Memberinfo findMemberinfoByName(String nickname) throws MemberServiceException;
	
	//用户登录
	Memberinfo login(String name,String passwd);
	
	//用户注销
	void loginout(String name);
	
	//获取在线人数
	int getIsonlineSum();
}

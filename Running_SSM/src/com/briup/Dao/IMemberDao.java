package com.briup.Dao;


import com.briup.Bean.Graderecord;
import com.briup.Bean.Memberinfo;
import com.briup.Bean.Pointaction;
import com.briup.Bean.Pointrecord;
import com.briup.Common.exception.DataAccessException;


public interface IMemberDao {
	//按姓名查找用户
	Memberinfo findMemberinfoByName(String name) throws DataAccessException;
	//保存或更新用户
	void saveOrUpdateMemberinfo(Memberinfo memberinfo) throws DataAccessException;
	//查询用户当前积分
	long findMemberinfoPoint(String name);
	//跟新用户积分
	void updateMemberinfoPoint(long point,String name);
	//查询用户最后一次登录时间
	String findLastDate(String nickname);
	
	//跟新用户最后一次登录时间
	void updateLastDate(String nickname,String date);
	//查询该动作增加多少积分
	Pointaction findPointaction(String actionName);
	//保存用户的加分记录
	void savePointrecord(Pointrecord pointrecord);
	//根据当前积分获取等级
	Graderecord findGradeBypoint(long point);
	//跟新用户的状态
	void updateMemberInfoStatus(long status,long isonline,String name);
	//获取在线人数
	int findIsonlineSum();
	//跟新memeberinfo表中的等级id
	void updateGradeId(long id,String name);
}

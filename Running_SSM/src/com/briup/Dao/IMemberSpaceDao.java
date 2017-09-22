package com.briup.Dao;

import com.briup.Bean.Memberspace;

public interface IMemberSpaceDao {
	//创建空间
	long SaveMemberspace(Memberspace memberspace);
	//获取空间信息
	Memberspace findMemberspace(long id);
}

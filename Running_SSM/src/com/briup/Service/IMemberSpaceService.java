package com.briup.Service;

import com.briup.Bean.Memberspace;

public interface IMemberSpaceService {
	//保存创建的空间
	String SaveMemberspace(Memberspace memberspace);
	//获取空间
	Memberspace getMemberSpace(long id);
}

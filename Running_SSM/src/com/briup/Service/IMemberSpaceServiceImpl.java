package com.briup.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.briup.Bean.Memberinfo;
import com.briup.Bean.Memberspace;
import com.briup.Bean.Pointaction;
import com.briup.Bean.Pointrecord;
import com.briup.Dao.IMemberDao;
import com.briup.Dao.IMemberSpaceDao;
import com.briup.Web.exception.OwnExceptionSource;
import com.briup.Web.exception.RegisterException;


@Service("memberSpaceService")
public class IMemberSpaceServiceImpl implements IMemberSpaceService{

	@Autowired
	private IMemberSpaceDao memberSpaceDao;
	@Autowired
	private IMemberDao memberDao;

	@Override
	public String SaveMemberspace(Memberspace memberspace) {
		// TODO Auto-generated method stub
		try {
			long num=memberSpaceDao.SaveMemberspace(memberspace);
			if(num==0){
				return "注册失败";
			}
			//获取该用户的个人信息
			Memberinfo memberinfo=memberDao.findMemberinfoByName(memberspace.getMemberinfo().getNickName());
			
			//获取该动作加多少积分
			Pointaction pointaction=memberDao.findPointaction("CREATEPERSONALSPACE");
			long point=memberinfo.getPoint()+pointaction.getPoint();
			//跟新积分
			memberDao.updateMemberinfoPoint(point, memberinfo.getNickName());
			//进行积分记录
			Pointrecord pointrecord=new Pointrecord();
			pointrecord.setNickname(memberinfo.getNickName());
			pointrecord.setPointaction(pointaction);
			Date spacedate=new Date();
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String receivedate=dateformat.format(spacedate);
			pointrecord.setReceivedate(receivedate);
			//存储积分记录
			memberDao.savePointrecord(pointrecord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "注册成功";
	}

	@Override
	public Memberspace getMemberSpace(long id) {
		// TODO Auto-generated method stub
		Memberspace memberspace=memberSpaceDao.findMemberspace(id);
		return memberspace;
	}

}

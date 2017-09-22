package com.briup.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.briup.Bean.Graderecord;
import com.briup.Bean.Memberinfo;
import com.briup.Bean.Pointaction;
import com.briup.Bean.Pointrecord;
import com.briup.Common.exception.DataAccessException;
import com.briup.Common.exception.MemberServiceException;
import com.briup.Dao.IMemberDao;

@Service("memberService")
public class IMemberServiceImpl implements IMemberService{
	@Autowired
	private IMemberDao memberDao;
	
	@Override
	public void registerMemberinfo(Memberinfo memberinfo) throws MemberServiceException {
		// TODO Auto-generated method stub
		Memberinfo recommender=null;
		try {
			//判断是否有推荐人
			if(!memberinfo.getRecommender().equals("0")){
				//有推荐人
				recommender=memberDao.findMemberinfoByName(memberinfo.getRecommender());
				//判断推荐人是否存在
				if(recommender!=null){
					//获取该用户当前积分
					long point=memberDao.findMemberinfoPoint(recommender.getNickName());
					//查找推荐一个人加多少积分
					Pointaction pointaction=memberDao.findPointaction("RECOMMEND");
					point=point+pointaction.getPoint();
					//跟新推荐人的积分
					memberDao.updateMemberinfoPoint(point, recommender.getNickName());
					//进行积分获取记录
					Pointrecord pointrecord=new Pointrecord();
					pointrecord.setNickname(recommender.getNickName());
					pointrecord.setPointaction(pointaction);
					Date redate=new Date();
					SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String receivedate=dateformat.format(redate);
					pointrecord.setReceivedate(receivedate);
					//存储记录
					memberDao.savePointrecord(pointrecord);
				}
			}
			//存储注册时间
			Date date=new Date();
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
			String date1=dateformat.format(date);
			memberinfo.setRegisterdate(date1);
			//查找注册加多少分
			Pointaction registerAction=memberDao.findPointaction("REGISTER");
			System.out.println(registerAction);
			//注册加50积分
			memberinfo.setPoint(registerAction.getPoint());
			//查找用户等级
			Graderecord graderecord=memberDao.findGradeBypoint(memberinfo.getPoint());
			//将等级放入到对象中
			memberinfo.setGraderecord(graderecord);
			
			//存储用户信息
			memberDao.saveOrUpdateMemberinfo(memberinfo);
			
			//获取时间
			Date date2=new Date();
			SimpleDateFormat ateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sdate=dateformat.format(date);
			//跟新用户最后一次登录时间
			memberDao.updateLastDate(memberinfo.getNickName(), sdate);
			
			
			//存储注册用户的积分获取记录
			Pointrecord registerRecord=new Pointrecord();
			registerRecord.setNickname(memberinfo.getNickName());
			registerRecord.setReceivedate(date1);
			registerRecord.setPointaction(registerAction);
			System.out.println(registerRecord);
			memberDao.savePointrecord(registerRecord);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Memberinfo findMemberinfoByName(String nickname) throws MemberServiceException {
		Memberinfo memberinfo=null;
		try {
			memberinfo=memberDao.findMemberinfoByName(nickname);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return memberinfo;
	}

	//用户登录
	@Override
	public Memberinfo login(String name, String passwd) {
		// TODO Auto-generated method stub
		Memberinfo memberinfo=null;
		try {
			//查找到用户
			memberinfo=memberDao.findMemberinfoByName(name);
			System.out.println("kkkkk"+memberinfo);
			if(memberinfo!=null){
				if(passwd.equals(memberinfo.getPasswd())){
					//获取最后的登录时间
					String lastLogindate=memberDao.findLastDate(name);
					if(lastLogindate==null){
						memberinfo.setLatestdate("未登录过");
					}else{
						memberinfo.setLatestdate(lastLogindate);
					}
					System.out.println("hhhhhhhhh"+lastLogindate);
					//获取日期
					String[] logdate=lastLogindate.split(" ");
					Date nowdate=new Date();
					SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd");
					String today=dateformat1.format(nowdate);
					//判断是否在同一天
					if(!logdate[0].equals(today)){
						//根据当前积分获取等级
						Graderecord graderecord=memberDao.findGradeBypoint(memberinfo.getPoint());
						//将等级放入到对象中
						memberinfo.setGraderecord(graderecord);
						//将等级id放入到用户表中
						memberDao.updateGradeId(graderecord.getId(), name);
						
						//查找登录加多少分
						Pointaction loginaction=memberDao.findPointaction("LOGIN");
						//获取该用户的当前积分
						long loginPoint=memberDao.findMemberinfoPoint(name);
						loginPoint=loginPoint+loginaction.getPoint();
						//跟新用户积分
						memberDao.updateMemberinfoPoint(loginPoint, name);
						//进行积分记录
						Pointrecord pointrecord=new Pointrecord();
						pointrecord.setNickname(name);
						pointrecord.setPointaction(loginaction);
						Date logindate=new Date();
						SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String receivedate=dateformat.format(logindate);
						pointrecord.setReceivedate(receivedate);
						//存储记录
						memberDao.savePointrecord(pointrecord);
					}
					System.out.println("同一天登录");
					//设置用户状态
					memberDao.updateMemberInfoStatus(0L, 1L, name);
					return memberinfo;
				}else{
					memberinfo=null;
				}
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return memberinfo;
	}

	@Override
	public void loginout(String name) {
		// TODO Auto-generated method stub
		try {
			//获取时间
			Date date=new Date();
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date1=dateformat.format(date);
			//跟新用户最后一次登录时间
			memberDao.updateLastDate(name, date1);
			//设置用户的状态
			memberDao.updateMemberInfoStatus(1L, 0L, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public int getIsonlineSum() {
		int sum=0;
		try {
			sum=memberDao.findIsonlineSum();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sum;
	}

	

}

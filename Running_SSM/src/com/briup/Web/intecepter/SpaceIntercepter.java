package com.briup.Web.intecepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.briup.Bean.Memberinfo;
import com.briup.Bean.Memberspace;
import com.briup.Common.Util.Util;
import com.briup.Service.IMemberService;
import com.briup.Service.IMemberSpaceService;

public class SpaceIntercepter extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		WebApplicationContext app=WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		IMemberSpaceService memberSpaceService=(IMemberSpaceService) app.getBean("memberSpaceService");
		IMemberService memberService=(IMemberService) app.getBean("memberService");
		
		Memberinfo memberinfo=(Memberinfo) request.getSession().getAttribute("memberinfo_login");
		//获取Memverinfo
		Memberinfo member=memberService.findMemberinfoByName(memberinfo.getNickName());
		//转换性别数据
		if("0".equals(member.getGender())){
			member.setGender("男");
		}else{
			member.setGender("女");
		}
		//转换地址信息
		String city=new Util().getProvinceNameById(member.getProvinceCity());
		member.setProvinceCity(city);
		//获取空间信息
		Memberspace memberspace=memberSpaceService.getMemberSpace(member.getId());
		memberspace.setMemberinfo(member);
		System.out.println("zzz"+memberspace);
		
		request.getSession().setAttribute("space_inter", memberspace);
		//储存空间信息
		//memberSpaceService.SaveMemberspace(memberspace);
		
		return true;
	}
	
}

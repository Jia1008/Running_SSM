package com.briup.Web.intecepter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.briup.Bean.Memberinfo;
import com.briup.Service.IMemberService;

public class AutoLoginIntercepter extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("登录拦截了");
		
		WebApplicationContext app=WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		IMemberService memberService=(IMemberService) app.getBean("memberService");
		
		//获取cookie
		Cookie[] cookies=request.getCookies();
		if(cookies!=null){
			System.out.println(cookies.length);
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("loginCookie")){
					//获取cookie值
					String date=cookie.getValue();
					String[] dates=date.split(",");
					Memberinfo memberinfo=memberService.login(dates[0], dates[1]);
					if(memberinfo!=null){
						//获取在线人数
						int isonline_sum=memberService.getIsonlineSum();
						System.out.println("在线人数："+isonline_sum);
						request.getSession().setAttribute("isonline_sum", isonline_sum);
						
						request.getSession().setAttribute("memberinfo_login", memberinfo);
						response.sendRedirect("/Running_SSM/activity");
					}
				}
			}
		}
		
		return true;
	}
}

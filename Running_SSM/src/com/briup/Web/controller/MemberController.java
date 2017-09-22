package com.briup.Web.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.briup.Bean.Memberinfo;
import com.briup.Common.Util.RandomChar;
import com.briup.Common.exception.MemberServiceException;
import com.briup.Service.IMemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private IMemberService memberService;
	
	//用户注册
	@RequestMapping(value="register",method=RequestMethod.POST)
	public String register(Memberinfo member){
		try {
			System.out.println(member);
			memberService.registerMemberinfo(member);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/login";
	}
	
	//用户登录
	@ResponseBody
	@RequestMapping(value="login",method=RequestMethod.POST)
	public String login(String username,String passwd,boolean autoLogin,HttpSession session,HttpServletRequest request,HttpServletResponse response){
		System.out.println("username="+username+" passwd="+passwd+" autoLogin="+autoLogin);
		Memberinfo memberinfo=null;
		try {
			memberinfo=memberService.login(username, passwd);
			System.out.println("pppppppppppp"+memberinfo);
			if(memberinfo!=null){
				session.setAttribute("memberinfo_login", memberinfo);
				//获取在线人数
				int isonline_sum=memberService.getIsonlineSum();
				System.out.println("在线人数："+isonline_sum);
				session.setAttribute("isonline_sum", isonline_sum);
				//用户勾选了自动登录
				if(autoLogin==true){
					System.out.println("勾选了");
					Cookie cookie_login=new Cookie("loginCookie", username+","+passwd);
					cookie_login.setMaxAge(60*60*24*7);
					cookie_login.setPath(request.getContextPath());
					response.addCookie(cookie_login);
				}
				return "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}
	
	//注销登录
	@RequestMapping("loginout")
	public void loginout(@RequestParam("name") String name,HttpServletRequest request,HttpServletResponse response) throws IOException{
		System.out.println("zzzzz"+name);
		Cookie[] cookies=request.getCookies();
		for(Cookie cookie: cookies){
			cookie.setMaxAge(0);
			cookie.setPath(request.getContextPath());
			response.addCookie(cookie);
		}
		request.getSession().removeAttribute("memberinfo_login");
		memberService.loginout(name);
		response.sendRedirect("/Running_SSM/login");
	}
	
	//判断用户是否已注册，返回给ajax
	@ResponseBody
	@RequestMapping(value="isReg",method=RequestMethod.POST)
	public String isRegister(String name){
		Memberinfo memberinfo=null;
		try {
			memberinfo=memberService.findMemberinfoByName(name);
			//System.out.println(memberinfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(memberinfo==null){
			return "1";
		}else{
			return "0";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="checkCode",method=RequestMethod.POST)
	public String checkCodes(String authCode,HttpSession session){
		System.out.println(authCode);
		String strCode=(String) session.getAttribute("strCode");
		if(authCode.toLowerCase().equals(strCode.toLowerCase())){
			return "1";
		}else{
			return "0";
		}
	}
	
	//生成验证码
	@RequestMapping("defineCode")
	public void getVerificationCode(String time, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException{
		//System.out.println(time);
		int width = 60;
        int height = 34;
        Random random = new Random();
        //设置response头信息
        //禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成缓冲区image类
        BufferedImage image = new BufferedImage(width, height, 1);
        //产生image类的Graphics用于绘制操作
        Graphics g = image.getGraphics();
        //Graphics类的样式
        g.setColor(this.getRandColor(200, 250));
        g.setFont(new Font("Times New Roman",0,28));
        g.fillRect(0, 0, width, height);
        //绘制干扰线
        for(int i=0;i<40;i++){
            g.setColor(this.getRandColor(130, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }
        //绘制字符
        String strCode = "";
        //这种方式只能生成数字
//        for(int i=0;i<4;i++){
//            String rand = String.valueOf(random.nextInt(10));
//            strCode = strCode + rand;
//            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
//            g.drawString(rand, 13*i+6, 28);
//        }
        //调用封装好的方法，然后截取字符，给其上色
        String Code=new RandomChar().getChars(4, 4);
        for(int i=0;i<4;i++){
        	String rand=Code.substring(i, i+1);
        	strCode=strCode+rand;
        	 g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
           g.drawString(rand, 13*i+6, 28);
        }
        System.out.println(strCode);
        //将字符保存到session中用于前端的验证
        session.setAttribute("strCode", strCode);
        g.dispose();
        ImageIO.write(image, "JPEG", response.getOutputStream());
        response.getOutputStream().flush();
		
	}
	
	//创建颜色
    Color getRandColor(int fc,int bc){
        Random random = new Random();
        if(fc>255){
            fc = 255;
        }
        if(bc>255){
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r,g,b);
    }
}

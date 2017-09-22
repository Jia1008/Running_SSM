package com.briup.Web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.briup.Bean.Memberinfo;
import com.briup.Bean.Memberspace;
import com.briup.Service.IMemberSpaceService;

@RequestMapping("space")
@Controller
public class SpaceController {

	@Autowired
	private IMemberSpaceService memberSpaceService;
	
	@RequestMapping("isSpace")
	public String space(HttpServletRequest request){
		Memberinfo memberinfo=(Memberinfo) request.getSession().getAttribute("memberinfo_login");
		//获取空间信息
		Memberspace memberspace=memberSpaceService.getMemberSpace(memberinfo.getId());
		if(memberspace==null){
			return "redirect:/noSpace";
		}else{
			return "redirect:/space";
		}
	}
	
	
	
	@RequestMapping(value="save",method=RequestMethod.POST)
	public String saveSpace(@RequestParam("icon") MultipartFile[] files,HttpServletRequest request){
		Memberspace memberspace=new Memberspace();
		memberspace.setOpinion(request.getParameter("opinion"));
		memberspace.setRuntime(request.getParameter("runtime"));
		memberspace.setRunplace(request.getParameter("runplace"));
		memberspace.setRunstar(request.getParameter("runstar"));
		memberspace.setRunhabit(request.getParameter("runhabit"));
		memberspace.setCellphone(request.getParameter("cellphone"));
		if(files!=null&&files.length>0){
			for (MultipartFile file : files) {
				// 保存文件
				String path=saveFile(request, file);
				memberspace.setIcon(path);
			}
		}
		System.out.println(memberspace);
		//获取当前用户的信息
		Memberinfo memberinfo=(Memberinfo) request.getSession().getAttribute("memberinfo_login");
		memberspace.setMemberinfo(memberinfo);
		String msg=memberSpaceService.SaveMemberspace(memberspace);
		return "redirect:/space";
	}

	private String saveFile(HttpServletRequest request, MultipartFile file) {
		// TODO Auto-generated method stub
		String filePath=null;
		String iconpath=null;
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				//保存的文件路径
				//需要的话可以给文件名上加时间戳
				filePath = request.getServletContext().getRealPath("/") + "upload/"
						+ file.getOriginalFilename();
				System.out.println("path:"+request.getServletContext().getRealPath("/"));
				//存储在upload中的路径
				iconpath="upload/"+file.getOriginalFilename();
				//永久存储路径
				String longPath=request.getServletContext().getRealPath("/");
				//E:\apache-tomcat-7.0.72\webapps\Running_SSM\
				int location=longPath.lastIndexOf("webapps");
				String path=longPath.substring(0, location);
				System.out.println(path);
				
				//永久存储文件
				File longfile=new File(path+"upload/"+ file.getOriginalFilename());
				if (!longfile.getParentFile().exists()){
					longfile.getParentFile().mkdirs();
				}
				
				File newFile = new File(filePath);
				//文件所在目录不存在就创建
				if (!newFile.getParentFile().exists()){
					newFile.getParentFile().mkdirs();
				}
				// 转存文件
				file.transferTo(newFile);
				//转存永久文件
				file.transferTo(longfile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return iconpath;
	}
}

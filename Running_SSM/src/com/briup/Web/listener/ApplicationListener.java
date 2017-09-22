package com.briup.Web.listener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ApplicationListener
 *
 */
@WebListener
public class ApplicationListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ApplicationListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce) {
        // TODO Auto-generated method stub
    	System.out.println("服务器启动");
    	String longPath=sce.getServletContext().getRealPath("/");
    	System.out.println("jjjj"+longPath);
    	//获取存储的图片
    	int location=longPath.lastIndexOf("webapps");
		String path=longPath.substring(0, location);
		System.out.println("kkkkk"+path);
		File file=new File(path+"upload");
		System.out.println("lll:"+longPath);
		getFile(file, longPath+"upload");
    }
    //查找该目录下的文件
    public void getFile(File file,String longpath){
    	if(file.exists()){
    		if(file.isDirectory()){
    			File[] files=file.listFiles();
    			for (File file2 : files) {
    				System.out.println("ss"+file2);
					getFile(file2,longpath);
				}
    		}else{
    			String point=file.getAbsolutePath();
    			System.out.println("point:"+point);
    			int x=point.lastIndexOf("\\");
    			String name=point.substring(x+1);
    			System.out.println("name:"+name);
    			System.out.println("zzz"+longpath+"\\"+name);
    			copyFile(file, longpath+"\\"+name);
    		}
    	}
    }
    
    //拷贝文件
    public void copyFile(File src,String aim){
    	 BufferedOutputStream bufferedOutputStream = null;
         BufferedInputStream bufferedInputStream = null;
    	//文件存在
    	if(src.exists()){
    		try {
				bufferedInputStream=new BufferedInputStream(new FileInputStream(src));
				bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(aim));
				byte[] bytes = new byte[1024];
	            int num = 0;
	            while ((num = bufferedInputStream.read(bytes)) != -1) {
	                bufferedOutputStream.write(bytes, 0, num);
	                bufferedOutputStream.flush();
	            }
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					bufferedInputStream.close();
					bufferedOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    		
    	}
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub
    	System.out.println("服务器销毁");
    }
	
}

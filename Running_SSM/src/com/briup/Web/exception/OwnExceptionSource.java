package com.briup.Web.exception;

public class OwnExceptionSource {
	public void make(long x) throws RegisterException{
		if(x==0){
			throw new RegisterException("注册失败");
		}
	}
}

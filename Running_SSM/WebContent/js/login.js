$(function(){
	//验证验证码
	$("input[name='authCode']").on("blur",function(){
		var $authCode=$("input[name='authCode']").val();
		if(!isEmpty($authCode)){
			$.ajax({
				type:"POST",
				url:"member/checkCode",
				data:"authCode="+$authCode,
				success:function(msg){
					if(msg=="0"){
						alert("验证码错误");
					}
				}
			});
		}else{
			alert("请输入验证码");
		}
	});
	
	
	$("img[name='login']").on("click",function(){
		var $username=$("input[name='username']").val();
		var $passwd=$("input[name='password']").val();
		var $authCode=$("input[name='authCode']").val();
		var $autoLogin=$("input[name='autoLogin']").prop("checked");
		
		if($username==""){
			alert("请填写用户名");
		}else if($passwd==""){
			alert("请填写密码");
		}else if($authCode==""){
			alert("请填写验证码");
		}else{
			$.ajax({
				type:"POST",
				url:"member/login",
				data:"username="+$username+"&passwd="+$passwd+"&autoLogin="+$autoLogin,
				success:function(msg){
					if(msg=="1"){
						window.location.href="activity";
					}else{
						alert("用户名或密码错误");
					}
				}
			});
		}
	});
});

$(function(){
	//封装一个方法：输入合法后显示√
	function Inputsuccess(a){
		a.text("√");
		a.css({
			"color": "green",
		});
	}
	
	//验证用户是否已注册过
	$("input[name='nickName']").on("blur",function(){
		//获取用户名
		var $name=$("input[name='nickName']").val();
		//获取后面的显示文字
		var $text=$("form font:eq(3)");
		//判断输入的是不是为空
		if(!isEmpty($name)){
			$.ajax({
				type:"POST",
				url:"member/isReg",
				data:"name="+$name,
				success:function(mag){
					//用户为注册。则为true
					if(mag=="1"){
						//调用封装的方法，显示√
						Inputsuccess($text);
					}else{
						$text.text("该用户名已经被注册");
						$text.css("color","red");
					}
				}
			});
		}else{
			$text.text("请输入值");
			$text.css("color","red");
		}
	});
	
	//验证密码设置是否合法
	$("input[name='passwd']").on("blur",function(){
		//获取用户输入的密码值
		var $passwd=$("input[name='passwd']").val();
		//获取后面的显示文字
		var $text=$("form font:eq(5)");
		var reg=/^[a-z0-9_-]{6,14}$/ig;
		if(reg.test($passwd)){
			//调用封装的方法，显示√
			Inputsuccess($text);
		}else{
			$text.text("密码设置不合法");
			$text.css("color","red");
		}
	});
	
	//判断确认密码是否与第一次输入的密码是否一致
	$("input[name='passwdre']").on("blur",function(){
		var $passwdre=$("input[name='passwdre']").val();
		var $passwd=$("input[name='passwd']").val();
		var $text=$("form font:eq(7)");
		if(!isEmpty($passwdre)){
			if($passwdre==$passwd){
				//调用封装的方法，显示√
				Inputsuccess($text);
			}else{
				$text.text("两次密码输入不一致");
				$text.css("color","red");
			}
		}else{
			$text.text("请输入值");
			$text.css("color","red");
		}
	});
	
	//判断邮箱输入是否合法
	$("input[name='email']").on("blur",function(){
		var $email=$("input[name='email']").val();
		var $text=$("form font:eq(9)");
		var reg=/^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/ig;
		if(!isEmpty($email)){
			if(reg.test($email)){
				Inputsuccess($text);
			}else{
				$text.text("邮箱格式不合法");
				$text.css("color","red");
			}
		}else{
			$text.text("请输入值");
			$text.css("color","red");
		}
	});
	
	//判断密码提示问题
	$("input[name='passwdQuestion']").on("blur",function(){
		var $passwdQuestion=$("input[name='passwdQuestion']").val();
		var $text=$("form font:eq(11)");
		if(checkStrLength($passwdQuestion) < 6){
			$text.text("输入字符小于6个字符");
			$text.css("color","red");
		}else{
			Inputsuccess($text);
		}
		
	});
	
	//判断密码提示
	$("input[name='passwdAnswer']").on("blur",function(){
		var $passwdQuestion=$("input[name='passwdAnswer']").val();
		var $text=$("form font:eq(13)");
		if(checkStrLength($passwdQuestion) < 6){
			$text.text("提示答案小于6个字符");
			$text.css("color","red");
		}else{
			Inputsuccess($text);
		}
		
	});
	
	//判断是否有推荐人
	$("input[name='recommender']").on("blur",function(){
		var $recommender=$("input[name='recommender']").val();
		var $text=$("form font:eq(15)");
		if(!isEmpty($recommender)){
			$.ajax({
				type:"POST",
				url:"member/isReg",
				data:"name="+$recommender,
				success:function(mag){
					if(mag=="1"){
						$text.text("该推荐人不存在");
						$text.css("color","red");
					}else{
						Inputsuccess($text);
					}
				}
			});
		}else{
			$text.text("输入为空");
			$text.css("color","red");
		}
	});
	
	//判断年龄是否合法
	$("input[name='age']").on("blur",function(){
		var $age=$("input[name='age']").val();
		var $text=$("form font:eq(17)");
		if(!isEmpty($age)){
			if(!isNaN($age)){
				Inputsuccess($text);
			}else{
				$text.text("请输入数字");
				$text.css("color","red");
			}
		}else{
			$text.text("输入为空");
			$text.css("color","red");
		}
	});
	
	//验证电话号码
	$("input[name='phone']").on("blur",function(){
		var $phone=$("input[name='phone']").val();
		var $text=$("form font:eq(18)");
		var reg=/^1[34578]\d{9}$/ig;
		if(reg.test($phone)){
			Inputsuccess($text);
		}else{
			$text.text("输入的电话号码不合法");
			$text.css("color","red");
		}
	});
	
	
	//点击更换验证码
	$("#changeCode").on("click",function(){
		//这里需要加一个时间戳，以此来区分不同的请求
		var timenow=new Date().getTime();
		$("#changeCode").attr("src","http://127.0.0.1:8888/Running_SSM/member/defineCode?time="+timenow);
	});
	
	//验证验证码是否正确
	$("input[name='authCode']").on("blur",function(){
		var $authCode=$("input[name='authCode']").val();
		var $text=$("form font:eq(19)");
		if(!isEmpty($authCode)){
			$.ajax({
				type:"POST",
				url:"member/checkCode",
				data:"authCode="+$authCode,
				success:function(msg){
					if(msg=="1"){
						Inputsuccess($text);
					}else{
						$text.text("验证码输入错误");
						$text.css("color","red");
					}
				}
			});
		}else{
			$text.text("请输入验证码");
			$text.css("color","red");
		}
	});
	
});

package com.example.demo.trainsModule.user.controer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.utils.RedisUtil;
import com.example.demo.mtmydbModule.user.dao.MtmyUserDao;
import com.example.demo.mtmydbModule.user.entity.MtmyUser;
import com.example.demo.trainsModule.user.dao.UserDao;
import com.example.demo.trainsModule.user.entity.User;

/**
 * 
 * @Description
 * @author coffee
 * 2019年12月26日
 */
@RestController
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private MtmyUserDao mtmyUserDao;
	
	@Autowired
	@Resource(name="redisUtil1")
	private RedisUtil redisUtil1;
	
	@Autowired
	@Resource(name="redisUtil2")
	private RedisUtil redisUtil2;
	
	
	@RequestMapping("login")
	public String login(){
		
		User user = new User();
		user.setId("1");
		user = userDao.queryUser(user);
		System.out.println(user);
		
		MtmyUser user1 = new MtmyUser();
		user1.setId("1");
		user1 = mtmyUserDao.queryUser(user1);
		System.out.println(user1);
		
		System.out.println("redis1应该存在值--------"+redisUtil1.get("AGOINCOME_1"));
		System.out.println("redis2-error--------"+redisUtil2.get("AGOINCOME_1"));
		System.out.println("redis1-error--------"+redisUtil1.get("mobile_user_13241643559"));
		System.out.println("redis2应该存在值--------"+redisUtil2.get("mobile_user_13241643559"));
		
		return "hello spring boot!";
	}
}

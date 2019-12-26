package com.example.demo.mtmydbModule.user.dao;

import com.example.demo.mtmydbModule.user.entity.MtmyUser;

public interface MtmyUserDao {
	
	public MtmyUser queryUser(MtmyUser mtmyUser);
	
	public void insertUserCid();
}

package com.example.demo.trainsModule.user.dao;

import com.example.demo.trainsModule.user.entity.User;

public interface UserDao {
	
	public User queryUser(User user);
	
	public void insertUserCid();
	
}

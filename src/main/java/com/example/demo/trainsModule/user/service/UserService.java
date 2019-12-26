package com.example.demo.trainsModule.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.trainsModule.user.dao.UserDao;

@Service
@Transactional(transactionManager="trainsTransactionManager")
public class UserService{
	
	@Autowired
	private UserDao userDao;

	public void insertUser(){
		userDao.insertUserCid();
		int a = (int)10 / (int)0;
		System.out.println(a);
	}
}

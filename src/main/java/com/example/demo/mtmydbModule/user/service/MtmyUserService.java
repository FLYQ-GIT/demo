package com.example.demo.mtmydbModule.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mtmydbModule.user.dao.MtmyUserDao;

@Service
@Transactional(transactionManager="mtmydbTransactionManager")
public class MtmyUserService{
	
	@Autowired
	private MtmyUserDao mtmyUserDao;

	public void insertUser(){
		mtmyUserDao.insertUserCid();
		int a = (int)10 / (int)0;
		System.out.println(a);
	}
}

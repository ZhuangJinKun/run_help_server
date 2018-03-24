package com.zjk.service.impl;

import com.zjk.dao.UserDao;
import com.zjk.entity.UserInfo;
import com.zjk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	public int insert(UserInfo userInfo) {
		UserInfo user = userDao.query(userInfo);
		if (user != null) {
			return 2;
		}
		boolean status = userDao.insert(userInfo);
		if (status) {
			return 1;
		} else {
			return 0;
		}
	}

	public UserInfo query(UserInfo userInfo) {
		UserInfo user = userDao.query(userInfo);
		if (user == null) {
			return null;
		} else {
			if (user.getPassword().equals(userInfo.getPassword())) {
				return user;
			} else {
				user.setuId(-1);
				return user;
			}
		}
	}

	public boolean update(UserInfo userInfo) {
		return userDao.update(userInfo);
	}
}

package com.github.cat.service;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public void delete(Object entity) {
		sleep(30);
		System.out.println("UserServiceImpl---删除方法:delete()---");
	}

	@Override
	public void getAllObjects() {
		sleep(40);
		System.out.println("UserServiceImpl---查找所有方法:getAllObjects()---");
	}

	@Override
	public void save(Object entity) {
		sleep(10);
		System.out.println("UserServiceImpl---保存方法:save()---");
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
		}
	}

	/**
	 * 模拟非注解方法,并抛出异常
	 * @param entity
	 */
	@Override
	public void update(Object entity) {
		sleep(20);
		int i = 10/0;
		System.out.println("UserServiceImpl---更新方法:update()---");
	}

}
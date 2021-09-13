package com.zebone.platform.framework.support.holder;

import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.ISession;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.framework.support.Session;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


public class SecurityHolder {
	
	private static ThreadLocal<User> LocalUser= new ThreadLocal<User>();

	protected static IUser getUser() {
		Subject currentUser = SecurityUtils.getSubject();
		return (IUser) currentUser.getPrincipal();
	}

	protected static ISession getSession(boolean createNewIfNo) {
		Subject currentUser = SecurityUtils.getSubject();
		return new Session(currentUser.getSession(createNewIfNo));
	}

	protected static ISession getSession() {
		return getSession(true);
	}

	public static void setLocalUser(User user){
		LocalUser.set(user);
	}
	
	public static void clearLocalUser(){
		LocalUser.remove();
		LocalUser.set(null);
	}
	
	public static SecurityContext getContext() {
		SecurityContext context = new SecurityContext();
		if(getUser() == null){
			context.user = LocalUser.get();
		}else{
			context.user = getUser();
		}
		
		context.session = getSession();
		return context;
	}

	public static void logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null && subject.getPrincipal() != null) {
			subject.logout();
		}
	}

	public static class SecurityContext {
		public IUser user;
		public ISession session;
	}
}

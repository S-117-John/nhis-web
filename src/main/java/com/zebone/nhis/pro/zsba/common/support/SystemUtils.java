package com.zebone.nhis.pro.zsba.common.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;  
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Date: 2014-6-18
 * @Author: jilongliang
 * @Description：System一些常用属性封装..
 */
@SuppressWarnings("all")
public class SystemUtils {
	 private static Properties props=System.getProperties(); //系统属性

	/**
	 * 获取当前操作系统名称. return 操作系统名称 例如:windows xp,linux 等.
	 */
	private static String osName() {
		return System.getProperty("os.name").toLowerCase();
	}
	
	/**获取操作系统版本*/
	private static String osVersion() {
		return System.getProperty("os.version");
	}
	/**获取Java运行时环境供应商*/
	private static String vendor() {
		return System.getProperty("java.vendor");
	}
	/**获取Java供应商的 URL*/
	private static String vendorUrl() {
		return System.getProperty("java.vendor.url");
	}
	/**Java 安装目录*/
	private static String home() {
		return System.getProperty("java.home");
	}
	/**获取类的版本*/
	private static String classVersion() {
		return System.getProperty("java.class.version");
	}
	/**
	 * Java 类路径
	 * @return
	 */
	private static String classPath() {
		return System.getProperty("java.class.path");
	}
	/**
	 * 操作系统的架构
	 * @return
	 */
	private static String osArch() {
		return System.getProperty("os.arch");
	}
	/**
	 * 获取用户的账户名称
	 * @return
	 */
	private static String userName() {
		return System.getProperty("user.name");
	}
	/**
	 * 获取用户的主目录
	 * @return
	 */
	public static String userHome() {
		return System.getProperty("user.home");
	}
	/**
	 * 用户的当前工作目录
	 * @return
	 */
	public static String userDir() {
		return System.getProperty("user.dir");
	}
	/**
	 * Java 虚拟机规范版本
	 * @return
	 */
	public static String vmSpecificationVersion() {
		return System.getProperty("java.vm.specification.version");
	}
	/**
	 * Java 虚拟机规范供应商
	 * @return
	 */
	public static String vmSpecificationVendor() {
		return System.getProperty("java.vm.specification.vendor");
	}
	/**
	 * Java 虚拟机规范名称
	 * @return
	 */
	public static String vmSpecificationName() {
		return System.getProperty("java.vm.specification.name");
	}
	/**
	 * Java 虚拟机实现版本
	 * @return
	 */
	public static String vmVersion() {
		return System.getProperty("java.vm.version");
	}
	/**
	 * Java 虚拟机实现供应商
	 * @return
	 */
	public static String vmVendor() {
		return System.getProperty("java.vm.vendor");
	}
	/**
	 * Java 虚拟机实现名称
	 * @return
	 */
	public static String vmName() {
		return System.getProperty("java.vm.name");
	}
	/**
	 * 一个或多个扩展目录的路径
	 * @return
	 */
	public static String extDirs() {
		return System.getProperty("java.ext.dirs");
	}	
	/**
	 *加载库时搜索的路径列表
	 * @return
	 */
	public static String library() {
		return System.getProperty("java.library.path");
	}
	/**
	 * 文件分隔符（在 UNIX 系统中是“/”）
	 * @return
	 */
	public static String fileSeparator() {
		return System.getProperty("file.separator");
	}
	/**
	 * 路径分隔符（在 UNIX 系统中是“:”）
	 * @return
	 */
	public static String pathSeparator() {
		return System.getProperty("path.separator");
	}
	/**
	 * 行分隔符（在 UNIX 系统中是“/n”）
	 * @return
	 */
	public static String lineSeparator() {
		return System.getProperty("line.separator");
	}
	/**
	 * 要使用的 JIT 编译器的名称
	 * @return
	 */
	public static String compiler() {
		return System.getProperty("java.compiler");
	}

	/**
	 * C:\Users\ADMINI~1\AppData\Local\Temp\ 获取当前临时目录
	 * 
	 * @return
	 */
	public static String getSystempPath() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * 以\分割文件
	 * 
	 * @return
	 */
	public static String getSeparator() {
		return System.getProperty("file.separator");
	}
	/***
	 * 换行
	 * @return
	 */
	public static String getNewLine() {
		return System.getProperty("line.separator");
	}
	
	/***
	 * Java版本
	 * @return
	 */
	public static String getJavaVersion() {
		return props.getProperty("java.version");
	}
	
	/***
	 * Java运行时环境规范版本
	 * @return
	 */
	public static String getSpecificationVersion() {
		return props.getProperty("java.specification.version");
	}
	/**
	 * Java运行时环境规范供应商
	 * @return
	 */
	public static String getSpecificationVender() {
		return props.getProperty("java.specification.vender");
	}
	/**
	 * Java运行时环境规范名称
	 * @return
	 */
	public static String getSpecificationName() {
		return props.getProperty("java.specification.name");
	}
	
	/**
	 * 获取当前请求对象
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println("我是"+getNewLine()+"中国人");
	}

}


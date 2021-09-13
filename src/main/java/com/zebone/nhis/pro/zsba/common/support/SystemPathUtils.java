package com.zebone.nhis.pro.zsba.common.support;

import java.io.File;

/**
 *@Author:liangjl
 *@Date:2014-12-1
 *@Version:1.0
 *@Description:专门处理当前项目的部署下面文件路径封装
 */
public class SystemPathUtils {
	private static  final String Web_Info_Class="WEB-INF/classes/";
	private static final String filePrefix="file:/";
	private static final String Slash="/";

	/**
	 * Thread.currentThread().getContextClassLoader().getResource("")
	 * 的getResource里面空串或者点或者是/输出的路径是一致 ""
	 * 
	 * 处理前 : file:/D:/Workspaces/MyEclipse%208.6/myapp/WebRoot/WEB-INF/classes/ 
	 * 处理后 : D:\Workspaces\MyEclipse%208.6\myapp\WebRoot\WEB-INF\classes\
	 * 有空格的%20
	 * @return
	 */
	public static String getClassPathByFilePrefix() {
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		// String path=Thread.currentThread().getContextClassLoader().getResource(".").toString();
		String temp = path.replaceFirst(filePrefix, "");//文件前缀
		String separator =SystemUtils.fileSeparator(); 
		String resultPath = temp.replaceAll(Slash, separator + separator);
		return resultPath;
	}
	/**
	 *getClassLoader().getResource()方法参数空串和点都是输出相同的路径唯有/是报空指针 ""
	 *得到的是当前类 文件的URI目录,不包括自己
	 *如：
	 * 处理前 : /D:/Workspaces/MyEclipse%208.6/myapp/WebRoot/WEB-INF/classes/ 
	 * 处理后 : D:\Workspaces\MyEclipse%208.6\myapp\WebRoot\WEB-INF\classes\
	 * 方法前面加了static
	 * 就不能用this.getClass(),只能用SystemPathUtils.class 因为this是代表他本身类
	 * @return
	 */
	public static String getClassPathBySlash() {
		// String path=this.getClass().getResource("").getPath();
		// String path=this.getClass().getResource(".").getPath();
		String path = SystemPathUtils.class.getResource(Slash).getPath();
		String temp = path.replaceFirst(Slash, "");//Slash斜杠
		String separator =SystemUtils.fileSeparator(); 
		String resultPath = temp.replaceAll(Slash, separator + separator);
		return resultPath;
	}
	
	public static String getInstallPath() {
		String path = SystemPathUtils.class.getResource(Slash).getPath();
		String temp = path.replaceFirst(Slash, "");//Slash斜杠
		String separator =SystemUtils.fileSeparator();
		String resultPath = temp.replaceAll(Slash, separator + separator);
		return resultPath.substring(0,(resultPath.indexOf("WEB-INF")));
		//return resultPath.substring(0,(resultPath.indexOf("WEB-INF")+7))+separator;
	}
	 
	/**
	 * 获取当前工程WebRoot路径
	 * 如:
	 * 处理前：file:/D:/Eclipse%208.6/myapp/WebRoot/WEB-INF/classes/
	 * 处理后：D:\Eclipse%208.6\napp\WebRoot\ 获取当前项目的路径
	 * @return
	 */
	public static String getWebRootPath() {
		// String path =Thread.currentThread().getContextClassLoader().getResource("").toString();
		String path = Thread.currentThread().getContextClassLoader().getResource(".").toString();
		String temp = path.replaceFirst(filePrefix, "").replaceFirst(Web_Info_Class, "");//文件前缀+WEB-INF/classes
		String separator =SystemUtils.fileSeparator(); 
		String resultPath = temp.replaceAll(Slash, separator + separator);
		return resultPath;
	}
	
	/**
	 * 获取工程 webapp的绝对路径，并拼装路径
	 * @param joinPath 要拼接的路径
	 * @return 拼接后的觉得路径
	 */
	public static String filePath(String joinPath){
		String filePath = "";
		try {
			String path = SystemPathUtils.class.getResource("").toURI().getPath();
			int n = path.lastIndexOf("WEB-INF");
			if (n != -1) {
				if ("\\".equals(File.separator)) {
					filePath = path.substring(1, n) + joinPath;//windows系统下
				} else if ("/".equals(File.separator)) {
					filePath = path.substring(0, n) + joinPath;//nux系统下
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}
	 
	 
	
	public static void main(String[] args) {
		 System.out.println(filePath("/123"));
	}
}

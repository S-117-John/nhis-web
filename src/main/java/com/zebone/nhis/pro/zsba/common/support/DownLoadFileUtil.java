package com.zebone.nhis.pro.zsba.common.support;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownLoadFileUtil {

	private final static Logger logger = LoggerFactory.getLogger(DownLoadFileUtil.class);
	
	/**
	 * 从网络Url中下载文件，存在则不下载
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void downLoadFromUrl(String urlStrParam, String fileNameParam, String savePathParam) {
		final String urlStr = urlStrParam;
		final String fileName = fileNameParam;
		final String savePath = savePathParam;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				InputStream inputStream = null;
				FileOutputStream fos = null;
				try {
					URL url = new URL(urlStr);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					// 设置超时间为10秒
					conn.setConnectTimeout(10 * 1000);
					// 防止屏蔽程序抓取而返回403错误
					conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		 
					// 得到输入流
					inputStream = conn.getInputStream();
					// 获取自己数组
					byte[] getData = readInputStream(inputStream);
		 
					// 文件保存位置
					String descDirNames = savePath;
					if (!descDirNames.endsWith(File.separator)) {
						descDirNames = descDirNames + File.separator;
					}
					File saveDir = new File(descDirNames);
					if (!saveDir.exists()) {
						saveDir.mkdirs();
					}
					//生成文件
					String loadFilePath = saveDir + File.separator + fileName;
					File file = new File(loadFilePath);
					if(file.exists()){
						logger.info("文件已存在本地！");
					}else{
						fos = new FileOutputStream(file);
						fos.write(getData);
						logger.info("下载完成:{}", loadFilePath);
					}
				} catch (ConnectException ce){
					logger.error("url不可访问:{}", ce.getLocalizedMessage());
				} catch (Exception e) {
					logger.error("下载文件到本地出现异常:{}", e.getLocalizedMessage());
				}finally{
					//关闭流
					try {
						if (fos != null) fos.close();
						if (inputStream != null) inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}
	
	/**
	 * 从网络Url中下载文件，不检查是否存在，存在则替换
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void downLoadFromUrl2(String urlStr, String fileName, String savePath) {
		InputStream inputStream = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置超时间为10秒
			conn.setConnectTimeout(10 * 1000);
			// 防止屏蔽程序抓取而返回403错误
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
 
			// 得到输入流
			inputStream = conn.getInputStream();
			// 获取自己数组
			byte[] getData = readInputStream(inputStream);
 
			// 文件保存位置
			String descDirNames = savePath;
			if (!descDirNames.endsWith(File.separator)) {
				descDirNames = descDirNames + File.separator;
			}
			File saveDir = new File(descDirNames);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}
			//生成文件
			String loadFilePath = saveDir + File.separator + fileName;
			File file = new File(loadFilePath);
			fos = new FileOutputStream(file);
			fos.write(getData);
			logger.info("下载完成:{}", loadFilePath);
		} catch (ConnectException ce){
			logger.error("url不可访问:{}", ce.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("下载文件到本地出现异常:{}", e.getLocalizedMessage());
		}finally{
			//关闭流
			try {
				if (fos != null) fos.close();
				if (inputStream != null) inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	/**
	 * 从输入流中获取字节数组
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}
	/**
	 * 从网络Url中下载文件
	 * @param urlStr		网络文件url
	 * @param subDir		从网络文件url中哪个目录开始截断
	 * @param thisSericeUri	当前服务的访问路径前缀
	 * @return String  		当前服务中的文件访问路径
	 * @author lpz
	 * @date 2019-1-29
	 */
	public static String downLoadFile(String urlStr, String apiFilePrefix, String subDir, String thisSericeUri) {
		String loadFilePath ="";
		try {
			String fileName = urlStr.substring(urlStr.lastIndexOf("/")+1, urlStr.length());
			String dir = "src/main/static" + urlStr.substring(urlStr.lastIndexOf("/"+subDir), urlStr.lastIndexOf("/")+1);
			String savePath = SystemPathUtils.filePath(dir);
			
			String filePath = savePath + File.separator + fileName;
			File file = new File(filePath);
			if(!file.exists()){
				downLoadFromUrl(apiFilePrefix + urlStr, fileName, savePath);
			}
			
			loadFilePath = thisSericeUri + dir + fileName;
			loadFilePath = loadFilePath.replaceAll("\\\\", "/");
		} catch (Exception e) {
			logger.error("从网络Url中下载文件:{}", e.getLocalizedMessage());
		}
		return loadFilePath;
	}
	
	/**
	 * 将网络图片转成Base64
	 * @param netImagePath
	 * @return
	 */
	public static String netImageToBase64(String netImagePath) {
		String strNetImageToBase64 = "";
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		try {
			// 创建URL
			URL url = new URL(netImagePath);
			final byte[] by = new byte[1024];
			// 创建链接
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(10 * 1000);

			InputStream is = conn.getInputStream();
			// 将内容读取内存中
			int len = -1;
			while ((len = is.read(by)) != -1) {
				data.write(by, 0, len);
			}
			// 对字节数组Base64编码

			strNetImageToBase64 = Base64.getEncoder().encodeToString(data.toByteArray());
			strNetImageToBase64 = strNetImageToBase64.replaceAll("\\r\\n", "");
			System.out.println("网络图片转换Base64:" + strNetImageToBase64);
			// 关闭流
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strNetImageToBase64;
	}
	
	/**
	 * 将本地图片转成Base64
	 * @param imgPath
	 * @return
	 */
	public static String localImageToBase64(String imgPath) {
		String strLocalImageToBase64 = "";
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        // 返回Base64编码过的字节数组字符串
        strLocalImageToBase64 = Base64.getEncoder().encodeToString(Objects.requireNonNull(data));
        strLocalImageToBase64 = strLocalImageToBase64.replaceAll("\\r\\n", "");
        System.out.println("本地图片转换Base64:" + strLocalImageToBase64);
        return strLocalImageToBase64;
    }
	
}

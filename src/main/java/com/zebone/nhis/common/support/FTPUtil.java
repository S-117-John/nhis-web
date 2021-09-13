package com.zebone.nhis.common.support;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import com.zebone.platform.modules.exception.BusException;

public class FTPUtil {
	
	private static String FTP_SERVER = "192.168.200.12";

	//public static  Integer FTP_PORT = 20;
	public static  Integer FTP_PORT = 21;//192.168.0.5上的ftp_mz端口
	public static  String FTP_USER = "jszb";
	public static  String FTP_PWD = "jszb";
	public static final Log logger = LogFactory.getLog(FTPUtil.class);

	public static final String FTP_BASE_HIS_DIR = "ArchiveNew/history/";
	
	
	public static FTPClient ftpClient = new FTPClient();

	static {
		// 设置将过程中使用到的命令输出到控制台
		ftpClient.addProtocolCommandListener(new PrintCommandListener(
				new PrintWriter(System.out)));
		// Ftp服务器地址
		try {
			FTP_SERVER = ApplicationUtils.getPropertyValue("ftpIp", "192.168.200.12");
			FTP_USER = ApplicationUtils.getPropertyValue("ftpUser", "jszb");
			FTP_PWD = ApplicationUtils.getPropertyValue("ftpPwd", "jszb");
			String port = ApplicationUtils.getPropertyValue("ftp_port", "21");
			if(!CommonUtils.isEmptyString(port)){
				FTP_PORT = Integer.parseInt(port);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// 传输超时5分钟
		ftpClient.setDataTimeout(5 * 60 * 1000);
		// 连接超时60秒
		ftpClient.setConnectTimeout(60 * 1000);
		
		
	}

	public enum UploadStatus {
		Create_Directory_Fail, // 远程服务器相应目录创建失败
		Create_Directory_Success, // 远程服务器闯将目录成功
		Upload_New_File_Success, // 上传新文件成功
		Upload_New_File_Failed, // 上传新文件失败
		File_Exits, // 文件已经存在
		Remote_Bigger_Local, // 远程文件大于本地文件
		Upload_From_Break_Success, // 断点续传成功
		Upload_From_Break_Failed, // 断点续传失败
		Delete_Remote_Faild;// 删除远程文件失败
	}

	public enum DownloadStatus {
		Remote_File_Noexist, // 远程文件不存在
		Local_Bigger_Remote, // 本地文件大于远程文件
		Download_From_Break_Success, // 断点下载文件成功
		Download_From_Break_Failed, // 断点下载文件失败
		Download_New_Success, // 全新下载文件成功
		Download_New_Failed;// 全新下载文件失败
	}
	
	
	public static byte[] imag2PDF(byte[] input,String fileName) {
		com.lowagie.text.Document document = new com.lowagie.text.Document();
		ByteArrayOutputStream target = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(document, target);
			document.open();
			Image img = Image.getInstance(input);
			document.addAuthor("EmrArchiveApplication");
			document.addCreationDate();
			document.addCreator("iText library");
			document.addTitle("ScannerImg");
			if(fileName.startsWith("ARC_")){
				img.setWidthPercentage(100);
			}else{
				img.scalePercent(75);
			}
			document.add(img);
		} catch (Exception e) {
			logger.info("##Not Picture Formate," + e.getMessage());
			return input;
		}
		document.close();
		return target.toByteArray();
	}
	 
	/**
	 * 文件上传 
	 * @param content 文件的二进制流
	 * @param remote  上传的目标路径
	 * @param password 文件的加密密码
	 * @return 上传状态
	 */
	public synchronized static Map<String, Object>  uploadFileNew(byte[] content,
			String remote, String password ){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//1.进行文件加密
			if(password!=null)
			content = PdfStamperUtils.encrypt(content, password);			
			//2.ftp服务器初始化			
				//2.1 登录ftp服务器
				connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
			
				//2.2设置传输模式，传输方式以及编码
				ftpClient.enterLocalPassiveMode();
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.setControlEncoding("GBK");
			//3.上传
				//3.1对远程目标目录的处理
				String fileName = remote;
				if (remote.contains("/")) {
					fileName = remote.substring(remote.lastIndexOf("/") + 1);
					// 创建服务器远程目录结构，创建失败直接返回
					if (changeDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
						logger.error("##Create Remote Path Fail,path:" + remote);
						result.put("UploadStatus",
								UploadStatus.Create_Directory_Fail);
						return result;
					}
				}
				// 3.2检查远程是否存在文件，存在则将文件放至历史文件夹或者临时文件夹
				FTPFile[] files = ftpClient.listFiles(new String(fileName
						.getBytes("GBK"), "iso-8859-1"));
				String histFileName = getFtpHistFileName(remote,null);
				if (files.length == 1) {
					// 转的根目录-创建历史目录(archvie/history/20110901)
					ftpClient.changeWorkingDirectory("/");	
					
					if (changeDirecroty(getFtpHistFileName(remote,null), ftpClient) == UploadStatus.Create_Directory_Fail) {
						logger.error("##Create Remote Path Fail,path:" + remote);
						result.put("UploadStatus",
								UploadStatus.Create_Directory_Fail);
						return result;
					}	
					ftpClient.changeWorkingDirectory("/");
					// 文件转移
					boolean sucess = ftpClient.rename(remote, histFileName);
					if (!sucess) {
						logger.error("##remote:" + remote);
						logger.error("##histFileName:" + histFileName);
						logger.error("##Delte Remote File Fail,file222:" + fileName);
						result
								.put("UploadStatus",
										UploadStatus.Delete_Remote_Faild);
						return result;
					}
	
					// 历史文件路径
					result.put("HistoryFileName", histFileName);
					// 转到文件路径以进行写入操作
					changeDirecroty(remote, ftpClient);
				}
			//3.3文件传输
			OutputStream out = ftpClient.appendFileStream(new String(fileName
					.getBytes("GBK"), "iso-8859-1"));
			
			out.write(content, 0, content.length);
			System.out.println(content.length);
			out.flush();
			out.close();
			UploadStatus status = ftpClient.completePendingCommand() ? UploadStatus.Upload_New_File_Success
					: UploadStatus.Upload_New_File_Failed;
			result.put("UploadStatus", status);

			if (UploadStatus.Upload_New_File_Failed == status) {
				// 转的根目录(Directory changed to /f:/Serv-U)
				ftpClient.changeWorkingDirectory("/");

				// 回滚历史文件转移
				boolean flag = ftpClient.rename(histFileName, remote);
				if (flag) {
					result.remove("HistoryFileName");
				}
			}

		} catch (Exception ex) {

		} finally {
			// 断开连接
			disconnect();
		}
		return result;
	};
	
	
	/**
	 * 文件上传 
	 * @param content 文件的二进制流
	 * @param remote  上传的目标路径
	 * @param password 文件的加密密码
	 * @return 上传状态
	 */
	public synchronized static Map<String, Object>  uploadFileNew(byte[] content,
			String remote, String password,String version ){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//1.进行文件加密
			if(password!=null)
			content = PdfStamperUtils.encrypt(content, password);			
			//2.ftp服务器初始化			
				//2.1 登录ftp服务器
				connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
			
				//2.2设置传输模式，传输方式以及编码
				ftpClient.enterLocalPassiveMode();
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.setControlEncoding("GBK");
			//3.上传
				//3.1对远程目标目录的处理
				String fileName = remote;
				if (remote.contains("/")) {
					fileName = remote.substring(remote.lastIndexOf("/") + 1);
					// 创建服务器远程目录结构，创建失败直接返回
					if (changeDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
						logger.error("##Create Remote Path Fail,path:" + remote);
						result.put("UploadStatus",
								UploadStatus.Create_Directory_Fail);
						return result;
					}
				}
				// 3.2检查远程是否存在文件，存在则将文件放至历史文件夹或者临时文件夹
				FTPFile[] files = ftpClient.listFiles(new String(fileName
						.getBytes("GBK"), "iso-8859-1"));
				String histFileName = getFtpHistFileName(remote,version);
				if (files.length == 1) {
					// 转的根目录-创建历史目录(archvie/history/20110901)
					ftpClient.changeWorkingDirectory("/");	
					
					if (changeDirecroty(getFtpHistFileName(remote,version), ftpClient) == UploadStatus.Create_Directory_Fail) {
						logger.error("##Create Remote Path Fail,path:" + remote);
						result.put("UploadStatus",
								UploadStatus.Create_Directory_Fail);
						return result;
					}	
					ftpClient.changeWorkingDirectory("/");
					// 文件转移
					boolean sucess = ftpClient.rename(remote, histFileName);
					if (!sucess) {
						logger.error("##remote:" + remote);
						logger.error("##histFileName:" + histFileName);
						logger.error("##Delte Remote File Fail,file222:" + fileName);
						result
								.put("UploadStatus",
										UploadStatus.Delete_Remote_Faild);
						return result;
					}
	
					// 历史文件路径
					result.put("HistoryFileName", histFileName);
					// 转到文件路径以进行写入操作
					changeDirecroty(remote, ftpClient);
				}
			//3.3文件传输
			OutputStream out = ftpClient.appendFileStream(new String(fileName
					.getBytes("GBK"), "iso-8859-1"));
			
			out.write(content, 0, content.length);
			System.out.println(content.length);
			out.flush();
			out.close();
			UploadStatus status = ftpClient.completePendingCommand() ? UploadStatus.Upload_New_File_Success
					: UploadStatus.Upload_New_File_Failed;
			result.put("UploadStatus", status);

			if (UploadStatus.Upload_New_File_Failed == status) {
				// 转的根目录(Directory changed to /f:/Serv-U)
				ftpClient.changeWorkingDirectory("/");

				// 回滚历史文件转移
				boolean flag = ftpClient.rename(histFileName, remote);
				if (flag) {
					result.remove("HistoryFileName");
				}
			}

		} catch (Exception ex) {

		} finally {
			// 断开连接
			disconnect();
		}
		return result;
	};
	/**
	 * 从远程服务器下载文件
	 * @param remote ftp文件路径 
	 * @return 文件的二进制流
	 */
	public synchronized static ByteArrayOutputStream downloadFile(String path,String Filename){
		
		//1.ftpClient初始化
		connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
		ftpClient.enterLocalPassiveMode();
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		//2.检查远程文件是否存在
		FTPFile[] files = null;
		try {
			ftpClient.changeWorkingDirectory(path);
			files = ftpClient.listFiles(new String(Filename.getBytes("GBK"),
					"iso-8859-1"));
		} catch (UnsupportedEncodingException e) {
			logger.debug(e.getMessage());
			throw new BusException(e.getMessage());
		} catch (IOException e) {
			logger.debug(e.getMessage());
			throw new BusException(e.getMessage());
		}
		if (files == null || files.length != 1) {
			throw new BusException("error.code.ftp.file.not.exist", Filename);
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		InputStream in = null;
		try {
			in = ftpClient.retrieveFileStream(new String(
					Filename.getBytes("GBK"), "iso-8859-1"));
		} catch (UnsupportedEncodingException e) {
			logger.debug(e.getMessage());
			throw new BusException(e.getMessage());
		} catch (IOException e) {
			logger.debug(e.getMessage());
			throw new BusException(e.getMessage());
		}
		if (in == null) {
			throw new BusException("文件下载失败,文件名:" + Filename);
		}
		byte[] bytes = new byte[1024];
		int c;
		try {
			while ((c = in.read(bytes)) != -1) {
				os.write(bytes, 0, c);
			}
		} catch (IOException e) {
			throw new BusException(e.getMessage());
		}
		try {
			os.close();
		} catch (IOException e) {
			throw new BusException(e.getMessage());
		}
		boolean upNewStatus = false;
		try {
			upNewStatus = ftpClient.completePendingCommand();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (upNewStatus) {
			logger.info("##Ftp 下载成功");
		} else {
			throw new BusException("error.code.ftp.file.fail.download",
					Filename);
		}
		return os;
	};
	
	
	
	

	/**
	 * 文件复制移动
	 * @param fileName 文件名-test.mp4
	 * @param from 原路径-AA/BB
	 * @param to  目标路径-CC/DD
	 * @throws IOException 
	 */
	public synchronized static double moveFile(String fileName,String from,String to) throws IOException{
		
		boolean success = true;
		try{
		connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
		// 设置PassiveMode传输
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制流的方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("GBK");
		
		  return copyFile(fileName,from,to);
		}catch (Exception e){
			success = false;
			return 0.0;
		}
		
	};
	
//	/** */
//	/**
//	 * 从FTP服务器上下载文件
//	 * 
//	 * @param remote
//	 *            远程文件路径
//	 * @return 文件二进制流
//	 * @throws BusException
//	 */
//	public static ByteArrayOutputStream download(String remote) {
//		connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
//		// 设置被动模式
//		ftpClient.enterLocalPassiveMode();
//
//		// 设置以二进制方式传输
//		try {
//			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		// 检查远程文件是否存在
//		FTPFile[] files = null;
//		try {
//			files = ftpClient.listFiles(new String(remote.getBytes("GBK"),
//					"iso-8859-1"));
//		} catch (UnsupportedEncodingException e) {
//			logger.debug(e.getMessage());
//			throw new BusException(e.getMessage());
//		} catch (IOException e) {
//			logger.debug(e.getMessage());
//			throw new BusException(e.getMessage());
//		}
//		if (files == null || files.length != 1) {
//			throw new BusException("error.code.ftp.file.not.exist", remote);
//		}
//
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		InputStream in = null;
//		try {
//			in = ftpClient.retrieveFileStream(new String(
//					remote.getBytes("GBK"), "iso-8859-1"));
//		} catch (UnsupportedEncodingException e) {
//			logger.debug(e.getMessage());
//			throw new BusException(e.getMessage());
//		} catch (IOException e) {
//			logger.debug(e.getMessage());
//			throw new BusException(e.getMessage());
//		}
//		if (in == null) {
//			throw new BusException("文件下载失败,文件名:" + remote);
//		}
//		byte[] bytes = new byte[1024];
//		int c;
//		try {
//			while ((c = in.read(bytes)) != -1) {
//				os.write(bytes, 0, c);
//			}
//		} catch (IOException e) {
//			throw new BusException(e.getMessage());
//		}
//		try {
//			os.close();
//		} catch (IOException e) {
//			throw new BusException(e.getMessage());
//		}
//
//		boolean upNewStatus = false;
//		try {
//			upNewStatus = ftpClient.completePendingCommand();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		if (upNewStatus) {
//			logger.info("##Ftp 下载成功");
//		} else {
//			throw new BusException("error.code.ftp.file.fail.download",
//					remote);
//		}
//		return os;
//	}
	
	
	/** */
	/**
	 * 从FTP服务器上下载文件
	 * 
	 * @param remote
	 *            远程文件路径
	 * @param local
	 *            本地文件路径
	 * @return 上传的状态
	 * @throws IOException
	 */
	public static DownloadStatus download(String remote, String local)
			throws IOException {
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		DownloadStatus result;

		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(new String(
				remote.getBytes("GBK"), "iso-8859-1"));
		if (files.length != 1) {
			logger.info("远程文件不存在");
			return DownloadStatus.Remote_File_Noexist;
		}

		long lRemoteSize = files[0].getSize();
		File f = new File(local);
		// 本地存在文件，进行断点下载
		if (f.exists()) {
			long localSize = f.length();
			// 判断本地文件大小是否大于远程文件大小
			if (localSize >= lRemoteSize) {
				logger.info("本地文件大于远程文件，下载中止");
				return DownloadStatus.Local_Bigger_Remote;
			}

			// 进行断点续传，并记录状态
			FileOutputStream out = new FileOutputStream(f, true);
			ftpClient.setRestartOffset(localSize);
			InputStream in = ftpClient.retrieveFileStream(new String(remote
					.getBytes("GBK"), "iso-8859-1"));
			byte[] bytes = new byte[1024];
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
			}
			in.close();
			out.close();
			boolean isDo = ftpClient.completePendingCommand();
			if (isDo) {
				result = DownloadStatus.Download_From_Break_Success;
			} else {
				result = DownloadStatus.Download_From_Break_Failed;
			}
		} else {
			OutputStream out = new FileOutputStream(f);
			InputStream in = ftpClient.retrieveFileStream(new String(remote
					.getBytes("GBK"), "iso-8859-1"));
			byte[] bytes = new byte[1024];
			long step = lRemoteSize / 100;
			long process = 0;
			long localSize = 0L;
			int c;
			while ((c = in.read(bytes)) != -1) {
				out.write(bytes, 0, c);
				localSize += c;
				long nowProcess = localSize / step;
				if (nowProcess > process) {
					process = nowProcess;
					if (process % 10 == 0)
						logger.info("下载进度：" + process);
				}
			}
			in.close();
			out.close();
			boolean upNewStatus = ftpClient.completePendingCommand();
			if (upNewStatus) {
				result = DownloadStatus.Download_New_Success;
			} else {
				result = DownloadStatus.Download_New_Failed;
			}
		}
		return result;
	}

	public static String getFtpHistFileName(String filePath,String version) {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = df.format(new Date(System.currentTimeMillis()));
		String hisFileName = StringUtils.substringAfterLast(filePath, "/");
		if(hisFileName.contains(".PDF")){
			hisFileName = hisFileName.replace(".PDF", "_" + time+"_"+version + ".PDF");
		}else{
			hisFileName = hisFileName.replace(".pdf", "_" + time+"_"+version + ".pdf");
		}
		
		
		return FTP_BASE_HIS_DIR + time + "/" + hisFileName;
	}

	
	
	 /** 
     *  
     * <p>删除ftp上的文件</p> 
     * @author tangw 2010-12-26 
     * @param srcFname 
     * @return true || false 
     */  
    public static boolean removeFile(String srcFname){  
        boolean flag = false;  
    	connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
		// 设置PassiveMode传输
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制流的方式传输
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ftpClient.setControlEncoding("GBK");
        if( ftpClient!=null ){  
            try {  
                flag = ftpClient.deleteFile(srcFname);  
            } catch (IOException e) {  
                e.printStackTrace();  
              
            }  
        }  
        return flag;  
    }
	
//	/**
//	 * 转移文件
//	 * @param from 源文件路径
//	 * @param dest 目标文件路径
//	 * @return
//	 * @throws IOException
//	 */
//	public synchronized static Map<String, Object> moveFile(String from,
//			String dest) throws IOException {
//		Map<String, Object> result = new HashMap<String, Object>();
//		String histFileName = getFtpHistFileName(dest);
//		connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
//
//		// 设置PassiveMode传输
//		ftpClient.enterLocalPassiveMode();
//		// 设置以二进制流的方式传输
//		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//		ftpClient.setControlEncoding("GBK");
//
//		// 对远程目录的处理
//		String fileName = dest;
//		if (dest.contains("/")) {
//			fileName = dest.substring(dest.lastIndexOf("/") + 1);
//			// 创建服务器远程目录结构，创建失败直接返回
//			if (changeDirecroty(dest, ftpClient) == UploadStatus.Create_Directory_Fail) {
//				logger.error("##Create Remote Path Fail,path:" + dest);
//				result.put("UploadStatus", UploadStatus.Create_Directory_Fail);
//				return result;
//			}
//		}
//
//		// 检查远程是否存在文件
//		FTPFile[] files = ftpClient.listFiles(new String(dest.getBytes("GBK"),
//				"iso-8859-1"));
//		if (files.length == 1) {
//			// 转的根目录(Directory changed to /f:/Serv-U)
//			changeDirecroty("/", ftpClient);
//
//			// 创建历史目录(archvie/history/20110901)
//			if (changeDirecroty(getFtpHistFileName(dest), ftpClient) == UploadStatus.Create_Directory_Fail) {
//				logger.error("##Create Remote Path Fail,path:" + dest);
//				result.put("UploadStatus", UploadStatus.Create_Directory_Fail);
//				return result;
//			}
//
//			// 转的根目录(Directory changed to /f:/Serv-U)
//			changeDirecroty("/", ftpClient);
//
//			// 文件转移
//			boolean sucess = ftpClient.rename(dest, histFileName);
//			if (!sucess) {
//				logger.error("##dest:" + dest);
//				logger.error("##histFileName:" + histFileName);
//				logger.error("##Delte Remote File Fail,file111:" + fileName);
//				result.put("UploadStatus", UploadStatus.Delete_Remote_Faild);
//				return result;
//			}
//
//			// 历史文件路径
//			result.put("HistoryFileName", histFileName);
//		}
//
//		// 对远程目录的处理
//		String fromFileName = from;
//		if (fromFileName.contains("/")) {
//			fromFileName = dest.substring(from.lastIndexOf("/") + 1);
//			// 创建服务器远程目录结构，创建失败直接返回
//			if (changeDirecroty(fromFileName, ftpClient) == UploadStatus.Create_Directory_Fail) {
//				logger.error("##Create Remote Path Fail,path:" + fromFileName);
//				result.put("UploadStatus", UploadStatus.Create_Directory_Fail);
//				return result;
//			}
//		}
//
//		// 检查远程是否存在文件
//		FTPFile[] fromFiles = ftpClient.listFiles(new String(fromFileName
//				.getBytes("GBK"), "iso-8859-1"));
//		if (fromFiles.length == 1) {
//			// 转的根目录(Directory changed to /f:/Serv-U)
//			changeDirecroty("/", ftpClient);
//
//			// 文件转移
//			boolean sucess = ftpClient.rename(from, dest);
//		}
//
//		UploadStatus status = ftpClient.completePendingCommand() ? UploadStatus.Upload_New_File_Success
//				: UploadStatus.Upload_New_File_Failed;
//		result.put("UploadStatus", status);
//
//		if (UploadStatus.Upload_New_File_Failed == status) {
//			// 转的根目录(Directory changed to /f:/Serv-U)
//			changeDirecroty("/", ftpClient);
//
//			// 回滚历史文件转移
//			boolean flag = ftpClient.rename(histFileName, dest);
//			if (flag) {
//				result.remove("HistoryFileName");
//			}
//		}
//
//		// 断开连接
//		disconnect();
//
//		return result;
//	}
	
	/**
	 * 连接到FTP服务器
	 * 
	 * @param hostname
	 *            主机名
	 * @param port
	 *            端口
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 是否连接成功
	 * @throws IOException
	 */
	public static boolean connect(String hostname, int port, String username,
			String password) {
		logger.info("##Connect To Ftp Server With Parameters[IP:" + hostname
				+ ",PORT:" + port + ",UserName:" + username + " ,Password:"
				+ password);
		String message = null;
		try {
			ftpClient.connect(hostname, port);
		} catch (SocketException e) {
			message ="FTP服务器连接失败！"+ hostname+String
							.valueOf(port)+ username+password+ e.getMessage();
			throw new BusException(message);
		} catch (IOException e) {
			message = 
					"cant.not.connect.to.ftp.server"+ hostname+ String
							.valueOf(port)+ username+ password+e.getMessage();
			throw new BusException(message);
		}
		ftpClient.setControlEncoding("GBK");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			try {
				ftpClient.login(username, password);
				return true;
			} catch (IOException e) {
				message ="cant.not.connect.to.ftp.server"+hostname+String
								.valueOf(port)+username+password+e
								.getMessage();
				colse(hostname, port, username, password);
				throw new BusException(message);
			}
		}
		colse(hostname, port, username, password);
		return false;
	}

	public static boolean connect(){
		return connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
	}
	private static void colse(String hostname, int port, String username,
			String password) {
		disconnect();
	}
	
	

	/**
	 * 断开与远程服务器的连接
	 * 
	 * @throws IOException
	 */
	public static void disconnect() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	/**
	 * 递归创建远程服务器目录
	 * 
	 * @param remote
	 *            远程服务器文件绝对路径
	 * @param ftpClient
	 *            FTPClient 对象
	 * @return 目录创建是否成功
	 * @throws IOException
	 */
	public static UploadStatus changeDirecroty(String remote,
			FTPClient ftpClient) throws IOException {
		UploadStatus status = UploadStatus.Create_Directory_Success;
		String directory = null;
		// 转到Ftp指定的根(默认)目录
		if (remote.equals("./")) {
			directory = ".";
		} else if (remote.equals("/")) {
			directory = "";
		} else if (remote.contains("/")) {
			directory = remote.substring(0, remote.lastIndexOf("/") + 1);
		} else {
			directory = remote;
		}
		if (!directory.equals("/")
				&& !ftpClient.changeWorkingDirectory(new String(directory
						.getBytes("GBK"), "iso-8859-1"))) {
			// 如果远程目录不存在，则递归创建远程服务器目录
			int start = 0;
			int end = 0;
			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf("/", start);
			while (true) {
				String subDirectory = new String(remote.substring(start, end)
						.getBytes("GBK"), "iso-8859-1");
				if (!ftpClient.changeWorkingDirectory(subDirectory)) {
					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						logger.info("创建目录失败");
						return UploadStatus.Create_Directory_Fail;
					}
				}

				start = end + 1;
				end = directory.indexOf("/", start);

				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return status;
	}

	 /** 
	 * 复制文件. 
	 *  
	 * @param sourceFileName 
	 * @param targetFile 
	 * @throws IOException 
	 */  
	public static double copyFile(String sourceFileName, String sourceDir, String targetDir) throws IOException {  

		ByteArrayInputStream in = null;  
	    ByteArrayOutputStream fos = new ByteArrayOutputStream();
	    double res = 0.0;
	    try {  
	        if (!existDirectory(targetDir)) {  
	            createDirectory(targetDir);  
	        }  
	         
	        // 变更工作路径  
	        ftpClient.changeWorkingDirectory("/");
	        ftpClient.changeWorkingDirectory(sourceDir);  
	        
	        // 将文件读到内存中  
	        ftpClient.retrieveFile(new String(sourceFileName.getBytes("GBK"), "iso-8859-1"), fos);  
	        res = fos.toByteArray().length;
	        in = new ByteArrayInputStream(fos.toByteArray()); 
	        
	        if (in != null) {  
	            ftpClient.changeWorkingDirectory(targetDir);  
	        	OutputStream out = ftpClient.appendFileStream(new String(sourceFileName
	    				.getBytes("GBK"), "iso-8859-1"));
	            byte[] bytes = new byte[1024];
	    		int c;
	    		while ((c = in.read(bytes)) != -1) {
	    			out.write(bytes, 0, c);
	    			
	    		}
	    		out.flush();
	    		in.close();
	    		out.close();
	        }  
	        return res;
	    } finally {  
	        // 关闭流  
	        if (in != null) {  
	            in.close();  
	        }  
	        if (fos != null) {  
	            fos.close();  
	        }  
	    }  
	}  
	  
	/** 
	 * 复制文件夹. 
	 *  
	 * @param sourceDir 
	 * @param targetDir 
	 * @throws IOException 
	 */  
	public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {  
	    // 新建目标目录  
	    if (!existDirectory(targetDir)) {  
	        createDirectory(targetDir);  
	    }  
	    // 获取源文件夹当前下的文件或目录  
	    FTPFile[] ftpFiles = ftpClient.listFiles(sourceDir);  
	    for (int i = 0; i < ftpFiles.length; i++) {  
	        if (ftpFiles[i].isFile()) {  
	            copyFile(ftpFiles[i].getName(), sourceDir, targetDir);  
	        } else if (ftpFiles[i].isDirectory()) {  
	            copyDirectiory(sourceDir + "/" + ftpFiles[i].getName(), targetDir + "/" + ftpFiles[i].getName());  
	        }  
	    }  
	}  
	
    /** 
     * 检查文件夹在当前目录下是否存在 
     *  
     * @param path 目录路径 
     * @return 
     * @throws IOException boolean 
     */  
    public static boolean existDirectory(String path) throws IOException {  
        boolean flag = false;  
        FTPFile[] ftpFileArr = ftpClient.listFiles();  
        for (FTPFile ftpFile : ftpFileArr) {  
            if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {  
                flag = true;  
                break;  
            }  
        }  
        return flag;  
    }   
	
    /** 
     * 创建文件目录 
     *  
     * @param pathName 目录路径 
     * @return 
     * @throws IOException boolean 
     */  
    public static boolean createDirectory(String pathName) throws IOException {  
        String[] path = pathName.split("/");  
        FTPFile[] file = null;  
        for(int j=0;j<path.length;j++) {  
            try {  
                file = ftpClient.listFiles(path[j]);  
                if(file.length==0) {  
                    throw new Exception();  
                }  
            }catch(Exception e) {  
                //不存在此目录  
            	String dir = new String(path[j]
						.getBytes("GBK"), "iso-8859-1");
            	ftpClient.setControlEncoding("GBK");
                ftpClient.makeDirectory(dir);  
            }finally {  
                ftpClient.changeWorkingDirectory(path[j]);  
            }  
        }  
        return true;  
    }  
    
    /**
     * 改名FTP上的文件 
     * @param srcFname
     * @param targetFname
     * @return
     */
    public synchronized static boolean renameFile(String dir,String srcFname,String targetFname){    
        boolean flag = false;    
        
        if( ftpClient!=null ){    
            try {    
            	connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
        		// 设置PassiveMode传输
        		ftpClient.enterLocalPassiveMode();
        		// 设置以二进制流的方式传输
        		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        		ftpClient.setControlEncoding("GBK");
        		ftpClient.changeWorkingDirectory(dir);
                flag = ftpClient.rename(srcFname,targetFname);    
            } catch (IOException e) {    
                e.printStackTrace();    
                disconnect();    
            }    
        }    
        return flag;    
    }

    public static   byte[] input2byte(InputStream inStream)
	        throws IOException
	    {
	        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
	        byte[] buff = new byte[100];
	        int rc = 0;
	        while ((rc = inStream.read(buff, 0, 100)) > 0)
	        {
	            swapStream.write(buff, 0, rc);
	        }
	        byte[] in2b = swapStream.toByteArray();
	        
	        swapStream.close();
	        
	        return in2b;
	    }
    
    
	public synchronized static double moveFile(String fileName, String from,
			String to, String newFileName) {

		
		boolean success = true;
		double size = 0.0;
		try{
		connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
		// 设置PassiveMode传输
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制流的方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.setControlEncoding("GBK");
		
		 size = copyFile(fileName,from,to);
		 ftpClient.changeWorkingDirectory(to);
		 renameFile(to,fileName,newFileName); 
		}catch (Exception e){
			success = false;
			disconnect();
			return size;			    
		}finally{
			 disconnect();   
		}
		return size;
		
	}    
	
	public synchronized static List<String> getFileListPath(String dir) {		
		List<String> paths =  new ArrayList<String>();
		try{
			connect(FTP_SERVER, FTP_PORT, FTP_USER, FTP_PWD);
			// 设置PassiveMode传输
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制流的方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding("GBK");
			 boolean flag = ftpClient.changeWorkingDirectory(dir);
			 if(!flag)return paths;
			FTPFile[] files =  ftpClient.listFiles();
			String prefix = "ftp://";
			 if(files!=null && files.length>0){
				 for(FTPFile file : files){
					 if(!file.isFile()) continue;
					 String filename = file.getName();
					 if(CommonUtils.isEmptyString(filename))continue;
					 StringBuilder path = new StringBuilder(prefix);
					 if(!CommonUtils.isEmptyString(FTP_PWD)){
							path.append(FTP_USER);
							path.append(":");
							path.append(FTP_PWD);
							path.append("@");
					  }
					 path.append(FTP_SERVER);
					 path.append(":");
					 path.append(FTP_PORT);
					 path.append(dir);
					 path.append("/");
					 path.append(filename);
					 paths.add(path.toString()); 
					
				 }
			 }
			}catch (Exception e){
				disconnect();			    
			}finally{
				 disconnect();   
			}
		
		return paths;
	}
	
	public static void  main(String [] args) throws IOException{
		FTPUtil.connect("192.168.0.5",123,"ftp_mz_read","ftp_mz_read");
		int a = ftpClient.getConnectTimeout();
		int b = ftpClient.getDefaultTimeout();
		long c = ftpClient.getControlKeepAliveReplyTimeout();
		long d = ftpClient.getControlKeepAliveTimeout();
		int  e = ftpClient.getSoTimeout();
		
		int a1 = ftpClient.getConnectTimeout();
		int b1 = ftpClient.getDefaultTimeout();
		long c1 = ftpClient.getControlKeepAliveReplyTimeout();
		long d1 = ftpClient.getControlKeepAliveTimeout();
		int  e1 = ftpClient.getSoTimeout();
		
		System.out.println("a="+a+":"+"a1="+a1);
		System.out.println("b="+b+":"+"b1="+b1);
		System.out.println("c="+c+":"+"c1="+c1);
		System.out.println("d="+d+":"+"d1="+d1);
		System.out.println("e="+e+":"+"e1="+e1);
	}
}

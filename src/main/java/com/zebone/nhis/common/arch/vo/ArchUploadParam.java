package com.zebone.nhis.common.arch.vo;

import java.io.IOException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.net.ftp.FTPFile;

import com.zebone.nhis.common.support.FTPUtil;


@XmlRootElement(name = "request")
public class ArchUploadParam {
	
	private UploadBody uploadBody;
	
	private UploadHead uploadHead;

	
	
	
	
	
	@XmlElement(name="body")
	public UploadBody getUploadBody() {
		return uploadBody;
	}







	public void setUploadBody(UploadBody uploadBody) {
		this.uploadBody = uploadBody;
	}






	@XmlElement(name="head")
	public UploadHead getUploadHead() {
		return uploadHead;
	}







	public void setUploadHead(UploadHead uploadHead) {
		this.uploadHead = uploadHead;
	}







	public static void main(String [] args) throws IOException{
	  
		String xml = "<request><head>"+//
		 "<visittype>3</visittype>"+// --就诊类型1门诊2急诊3住院4体检
		 "<docname> HIS_010293128.pdf</docname>"+// --文件名称
		 "<doctype> </doctype> -"+//-文件类型：检查，检验，病历等
		"<sysname>HIS</sysname> "+// --系统名称
		"<date>2017-06-16 10:39:00</date>"+// --报告日期
		"<user>张小青111</user> "+//  --操作员
		"<size>32768000</size>"+// --文件大小
		"<memo>住院电子病历-出院小结</memo>"+// --说明
		"<paticode>0123344</paticode>"+// --患者编码
		"<patiname>王鹤松</patiname>"+//--姓名
		"<visitcode>09238019</visitcode>"+//--就诊编码
		"<visitdate>2017-06-14</visitdate>"+//--就诊日期
		"<path>../2001~4000/0123344_王鹤松/09238019_20170505/</path>"+//--文件路径
		"<status>0</status>"+//  --0未归档1已归档
		"</head><body>"
		+ "<filecontent>aaaaa</filecontent></body></request>";
	
		
		FTPUtil.connect("192.168.200.12",20,"jszb","jszb");
		FTPUtil.ftpClient.enterLocalPassiveMode();
		FTPFile[] files = FTPUtil.ftpClient.listFiles("/archive");
		for(FTPFile file : files){
			System.out.println(file.getName());
		}
		
		
		
		
		
//		FTPUtil.uploadFileNew(xml.toString().getBytes(), "/archive/test2.xml", null);
//		 OutputFormat format = OutputFormat.createPrettyPrint();
//        try {
//        	
//            // 创建XMLWriter对象
//            XMLWriter writer = new XMLWriter(FTPUtil.downloadFile("/archive", "test2.xml"), format);
//
//            //设置不自动进行转义
//            writer.setEscapeText(false);
//
//            // 生成XML文件
//            writer.write(document);
//
//            //关闭XMLWriter对象
//            writer.close();
//            
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		
		
	}
	
}

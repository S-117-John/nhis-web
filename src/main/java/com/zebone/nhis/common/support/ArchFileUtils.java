package com.zebone.nhis.common.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ArchFileUtils {
	
	 public static String TEMPPATH = "D:\\TEMP\\";
	 
	 public static String SUFFIX = ".txt";
	
	 //读取文件
	 public static String writeInFile(String fileName) throws IOException{
		 
		 //1.入参检验
		 if(CommonUtils.isEmptyString(fileName)){
			 return null;
		 }
		 
		 //2.写入指定目录
         StringBuffer str = new StringBuffer("");
         String count = "";
         BufferedReader bf = null;
         InputStreamReader isr = new InputStreamReader(new FileInputStream(TEMPPATH+fileName), "GBK");
         try {
             // 使用字符流对文件进行读取
              bf = new BufferedReader(isr);
             while (true) {
                 if ((count = bf.readLine()) != null) {
                     str = str.append(count);
                 } else {
                     break;
                 }
             }
             // 关闭流
             isr.close();
             bf.close();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
             return null;
         }finally{
        	 isr.close();
        	 if(bf!=null){
        		 bf.close(); 
        	 }
         }
         return str.toString();
     }

    //读取文件
    public static String writeInFiles(String fileName) throws IOException{

        //1.入参检验
        if(CommonUtils.isEmptyString(fileName)){
            return null;
        }

        //2.写入指定目录
        StringBuffer str = new StringBuffer("");
        String count = "";
        BufferedReader bf = null;
        InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "GBK");
        try {
            // 使用字符流对文件进行读取
            bf = new BufferedReader(isr);
            while (true) {
                if ((count = bf.readLine()) != null) {
                    str = str.append(count);
                } else {
                    break;
                }
            }
            // 关闭流
            isr.close();
            bf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }finally{
            isr.close();
            if(bf!=null){
                bf.close();
            }
        }
        return str.toString();
    }
	 
	 
	 //字符流写入方法
     public  static boolean getReader(String file,String fileName){
    	 if(CommonUtils.isEmptyString(fileName)){
    		 return false;
    	 }
    	 fileName =fileNameTrans(fileName);
    	 boolean flag = false;
    	 PrintWriter pw = null;
    	 OutputStreamWriter outstream = null;
          try {
        	  outstream = new OutputStreamWriter(new FileOutputStream(fileName), "GBK");
              //其中true表示在原本文件内容的尾部添加，若不写则表示清空文件后再添加内容
               pw=new PrintWriter(outstream);
              pw.write(file);
              outstream.close();
              pw.close();
              flag =true;
          } catch (IOException e) {
              e.printStackTrace();
              flag = false;
          }finally{
        	  if(outstream!=null){
        		  try {
					outstream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	  }
        	  if(pw!=null){
        		  pw.close();
        	  }
          }
          
          return flag;
    }
     /** 
      * 删除单个文件 
      * @param   sPath 被删除文件path 
      * @return 删除成功返回true，否则返回false 
      */  
     public static boolean deleteFile(String sPath) {  
         boolean flag = false;  
         File file = new File(sPath);  
         // 路径为文件且不为空则进行删除  
         if (file.isFile() && file.exists()) {  
             file.delete();  
             flag = true;  
         }  
         return flag;  
     }  
     
     
     public static String fileName(String file){
    	 if(CommonUtils.isEmptyString(file)){
    		 return null;
    	 }
    	 if(file.lastIndexOf(".")!=-1){
    		 file = file.substring(0,file.lastIndexOf("."));
    	 }
    	 return file;
     }
     
     public static String fileNameTrans(String fileName){
    	 
    	 return fileName(fileName)!=null?TEMPPATH+fileName(fileName)+SUFFIX:null;
     }
 	 public static String filestr = null;
	
 	 public static void main(String[] args) throws IOException {
 		 
 		
// 		 filestr = ArchFileUtils.writeInFile("upload.txt");
// 	     String name = "C:\\TEMP\\";
// 		 long start1 = System.currentTimeMillis();
// 		 for(int i = 0;i<1000;i++){
// 			     
// 			      long start = System.currentTimeMillis();
// 			      ArchFileUtils.getReader(filestr,name+i+".txt");
// 			      //deleteFile(name+i+".txt");
// 		          long end = System.currentTimeMillis();
// 		          System.out.println("文件为："+(name+i+".txt")+"用时："+(end-start));
// 		          
// 		 }
// 		 long end1 = System.currentTimeMillis();
//
	     System.out.println(fileName("1111111111w"));
 	 }
 	 
}

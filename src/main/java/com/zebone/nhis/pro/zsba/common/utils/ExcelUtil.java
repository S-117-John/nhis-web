package com.zebone.nhis.pro.zsba.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableFont.FontName;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 导出Excel工具类
 * @author cavancao
 * @date 2019-01-29
 */
public class ExcelUtil {
	
    private static String defaultEncoding = "GBK";
    
    /**
     * 1创建工作簿，用于response的输出流返回
     * @param response HttpServletResponse
     * @param fileName 文件名
     * @return
     */
    public static WritableWorkbook createWorkBook(HttpServletResponse response, OutputStream out, String fileName){
        WritableWorkbook wwb = null;
        response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename="+Encodes.urlEncode(fileName));
		wwb = createWorkBook(out);
        return wwb;
    }
    
    /**
     * 2创建工作簿，用于导出文件到某个路径
     * @param exportPath
     * @return
     */
    public static WritableWorkbook createWorkBook(String exportPath){
        File file = new File(exportPath);
        if(!file.exists()||file.isDirectory()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WritableWorkbook wwb = createWorkBook(file);
        return wwb;
    }
    
    /**
     * 创建WritableWorkbook
     * @param obj
     * @return
     */
    private static WritableWorkbook createWorkBook(Object obj){
        if(obj==null){
            return null;
        }
        WorkbookSettings setting = new WorkbookSettings();
        setting.setEncoding(defaultEncoding);
        WritableWorkbook wwb = null;
        try {
            if(obj instanceof File){
                File file = (File)obj;
                wwb = Workbook.createWorkbook(file,setting);
            }else if(obj instanceof OutputStream){
            	OutputStream os = (OutputStream)obj;
                wwb = Workbook.createWorkbook(os,setting);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wwb;
    }
    
    /**
     * 根据SheetNames数量创建对应数量的sheet
     * @param sheetNames
     * @param wwb
     * @return
     */
    public static List<WritableSheet> createSheet(String[] sheetNames,WritableWorkbook wwb){
        if(sheetNames==null||sheetNames.length==0){
            return null;
        }
        if(wwb==null){
            return null;
        }
        int sheetNum = sheetNames.length;
        List<WritableSheet> list = new ArrayList<WritableSheet>(sheetNum);
        
        for(int i= 0; i<sheetNum ; i++){
            WritableSheet ws = wwb.createSheet(sheetNames[i], i);
            list.add(ws);
        }
        
        return list;
        
    }
    
    
    /**
     * 创建title格式
     * @return
     * @throws WriteException 
     */
    public static WritableCellFormat getTitleFormat( int fontSize) {
        FontName fontName = WritableFont.createFont("宋体");
        boolean isItalic = false;//是否斜体
        
        //参数依次是：字体设置/字体大小/字体粗细/是否是斜体/下划线类型/颜色
        WritableFont titleFont = new WritableFont(fontName, fontSize, WritableFont.BOLD, isItalic, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
        
    	try {
    		//设置边框
      		titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
      		//设置自动换行
      		titleFormat.setWrap(true);
      		//设置文字居中对齐方式
      		titleFormat.setAlignment(Alignment.CENTRE);
      		//设置垂直居中
      		titleFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return titleFormat;
    }
    
    
    /**
     * 获取内容格式
     * @return
     */
    public static WritableCellFormat getContentFormat(){
        WritableCellFormat contentFormat = new WritableCellFormat();
        try {
            contentFormat.setWrap(true);//是否换行
            contentFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);//全框/细线/黑色
            contentFormat.setAlignment(Alignment.LEFT);//水平居左
        } catch (WriteException e) {
            e.printStackTrace();
        }
        
        return contentFormat;
    }
    
    /**
     * 单元格插入内容
     * @param sheet 表单对象
     * @param columnIndex 列
     * @param rowIndex 行
     * @param content 内容
     * @param format 格式
     */
    public static void addCell(WritableSheet sheet,int columnIndex,int rowIndex,Object content,WritableCellFormat format){
        WritableCell cell = null;
        
        if(content==null){
            content = "";
        }
        
        if(content instanceof Integer){
            if(format!=null){
                cell = new Number(columnIndex, rowIndex, (Integer)content, format);
            }else{
                cell = new Number(columnIndex, rowIndex, (Integer)content);
            }
            
        } else if(content instanceof Long){
            if(format!=null){
                cell = new Number(columnIndex, rowIndex, (Long)content, format);
            }else{
                cell = new Number(columnIndex, rowIndex, (Long)content);
            }
            
        } else if(content instanceof Short){
            if(format!=null){
                cell = new Number(columnIndex, rowIndex, (Short)content, format);
            }else{
                cell = new Number(columnIndex, rowIndex, (Short)content);
            }
            
        } else if(content instanceof Double){
            if(format!=null){
                cell = new Number(columnIndex, rowIndex, (Double)content, format);
            }else{
                cell = new Number(columnIndex, rowIndex, (Double)content);
            }
            
        } else if(content instanceof Float){
            if(format!=null){
                cell = new Number(columnIndex, rowIndex, (Float)content, format);
            }else{
                cell = new Number(columnIndex, rowIndex, (Float)content);
            }
            
        } else if(content instanceof BigDecimal){
        	if(format!=null){
                cell = new Number(columnIndex, rowIndex, Double.parseDouble(content.toString()), format);
            }else{
                cell = new Number(columnIndex, rowIndex, Double.parseDouble(content.toString()));
            }
        	
        } else {
            if(format!=null){
                cell = new Label(columnIndex, rowIndex, String.valueOf(content), format);
            }else{
                cell = new Label(columnIndex, rowIndex, String.valueOf(content));
            }
        }
        
        try {
            sheet.addCell(cell);
            sheet.setColumnView(columnIndex, 20);//设置单元格列宽(或者设置为内容长度的3倍)
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 解析一个list对象插入到sheet
     * @param sheet
     * @param dataList
     * @param titles
     */
	public static void setSheetData(WritableSheet sheet, List<Object[]> dataList, String[] titles){
        if(sheet==null || dataList==null || titles==null){
            return;
        }
        WritableCellFormat format = getContentFormat();
        
        int rowIndex = 1;//默认第一行还有个大标题，如果不需要那个大标题，此处改成0
        //先将列名插入sheet
        for(int columnIndex=0;columnIndex<titles.length;columnIndex++){
            addCell(sheet, columnIndex, rowIndex, titles[columnIndex], format);
        }
        
        rowIndex++;
        for(int i=0;i<dataList.size();i++){
        	Object[] data = dataList.get(i);
        	for(int columnIndex=0; columnIndex<data.length; columnIndex++){
        		addCell(sheet, columnIndex, rowIndex, data[columnIndex], format);
        	}
            rowIndex++;
        }
    }
	
    public static void main(String[] args) {
        //导出开始
        WritableWorkbook wwb = null;
        try {
            wwb = createWorkBook("D:\\Test\\excel\\test.xls");
            String[] names = {"数据导出Sheet1", "数据导出Sheet2"};
            List<WritableSheet> sheets = createSheet(names, wwb);
            
            WritableCellFormat titleFormat = getTitleFormat(12);
            //针对不同sheet填充数据
            for(WritableSheet sheet : sheets){
            	if(sheet.getName().equals("数据导出Sheet1")){
            		//模拟标题
                    addCell(sheet, 0, 0, "测试数据导出2", titleFormat);
                    //模拟表头
                    String[] titles = {"姓名","性别","年龄"};
                    //模拟数据
                    List<Object[]> dataList = new ArrayList<Object[]>();
                    dataList.add(new Object[]{"王小二","男",25});
                    dataList.add(new Object[]{"张小三","女",23});
                    //填充数据
                    setSheetData(sheet, dataList, titles);
                    //合并标题
                    sheet.mergeCells(0, 0, titles.length-1, 0);
            	}
            	if(sheet.getName().equals("数据导出Sheet2")){
            		//模拟标题
                    addCell(sheet, 0, 0, "测试数据导出2", titleFormat);
                    //模拟表头
                    String[] titles = {"姓名","性别","年龄","地址"};
                    //模拟数据
                    List<Object[]> dataList = new ArrayList<Object[]>();
                    dataList.add(new Object[]{"赵老六","男",67,"广东省"});
                    dataList.add(new Object[]{"李小花","女",45,"广东省"});
                    //填充数据
                    setSheetData(sheet, dataList, titles);
                    //合并标题
                    sheet.mergeCells(0, 0, titles.length-1, 0);
            	}
            }
            wwb.write();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e1) {
			e1.printStackTrace();
		} catch (WriteException e2) {
			e2.printStackTrace();
        } finally{
            if(wwb!=null){
                try {
                    wwb.close();
                    wwb = null;
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("系统提示：Excel导出完成...");
        }
    }
}
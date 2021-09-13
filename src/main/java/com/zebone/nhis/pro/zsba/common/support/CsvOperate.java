package com.zebone.nhis.pro.zsba.common.support;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class CsvOperate {
	
	/**
	 * 读取CSV文件
	 */
	public static ArrayList<String[]> readCsv(String csvFilePath){
		ArrayList<String[]> csvList = new ArrayList<String[]>(); // 用来保存数据
		try {
			CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("GB2312")); // 一般用这编码读就可以了
			reader.readHeaders(); // 跳过表头 如果需要表头的话，不要写这句。
			while (reader.readRecord()) { // 逐行读入除表头的数据
				csvList.add(reader.getValues());
			}
			reader.close();
			/*for (int row = 0; row < csvList.size(); row++) {
				String cell = csvList.get(row)[0]; // 取得第row行第0列的数据
				System.out.println(cell);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return csvList;
	}
	
	/**
	 * 写入CSV文件
	 */
	public static void writeCsv(String csvFilePath,String[] contents) {
		try {
			CsvWriter wr = new CsvWriter(csvFilePath, ',',Charset.forName("GB2312"));
			wr.writeRecord(contents);
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package com.zebone.nhis.common.support;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public class PdfStamperUtils {
	private   Logger logger = LogManager.getLogger(PdfStamperUtils.class);
	private static final String SEPARATOR = "&&";

	// 所有者密码
	public static final String OWNER_PASSWORD = "admin";

	// 生成临时文件前缀
	private static final String tmp = "temp";

	/**
	 * txt文件转换为pdf文件
	 * 
	 * @param txtFile
	 *            txt文件路径
	 * @param pdfFile
	 *            pdf文件路径
	 * @param userPwd
	 *            用户密码
	 * @param stmpWord
	 *            水印内容
	 * @param permission
	 *            操作权限
	 */
	public static void txt2PDF(String txtFile, String pdfFile, String userPwd,
			String stmpWord, int permission) {
		try {
			// 生成临时文件
			File pdf = File.createTempFile(tmp, ".pdf");

			// 创建pdf文件到临时文件
			if (createPDF(txtFile, pdf)) {
				// 增加水印和加密
				stamp(pdf.getPath(), pdfFile, userPwd, OWNER_PASSWORD,
						stmpWord, permission);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建PDF文档
	 * 
	 * @param txtFilePath
	 *            txt文件路径（源文件）
	 * @param pdfFilePath
	 *            pdf文件路径(新文件)
	 */
	private static boolean createPDF(String txtFilePath, File file) {
		// 设置纸张
		Rectangle rect = new Rectangle(PageSize.A4);
		// 设置页码
		HeaderFooter footer = new HeaderFooter(new Phrase("页码:",
				setChineseFont()), true);
		footer.setBorder(Rectangle.NO_BORDER);
		// step1
		Document doc = new Document(rect, 50, 50, 50, 50);
		doc.setFooter(footer);
		try {
			FileReader fileRead = new FileReader(txtFilePath);
			BufferedReader read = new BufferedReader(fileRead);
			// 设置pdf文件生成路径 step2
			PdfWriter.getInstance(doc, new FileOutputStream(file));
			// 打开pdf文件 step3
			doc.open();
			// 实例化Paragraph 获取写入pdf文件的内容，调用支持中文的方法. step4
			while (read.ready()) {
				// 添加内容到pdf(这里将会按照txt文件的原始样式输出)
				doc.add(new Paragraph(read.readLine(), setChineseFont()));
			}
			// 关闭pdf文件 step5
			doc.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 在pdf文件中添加水印
	 * 
	 * @param source
	 *            原始文件
	 * @param target
	 *            水印输出文件
	 * @param stmpWord
	 *            水印名字
	 */
	public static byte[] encrypt(byte[] source, String userPwd) {
		ByteArrayOutputStream target = new ByteArrayOutputStream();
		try {
			PdfReader reader = new PdfReader(source);

			PdfStamper stamper = new PdfStamper(reader, target);
			// 设置密码,及屏蔽打印
			if (StringUtils.isNotBlank(userPwd)) {
				stamper.setEncryption(userPwd.getBytes(), OWNER_PASSWORD
						.getBytes(), 0, 0);
			}
			stamper.close();
			target.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target.toByteArray();
	}

	/**
	 * 在pdf文件中添加水印
	 * 
	 * @param source
	 *            原始文件
	 * @param target
	 *            水印输出文件
	 * @param stmpWord
	 *            水印名字
	 */
	public static byte[] deEncrypt(byte[] source, String userPwd) {
		ByteArrayOutputStream target = new ByteArrayOutputStream();
		if (StringUtils.isBlank(userPwd)) {
			userPwd = OWNER_PASSWORD;
		}
		try {
			PdfReader reader;
			try {
				reader = new PdfReader(source);
			} catch (Exception ex) {
				reader = new PdfReader(source, userPwd.getBytes());
			}

			com.lowagie.text.Document document = new com.lowagie.text.Document();
			PdfCopy copy = new PdfCopy(document, target);
			document.open();
			int n = reader.getNumberOfPages();
			for (int j = 1; j <= n; j++) {
				PdfImportedPage page = copy.getImportedPage(reader, j);
				copy.addPage(page);
			}
			document.close();
		} catch (com.lowagie.text.DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return target.toByteArray();
	}
	public static int getPDFSize(byte[] source) {
		String userPwd = OWNER_PASSWORD;
		PdfReader reader = null;
		try {
			try {
				reader = new PdfReader(source);
			} catch (Exception e) {
				reader = new PdfReader(source, userPwd.getBytes());
			}
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return reader.getNumberOfPages();
	}
	public static byte[] printSelf(byte[] source) {
		ByteArrayOutputStream target = new ByteArrayOutputStream();

		try {
			PdfReader reader;

			reader = new PdfReader(source);

			com.lowagie.text.Document document = new com.lowagie.text.Document();

			PdfCopy copy = new PdfCopy(document, target);
			document.open();

			int n = reader.getNumberOfPages();
			for (int j = 1; j <= n; j++) {
				PdfImportedPage page = copy.getImportedPage(reader, j);
				copy.addPage(page);
			}
			// 使其可以打印
			copy.setViewerPreferences(PdfWriter.HideMenubar|PdfWriter.HideToolbar|PdfWriter.HideWindowUI|PdfWriter.PageModeFullScreen);
			StringBuffer script = new StringBuffer();
			script
					.append("this.print({bUI:true,bSilent:true,bShrinkToFit:true})");
			copy.addJavaScript(script.toString());
			document.close();
		} catch (com.lowagie.text.DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return target.toByteArray();
	}

	/**
	 * 在pdf文件中添加水印
	 * 
	 * @param source
	 *            原始文件
	 * @param target
	 *            水印输出文件
	 * @param stmpWord
	 *            水印名字
	 */
	public static void stamp(String source, String target, String userPwd,
			String ownerPwd, String stmpWord, int permission) {
		try {
			PdfReader reader = new PdfReader(source);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					target));
			// 设置密码
			if (StringUtils.isNotBlank(userPwd)) {
				stamper.setEncryption(userPwd.getBytes(), ownerPwd.getBytes(),
						permission, false);
			}

			BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
					BaseFont.NOT_EMBEDDED);
			int total = reader.getNumberOfPages() + 1;
			// Image image = Image.getInstance(imageFilePath);
			// image.setAbsolutePosition(200, 400);
			PdfContentByte under;
			int j = stmpWord.length();
			char c = 0;
			int rise = 0;
			for (int i = 1; i < total; i++) {
				rise = 500;
				under = stamper.getUnderContent(i);
				// 添加图片
				// under.addImage(image);
				under.beginText();
				under.setColorFill(Color.red);
				under.setFontAndSize(base, 30);
				// 设置水印文字字体倾斜 开始
				if (j >= 15) {
					under.setTextMatrix(200, 120);
					for (int k = 0; k < j; k++) {
						under.setTextRise(rise);
						c = stmpWord.charAt(k);
						under.showText(c + "");
						rise -= 20;
					}
				} else {
					under.setTextMatrix(180, 100);
					for (int k = 0; k < j; k++) {
						under.setTextRise(rise);
						c = stmpWord.charAt(k);
						under.showText(c + "");
						rise -= 18;
					}
				}
				// // 字体设置结束
				// under.endText();
				// // 画一个圆
				// under.ellipse(250, 450, 350, 550);
				// under.setLineWidth(1f);
				// under.stroke();
			}
			stamper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void notAllowPrint(byte[] input, ByteArrayOutputStream out) {
		try {
			com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader(
					new ByteArrayInputStream(input));
			// 设置加密权限
			//PdfEncryptor.encrypt(reader, out, null, null, -1, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stamp(byte[] source, ByteArrayOutputStream target,
			String userPwd, String stmpWord) {
		stamp(source, target, userPwd, stmpWord, false);
	}

	public static void stamp(byte[] source, ByteArrayOutputStream target,
			String userPwd, String stmpWord, Boolean prtEnable) {
		try {
			PdfReader reader = new PdfReader(source);
			PdfStamper stamper = new PdfStamper(reader, target);
			PdfWriter writer = stamper.getWriter();
			// 设置密码,及屏蔽打印
			if (StringUtils.isNotBlank(userPwd)) {
				stamper.setEncryption(userPwd.getBytes(), OWNER_PASSWORD
						.getBytes(), PdfWriter.ALLOW_FILL_IN
						| PdfWriter.ALLOW_ASSEMBLY, false);
			}
			if (!prtEnable) {
				// 屏蔽打印
				stamper.setEncryption(null, null, PdfWriter.ALLOW_FILL_IN
						| PdfWriter.ALLOW_ASSEMBLY, false);

				// 隐藏菜单栏、工具栏’PdfWriter.PageModeFullScreen:防止用户按F8出现菜单栏
				writer.setViewerPreferences(PdfWriter.HideWindowUI
						| PdfWriter.HideMenubar | PdfWriter.HideToolbar
						| PdfWriter.PageModeFullScreen);
			}

			BaseFont base = null;
			try {
				// 楷体字
				base = BaseFont.createFont("c:/windows/fonts/simkai.ttf",
						BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			} catch (Exception e) {
				base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
						BaseFont.EMBEDDED);
			}

			int total = reader.getNumberOfPages() + 1;
			PdfContentByte over;
			if (StringUtils.isNotBlank(stmpWord)) {
				String[] sws = StringUtils.split(stmpWord, SEPARATOR);
				for (int i = 1; i < total; i++) {
					over = stamper.getOverContent(i);
					over.setColorFill(Color.red);
					over.setFontAndSize(base, 22);

					over.saveState();
					// 设置透明度
					PdfGState gs = new PdfGState();
					gs.setFillOpacity(0.2f);
					over.setGState(gs);

					if (sws.length > 1) {
						// 倾斜部分水印
						slant(over, sws[0], 500, base);
						slant(over, sws[0], 240, base);
						String[] strs = StringUtils.split(sws[1], "#");
						for (int k = 0; k < strs.length; k++) {
							bottom(base, over, strs[k], -700 - 20 * k);
						}
					}
				}
			}
			stamper.close();
			target.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void bottom(BaseFont base, PdfContentByte over, String str,
			int rise) {
		// 底部的水印
		over.setFontAndSize(base, 18);
		over.beginText();
		over.setTextMatrix(360, 800);
		over.setTextRise(rise);
		over.showText(str);
		over.endText();
	}

	private static void slant(PdfContentByte over, String word, int rise,
			BaseFont base) {
		int j = word.length();
		char c = 0;
		over.setFontAndSize(base, 42);
		// 设置水印文字字体倾斜 开始
		over.beginText();
		if (j >= 15) {
			over.setTextMatrix(200, 120);
			for (int k = 0; k < j; k++) {
				over.setTextRise(rise);
				c = word.charAt(k);
				over.showText(c + "");
				rise -= 20;
			}
		} else {
			over.setTextMatrix(180, 100);
			for (int k = 0; k < j; k++) {
				over.setTextRise(rise);
				c = word.charAt(k);
				over.showText(c + "");
				rise -= 18;
			}
		}
		over.endText();
	}

	/**
	 * 设置中文
	 * 
	 * @return Font
	 */
	private static Font setChineseFont() {
		BaseFont base = null;
		Font fontChinese = null;
		try {
			base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
					BaseFont.EMBEDDED);
			fontChinese = new Font(base, 12, Font.NORMAL);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fontChinese;
	}

	public static void main(String[] args) {
		// try {
		// File pdf = File.createTempFile(tmp, ".pdf");
		// System.out.println(pdf.getPath());
		// System.out.println(pdf.getAbsolutePath());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// txt2PDF("D:/dc2.pdf", pdfFilePath, "123", "www.emice.com", 16);
//		String hospName = DataConfigUtils.getValueByName("hospName");
//		stamp("D:/dc2.pdf", "D:/dcpdfFilePath.pdf", "", OWNER_PASSWORD,
//				hospName, 16);
	}

}
package com.zebone.nhis.ma.pub.arch.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.module.arch.BdArchDoctype;
import com.zebone.nhis.ma.pub.arch.vo.ArchInputParam;
import com.zebone.nhis.ma.pub.arch.vo.ArchUploadBody;
import com.zebone.nhis.ma.pub.arch.vo.ArchUploadContent;
import com.zebone.nhis.ma.pub.arch.vo.ArchUploadHead;
import com.zebone.nhis.ma.pub.arch.vo.hos.Baseinfo;
import com.zebone.nhis.ma.pub.arch.vo.hos.Export;
import com.zebone.nhis.ma.pub.arch.vo.hos.Info;
import com.zebone.nhis.ma.pub.arch.vo.hos.Inhospital;
import com.zebone.nhis.ma.pub.arch.vo.hos.Patient;
import com.zebone.nhis.ma.pub.arch.vo.hos.Report;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

import sun.misc.BASE64Encoder;

@Service
public class FileArchHttpHandler {

	private static Logger log = LoggerFactory.getLogger(FileArchHttpHandler.class);
	
	@Resource
	private FileArchOuterService archService;

	private static void archFileTest() {
		String filepath = "D:\\emrdata\\test";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("begin1:"+new Date());
		File file = new File(filepath);//File类型可以是文件也可以是文件夹
		File[] fileList = file.listFiles();//将该目录下的所有文件放置在一个File类型的数组中
		List<ArchInputParam> list = new ArrayList<ArchInputParam>();
		for (File file2 : fileList) {
			System.out.println(file2.getName());
			
			String sendUrl = "http://127.0.0.1:8080/nhis/static/arch/exec";
			ArchInputParam input = new ArchInputParam();
			input.setFuncId("ARCH_01");
			input.setPkOrg("~");
			ArchUploadContent content = new ArchUploadContent();
			ArchUploadHead head = new ArchUploadHead();
			ArchUploadBody body = new ArchUploadBody();
			input.setContent(content);
			content.setBody(body);
			content.setHead(head);
			head.setVisittype("1");
			head.setDocname(file2.getName());
			head.setDoctype("0036");
			head.setSysname("VTE");
			Date date = new Date();
			head.setDate(sdf.format(date));
			head.setUser("张小青2");
			head.setSex("女");
			head.setAge("12");
			head.setDept("测试科室");
			head.setDeptr("B超室");
			head.setRid("20090907853473");
			head.setRserial("76200");
			head.setMemo(file2.getName()+"-名称");
			head.setPid("002000029700");
			head.setPaticode("20000297");
			head.setVisitcode("21011700109");
			head.setTimes("1");
			head.setPatiname("测试555");
			head.setVisitdate(sdf.format(date));
			head.setIsarch("0");
	        
			try {
				body.setFilecontent(encodeBase64File(file2));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			list.add(input);
			
			//Map<String, Object> result = sendPost(JsonUtil.writeValueAsString(input),sendUrl);
			//System.out.println(file2.getName()+"-result:"+result);
		}
		System.out.println("end1:"+new Date());
		for (ArchInputParam input : list) {
			String sendUrl = "http://127.0.0.1:8080/nhis/static/arch/exec";

			Map<String, Object> result = sendPost(JsonUtil.writeValueAsString(input),sendUrl);
			input.getContent().setBody(null);
			input.setContent(null);
			
		}
		
		System.out.println("end:"+new Date());
		
	}
	
    /**
    * <p>将文件转成base64 字符串</p>
    * @param path 文件路径
    * @return
    * @throws Exception
    */
    public static String encodeBase64File(File file) throws Exception {
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }
    
	/**
	 * java httpClient4.5 post请求
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> sendPost(String sendMsg, String sendUrl) {
		HttpPost httpPost = new HttpPost(sendUrl);
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		StringEntity entity;
		Map<String,Object> mres = new HashMap<String, Object>();
		try {
			entity = new StringEntity(sendMsg, "UTF-8"); //解决参数中文乱码问题
			entity.setContentEncoding("UTF-8");//设置编码格式
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			// 发起请求
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
			// 请求结束，返回结果。并解析json。
			String resData = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
			System.out.println(resData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != closeableHttpClient) {
				try {
					closeableHttpClient.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return mres;
	}
	
	
	public static String httpClientUploadFile2(String url, File file) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        //每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
        String boundary ="--------------4585696313564699----------------";
        try {
                //文件名
                String fileName = file.getName();
                HttpPost httpPost = new HttpPost(url);
                //设置请求头
                httpPost.setHeader("Content-Type","multipart/form-data; boundary="+boundary);

                //HttpEntity builder
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                //字符编码
                builder.setCharset(Charset.forName("UTF-8"));
                //模拟浏览器
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                //boundary
                builder.setBoundary(boundary);
                //multipart/form-data
                //builder.addPart("multipartFile",new FileBody(file));
                builder.addPart("file",new FileBody(file));
                // binary
//    builder.addBinaryBody("name=\"multipartFile\"; filename=\"test.docx\"", new FileInputStream(file), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
                //其他参数
                //builder.addTextBody("filename", fileName,  ContentType.create("text/plain", Consts.UTF_8));
                //HttpEntity
                HttpEntity entity = builder.build();
                httpPost.setEntity(entity);
                // 执行提交
                HttpResponse response = httpClient.execute(httpPost);
                //响应
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                        // 将响应内容转换为字符串
                        result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                }
        } catch (IOException e) {
                e.printStackTrace();
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
                try {
                        httpClient.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        System.err.println("result"+result);
        return result;
}
	

	public static String getSuffix(final MultipartFile file){
        if(file == null || file.getSize() == 0){
            return null;
        }
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }
	public static JSONObject uploadFile(String urlStr, MultipartFile file, String token) throws IOException {

        // 后缀
        String suffix = getSuffix(file);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(urlStr);

        uploadFile.setHeader("authorization","Bearer " + token);

        DecimalFormat df = new DecimalFormat("#.##");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //  HTTP.PLAIN_TEXT_TYPE,HTTP.UTF_8
        builder.addTextBody("name", file.getOriginalFilename(), ContentType.create("text/plain", Consts.UTF_8));
        builder.addTextBody("size", df.format((double) file.getSize() / 1024), ContentType.TEXT_PLAIN);
        builder.addTextBody("suffix", suffix, ContentType.TEXT_PLAIN);

        // 把文件加到HTTP的post请求中
                // String filepath = "/user/test/123.png"
        // File f = new File(filepath);
        builder.addBinaryBody(
                "file",
                file.getInputStream(),
                                // new FileInputStream(f),
                ContentType.APPLICATION_OCTET_STREAM,
                file.getOriginalFilename()
        );

        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(uploadFile);
        HttpEntity responseEntity = response.getEntity();
        String sResponse= EntityUtils.toString(responseEntity, "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(sResponse);

        // {"code":1,"data":"7efb19980373dd90f5077576afa7481a","message":""}
        // {"code":401,"httpStatus":null,"data":"373656a2-baff-423a-93fb-704f51003509","message":"error"}

        return jsonObject;

    }
	
	public static Map<String, String> httpPostRequest2(String url, List<MultipartFile> multipartFiles,String fileParName,
	        Map<String, Object> params, int timeout) {
	    Map<String, String> resultMap = new HashMap<String, String>();
//	    CloseableHttpClient httpClient = HttpClients.createDefault();
//	    String result = "";
//	    try {
//	    HttpPost httpPost = new HttpPost(url);
//	        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//	        builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));
//	        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//	        String fileName = null;
//	        MultipartFile multipartFile = null;
//	        for (int i = 0; i < multipartFiles.size(); i++) {
//	            multipartFile = multipartFiles.get(i);
//	            fileName = multipartFile.getOriginalFilename();
//	            builder.addBinaryBody(fileParName, multipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
//	        }
//	       
//	        for (Map.Entry<String, Object> entry : params.entrySet()) {
//	            if(entry.getValue() == null)
//	                continue;
//	            // 类似浏览器表单提交，对应input的name和value
//	            builder.addTextBody(entry.getKey(), entry.getValue().toString(), contentType);
//	        }
//	        HttpEntity entity = builder.build();
//	        httpPost.setEntity(entity);
//	        HttpResponse response = httpClient.execute(httpPost);// 执行提交
//
//	        // 设置连接超时时间
//	        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
//	                .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
//	        httpPost.setConfig(requestConfig);
//
//	        HttpEntity responseEntity = response.getEntity();
//	        resultMap.put("scode", String.valueOf(response.getStatusLine().getStatusCode()));
//	        resultMap.put("data", "");
//	        if (responseEntity != null) {
//	            // 将响应内容转换为字符串
//	            result = EntityUtils.toString(responseEntity, java.nio.charset.Charset.forName("UTF-8"));
//	            resultMap.put("data", result);
//	        }
//	    } catch (Exception e) {
//	        resultMap.put("scode", "error");
//	        resultMap.put("data", "HTTP请求出现异常: " + e.getMessage());
//
//	        Writer w = new StringWriter();
//	        e.printStackTrace(new PrintWriter(w));
//	       
//	    } finally {
//	        try {
//	            httpClient.close();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
	    return resultMap;
	}


//	/**
//     * multipart/form-data
//     * @param map
//     * @param url
//     * @param charset
//     * @return
//     */
//    public String requestByPostParts(Map<String,Object> map, String url,String charset) {
//        HttpClient httpClient = HttpClients.createDefault();
//        HttpPost post = new HttpPost(url);
//        //post.setHeader("Content-Type", "multipart/form-data");//去掉Header
//        BufferedReader br = null;
//        try
//        {
//            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//            //multipartEntityBuilder.addTextBody("file",new File("d:/File/in.json"));
//            FormBodyPart bodyPart = new FormBodyPart(charset, null);
//            
//            multipartEntityBuilder.addPart(bodyPart);
//            HttpEntity httpEntity=multipartEntityBuilder.build();
//            // 设置请求参数
//            post.setEntity(httpEntity);
//            // 发起交易
//            HttpResponse resp = httpClient.execute(post);
//            int ret = resp.getStatusLine().getStatusCode();
//            // 响应分析
//            HttpEntity entity = resp.getEntity();
//            br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
//            StringBuffer responseString = new StringBuffer();
//            String result = br.readLine();
//            while (result != null)
//            {
//                responseString.append(result);
//                result = br.readLine();
//            }
//            return responseString.toString();
//        } catch (Exception e)
//        {
//            log.error(e.getMessage(), e);
//            return "";
//        } finally
//        {
//            if (br != null)
//            {
//                try
//                {
//                    br.close();
//                } catch (IOException e)
//                {
//                    log.error(e.getMessage(), e);
//                }
//            }
//        }
//    }
	
	public static String httpClientUploadFile(String url, File file) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        //每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
        String boundary ="--------------4585696313564699";
        try {
                //文件名
                String fileName = file.getName();
                HttpPost httpPost = new HttpPost(url);
                //设置请求头
                httpPost.setHeader("Content-Type","multipart/form-data; boundary="+boundary);

                //HttpEntity builder
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                //字符编码
                builder.setCharset(Charset.forName("UTF-8"));
                //模拟浏览器
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                //boundary
                builder.setBoundary(boundary);
                //multipart/form-data
                //builder.addPart("multipartFile",new FileBody(file));
                builder.addPart("file",new FileBody(file));
                // binary

                HttpEntity entity = builder.build();
                httpPost.setEntity(entity);
                // 执行提交
                HttpResponse response = httpClient.execute(httpPost);
                //响应
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                        // 将响应内容转换为字符串
                        result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                }
        } catch (IOException e) {
                e.printStackTrace();
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
                try {
                        httpClient.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        log.info("result"+result);
        return result;
	}
	
	/**
	 * 人医住院病历上传测试
	 * @param fileInfo
	 * @param start
	 * @throws Exception 
	 */
	public void uploadHosEmrFile(String param , IUser user) throws Exception {
		User u = (User)user;
		Properties props = new Properties();
	    // 使用ClassLoader加载properties配置文件生成对应的输入流
	    InputStream in = FileArchHandler.class.getClassLoader().getResourceAsStream("config/arch.properties");
	    // 使用properties对象加载输入流
	    props.load(in);

	    String sendUrl = "http://127.0.0.1:8080/nhis/static/arch/exec";
	    
		String updatePath = props.getProperty("arch.hos.emr.path");

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMddHHmmss");
		
		if(StringUtils.isEmpty(updatePath)) return;
		
		List<BdArchDoctype> docTypeList = archService.queryBdArchDoctype();
		
		File patFiles = new File(updatePath);
		File[] patFileList = patFiles.listFiles();
		List<ArchInputParam> list = new ArrayList<ArchInputParam>();
		for (File patFile : patFileList) {
			String pvPath = patFile.getPath();//000089421100_2/exportresult.erc
			File pvFiles = new File(pvPath);
			File[] pvFileList = pvFiles.listFiles();
			//处理属性文件
			Export exp=null;
			Patient patient = null;
			Baseinfo baseinfo = null;
			Inhospital hosp= null;
			ArchInputParam input = null;
			ArchUploadContent content =null;
			ArchUploadHead head = null;
			ArchUploadBody body = null;
			List<Report> reports = new ArrayList<Report>();
			for (File pvFile : pvFileList) {
				if(pvFile.isFile()) {
					String fileName = pvFile.getName();
					if(fileName.equals("exportresult.erc")) {
						//String xml = fileToStr(pvFile);
						//Export exp = (Export)XmlUtil.XmlToBean(Export.class, xml);
						exp = generateBean(pvFile);
						if(exp==null) continue;
						
						patient=exp.getPatient();
						if(patient==null) continue;
						
						baseinfo=patient.getBaseinfo();
						if(baseinfo==null) continue;
						
						hosp=patient.getInhospital();
						if(hosp==null) continue;
						
						reports = hosp.getReport();
						if(reports!=null&&reports.size()>0) {
							//报告列表
							for (Report report : reports) {
								//<report id="FrontSheet" vid="1" num="1" pages="4" name="病案首页" file="FrontSheet_1.pdf"/>
								//<report id="HospitalRecord" vid="2" num="1" pages="2" name="入院记录" file="HospitalRecord_2.pdf"/>
								//System.out.println(report.getName());
							}
						}
						
						break;
					}
					//System.out.println(pvFile.getName());
				}
			}
			if(reports.size()>0) {
				//处理就诊记录
				for (File pvFile : pvFileList) {
					if(pvFile.isDirectory()) {
						String pdfPath = pvFile.getPath();
						File pdfFiles = new File(pdfPath);
						File[] pdfFileList = pdfFiles.listFiles();
						//就诊记录下所有的pdf
						for (File pdfFile : pdfFileList) {
							Report rpt = getEmrReport(reports,pdfFile.getName());
							if(rpt==null) continue;
							
							input = new ArchInputParam();
							input.setFuncId("ARCH_01");
							input.setPkOrg("~");
							content = new ArchUploadContent();
							head = new ArchUploadHead();
							body = new ArchUploadBody();
							input.setContent(content);
							content.setBody(body);
							content.setHead(head);
							head.setVisittype("1");
							head.setDocname(pdfFile.getName());
							head.setDoctype(getDocType(docTypeList,rpt.getId()));
							head.setSysname("EMR");
							Date date = new Date();
							String dateStr="";
							if(hosp.getOutdate()!=null) {
								dateStr=sdf.format(sdf2.parse(hosp.getOutdate().toString()));
							}else {
								dateStr = sdf.format(date);
							}
							head.setDate(dateStr);
							head.setUser(u.getNameEmp());
							head.setSex(getInfo(baseinfo.getInfo(),"sex").getContent());
							head.setAge("");
							head.setDept(hosp.getOutdeptname());
							head.setDeptr("");
							head.setRid("");
							head.setRserial("");
							head.setMemo(rpt.getName());
							head.setPaticode(patient.getPid().toString());
							String ipid=hosp.getIpid();
							if(StringUtils.isEmpty(ipid)) {
								String[] strs= ipid.split("-");
								if(strs==null||strs.length!=2) {
									continue;
								}else {
									head.setPid(strs[0]);
									head.setTimes(strs[1]);
								}
							}
							head.setVisitcode(hosp.getIpid());
							//head.setTimes("1");
							head.setPatiname(getInfo(baseinfo.getInfo(),"name").getContent());
							head.setVisitdate(sdf.format(date));
							head.setIsarch("0");
					        
							try {
								body.setFilecontent(encodeBase64File(pdfFile));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							sendPost(JsonUtil.writeValueAsString(input),sendUrl);
							
						}
					}
				}
			}
			
		}
		
	}
	
	public  String getDocType(List<BdArchDoctype> list,String code) {
        String rtn = "0024";//其他记录
        for (BdArchDoctype bdArchDoctype : list) {
			String codeExt=bdArchDoctype.getCodeExt();
			if(StringUtils.isEmpty((codeExt))) continue;
			if(codeExt.indexOf(code)>=0) {
				return bdArchDoctype.getCodeDoctype();
			}
		}
        return rtn;
    }
	
	public static  Report getEmrReport(List<Report> list,String fileName) {
        for (Report rpt : list) {
			String file=rpt.getFile();
			if(file.equals(fileName)) {
				return rpt;
			}
		}
        return null;
    }
	
	public Info getInfo(List<Info> list,String name) {
        for (Info info : list) {
        	if(info.getId().equals(name)){
        		return info;
        	}
		}
        return null;
    }
	
	public static Export generateBean(File file) {
        JAXBContext jc = null;
        Export rtn = null;
        try {
            jc = JAXBContext.newInstance(Export.class);
            Unmarshaller uma = jc.createUnmarshaller();
            rtn = (Export) uma.unmarshal(file);
            System.out.println(rtn);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return rtn;
    }
	
    public static String fileToStr(File file) throws Exception {
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new String(buffer);
    }	
}

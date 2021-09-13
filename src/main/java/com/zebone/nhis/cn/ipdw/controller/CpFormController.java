package com.zebone.nhis.cn.ipdw.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.apache.commons.lang3.StringUtils;
import com.zebone.nhis.cn.ipdw.dao.CnOrderMapper;
import com.zebone.nhis.common.module.cn.cp.CpTempPhase;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.RenderUtils;

@Controller
public class CpFormController {

	@Autowired
	private CnOrderMapper oderDao;
	
	@RequestMapping(value = "/static/cpFormLogin", method = RequestMethod.GET)
	public void logina(HttpServletRequest request,HttpServletResponse response) {
		String pkCprec = request.getParameter("pkCprec");
		String pkCptemp = request.getParameter("pkCptemp");
    if(StringUtils.isBlank(pkCprec) || StringUtils.isBlank(pkCptemp)) throw new BusException("传参有误!");    
    List<Map<String,Object>> titleData = oderDao.qryCpFormTitleData(pkCprec);
    if(titleData==null||titleData.size()!=1) return;
    Map<String,Object> cpmap = titleData.get(0);
    String phaseSql = "	SELECT  *  FROM cp_temp_phase WHERE PK_CPTEMP=?  ORDER BY SORTNO ";
    List<CpTempPhase> tempPhase  = DataBaseHelper.queryForList(phaseSql, CpTempPhase.class, new Object[]{pkCptemp});  
	String title = cpmap.get("diagname")+"临床路径表单";
	String dtsex = cpmap.get("dtSex")==null?"":(String)cpmap.get("dtSex");
	String agePv = cpmap.get("agePv")==null?"":(String)cpmap.get("agePv");
	String codeOp = cpmap.get("codeOp")==null?"":(String)cpmap.get("codeOp");
	String codeIp = cpmap.get("codeIp")==null?"":(String)cpmap.get("codeIp");
	Object  dateBegin= cpmap.get("dateBegin");
	Object dateEnd = cpmap.get("dateEnd");
    String yearBegin = "";
    String monthBegin = "";
    String dayBegin ="";
    String yearEnd = "";
    String monthEnd = "";
    String dayEnd = "";
	Calendar c = Calendar.getInstance();
	if(dateBegin!=null){
		c.setTime((Date)dateBegin);
		yearBegin = String.valueOf(c.get(Calendar.YEAR));
		monthBegin = String.valueOf(c.get(Calendar.MONTH)+1);
		dayBegin = String.valueOf(c.get(Calendar.DAY_OF_MONTH));	 
	}
	if(dateEnd!=null){
		c.setTime((Date)dateEnd);
		yearEnd = String.valueOf(c.get(Calendar.YEAR));
		monthEnd = String.valueOf(c.get(Calendar.MONTH)+1);
		dayEnd = String.valueOf(c.get(Calendar.DAY_OF_MONTH));	 
	}
  
	String docType = "<!DOCTYPE html> \n";
		String html = (docType +
		    "<html>\n" +
		    "<head ><title>" + title + "</title><meta http-equiv=X-UA-Compatible content='IE=edge,chrome=1'></head>\n" +
		    "<body bgcolor=\"#f0f0f0\">\n" +
	    "<h1 align=\"center\">" + title + "</h1>\n" +
		"<div align=\"center\"><table>"+
	    "<tr><td><div>适用对象：<b>第一诊断为</b>"+cpmap.get("diagname")+"(ICD-10："+cpmap.get("diagcode")+")</div></td></tr>\n" +
	    "<tr><td><div>患者姓名：<u>"+cpmap.get("namePi")+"</u> 性别：<u>"+dtsex+"</u> 年龄：<u>"+agePv+"</u> </div></td></tr>\n" +
	    "<tr><td><div>门诊号：<u>"+codeOp+"</u> 住院号：<u>"+codeIp+"</u></div></td></tr>\n" +
	    "<tr><td><div>住院日期：<u>"+yearBegin+"</u>年<u>"+monthBegin+"</u>月<u>"+dayBegin+"</u>日   出院日期：<u>"+yearEnd+"</u>年<u>"+monthEnd+"</u>月<u>"+dayEnd+"</u>日   标准住院日："+tempPhase.get(tempPhase.size()-1).getDaysMin()+"-"+tempPhase.get(tempPhase.size()-1).getDaysMax()+"天</div></td></tr>\n" +
	    "</table></div>"+
	    getHtml(pkCprec,pkCptemp,tempPhase)+
		    "</body></html>");

		RenderUtils.renderHtml(response, html);
	}
	
private String getHtml(String pkCprec,String pkCptemp,List<CpTempPhase> tempPhase){
		  
        Map<String,List<Map<String,Object>>> work = new HashMap<String,List<Map<String,Object>>>(); //诊疗活动
        Map<String,List<Map<String,Object>>> longOrd = new HashMap<String,List<Map<String,Object>>>(); //长期医嘱
        Map<String,List<Map<String,Object>>> onceOrd = new HashMap<String,List<Map<String,Object>>>(); //临时医嘱
        Map<String,List<Map<String,Object>>> nswork = new HashMap<String,List<Map<String,Object>>>(); //护理工作
        List<Map<String,Object>> ordList = new ArrayList<Map<String,Object>>();
        ordList = oderDao.qryCpFormData(pkCprec, pkCptemp);
        int idxOrd = 0;
        for(CpTempPhase phase :tempPhase){
        	String pkCpphase = phase.getPkCpphase();
        	List<Map<String,Object>> wordM = new ArrayList<Map<String,Object>>();
        	List<Map<String,Object>> longOrdM = new ArrayList<Map<String,Object>>();
        	List<Map<String,Object>> onceOrdM = new ArrayList<Map<String,Object>>();
        	List<Map<String,Object>> nsworkM = new ArrayList<Map<String,Object>>();
        	for(int i=idxOrd;i<ordList.size();i++){      
        		idxOrd = i;
        		Map<String,Object> ordM = ordList.get(i);
            	String cpphase = (String)ordM.get("pkCpphase");
            	if(!pkCpphase.equals(cpphase)) break;	//医嘱必须是按阶段排好序的，减少循环次数
	    		String ordType = (String) ordM.get("ordType");
	    		if("work".equals(ordType)){
	    			wordM.add(ordM);
	    		}else if("longOrd".equals(ordType)){
	    			longOrdM.add(ordM);
	    		}else if("onceOrd".equals(ordType)){
	    			onceOrdM.add(ordM);
	    		}else if("nsword".equals(ordType)){
	    			nsworkM.add(ordM);
	    		}
            }
          work.put(pkCpphase, wordM);
          longOrd.put(pkCpphase, longOrdM);
          onceOrd.put(pkCpphase, onceOrdM);
          nswork.put(pkCpphase, nsworkM);
        }
	int phaseCount=tempPhase.size();	//几个阶段
//	String html=	"<table width='100%' height='100%' border='1'>"+
//		  "<tr>"+
//		    "<td>"+
		    String html=        "<div align='center'>"+
		          "<TABLE style='BORDER-COLLAPSE: collapse' borderColor=#000000 cellSpacing=0 cellPadding=0 width=645 border=1>"+
		              "<TBODY>"+
		                  "<TR>\n"+ //第一行
		                       "<TD width=40><P align=center>时间</P></TD>";
   for(int i=0;i<phaseCount;i++){
	   int min = tempPhase.get(i).getDaysMin();
	   int max = tempPhase.get(i).getDaysMax();
	   String phaseName = tempPhase.get(i).getNamePhase();
	                     html+="<TD width=200><P align=center>住院第"+(min==max?min:min+"-"+max)+"天"+(StringUtils.isBlank(phaseName)?"":"("+phaseName+")")+"</P></TD>";
   }
                     html+="</TR>"+
			              " <TR>\n"+  //第二行      
		                	" <TD width=40 height='100%' align='center' valign='middle'>"+
		                	    " <table width='31' height='100%' border='0'>"+
									"  <tr> "+
									" <td><div align='center'>主</div></td>"+
									"  </tr>"+
									"  <tr> "+
									"  <td><div align='center'>要</div></td>"+
									" </tr>"+
									" <tr> "+
									"  <td><div align='center'>诊</div></td>"+
									" </tr>"+
									" <tr> "+
									"  <td><div align='center'>疗</div></td>"+
									"  </tr>"+
									" <tr> "+
									" <td><div align='center'>活</div></td>"+
									" </tr>"+
									"  <tr > "+
									" <td><div align='center'>动</div></td>"+
									"  </tr>"+
									" </table></TD>";
  for(int i=0;i<phaseCount;i++){
	  String phase = tempPhase.get(i).getPkCpphase();
	  List<Map<String,Object>> words = work.get(phase);
	  html+="<TD width=200>"+
			  " <table width='140' height='100%' border='0'>";
	  for(int j=0;j<words.size();j++){

    	  html+=" <tr vAlign=top width=200><input type='checkbox' onClick='return false'  "+(words.get(j).get("flagUse").equals("true")?"checked=true":" ")+">"+words.get(j).get("ordName")+"</tr><br>";
      }		
	  html+= " </table>"
	  	 + "</TD>";                 
   }             
		  html+= "</TR>"+
			" <TR>\n"+    //    第三行             
					" <TD width=40 height='100%' align='center' valign='middle'> "+
						"<table width='31' height='100%' border='0'>"+
						" <tr> "+
						" <td><div align='center'>重</div></td>"+
						" </tr>"+
						" <tr> "+
						" <td><div align='center'>点</div></td>"+
						"  </tr>"+
						"  <tr> "+
						"  <td><div align='center'>医</div></td>"+
						" </tr>"+
						"  <tr> "+
						"  <td><div align='center'>嘱</div></td>"+
						" </tr>"+
						" </table>"+
					" </TD>";
		  for(int i=0;i<phaseCount;i++){
			  String phase = tempPhase.get(i).getPkCpphase();
			  List<Map<String,Object>> longOrds = longOrd.get(phase);
			  List<Map<String,Object>> onceOrds = onceOrd.get(phase);
			  html+= " <TD vAlign=top width=200>"+
		                 "<table>"+
			          "<br>长期医嘱:<br>";
						 for(int j=0;j<longOrds.size();j++)	  {
							 html+=" <tr><input type='checkbox' onClick='return false' "+(longOrds.get(j).get("flagUse").equals("true")?"checked=true":" ")+">"+longOrds.get(j).get("ordName")+"</tr><br>";
						 }
			         html+= "<br>临时医嘱:<br>";
						 for(int j=0;j<onceOrds.size();j++)	  {
							 html+=" <tr><input type='checkbox' onClick='return false' "+(onceOrds.get(j).get("flagUse").equals("true")?"checked=true":" ")+">"+onceOrds.get(j).get("ordName")+"</tr><br>";
						 }
	         html+="</table></TD>";
		  }
			html+= "</TR>"+
			" <TR>\n"+       //    第四行    
				"  <TD width=40 height='100%' align='center' valign='middle'> "+
				    "<table width='31' height='100%' border='0'>"+
						"  <tr> "+
						   "  <td><div align='center'>主要</div></td>"+
						"  </tr>"+
						"  <tr> "+
						   "  <td><div align='center'>护理</div></td>"+
						"  </tr>"+
						"  <tr> "+
						   "  <td><div align='center'>工作</div></td>"+
						"  </tr>"+
				    "</table></TD>";
	   for(int i=0;i<phaseCount;i++){
		      String phase = tempPhase.get(i).getPkCpphase();
			  List<Map<String,Object>> nswords = nswork.get(phase);
			  html+="<TD width=200>"+
					  " <table width='31' height='100%' border='0'>";
			  for(int j=0;j<nswords.size();j++){

		    	  html+=" <tr vAlign=top width=200><input type='checkbox' onClick='return false' "+(nswords.get(j).get("flagUse").equals("true")?"checked=true":" ")+">"+nswords.get(j).get("ordName")+"</tr><br>";
		      }		
			  html+= " </table>"
			  	 + "</TD>";                 
	     } 
			 html+= "</TR>"+
			" <TR>"+       //     第五行         
				"  <TD width=40 height='100%' align='center' valign='middle'> "+ 
					"<table width='31' height='100%' border='0'>"+
					"  <tr> "+
					"   <td><div align='center'>病情</div></td>"+
					"  </tr>"+
					"  <tr> "+
					"   <td><div align='center'>变异</div></td>"+
					"  </tr>"+
					"  <tr> "+
					"   <td><div align='center'>记录</div></td>"+
					"   </tr>"+
					"   </table></TD>";
		for(int i=0;i<phaseCount;i++){
			html+="  <TD vAlign=top width=200>"+
					"     <P>□无&nbsp; □有，原因：</P>"+
					"     <P>1．</P>"+
					"     <P>2．</P></TD>";
		}
	   html+="  </TR>"+
			" <TR>"+     //   第六行            
				"   <TD width=40 height='100%' align='center' valign='middle'> "+
						"<table width='31' height='100%' border='0'>"+
						" <tr> "+
						  " <td><div align='center'>护士</div></td>"+
						" </tr>"+
						" <tr> "+
						"  <td><div align='center'>签名</div></td>"+
						" </tr>"+
						" </table></TD>";
		for(int i=0;i<phaseCount;i++){
			html+= "  <TD vAlign=top width=200><P align=center></P></TD>";
		}
	html+=	"</TR>"+
			" <TR>"+      //   第七行                          
				"  <TD width=40 height='100%' align='center' valign='middle'> "+
					 "<table width='31' height='100%' border='0'>"+
					"  <tr> "+
					"    <td><div align='center'>医师</div></td>"+
					" </tr>"+
					"  <tr> "+
					"    <td><div align='center'>签名</div></td>"+
					"  </tr>"+
					"</table> </TD>";
	for(int i=0;i<phaseCount;i++){
		html+= 	"  <TD vAlign=top width=200><P></P></TD>";
	} 
	  html+="</TR>"+
	" </TBODY></TABLE>"+
  "  </div>";
//  + "</td>"+
// " </tr>"+
//" </table> ";
	
	return html;
	}
	
}

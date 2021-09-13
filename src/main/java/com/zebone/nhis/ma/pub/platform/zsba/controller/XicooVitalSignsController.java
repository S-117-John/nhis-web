package com.zebone.nhis.ma.pub.platform.zsba.controller;


import com.zebone.nhis.ma.pub.platform.zsba.service.XicooVitalSignsService;
import com.zebone.nhis.ma.pub.platform.zsba.vo.VitalSignsData;
import com.zebone.nhis.ma.pub.platform.zsba.vo.VitalSignsInfoDto;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/static/api")
public class XicooVitalSignsController {
    @Autowired
    private XicooVitalSignsService xicooVitalSignsService;


    @RequestMapping(value = "/getPatInfoById", method = RequestMethod.GET)
    @ResponseBody
    public String  getPatInfoById(@RequestParam(value = "dn") String dn,
                                     @RequestParam(value = "_id") String _id,
                                     @RequestParam(value = "type", required = false) Integer type) {
        return xicooVitalSignsService.getPatInfo(dn, _id, type);
    }

    @RequestMapping(value = "/getPatInfoByDept", method = RequestMethod.GET)
    @ResponseBody
    public String getPatInfoByDept(@RequestParam(value = "dn") String dn,
                                   @RequestParam(value = "_id") String _id) {
        return xicooVitalSignsService.getDeptInfo(dn, _id);
    }

    @RequestMapping(value = "/saveVtsOccAndDt", method = RequestMethod.POST)
    @ResponseBody
    public void saveVtsOccAndDt(@RequestParam(value = "data") String data ,
                                @RequestParam(value = "usercode") String usercode) throws Exception {
        VitalSignsInfoDto info=new VitalSignsInfoDto();
          info.setData(JsonUtil.readValue(data,VitalSignsData.class));
          info.setUsercode(usercode);
        xicooVitalSignsService.postVitalSigns(info);
    }

}

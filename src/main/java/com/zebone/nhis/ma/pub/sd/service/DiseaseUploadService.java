package com.zebone.nhis.ma.pub.sd.service;

import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.arch.ArchDoc;
import com.zebone.nhis.common.module.arch.ArchPv;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.sd.dao.DiseaseUploadMapper;
import com.zebone.nhis.ma.pub.sd.vo.ArchDocPicVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 患者病情上传
 */
@Service
public class DiseaseUploadService {

    @Resource
    private DiseaseUploadMapper uploadMapper;

    /**
     * 查询患者病情列表
     * 005002003008
     *
     * @param param
     * @param user
     * @return
     */
    public Map<String, Object> getDiseaseList(String param, IUser user) {
        String pkPv = JsonUtil.getFieldValue(param, "pkPv");

        List<Map<String, Object>> archDocs = uploadMapper.getDiseaseList(pkPv);
        ArchPv archPv = DataBaseHelper.queryForBean("select * from arch_pv where pk_pv=?", ArchPv.class, pkPv);
        Map<String, Object> diseaseVo = Maps.newHashMap();
        diseaseVo.put("archDocs", archDocs);
        diseaseVo.put("archPv", archPv);
        return diseaseVo;
    }

    /**
     * 保存患者病情
     * 005002003009
     *
     * @param param
     * @param user
     */
    public void savePic(String param, IUser user) {
        ArchPv archPv = JsonUtil.readValue(JsonUtil.getJsonNode(param, "archPv"), ArchPv.class);
        List<ArchDocPicVo> archDocs = JsonUtil.readValue(JsonUtil.getJsonNode(param, "archDocs"), new TypeReference<List<ArchDocPicVo>>() {
        });
        if (StringUtils.isEmpty(archPv.getPkArchpv())) {
            archPv.setPkArchpv(NHISUUID.getKeyId());
            DataBaseHelper.insertBean(archPv);
        }
        if (archDocs.size() > 0) {
            for (ArchDocPicVo ar : archDocs) {
                if (StringUtils.isEmpty(ar.getPkArchpv())) {
                    ar.setPkArchpv(archPv.getPkArchpv());
                }
                ar.setPkArchdoc(NHISUUID.getKeyId());
                ar.setCreator(UserContext.getUser().getPkEmp());
                ar.setCreateTime(new Date());
                ar.setTs(new Date());
                if (!StringUtils.isEmpty(ar.getOldPkDoc())) {//更新修改的记录
                    DataBaseHelper.update("Update arch_doc Set flag_cancel=1, date_cancel=?, pk_emp_cancel=?, name_emp_cancel=?, del_flag=1 Where pk_archdoc=? ", new Object[]{new Date(), UserContext.getUser().getPkEmp(), UserContext.getUser().getUserName(), ar.getOldPkDoc()});
                }
            }

            archDocs = fileUpload(archDocs);
            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ArchDoc.class), archDocs);
        }
    }

    /**
     * 删除病情图片记录
     * 005002003010
     *
     * @param param
     * @param user
     */
    public void delPic(String param, IUser user) {
        String pkArchdoc = JsonUtil.getFieldValue(param, "pkArchdoc");
        ArchDoc archDoc = DataBaseHelper.queryForBean("select * from arch_doc where pk_archdoc=?", ArchDoc.class, pkArchdoc);
        if ("0".equals(archDoc.getFlagLook()) && !UserContext.getUser().getPkEmp().equals(archDoc.getPkEmpUpload())) {
            throw new BusException("您没有权限删除改文件！");
        }
        DataBaseHelper.update("Update arch_doc Set flag_cancel=1, date_cancel=?, pk_emp_cancel=?, name_emp_cancel=?, del_flag=1 Where pk_archdoc=? ", new Object[]{new Date(), UserContext.getUser().getPkEmp(), UserContext.getUser().getUserName(), pkArchdoc});
    }

    /**
     * 从ftp获取患者病情图片
     * 005002003011
     *
     * @param param
     * @param User
     * @return
     */
    public byte[] getPicByte(String param, IUser User) {
        String pkArchdoc = JsonUtil.getFieldValue(param, "pkArchdoc");
        ArchDoc archDoc = DataBaseHelper.queryForBean("select * from arch_doc where pk_archdoc=?", ArchDoc.class, pkArchdoc);
        if ("0".equals(archDoc.getFlagLook()) && !UserContext.getUser().getPkEmp().equals(archDoc.getPkEmpUpload())) {
            throw new BusException("您没有查看该文件的权限！");
        }
        return fileDownload(archDoc.getPath());
    }

    /**
     * ftp下载图片
     *
     * @param path
     * @return
     */
    public static byte[] fileDownload(String path) {
        FTPClient ftpClient = new FTPClient();
        String ftpIp = ApplicationUtils.getPropertyValue("emr.ftp.host", "127.0.0.1");//ip
        int ftpPort = Integer.parseInt(ApplicationUtils.getPropertyValue("emr.ftp.port", "21"));//端口
        String ftpUser = ApplicationUtils.getPropertyValue("emr.ftp.user", "emr");//登录名
        String ftpPwd = ApplicationUtils.getPropertyValue("emr.ftp.pwd", "emr");//密码

        byte[] bytes;
        ByteArrayOutputStream bos = null;
        try {
            //连接
            ftpClient.connect(ftpIp, ftpPort);
            //登录
            ftpClient.login(ftpUser, ftpPwd);

            //设置被动模式
            ftpClient.enterLocalPassiveMode();

            // 检查远程文件是否存在
//            FTPFile[] files = ftpClient.listFiles(new String(path.getBytes("GBK"), StandardCharsets.ISO_8859_1));
//            if (files.length != 1) {
//                throw new BusException("远程文件不存在");
//            }

            //获取文件流
            InputStream in = ftpClient.retrieveFileStream(new String(path.getBytes("GBK"), StandardCharsets.ISO_8859_1));
            if (null == in) {
                throw new BusException("远程文件不存在");
            }
            bos = new ByteArrayOutputStream();
            int length;
            byte[] buf = new byte[2048];
            while (-1 != (length = in.read(buf, 0, buf.length))) {
                bos.write(buf, 0, length);
            }
            ByteArrayInputStream fis = new ByteArrayInputStream(bos.toByteArray());
            bos.flush();
            bos.close();

            bytes = new byte[fis.available()];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = fis.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset != bytes.length) {
                throw new IOException("未能获取完整文件，请重试！");
            }
            fis.close();

//            bytes = new byte[(int) files[0].getSize()];
//            long step = files[0].getSize() / 100;
//            long process = 0;
//            long localSize = 0L;
//            int c;
//            while ((c = in.read(bytes)) != -1) {
//                localSize += c;
//                long nowProcess = localSize / step;
//                if (nowProcess > process) {
//                    process = nowProcess;
//                    if (process % 10 == 0)
//                        System.out.println("下载进度：" + process);
//                }
//            }
            in.close();

            // 退出ftp
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return bytes;
    }

    /**
     * FTP上传文件(批量)
     */
    public static List<ArchDocPicVo> fileUpload(List<ArchDocPicVo> docs) {
        FTPClient ftpClient = new FTPClient();
        String pathStr = "";//文件保存路径
        String ftpIp = ApplicationUtils.getPropertyValue("emr.ftp.host", "127.0.0.1");//ip
        int ftpPort = Integer.parseInt(ApplicationUtils.getPropertyValue("emr.ftp.port", "21"));//端口
        String ftpUser = ApplicationUtils.getPropertyValue("emr.ftp.user", "emr");//登录名
        String ftpPwd = ApplicationUtils.getPropertyValue("emr.ftp.pwd", "emr");//密码
        String ftpPath = ApplicationUtils.getPropertyValue("emr.ftp.path", "emr");//上传路径
        try {
            OutputStream out = null;
            for (ArchDocPicVo ar : docs) {
                //ip、端口
                ftpClient.connect(ftpIp, ftpPort);
                //用户名密码
                ftpClient.login(ftpUser, ftpPwd);

                if (ar.getPicByte() == null || ar.getPicByte().length <= 0) continue;

                //设置上传目录
                pathStr = ftpPath + ar.getPath() + "/";
                String directory = pathStr.substring(0, pathStr.lastIndexOf("/") + 1);
                if (!directory.equals("/")
                        && !ftpClient.changeWorkingDirectory(new String(directory.getBytes("GBK"), StandardCharsets.ISO_8859_1))) {

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
                        String subDirectory = new String(pathStr.substring(start, end).getBytes("GBK"), StandardCharsets.ISO_8859_1);
                        if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                            if (ftpClient.makeDirectory(subDirectory)) {
                                ftpClient.changeWorkingDirectory(subDirectory);
                            } else {
//                                throw new RuntimeException("创建目录失败！");
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

                ftpClient.changeWorkingDirectory(pathStr);
                ftpClient.setControlEncoding("GBK");
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);

                out = ftpClient.storeFileStream(new String(ar.getFileName().getBytes("GBK"), StandardCharsets.ISO_8859_1));
//                out = ftpClient.appendFileStream(new String(ar.getFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
                out.write(ar.getPicByte(), 0, ar.getPicByte().length);

                out.flush();
                ar.setPath(pathStr + ar.getFileName());
            }

            // 关闭输入流
            out.close();
            // 退出ftp
            ftpClient.logout();
            //System.out.println("====上传成功====");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return docs;
    }
}

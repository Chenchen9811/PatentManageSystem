package com.example.demo.Utils;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.Constants;
import com.example.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class CommonUtil implements Constants {


    /**
     * 获取ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    public static String getJSONString(int code, String message, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message", message);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put("key", map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String generatePermissionCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowTime = sdf.format(new Date());
        Random random = new Random();
        int randomNum = random.nextInt(900) + 100;
        return "PERMISSION" + nowTime + randomNum;
    }

    public static String generateCode(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowTime = sdf.format(new Date());
        Random random = new Random();
        int randomNum = random.nextInt(10000) + 100;
        return str + nowTime + randomNum;
    }


    //subList手动分页，page为第几页，rows为每页个数
    public static <T> List<T> subList(List<T> list, int page, int rows) throws Exception {
        List<T> listSort = new ArrayList<>();
        int size = list.size();
        int pageStart = page == 1 ? 0 : (page - 1) * rows;//截取的开始位置
        int pageEnd = size < page * rows ? size : page * rows;//截取的结束位置
        if (size > pageStart) {
            listSort = list.subList(pageStart, pageEnd);
        }
        //总页数
        int totalPage = list.size() / rows;
        return listSort;
    }

    public static Timestamp stringDateToTimeStamp(String date) {
        date = date + " 00:00:00";
        Timestamp ts = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setLenient(false);
        try {
            ts = new Timestamp(format.parse(date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
//            log.info("转换有异常>>>>>>>"+e.getMessage());
            return null;
        }
//        log.info("Timestamp>>>>>>>"+ts.toString());
        return ts;
    }

    public static java.sql.Date stringToDate(String date) {
        SimpleDateFormat format = null;
        try {
            format = new SimpleDateFormat("yyyy-MM-dd");
            return new java.sql.Date(format.parse(date).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("转换有异常>>>>>>>" + e.getMessage());
            return null;
        }
    }

    public static Integer getProposalTypeCode(String type) {
        switch (type) {
            case "发明专利":
                return 0;
            case "实用新型专利":
                return 1;
            case "外观设计专利":
                return 2;
            case "软著":
                return 3;
            case "商标":
                return 4;
            default:
                return 5;
        }
    }

    public static String getProposalTypeString(Integer code) {
        switch (code) {
            case 0 :
                return "发明专利";
            case 1:
                return "实用新型专利";
            case 2:
                return "外观设计专利";
            case 3:
                return "软著";
            case 4:
                return "商标";
            default:
                return "未知类型";
        }
    }

    public static Integer getProposalStatusCode(String proposalStatus) {
        switch (proposalStatus) {
            case "全部":
                return 0;
            case "在审":
                return 1;
            case "通过":
                return 2;
            case "不通过":
                return 3;
            case "补充文件":
                return 4;
            default:
                return 5;
        }
    }

    public static User generateUser(String userName, Long departmentId) {
        User user = new User();
        user.setUserCode(CommonUtil.generateCode("U"));
        user.setUserName(userName);
        user.setDepartmentId(departmentId);
        user.setDelFlag("N");
        return user;
    }

    public static String getProposalStatusString(Integer code) {
        switch (code) {
            case 0:
                return "全部";
            case 1:
                return "在审";
            case 2:
                return "通过";
            case 3:
                return "不通过";
            case 4:
                return "补充文件";
            default:
                return "未知状态";
        }
    }


    public static Long getDistanceDays(Timestamp currentTimeStamp, Timestamp timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date cur = formatter.parse(CommonUtil.getYmdbyTimeStamp(currentTimeStamp), new ParsePosition(0));
        java.util.Date pre = formatter.parse(CommonUtil.getYmdbyTimeStamp(timestamp), new ParsePosition(0));
        return (cur.getTime() - pre.getTime()) / (1000 * 60 * 60 * 24);
    }


    public static String getFileUrl(String fileName) {
        return UPLOAD_PATH + File.separator + fileName;
    }

    public static String getFilePath(File file) {
        String absolutePath = file.getAbsolutePath();
        return absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
    }

    public static String getYmdbyTimeStamp(Timestamp timestamp) {
        return timestamp.toString().substring(0, 10);
    }

    public static String getJSONString(int code, String message) {
        return getJSONString(code, message, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    public static Long generateId() {
        return (System.currentTimeMillis() + 8 * 3600 * 1000) * 10000 + 621355968000000000L;
    }

    public static int getRfidThoughModbus(short[] origin) {
        return origin[0] * 100 + origin[1];
    }
}

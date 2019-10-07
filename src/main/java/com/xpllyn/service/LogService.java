package com.xpllyn.service;

import com.xpllyn.mapper.LogMapper;
import com.xpllyn.pojo.Log;
import com.xpllyn.utils.MessageInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日志服务实现类
 */
@Service
public class LogService implements ILogService {
    @Autowired
    private MessageInfoUtils messageInfoUtils = null;

    @Autowired
    private LogMapper logMapper;

    @Override
    public boolean insertLog(Log log) {
        boolean flag = false;
        try {
            flag = logMapper.insertLog(log);
        }catch (Exception e){
            log.setStatus("fail");
            flag = logMapper.insertLog(log);
        }
        return flag;
    }

    @Override
    public List<Log> selectLogByType(String type) {
        List<Log> LogList = new ArrayList<>();
        try{
            LogList = logMapper.selectLogByType(type);
        }catch (Exception e){
            e.printStackTrace();
        }
        return LogList;
    }

    @Override
    public List<Log> selectAllLogs() {
        List<Log> LogList = new ArrayList<>();
        try{
            LogList = logMapper.selectAllLogs();
        }catch (Exception e){
            e.printStackTrace();
        }
        return LogList;
    }

    @Override
    public Log assembleLogObject(String type, HttpServletRequest request) {
        String ip = messageInfoUtils.getUserIp(request);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        String status = "success";
        return new Log(ip, date, type, "None", status);
    }

    @Override
    public Log assembleLogObject(HttpServletRequest request) {
        String bookname = request.getParameter("bookname");
        String type = request.getParameter("type");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        String ip = messageInfoUtils.getUserIp(request);
        String status = "success";
        Log log = null;
        if(type.equals("book_download")){
            log = new Log(ip, date, type, bookname, status);
        }
        else if(type.equals("insertMessage")){
            log = new Log(ip, date, type, status);
        }
        return log;
    }

}

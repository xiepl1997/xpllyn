package com.xpllyn.service;

import com.xpllyn.pojo.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * function: 日志服务接口
 * author: xiepl1997
 * date: 2019/10/6
 */
public interface ILogService {

    /**
     * 插入日志，成功返回true，失败返回false
     * @param log
     * @return
     */
    boolean insertLog(Log log);

    /**
     * 通过type获取日志
     * @param type
     * @return
     */
    List<Log> selectLogByType(String type);

    /**
     * 获取所有日志
     * @return
     */
    List<Log> selectAllLogs();

    /**
     * 生成Log对象
     * @return
     */
    Log assembleLogObject(String type, HttpServletRequest request);

    /**
     * 由http请求生成Log对象
     * @param request
     * @return
     */
    Log assembleLogObject(HttpServletRequest request);
}

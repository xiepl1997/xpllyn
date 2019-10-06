package com.xpllyn.mapper;

import com.xpllyn.pojo.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * name: LogMapper
 * author: xiepl1997
 * date: 2019/10/06
 */
@Mapper
public interface LogMapper {
    @Insert("insert into Log(ip,date,type,status) values(#{ip},#{date},#{type},#{status})")
    boolean insertLog(Log log);

    @Select("select * from Log")
    List<Log> selectAllLogs();

    @Select("select * from Log where type = #{type}")
    List<Log> selectLogByType(@Param("type") String type);

}

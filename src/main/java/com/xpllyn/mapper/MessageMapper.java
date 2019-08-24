package com.xpllyn.mapper;

import com.xpllyn.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * MessageMapper
 * created by xiepl1997 at 2019-8-21
 */
@Mapper
public interface MessageMapper {

    @Insert("insert into Message(ip,id,pre_id,name,province,city,time,content) values(#{ip}, #{id}, #{pre_id}, #{name},#{province}, #{city}, #{time}, #{content})")
    boolean insert(Message message);

    @Select("select * from Message order by time desc")
    List<Message> selectAll();

}

package com.xpllyn.mapper;

import com.xpllyn.pojo.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MessageMapper
 * created by xiepl1997 at 2019-8-21
 */
@Mapper
@Component
public interface MessageMapper {

    @Insert("insert into message(ip,pre_id,name,province,city,create_time,update_time,content) values(#{ip},#{pre_id},#{name},#{province},#{city},current_timestamp,current_timestamp,#{content})")
    boolean insert(Message message);

    @Select("select * from message order by create_time desc")
    List<Message> selectAll();

}

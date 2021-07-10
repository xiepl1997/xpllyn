package com.xpllyn.mapper;

import com.xpllyn.pojo.ReadMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * ReadMessageMapper
 * created by xiepl1997 at 2021-7-9
 */
@Mapper
@Component
public interface ReadMessageMapper {

    @Insert("insert into readmessage(read_user_id, send_user_id, read_time) values(#{read_user_id}, #{send_user_id}, #{read_time})")
    boolean insertReadMessage(ReadMessage rm);

    @Update("update readmessage set time = #{time} where read_user_id = #{rid} and send_user_id = #{sid}")
    boolean updateReadMessage(int rid, int sid, Timestamp time);

    @Select("select * from readmessage where read_user_id = #{rid} and send_user_id = #{sid}")
    ReadMessage getReadMessage(int rid, int sid);
}

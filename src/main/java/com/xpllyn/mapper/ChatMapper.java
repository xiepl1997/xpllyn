package com.xpllyn.mapper;

import com.xpllyn.pojo.ChatMessage;
import com.xpllyn.pojo.GroupMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ChatMapper {

    @Insert("insert into groupmessage(group_id, user_id, content, create_time) values(#{group_id}, #{user_id}, #{content}, #{create_time})")
    boolean insertGroupMessage(GroupMessage groupmessage);

    @Insert("insert into chatmessage(from_user_id, to_user_id, content, create_time) values(#{from_user_id},#{to_user_id},#{content},#{create_time})")
    boolean insertChatMessage(ChatMessage cm);

    @Select("select * from groupmessage order by create_time")
    List<GroupMessage> getGroupMessage();

    @Select("select * from chatmessage order by create_time")
    List<ChatMessage> getChatMessage();
}

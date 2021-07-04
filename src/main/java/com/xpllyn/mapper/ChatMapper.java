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

    /**
     * 获取世界频道的聊天记录（最近十五条）
     * @return
     */
    @Select("select * from (select * from groupmessage t where t.group_id = 1 order by create_time desc limit 0,15) m order by m.create_time")
    List<GroupMessage> getGlobalGroupMessage15();

    /**
     * 获取两个用户的聊天记录（最近十五条）
     * @param id1
     * @param id2
     * @return
     */
    @Select("select * from (select * from chatmessage t where t.from_user_id = #{id1} and t.to_user_id = #{id2} or t.from_user_id = #{id2} and t.to_user_id = #{id1} order by t.create_time desc limit 0,15) m order by m.create_time")
    List<ChatMessage> getChatMessage15ByIds(int id1, int id2);

    /**
     * 获取群消息最近count条消息记录
     * @param groupId
     * @param start
     * @param count
     * @return
     */
    @Select("select * from (select * from groupmessage t where t.group_id = #{groupId} order by t.create_time desc limit #{start}, #{count}) m order by m.create_time")
    List<GroupMessage> getLatestGroupMessage(int groupId, int start, int count);

    /**
     * 获取单聊消息最近count条消息记录
     * @param id1
     * @param id2
     * @param start
     * @param count
     * @return
     */
    @Select("select * from (select * from chatmessage t where t.from_user_id = #{id1} and t.to_user_id = #{id2} or t.from_user_id = #{id2} and t.to_user_id = #{id1} order by t.create_time desc limit #{start},#{count}) m order by m.create_time")
    List<ChatMessage> getLatestChatMessageByIds(int id1, int id2, int start, int count);
}

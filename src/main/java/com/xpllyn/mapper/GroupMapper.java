package com.xpllyn.mapper;

import com.xpllyn.pojo.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface GroupMapper {

    @Select("select * from group where id = #{id}")
    Group getByGroupId(String id);

    @Select("select user_id from group_to_user where group_id = #{id}")
    List<Integer> getMemberByGroupId(String id);
}

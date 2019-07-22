package com.xpllyn.mapper;

import com.xpllyn.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User findByUserId(@Param("id") String id);


}

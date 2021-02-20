package com.xpllyn.service;

import com.xpllyn.pojo.Group;

import java.util.List;

public interface IGroupService {

    Group getByGroupId(String id);

    List<Integer> getMemberByGroupId(String id);
}

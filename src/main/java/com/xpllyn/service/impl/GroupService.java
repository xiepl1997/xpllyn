package com.xpllyn.service.impl;

import com.xpllyn.mapper.GroupMapper;
import com.xpllyn.pojo.Group;
import com.xpllyn.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService implements IGroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public Group getByGroupId(String id) {
        return groupMapper.getByGroupId(id);
    }

    @Override
    public List<Integer> getMemberByGroupId(String id) {
        return groupMapper.getMemberByGroupId(id);
    }
}

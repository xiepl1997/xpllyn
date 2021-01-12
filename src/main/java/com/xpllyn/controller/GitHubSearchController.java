package com.xpllyn.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xpllyn.pojo.GitHubProject;
import com.xpllyn.pojo.GitHubProjectOwner;
import com.xpllyn.utils.githubpageutil.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/GitHubPageSearch")
public class GitHubSearchController {

    @Autowired
    SearchUtil searchUtil = null;

    @RequestMapping("")
    public ModelAndView gotoGitHubPageSearch() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("tab_index", 4);
        mv.setViewName("github_page_search");
        return mv;
    }

    @RequestMapping("/{content}")
    @ResponseBody
    public List<GitHubProject> search(@PathVariable("content") String content) {
        JSONObject jsonObject = searchUtil.readJsonFromUrl(content);

        // 获取所有的GitHubProject
        JSONArray projectArray  = jsonObject.getJSONArray("items");
        List<GitHubProject> projectList = JSON.parseArray(projectArray.toJSONString(), GitHubProject.class);

        for (int i = 0; i < projectList.size(); i++) {
            String name = projectList.get(i).getName();
            if (name.length() >= 9 && name.substring(name.length() - 9).equals("github.io")) {
                projectList.get(i).setIo_url("http://" + name);
            }
        }
        return projectList;
    }
}

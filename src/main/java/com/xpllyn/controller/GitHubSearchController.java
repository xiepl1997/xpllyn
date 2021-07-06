package com.xpllyn.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xpllyn.pojo.GitHubRepository;
import com.xpllyn.utils.githubpageutil.SearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/GitHubPageSearch")
@Slf4j
public class GitHubSearchController {

    @Autowired
    SearchUtil searchUtil = null;

    @RequestMapping("")
    public ModelAndView gotoGitHubPageSearch() {
        log.info("【GitHubPage Search】 用户进入。");
        ModelAndView mv = new ModelAndView();
        mv.addObject("tab_index", 4);
        mv.setViewName("github_page_search");
        return mv;
    }

    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> search(@RequestParam("q") String q,
                                      @RequestParam("page") int page,
                                      @RequestParam("otherConditions") String otherConditions) {
        JSONObject jsonObject = searchUtil.readJsonFromUrl(q, page, otherConditions);

        // 获取所有的GitHubProject
        JSONArray repositoryArray  = jsonObject.getJSONArray("items");
        int repository_count = jsonObject.getInteger("total_count");
        List<GitHubRepository> repositoryList = JSON.parseArray(repositoryArray.toJSONString(), GitHubRepository.class);

        for (int i = 0; i < repositoryList.size(); i++) {
            String name = repositoryList.get(i).getName();
            String description = repositoryList.get(i).getDescription();
            if (name.length() >= 9 && name.substring(name.length() - 9).equals("github.io")) {
                repositoryList.get(i).setIo_url("http://" + name);
            }
            if (description != null && description.length() > 200) {
                repositoryList.get(i).setDescription(description.substring(0, 200) + "...");
            }

        }

//        // 创建cookie，存储当前查询
//        Cookie cookie = new Cookie("q_history", q);
//        //保存一天
//        cookie.setMaxAge(24*60*60);

        // 总页数，每页10个结果
        int page_count = (int)Math.ceil(repository_count / 10.0);

        Map<String, Object> map = new HashMap<>();
        map.put("repositoryList", repositoryList);
        map.put("repository_count", repository_count);
        map.put("page_count", page_count);

        log.info("【GitHubPage Search】 用户发起查询：" + q);

        return map;
    }
}

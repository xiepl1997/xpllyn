package com.xpllyn.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xpllyn.pojo.GitHubRepository;
import com.xpllyn.utils.githubpageutil.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

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

    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> search(@RequestParam("q") String q) {
        JSONObject jsonObject = searchUtil.readJsonFromUrl(q, 1);

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
            if (description.length() > 200) {
                repositoryList.get(i).setDescription(description.substring(0, 200) + "...");
            }

        }

        Queue queue = null;
        try {
            queue = searchUtil.getLanguageOrder(q);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List list = new ArrayList();
        while (!queue.isEmpty()) {
            list.add(queue.poll());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("repositoryList", repositoryList);
        map.put("language_order", list);
        map.put("repository_count", repository_count);
        return map;
    }
}

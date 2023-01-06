package com.pika.gstore.search.web;

import com.pika.gstore.search.service.MallSearchService;
import com.pika.gstore.search.vo.SearchParams;
import com.pika.gstore.search.vo.SearchResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/3 22:57
 */
@RestController
public class ListController {
    @Resource
    private MallSearchService mallSearchService;
    @GetMapping(value = {"list.html","/"})
    public ModelAndView list(SearchParams searchParams, HttpServletRequest request) {
        String queryString = request.getQueryString();
        searchParams.set_queryString(queryString);
        ModelAndView modelAndView = new ModelAndView("list");
        SearchResult result = mallSearchService.search(searchParams);
        modelAndView.addObject("result", result);
        return modelAndView;
    }
}

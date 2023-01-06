package com.pika.gstore.search.service;

import com.pika.gstore.search.vo.SearchParams;
import com.pika.gstore.search.vo.SearchResult;

/**
 * @author pi'ka'chu
 */
public interface MallSearchService {
    SearchResult search(SearchParams searchParams);
}

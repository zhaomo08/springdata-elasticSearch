package com.itheima.repositories;

import com.itheima.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

/**
 * @author zjc
 * @create 2020--03--04--16:56
 */
public interface ArticleRepository extends ElasticsearchCrudRepository<Article,Long> {

    List<Article> findByTitle(String title);
    List<Article> findByTitleOrContent(String title,String content);
    List<Article> findByTitleOrContent(String title, String content, Pageable pageable);
}

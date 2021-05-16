package com.byxm.mapper;

import com.byxm.model.Article;

import java.util.List;

public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<Article> selectAll();

    int selectCount();

    List<Article> manageSelectAll();

    List<Article> manageSelect(String title, String examine);

    List<Article> selectReadyArticle();
}
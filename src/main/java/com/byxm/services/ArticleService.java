package com.byxm.services;

import com.byxm.model.Article;

import java.util.List;

public interface ArticleService {
    Article getArticleById(int id);

    List<Article> getThree();

    List<Article> getAll();

    List<Article> articlePage(int page, int size);

    int getArticleCount();

    List<Article> getArticlePage(Integer page, Integer size,String title,String examine);

    List<Article> getAllArticle();

    int modifyArticle(Article article);

    int removeArticle(Integer id);

    int addArticle(Article article);

    List<Article> getReadyArticlePage(Integer page, Integer size);
}

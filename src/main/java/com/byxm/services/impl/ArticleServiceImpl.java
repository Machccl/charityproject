package com.byxm.services.impl;

import com.byxm.mapper.ArticleMapper;
import com.byxm.model.Article;
import com.byxm.services.ArticleService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Article getArticleById(int id) {

        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Article> getThree() {
        PageHelper.startPage(1,3," createtime desc");
        List<Article> articles = articleMapper.selectAll();
        return articles;
    }

    @Override
    public List<Article> getAll() {
        return articleMapper.selectAll();
    }

    @Override
    public List<Article> articlePage(int page, int size) {
        PageHelper.startPage(page,size," createtime desc");
        List<Article> articles = articleMapper.selectAll();
        return articles;
    }

    @Override
    public int getArticleCount() {
        return articleMapper.selectCount();
    }

    @Override
    public List<Article> getArticlePage(Integer page, Integer size,String title,String examine) {
        PageHelper.startPage(page,size);
        List<Article> articles = articleMapper.manageSelect(title,examine);
        return articles;
    }

    @Override
    public List<Article> getAllArticle() {
        return articleMapper.manageSelectAll();
    }

    @Override
    public int modifyArticle(Article article) {
        return articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public int removeArticle(Integer id) {
        return articleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int addArticle(Article article) {
        return articleMapper.insertSelective(article);
    }

    @Override
    public List<Article> getReadyArticlePage(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<Article> articles = articleMapper.selectReadyArticle();
        return articles;
    }
}

package com.service;

import java.util.List;

import com.dao.BoardDAO;
import com.domain.ArticleVO;

public class BoardService {
	
	BoardDAO dao; 
	
	public BoardService() {
		dao = new BoardDAO(); 
	}
	
	public List<ArticleVO> listArticles() {
		List<ArticleVO> articlesList = dao.selectAllArticles();
		return articlesList;
	}
	
	public int addArticle(ArticleVO article){
		return dao.insertNewArticle(article);		
	}

	public ArticleVO viewArticle(int articleNO) {
		return dao.selectArticle(articleNO);
	}
	
	public void modArticle(ArticleVO article) {
		dao.updateArticle(article);
	}
}

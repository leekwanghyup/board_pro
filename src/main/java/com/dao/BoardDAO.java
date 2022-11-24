package com.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.domain.ArticleVO;

public class BoardDAO {
	private DataSource dataSource;

	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<ArticleVO> selectAllArticles() {
		List<ArticleVO> articlesList = new ArrayList<>();
		String query = "SELECT LEVEL,articleNO,parentNO,title,content,id,writeDate" 
				+ " from t_board START WITH  parentNO=0 CONNECT BY PRIOR articleNO=parentNO"
				+ " ORDER SIBLINGS BY articleNO DESC";
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
		){
			while (rs.next()) {
				ArticleVO article = ArticleVO.builder()
					.level(rs.getInt("level"))
					.articleNO(rs.getInt("articleNO"))
					.parentNO(rs.getInt("parentNO"))
					.title(rs.getString("title"))
					.content(rs.getString("content"))
					.id(rs.getString("id"))
					.writeDate(rs.getDate("writeDate")).build();
				articlesList.add(article);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articlesList;
	}
}


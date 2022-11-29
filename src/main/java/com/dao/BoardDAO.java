package com.dao;

import java.net.URLEncoder;
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
	
	// 새로운 게시글 번호 생성 
	private int getNewArticleNO() {
		int newArticleNumber = 0; 
		String query = "SELECT  max(articleNO) as newNo from t_board";
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
		){
			if(rs.next()) newArticleNumber = rs.getInt("newNo")+1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newArticleNumber;
	}
	
	// 새로운 글 작성 
	public int insertNewArticle(ArticleVO article) {
		String query = "INSERT INTO t_board (articleNO, parentNO, title, content, imageFileName, id)";
		query +=  "VALUES (?, ? ,?, ?, ?, ?)";
		int articleNo = getNewArticleNO();
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);
		){
			pstmt.setInt(1, articleNo);
			pstmt.setInt(2, article.getParentNO());
			pstmt.setString(3, article.getTitle());
			pstmt.setString(4, article.getContent());
			pstmt.setString(5, article.getImageFileName());
			pstmt.setString(6, article.getId());
			pstmt.executeUpdate();
		} catch (Exception e) {
				e.printStackTrace();
		}
		return articleNo;
	}
	
	// 글 상세 
	public ArticleVO selectArticle(int articleNO){
		String query ="select articleNO, parentNO, title, content, imageFileName, id, writeDate from t_board where articleNO=?";
		ArticleVO article = new ArticleVO();
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);
		){
			pstmt.setInt(1, articleNO);
			try(ResultSet rs=pstmt.executeQuery()){
				if(rs.next()) {
					int _articleNO =rs.getInt("articleNO");
					int parentNO=rs.getInt("parentNO");
					String title = rs.getString("title");
					String content =rs.getString("content");
					String imageFileName = null; 
					if(rs.getString("imageFileName")!=null) {
						imageFileName = URLEncoder.encode(rs.getString("imageFileName"), "UTF-8");
					}
					String id = rs.getString("id");
					Date writeDate = rs.getDate("writeDate");
					article.setArticleNO(_articleNO);
					article.setParentNO (parentNO);
					article.setTitle(title);
					article.setContent(content);
					article.setImageFileName(imageFileName);
					article.setId(id);
					article.setWriteDate(writeDate);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return article; 
	}
	
	// 글 수정 
	public void updateArticle(ArticleVO article) {
		int articleNO = article.getArticleNO();
		String imageFileName = article.getImageFileName();
		
		// 이미지 파일 여부에 따른 동적쿼리 생성
		String query = "update t_board  set title=?,content=?";
		if (imageFileName!=null && imageFileName.length()!= 0) {
			query += ",imageFileName=?";
		}
		query += " where articleNO=?";
		
		try (
			Connection conn = dataSource.getConnection();				
			PreparedStatement pstmt = conn.prepareStatement(query);
		){
			pstmt.setString(1, article.getTitle());
			pstmt.setString(2, article.getContent());
			if (imageFileName != null && imageFileName.length() != 0) {
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNO);
			} else {
				pstmt.setInt(3, articleNO);
			}
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 글 삭제 
	public void deleteArticle(int  articleNO) {
		String query = "DELETE FROM t_board ";
		query += " WHERE articleNO in (";
		query += "  SELECT articleNO FROM  t_board ";
		query += " START WITH articleNO = ?";
		query += " CONNECT BY PRIOR  articleNO = parentNO )";
		try (
			Connection conn = dataSource.getConnection();	
			PreparedStatement pstmt = conn.prepareStatement(query);
		){
			pstmt.setInt(1, articleNO);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 삭제한 글 번호 
	public List<Integer> selectRemovedArticles(int articleNO) {
		List<Integer> articleNOList = new ArrayList<Integer>(); // 삭제대상 게시글 번호 목록
		String query = "SELECT articleNO FROM  t_board";
		query += " START WITH articleNO = ?";
		query += " CONNECT BY PRIOR articleNO = parentNO";
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);
		){
			pstmt.setInt(1, articleNO);
			try(ResultSet rs = pstmt.executeQuery();){
				while (rs.next()) {
					articleNO = rs.getInt("articleNO");
					System.out.println("BoadDAO : "+articleNO);
					articleNOList.add(articleNO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleNOList;
	}
	
}


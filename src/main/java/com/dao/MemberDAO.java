package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.common.ConnectionUtil;
import com.domain.MemberVO;

public class MemberDAO {
	
	DataSource dataSource; 
	
	public MemberDAO() {
		dataSource = ConnectionUtil.getDataSource();
	}

	// 회원가입
	public void addmember(MemberVO memberVO) {
		String query = "INSERT INTO T_MEMBER(MNO,ID,PWD,NAME,EMAIL)";
		query+=" VALUES(MNO_SEQ.nextval,?,?,?,?)";
		try (
			Connection conn = dataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);
		){
			pstmt.setString(1, memberVO.getId());
			pstmt.setString(2, memberVO.getPwd());
			pstmt.setString(3, memberVO.getName());
			pstmt.setString(4, memberVO.getEmail());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 로그인 체크 
	public boolean loginCheck(MemberVO memberVO) {
		boolean result = false;
		String query = "select decode(count(*),1,'true','false') as result from t_member  where id=? and pwd=?";
		try (
			Connection con = dataSource.getConnection();
			PreparedStatement pstmt = con.prepareStatement(query);
		){
			pstmt.setString(1, memberVO.getId());
			pstmt.setString(2, memberVO.getPwd());
			try(ResultSet rs = pstmt.executeQuery();){
				if(rs.next()) result = Boolean.parseBoolean(rs.getString("result"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}

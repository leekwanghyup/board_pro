package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
}

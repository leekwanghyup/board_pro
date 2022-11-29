package com.service;

import com.dao.MemberDAO;
import com.domain.MemberVO;

public class MemberService {

	private MemberDAO dao; 
	
	public MemberService(MemberDAO dao) {
		this.dao = dao;
	}
	public void joinMember(MemberVO memberVO) {
		dao.addmember(memberVO);
	}

}

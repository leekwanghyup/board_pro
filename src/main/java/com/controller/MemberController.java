package com.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.MemberDAO;
import com.domain.AuthVO;
import com.domain.MemberVO;
import com.service.MemberService;

@WebServlet("/member/*")
public class MemberController extends HttpServlet{
	
	private MemberService service;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		service = new MemberService(new MemberDAO());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		final String PREFIX = "/WEB-INF/views/member/";
		final String SUFFIX = ".jsp";
		
		String pathInfo = request.getPathInfo();
		String contextPath = request.getContextPath();
		String nextPage = null;
		HttpSession session = null; 

		// 회원가입 폼
		if(pathInfo.equals("/joinForm")) { 
			nextPage = "joinForm";
		} 
		
		// 회원가입 처리
		else if(pathInfo.equals("/join")) { 
			MemberVO memberVO = MemberVO.builder()
					.id(request.getParameter("userId"))
					.pwd((String) request.getAttribute("userPwd"))
					.name(request.getParameter("userName"))
					.email(request.getParameter("email"))
					.build();
			service.joinMember(memberVO);
			response.sendRedirect(contextPath+"/");
			return;
		}
		
		// 나의 정보 보기
		else if(pathInfo.equals("/myPage")) {
			
		} else if(pathInfo.equals("/loginForm")) {
			nextPage = "loginForm";
		}
			
		else if(pathInfo.equals("/login")) {
			String userId = request.getParameter("userId");
			String userPwd = (String) request.getAttribute("userPwd");
			MemberVO vo = MemberVO.builder().id(userId).pwd(userPwd).build();
			if(service.loginService(vo)) {
				session = request.getSession();
				session.setAttribute("auth", new AuthVO(vo.getId()));
				response.sendRedirect(contextPath+"/");
			} else {
				System.out.println("MemberController.login 아이디 또는 비밀번호 맞지 않음");
			}
			return; 
		} else if(pathInfo.equals("/logout")) {
			session = request.getSession(false);
			session.removeAttribute("auth");
			response.sendRedirect(contextPath+"/");
			return; 
		}
		else {
			System.out.println("존재하지 않는 페이지");
			return; 
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(PREFIX+nextPage+SUFFIX);
		rd.forward(request, response);
	}
}

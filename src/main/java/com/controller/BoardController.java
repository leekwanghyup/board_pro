package com.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.BoardService;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	
	private BoardService service;
	
	@Override
	public void init() throws ServletException {
		service = new BoardService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		final String PREFIX = "/WEB-INF/views/board/";
		final String SUFFIX = ".jsp";
		
		String pathInfo = request.getPathInfo(); 
		String nextPage = null;
		
		if(pathInfo.equals("/") || pathInfo.equals("/listArticles") ||  pathInfo==null) {
			nextPage = "listArticles";
		} else { // 존재하지 않는 페이지 
				
		}
		RequestDispatcher rd = request.getRequestDispatcher(PREFIX+nextPage+SUFFIX);
		rd.forward(request, response);
	}

}

package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.domain.ArticleVO;
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
		String contextPath = request.getContextPath();
		String nextPage = null;
		
		if(pathInfo==null || pathInfo.equals("/") || pathInfo.equals("/listArticles")) { // 글 목록 
			List<ArticleVO> listArticles = service.listArticles();
			request.setAttribute("listArticles", listArticles);
			nextPage = "listArticles";
		} else if(pathInfo.equals("/articleForm")) { // 글쓰기 폼
			nextPage = "articleForm";
		} else if(pathInfo.equals("/addArticle")) {
			System.out.println("글 등록 처리");
			Map<String, String> articleMap = upload(request, response);
			String title = articleMap.get("title");
			String content = articleMap.get("content");
			String id = articleMap.get("id");
			String imageFileName = articleMap.get("imageFileName");
			ArticleVO vo = new ArticleVO(); 
			vo.setParentNO(0);
			vo.setId(id);
			vo.setTitle(title);
			vo.setContent(content);
			vo.setImageFileName(imageFileName);
			System.out.println(vo);
			response.sendRedirect(contextPath+"/board");
			return;
		}
		
		else { // 존재하지 않는 페이지
			System.out.println("존재하지 않는 페이지");
			return; 
		}
		RequestDispatcher rd = request.getRequestDispatcher(PREFIX+nextPage+SUFFIX);
		rd.forward(request, response);
	}

	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> articleMap = new HashMap<String, String>();
		String encoding = "utf-8";
		File currentDirPath = new File("c:/file_repo");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(1024*1024*10); // 업로드 파일 최대 크기 지정 10MB
		try {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem fileItem : items) {
				if (fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				} else { // 요청정보가 파일일 때 
					if (fileItem.getSize() > 0) {
						String fileName = fileItem.getName(); // 파일이름
						articleMap.put(fileItem.getFieldName(), fileName);
						File uploadFile = new File(currentDirPath,fileName); // 경로 지정
						fileItem.write(uploadFile); // 파일업로드 
					} 
				} 
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleMap;
	}

}

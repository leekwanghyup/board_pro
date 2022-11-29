package com.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
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
import org.apache.commons.io.FileUtils;

import com.domain.ArticleVO;
import com.service.BoardService;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	
	private static final String ARTICLE_IMAGE_REPO = "c:/file_repo";
	private static final String ENCODING = "utf-8";
	
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
		
		// 글 목록
		if(pathInfo==null || pathInfo.equals("/") || pathInfo.equals("/listArticles")) {  
			List<ArticleVO> listArticles = service.listArticles();
			request.setAttribute("listArticles", listArticles);
			nextPage = "listArticles";
		
		// 글쓰기 폼
		} else if(pathInfo.equals("/articleForm")) { 
			nextPage = "articleForm";
		} else if(pathInfo.equals("/addArticle")) { // 글등록 처리 
			Map<String, String> multipartRequest = getMultipartRequest(request, response);
			String title = multipartRequest.get("title");
			String content = multipartRequest.get("content");
			String id = multipartRequest.get("id");
			String imageFileName = multipartRequest.get("imageFileName");
			ArticleVO vo = ArticleVO.builder().parentNO(0)
			.id(id).title(title).content(content).imageFileName(imageFileName).build();
			int articleNO = service.addArticle(vo);
			
			// 이미지 파일을 업로드 한 경우 
			if(imageFileName!=null && imageFileName.length()!=0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO+"/temp/"+imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO +"/"+articleNO);
				destDir.mkdirs(); // 디렉토리 생성 
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			response.sendRedirect(contextPath+"/board");
			return;
			
		// 글 상세 보기
		} else if(pathInfo.equals("/viewArticle")) {  
			String articleNO = request.getParameter("articleNO");
			ArticleVO articleVO = service.viewArticle(Integer.parseInt(articleNO));
			request.setAttribute("article", articleVO);
			nextPage = "viewArticle";
			
		//글 수정 처리
		} else if(pathInfo.equals("/modArticle")) {  
			Map<String, String> multipartRequest = getMultipartRequest(request, response);
			int articleNO = Integer.parseInt(multipartRequest.get("articleNO"));
			String imageFileName = multipartRequest.get("imageFileName");
			ArticleVO articleVO = ArticleVO.builder()
					.articleNO(articleNO)
					.content(multipartRequest.get("content"))
					.title(multipartRequest.get("title"))
					.imageFileName(multipartRequest.get("imageFileName")).build();
			service.modArticle(articleVO);
			// 이미지 파일이 있는 경우 
			if (imageFileName != null && imageFileName.length() != 0) {
				String originalFileName = multipartRequest.get("originalFileName");
				File srcFile = new File(ARTICLE_IMAGE_REPO + "/" + "temp" + "/" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "/" + articleNO);
				destDir.mkdirs();
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
				// 수정 전 이미지 파일이 있는 경우 
				if(originalFileName!=null) {
					File oldFile = new File(ARTICLE_IMAGE_REPO + "/" + articleNO + "/" + URLDecoder.decode(originalFileName, "utf-8")); // 수정 전 파일 경로
					oldFile.delete(); // 수정 전 파일 삭제
				}
			}
			response.sendRedirect(contextPath+"/board");
			return;
			
		// 글 삭제 처리
		} else if(pathInfo.equals("/removeArticle")) {  
			Map<String,String> multipartRequest = getMultipartRequest(request, response);
			int articleNO = Integer.parseInt(multipartRequest.get("articleNO"));
			List<Integer> articleNOList = service.removeArticle(articleNO); // 삭제 대상 글 번호 목록
			for (int _articleNO : articleNOList) {
				File dir = new File(ARTICLE_IMAGE_REPO + "/" + _articleNO);
				if (dir.exists()) {
					FileUtils.deleteDirectory(dir);
				}
			}	
			response.sendRedirect(contextPath+"/board");
			return; 
		}
		
		// 존재하지 않는 페이지
		else { 
			System.out.println("존재하지 않는 페이지");
			return; 
		}
		RequestDispatcher rd = request.getRequestDispatcher(PREFIX+nextPage+SUFFIX);
		rd.forward(request, response);
	}

	private Map<String, String> getMultipartRequest(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> articleMap = new HashMap<String, String>();
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(1024*1024*10); // 업로드 파일 최대 크기 지정 10MB
		try {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem fileItem : items) {
				if (fileItem.isFormField()) {
					articleMap.put(fileItem.getFieldName(), fileItem.getString(ENCODING));
				} else { // 요청정보가 파일일 때 
					if (fileItem.getSize() > 0) {
						String fileName = fileItem.getName(); // 파일이름
						articleMap.put(fileItem.getFieldName(), fileName);
						File uploadFile = new File(currentDirPath+"/temp",fileName); // 경로 지정
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

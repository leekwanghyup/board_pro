package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/fileDownload")
public class FileDownloadController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String file_repo = "C:\\file_repo";
		String articleNO = request.getParameter("articleNO");
		String fileName = request.getParameter("imageFileName");
		File f = new File(file_repo,articleNO+"/"+fileName);
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Content-disposition", "attachment;fileName="+fileName);
		try(
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(f);
		){
			byte[] buffer = new byte[1024*8];
			int count = 0;
			while ((count = in.read(buffer))!=-1) {
				out.write(buffer, 0, count);
			}
		}
	}

}

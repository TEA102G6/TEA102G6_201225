package com.album.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.websocket.Session;

import com.album.model.AlbumService;
import com.album.model.AlbumVO;

@WebServlet("/album/album.do")
@MultipartConfig
public class albumServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");

//搜尋一個顯示
		if ("search".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			try {
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
				String album_id = req.getParameter("album_id");
				if (album_id == null || (album_id.trim()).length() == 0) {
					errorMsgs.add("請輸入專輯ID");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/album/list_all_album.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}

				/*************************** 2.開始查詢資料 *****************************************/
				AlbumService albumSvc = new AlbumService();
				AlbumVO albumVO = albumSvc.getOneAlbum(album_id);
				if (albumVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/album/list_all_album.jsp");
					failureView.forward(req, res);
					return;// 程式中斷
				}

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
				req.setAttribute("albumVO", albumVO); // 資料庫取出的empVO物件,存入req
				String url = "/back-end/album/list_one_album.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneEmp.jsp
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/album/list_all_album.jsp");
				failureView.forward(req, res);
			}
		}

// 取得樂團照片		
		if ("getAlbumPhoto".equals(action)) {

			String album_id = req.getParameter("album_id");
			ServletOutputStream out = res.getOutputStream();

			try {
			AlbumService albumSvc = new AlbumService();
			AlbumVO albumVO = albumSvc.getAlbumPhoto(album_id);
			res.setContentType("image/gif");
			res.setContentLength(albumVO.getAlbum_photo().length);
			out.write(albumVO.getAlbum_photo());
			} catch (Exception e) {
				InputStream in = getServletContext().getResourceAsStream("/images/fileNotFound.jpg");
				byte[] b = new byte[in.available()];
				in.read(b);
				out.write(b);
				in.close();
			} 

		}

// 修改資訊 先查一
		if ("getOne_For_Update".equals(action)) { // 來自listAllEmp.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*************************** 1.接收請求參數 ****************************************/
				String album_id = req.getParameter("album_id");

				/*************************** 2.開始查詢資料 ****************************************/
				AlbumService albumSvc = new AlbumService();
				AlbumVO albumVO = albumSvc.getOneAlbum(album_id);

				/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
				req.setAttribute("albumVO", albumVO); // 資料庫取出的VO物件,存入req
				String url = "/back-end/album/update_album.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_album.jsp
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				errorMsgs.add("無法取得要修改的資料:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/album/list_all_album.jsp");
				failureView.forward(req, res);
			}
		}

// 真的接收修改的資料要修改了		
		if ("update".equals(action)) { // 來自update_emp_input.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/

				String album_id = req.getParameter("album_id");

				// 取得原本的資料: 1. 有些資料不會改 2. 圖片沒上傳時用
				AlbumService albumSvc = new AlbumService();
				AlbumVO albumVO_origin = albumSvc.getOneAlbum(album_id);

				String band_id = albumVO_origin.getBand_id(); // 不能改

				String album_name = req.getParameter("album_name"); // 最長30字
				if (album_name == null || album_name.trim().length() == 0) {
					errorMsgs.add("專輯名稱: 請勿空白");
				} else if (album_name.trim().length() > 30) { // 以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("專輯名稱: 長度最長30字");
				}

				String album_intro = req.getParameter("album_intro"); // 最長300字
				if (album_intro == null || album_intro.trim().length() == 0) {
					errorMsgs.add("專輯簡介: 請勿空白");
				} else if (album_intro.trim().length() > 300) { // 以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("專輯簡介: 長度最長300字");
				}

//				album_photo
				Part album_photo_part = req.getPart("album_photo");
				InputStream in = album_photo_part.getInputStream();
				byte[] album_photo = new byte[in.available()];
				in.read(album_photo);
				in.close();

				Integer album_status = new Integer(req.getParameter("album_status")); // 下拉選單不須檢查

				Timestamp album_release_time = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse(req.getParameter("album_release_time")).getTime());

				AlbumVO albumVO = new AlbumVO();
				albumVO.setAlbum_id(album_id);
				albumVO.setBand_id(band_id);
				albumVO.setAlbum_name(album_name);
				albumVO.setAlbum_intro(album_intro);

				// 判斷圖片是否有重新上傳
				System.out.println(album_photo.length);
				if (album_photo.length != 0) {
					albumVO.setAlbum_photo(album_photo);
					System.out.println("A");
				} else {
					System.out.println(albumVO_origin.getAlbum_photo().length);
					albumVO.setAlbum_photo(albumVO_origin.getAlbum_photo());
					System.out.println("B");
				}

				albumVO.setAlbum_status(album_status);
				albumVO.setAlbum_release_time(album_release_time);

//				專輯新增時間
//				專輯最後編輯時間
//				專輯最後編輯者
				albumVO.setAlbum_add_time(albumVO_origin.getAlbum_add_time());
				albumVO.setAlbum_last_edit_time(albumVO_origin.getAlbum_last_edit_time());
				albumVO.setAlbum_last_editor(albumVO_origin.getAlbum_last_editor());

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("albumVO", albumVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/album/update_album.jsp");
					failureView.forward(req, res);
					return; // 程式中斷
				}

				/*************************** 2.開始修改資料 *****************************************/
				Timestamp album_last_edit_time = new Timestamp(
						new java.util.Date(System.currentTimeMillis()).getTime());
				String album_last_editor = (String) req.getSession().getAttribute("memberVO");

				albumVO.setAlbum_last_edit_time(
						new Timestamp(System.currentTimeMillis()));
				albumVO.setAlbum_last_editor((String) req.getSession().getAttribute("memberVO"));

				albumVO = albumSvc.updateAlbum(albumVO);

				/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
				req.setAttribute("albumVO", albumVO); // 資料庫update成功後,正確的的empVO物件,存入req
				String url = "/back-end/album/list_one_album.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 *************************************/
			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/album/update_album.jsp");
				failureView.forward(req, res);
			}
		}

// 新增專輯		
		if ("insert".equals(action)) { // 來自addEmp.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				/*********************** 1.接收請求參數 - 輸入格式的錯誤處理 *************************/
				HttpSession session = req.getSession();
				String band_id = (String) session.getAttribute("bandVO");
				String editor_id = (String) session.getAttribute("memberVO");
				System.out.println(band_id);
				String album_name = req.getParameter("album_name"); // 最長30字
				if (album_name == null || album_name.trim().length() == 0) {
					errorMsgs.add("專輯名稱: 請勿空白");
				} else if (album_name.trim().length() > 30) { // 以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("專輯名稱: 長度最長30字");
				}

				String album_intro = req.getParameter("album_intro"); // 最長300字
				if (album_intro == null || album_intro.trim().length() == 0) {
					errorMsgs.add("專輯簡介: 請勿空白");
				} else if (album_intro.trim().length() > 300) { // 以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("專輯簡介: 長度最長300字");
				}

//				album_photo
				Part album_photo_part = req.getPart("album_photo");
				InputStream in = album_photo_part.getInputStream();
				int fileLength = in.available();
				byte[] album_photo = new byte[fileLength];
				in.read(album_photo);
				in.close();
				if(fileLength==0) {
					errorMsgs.add("請選擇圖片");
				}

				java.sql.Timestamp album_release_time = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse(req.getParameter("album_release_time")).getTime());

				AlbumVO albumVO = new AlbumVO();
				albumVO.setBand_id(band_id);
				albumVO.setAlbum_name(album_name);
				albumVO.setAlbum_intro(album_intro);
				albumVO.setAlbum_photo(album_photo);
				albumVO.setAlbum_status(0);
				albumVO.setAlbum_add_time(new Timestamp(System.currentTimeMillis()));
				albumVO.setAlbum_release_time(album_release_time);
				albumVO.setAlbum_last_edit_time(new Timestamp(System.currentTimeMillis()));
				albumVO.setAlbum_last_editor(editor_id);

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("albumVO", albumVO); // 含有輸入格式錯誤的empVO物件,也存入req
					RequestDispatcher failureView = req.getRequestDispatcher("/back-end/album/update_album.jsp");
					failureView.forward(req, res);
					return; // 程式中斷
				}

				/*************************** 2.開始新增資料 ***************************************/
				AlbumService albumSvc = new AlbumService();
				albumVO = albumSvc.insertAlbum(albumVO);

				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
				String url = "/back-end/album/list_all_album.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/album/update_album.jsp");
				failureView.forward(req, res);
			}
			
		}

	}

}
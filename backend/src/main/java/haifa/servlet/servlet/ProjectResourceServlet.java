package haifa.servlet.servlet;

import haifa.servlet.database.CategoryResProvider;
import haifa.servlet.database.ConnPool;


import haifa.servlet.database.ReceiptResProvider;
import haifa.servlet.objects.Category;
import haifa.servlet.objects.Receipt;
import haifa.servlet.utils.FilesUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProjectResourceServlet
 */
public class ProjectResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// ========
	private static final int GET_ALL_RECEIPTS_JSON_REQ = 0;
	//private static final int INSERT_FOLDER_REQ = 1;
	//private static final int DELETE_FOLDER_REQ = 2;
	private static final int INSERT_RECEIPT_REQ = 3;
	private static final int DELETE_RECEIPT_REQ = 4;
	private static final int GET_RECEIPT_IMAGE_REQ = 5;
	//private static final int GET_RECEIPTS_OF_USER_JSON_REQ = 6;
	private static final int GET_FILE_FROM_FILESYSTEM_REQ = 7;

	private static final String USER_ID = "u_id";
	private static final String USER_NAME = "u_name";

	private static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
	private static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";

	private static final String RECEIPT_ID = "rec_id";
	private static final String RECEIPT_TITLE = "rec_title";
	private static final String RECEIPT_DESCRIPTION = "rec_desc";
	private static final String RECEIPT_USER_ID = "rec_uid";
	private static final String FILE_NAME ="name";

	private static final String REQ = "req";

	public static final int DB_RETRY_TIMES = 5;


	public void init(ServletConfig config) throws ServletException {

		super.init();

		String tmp = config.getServletContext().getInitParameter("localAppDir");
		if (tmp != null) {
			FilesUtils.appDirName = config.getServletContext().getRealPath(tmp);
			System.out.println(FilesUtils.appDirName);

		}

	}

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		String respPage = null;
		String userReq = req.getParameter(REQ);
		Connection conn = null;
		int retry = DB_RETRY_TIMES;

		if (userReq != null) {

			int reqNo = Integer.valueOf(userReq);
			System.out.println("ProjectResourceServlet:: req code ==>" + reqNo);
			while (retry > 0) {

				try {

					switch (reqNo) {

					// == folder apis
					case GET_ALL_RECEIPTS_JSON_REQ: {
						System.out.println("==>");

						conn = ConnPool.getInstance().getConnection();
						ReceiptResProvider receiptResProvider = new ReceiptResProvider();
						List<Receipt> receiptList = receiptResProvider
								.getAllReceipts(conn);
						String resultJson = Receipt.toJson(receiptList);

						if(resultJson == null ){
							System.out.println(receiptList.toString());
						}

						if (resultJson != null && !resultJson.isEmpty()) {
							respPage = resultJson;
							resp.addHeader("Content-Type",
									"application/json; charset=UTF-8");
							PrintWriter pw = resp.getWriter();
							pw.write(respPage);
						} else {
							resp.sendError(404);
						}

						retry = 0;
						break;
					}

					/*case INSERT_FOLDER_REQ: {
						String id = req.getParameter(FOLDER_ID);
						String title = req.getParameter(FOLDER_TITLE);
						respPage = RESOURCE_FAIL_TAG;
						resp.addHeader("Content-Type",
								"application/json; charset=UTF-8");
						conn = ConnPool.getInstance().getConnection();
						FoldersResProvider foldersResProvider = new FoldersResProvider();

						Folder folder = new Folder(id, title);
						if (foldersResProvider.insertFolder(folder, conn)) {
							respPage = RESOURCE_SUCCESS_TAG;
						}
						PrintWriter pw = resp.getWriter();
						pw.write(respPage);

						retry = 0;
						break;
					}

					case DELETE_FOLDER_REQ: {
						String id = req.getParameter(FOLDER_ID);
						respPage = RESOURCE_FAIL_TAG;
						resp.addHeader("Content-Type",
								"application/json; charset=UTF-8");
						conn = ConnPool.getInstance().getConnection();
						FoldersResProvider foldersResProvider = new FoldersResProvider();

						Folder folder = new Folder(id);
						if (foldersResProvider.deleteFolder(folder, conn)) {
							respPage = RESOURCE_SUCCESS_TAG;
						}
						PrintWriter pw = resp.getWriter();
						pw.write(respPage);

						retry = 0;
						break;
					} */
					// == end folder apis

					// == item apis
					case INSERT_RECEIPT_REQ: {
						String id = req.getParameter(RECEIPT_ID);

						String title = req.getParameter(RECEIPT_TITLE);

						String descrpition = req.getParameter(RECEIPT_DESCRIPTION);

						String userId = req.getParameter(RECEIPT_USER_ID);

						ServletInputStream isServ = req.getInputStream();

						DataInputStream is = new DataInputStream(isServ);

						ByteArrayOutputStream bin = new ByteArrayOutputStream(
								2048);
						int data;
						while ((data = is.read()) != -1) {
							bin.write((byte) data);
						}

						byte[] imageBlob = bin.toByteArray();

						respPage = RESOURCE_FAIL_TAG;
						resp.addHeader("Content-Type",
								"application/json; charset=UTF-8");
						conn = ConnPool.getInstance().getConnection();
						ReceiptResProvider receiptResProvider = new ReceiptResProvider();

						Receipt receipt = new Receipt(id, title, descrpition, imageBlob,
								userId);
						if (receiptResProvider.insertReceipt(receipt, conn)) {
							respPage = RESOURCE_SUCCESS_TAG;
						}
						PrintWriter pw = resp.getWriter();
						pw.write(respPage);

						retry = 0;
						break;
					}

					case DELETE_RECEIPT_REQ: {
						String id = req.getParameter(RECEIPT_ID);
						respPage = RESOURCE_FAIL_TAG;
						resp.addHeader("Content-Type",
								"application/json; charset=UTF-8");
						conn = ConnPool.getInstance().getConnection();
						ReceiptResProvider itemsResProvider = new ReceiptResProvider();
						Receipt receipt = new Receipt(id);
						if (itemsResProvider.deleteReceipt(receipt, conn)) {
							respPage = RESOURCE_SUCCESS_TAG;
						}
						PrintWriter pw = resp.getWriter();
						pw.write(respPage);

						retry = 0;
						break;
					}

					/*case GET_RECEIPTS_OF_USER_JSON_REQ: {

						String id = req.getParameter(USER_ID);
						conn = ConnPool.getInstance().getConnection();
						ReceiptResProvider itemsResProvider = new ReceiptResProvider();
						User folder = new User(id);
						List<Item> itemsList = itemsResProvider.getAllItems(
								folder, conn);
						String resultJson = Item.toJson(itemsList);

						if (resultJson != null && !resultJson.isEmpty()) {
							respPage = resultJson;
							resp.addHeader("Content-Type",
									"application/json; charset=UTF-8");
							PrintWriter pw = resp.getWriter();
							pw.write(respPage);
						} else {
							resp.sendError(404);
						}

						retry = 0;
						break;
					}*/

					case GET_RECEIPT_IMAGE_REQ: {
						String id = req.getParameter(RECEIPT_ID);
						respPage = RESOURCE_FAIL_TAG;

						conn = ConnPool.getInstance().getConnection();
						ReceiptResProvider receiptResProvider = new ReceiptResProvider();

						byte[] imgBlob = receiptResProvider.getImage(id, conn);

						if (imgBlob != null && imgBlob.length > 0) {
							ServletOutputStream os = resp.getOutputStream();
							os.write(imgBlob);
						} else {
							resp.sendError(404);
						}

						retry = 0;
						break;
					}
					
					case GET_FILE_FROM_FILESYSTEM_REQ: {
						String fileName = req.getParameter(FILE_NAME);
						
						File file = new File(FilesUtils.appDirName,fileName);
						
						byte[] blob = FilesUtils.getLocalCopy(file);

						if (blob != null && blob.length > 0) {
							ServletOutputStream os = resp.getOutputStream();
							os.write(blob);
						} else {
							resp.sendError(404);
						}

						retry = 0;
						break;
					}

					// == end items apis

					default:
						retry = 0;
					}

				} catch (SQLException e) {
					e.printStackTrace();
					retry--;
				} catch (Throwable t) {
					t.printStackTrace();
					retry = 0;
				} finally {
					if (conn != null) {
						ConnPool.getInstance().returnConnection(conn);
					}
				}
			}
		}

	}

}

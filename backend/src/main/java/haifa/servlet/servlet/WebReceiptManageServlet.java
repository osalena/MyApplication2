package haifa.servlet.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import haifa.servlet.database.CategoryResProvider;
import haifa.servlet.database.ReceiptResProvider;
import haifa.servlet.objects.Category;
import haifa.servlet.objects.Receipt;
import haifa.servlet.utils.FilesUtils;
import haifa.servlet.database.ConnPool;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WebReceiptManageServlet
 */


public class WebReceiptManageServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	
	public static final int DB_RETRY_TIMES=5;

	
	
	private static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
	private static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";
	
	private static final String RECEIPT_ID = "rec_id";
	private static final String RECEIPT_TITLE = "rec_title";
	private static final String RECEIPT_DESCRIPTION = "rec_desc";
	private static final String RECEIPT_USER_ID = "rec_uid";
	private static final String IS_DELETE = "delete";


   

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


		// Commons file upload classes are specifically instantiated
		FileItemFactory factory = new DiskFileItemFactory();

		ServletFileUpload upload = new ServletFileUpload(factory);
		ServletOutputStream out = null;

		int retry = DB_RETRY_TIMES;
		Connection conn = null;
		
		
		String receiptId = null;
		String receiptTitle = null;
		String receiptDesc = null;
		String receiptUserId = null;

		boolean isDelete = false;

		String fileName = null;

		byte [] image= null;
		String respPage = RESOURCE_FAIL_TAG;

		try {

			System.out.println("=======Receipt Servlet =======");
			// Parse the incoming HTTP request
			// Commons takes over incoming request at this point
			// Get an iterator for all the data that was sent
			List<FileItem> items = upload.parseRequest(req);
			Iterator<FileItem> iter = items.iterator();

			// Set a response content type
			resp.setContentType("text/html");

			// Setup the output stream for the return XML data
			out = resp.getOutputStream();

			// Iterate through the incoming request data
			while (iter.hasNext()) {
				// Get the current item in the iteration
				FileItem item = (FileItem) iter.next();

				// If the current item is an HTML form field
				if (item.isFormField()) {
					// If the current item is file data

					// If the current item is file data
					String fieldname = item.getFieldName();
					String fieldvalue = item.getString();

					System.out.println(fieldname + "=" + fieldvalue);


					if (fieldname.equals(RECEIPT_ID)) {
						receiptId =fieldvalue;
					} else if (fieldname.equals(RECEIPT_TITLE)) {
						receiptTitle = fieldvalue;
					} else if (fieldname.equals(RECEIPT_DESCRIPTION)) {
						receiptDesc = fieldvalue;
					} else if (fieldname.equals(RECEIPT_USER_ID)) {
						receiptUserId = fieldvalue;
					}
					else if(fieldname.equals(IS_DELETE)){

						isDelete = Boolean.valueOf(fieldvalue);

					}


				} else {

					fileName = item.getName();
					image=item.get();

				}
			}
            ReceiptResProvider receiptsResProvider = new ReceiptResProvider();
            Receipt receipt = new Receipt(receiptId, receiptTitle, receiptDesc, image, receiptUserId);
            System.out.println("WHAT IS THIS "+receipt.getId());
			while (retry > 0) {

				try {


					conn = ConnPool.getInstance().getConnection();



					if(isDelete){


						if(receiptsResProvider.deleteReceipt(receipt, conn)){
							respPage = RESOURCE_SUCCESS_TAG;
						}

					}
					else{
						if(receiptsResProvider.insertReceipt(receipt, conn)){
							System.out.println("HMM"+receipt.getId());
							respPage = RESOURCE_SUCCESS_TAG;
						}

					}

					if(image!=null && image.length>0){

						//FilesUtils.writeLocalCopy(fileName, image, false);
					}

					retry = 0;

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

			out.println(respPage);
			out.close();


		} catch (FileUploadException fue) {
			fue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}




	}

}

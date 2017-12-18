package haifa.servlet.servlet;

import haifa.servlet.database.CategoryResProvider;
import haifa.servlet.objects.Category;
import haifa.servlet.utils.FilesUtils;
import haifa.servlet.database.ConnPool;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WebFoldersManageServlet
 */


public class WebFoldersManageServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	
	public static final int DB_RETRY_TIMES=5;

	
	
	private static final String RESOURCE_FAIL_TAG = "{\"result_code\":0}";
	private static final String RESOURCE_SUCCESS_TAG = "{\"result_code\":1}";
	
	private static final String CATEGORY_ID = "c_id";
	private static final String CATEGORY_TITLE = "c_title";
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
		

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		PrintWriter pw = resp.getWriter();
		
		int retry = DB_RETRY_TIMES;
		Connection conn = null;
		
		
		
		String categoryId = null;
		String categoryName = null;
		
		boolean isDelete = false;
		
		String respPage = RESOURCE_FAIL_TAG;
		try {
			
			System.out.println("=======Folder Servlet =======");


			categoryId = req.getParameter(CATEGORY_ID);
			categoryName = req.getParameter(CATEGORY_TITLE);
			 String isDeleteParam = req.getParameter(IS_DELETE);
			 isDelete = Boolean.parseBoolean(isDeleteParam);
			
			while (retry > 0) {

				try {

				
					conn = ConnPool.getInstance().getConnection();
					
					CategoryResProvider categoryResProvider = new CategoryResProvider();
					Category category = new Category(categoryId, categoryName);
					
					if(isDelete){
						
						
						if(categoryResProvider.deleteCategory(category, conn)){
							respPage = RESOURCE_SUCCESS_TAG;
						}
						
					}
					else{
						if(categoryResProvider.insertCategory(category, conn)){
							respPage = RESOURCE_SUCCESS_TAG;
						}
						
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
			
			pw.write(respPage);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

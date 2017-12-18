package haifa.servlet.database;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import haifa.servlet.objects.Category;

public class CategoryResProvider {

	private static final String update_sql = "UPDATE category SET CategoryName=? WHERE idCategory=?;";

	private static final String select_sql = "SELECT * FROM  category WHERE id=?;";

	private static final String insert_sql = "INSERT category folders (idCategory, CategoryName) VALUES (?,?);";

	private static final String delete_sql = "DELETE FROM folders WHERE idCategory=?;";

	private static final String select_all_category_sql = "SELECT * FROM category;";

	public List<Category> getAllCategories(Connection conn) throws SQLException {

		List<Category> results = new ArrayList<Category>();

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(select_all_category_sql);

	
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString(1);
				String title = rs.getString(2);

				Category f = new Category(id, title);
				
				results.add(f);

			}
			/*
			// get items if exist
			ItemsResProvider itemResProvider = new ItemsResProvider();
			for(Folder folder:results){
				List<Item> items = itemResProvider.getAllItems(folder, conn);
				folder.setItems(items);
			}*/

		} catch (SQLException e) {
			throw e;

		} catch (Throwable e) {
			e.printStackTrace();

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return results;
	}



	public boolean insertCategory(Category obj, Connection conn) {

		boolean result = false;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement ps = null;
		PreparedStatement stt = null;

		try {

			String id = obj.getIdCategory();
			String name = obj.getCategoryName();

			stt = (PreparedStatement) conn.prepareStatement(select_sql);
			stt.setString(1, id);
			

			if (stt.execute()) {
				rs1 = stt.getResultSet();
				if (rs1.next()) {
					
					// its execute update
					ps = (PreparedStatement) conn.prepareStatement(update_sql);
					ps.setString(1, name);
					// where
					ps.setString(2, id);
					ps.execute();
					result = true;
				} else {

					// its execute insert
					ps = (PreparedStatement) conn.prepareStatement(insert_sql);
					ps.setString(1,id);
					ps.setString(2, name);
					ps.execute();
					result = true;

				}
			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			if (stt != null) {
				try {
					stt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;

	}


	public boolean deleteCategory(Category obj,
			Connection conn) throws SQLException {

		boolean result = false;
		PreparedStatement ps = null;


		try {

			if (obj != null) {
				
				String id = obj.getIdCategory();
				


				ps = (PreparedStatement) conn.prepareStatement(delete_sql);

			
				ps.setString(1, id);
				
				ps.execute();

				result = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}

		return result;
	}



}

package haifa.servlet.database;



import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import haifa.servlet.objects.Category;
import haifa.servlet.objects.Receipt;

public class ReceiptResProvider {

	private static final String update_sql = "UPDATE inforeceipt SET Title=?, Description=?, Image=?, UserID=? WHERE IdReceipt=?;";

	private static final String select_sql = "SELECT * FROM  inforeceipt WHERE IdReceipt=?;";

	private static final String select_img_sql = "SELECT image FROM  inforeceipt WHERE IdReceipt=?;";

	private static final String insert_sql = "INSERT INTO inforeceipt (IdReceipt, Title, Description, Image, UserID) VALUES (?, ?, ?, ?, ?);";

	private static final String delete_sql = "DELETE FROM inforeceipt WHERE IdReceipt=?;";

	private static final String select_all_receipts_sql = "SELECT * FROM inforeceipt;";

	public List<Receipt> getAllReceipts(Connection conn) throws SQLException {

		List<Receipt> results = new ArrayList<Receipt>();

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(select_all_receipts_sql);

	
			rs = ps.executeQuery();


			while (rs.next()) {

				String id = rs.getString(1);
				String title = rs.getString(2);
				String description = rs.getString(3);

				java.sql.Blob imageBlob = rs.getBlob(4);
				byte[] image = null;
				if (imageBlob != null) {
					image = imageBlob.getBytes(1, (int) imageBlob.length());
				}



				String userId = rs.getString(5);
				Receipt item = new Receipt(id, title, description, image, userId);

				results.add(item);


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



	public byte[] getImage(String recId, Connection conn)
			throws SQLException {

		byte[] result = null;

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(select_img_sql);

			ps.setString(1, recId);

			rs = ps.executeQuery();

			while (rs.next()) {

				java.sql.Blob imageBlob = rs.getBlob(1);
				if (imageBlob != null) {
					result = imageBlob.getBytes(1, (int) imageBlob.length());
				}
			}

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

		return result;
	}

	public boolean insertReceipt(Receipt obj, Connection conn) {

		boolean result = false;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement ps = null;
		PreparedStatement stt = null;

		try {

			String id = obj.getId();
			String title = obj.getTitle();
			String description = obj.getDescription();

			byte[] imageBytes = obj.getImage1();

			String userId = obj.getUserId();

			if (imageBytes == null) {
				imageBytes = getImage(id, conn);
			}



			stt = (PreparedStatement) conn.prepareStatement(select_sql);
			stt.setString(1, id);

			if (stt.execute()) {
				rs1 = stt.getResultSet();
				if (rs1.next()) {
					// its execute update
					ps = (PreparedStatement) conn.prepareStatement(update_sql);

					ps.setString(1, title);
					ps.setString(2, description);

					if (imageBytes != null) {
						InputStream is = new ByteArrayInputStream(imageBytes);
						ps.setBlob(3, is);

					} else {

						ps.setNull(3, Types.BLOB);
					}



					ps.setString(4, userId);

					// where
					ps.setString(5, id);

					ps.execute();

					result = true;

				} else {

					// its execute insert
					ps = (PreparedStatement) conn.prepareStatement(insert_sql);
					ps.setString(1, id);
					ps.setString(2, title);
					ps.setString(3, description);

					if (imageBytes != null) {
						InputStream is = new ByteArrayInputStream(imageBytes);
						ps.setBlob(4, is);

					} else {

						ps.setNull(4, Types.BLOB);
					}



					ps.setString(5, userId);

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



	public boolean deleteReceipt(Receipt obj, Connection conn) throws SQLException {

		boolean result = false;
		PreparedStatement ps = null;

		try {

			if (obj != null) {

				ps = (PreparedStatement) conn.prepareStatement(delete_sql);

				String id = obj.getId();

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

package haifa.servlet.objects;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public class Receipt {

	private String id;
	private String title;
	private String description;
	private byte[] image1 = null;
	private String date = null;
	private String category = null;
	private String userId;




	public Receipt() {

	}

	public Receipt(String id) {
		this.id = id;
	}

	public Receipt(String title, String description, byte[] image1) {
		this.title = title;
		this.description = description;
		this.image1 = image1;
	}


	public Receipt(String title, String description, String category) {
		this.title = title;
		this.description = description;
		this.category = category;
	}

	public Receipt(String id, String title, String description, byte[] image1, String userID) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.image1 = image1;
		this.userId=userID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getImage1() {
		return image1;
	}

	public void setImage1(byte[] image1) {
		this.image1 = image1;
	}


	public void setUserId(String userId) {
		this.userId  = userId;

	}

	public String getUserId() {
		return userId;
	}



	public JSONObject toJson() {

		JSONObject iObj = new JSONObject();
		iObj.put("id", getId());
		iObj.put("title", getTitle());
		iObj.put("description", getDescription());
		//iObj.put("img", isImageExists());
		iObj.put("userId", getUserId());

		return iObj;


	}

	private boolean isImageExists() {
		if (image1 == null || image1.length == 0) {
			return false;
		}
		return true;
	}

	public static String toJson(List<Receipt> list) {

		JSONObject jsonObj = new JSONObject();

		if (list == null) {
			return null;
		}

		if (list.size() == 0) {
			return null;
		}

		JSONArray jsonArray = new JSONArray();

		for (Receipt receipt : list) {

			if (receipt != null) {

				JSONObject itemObj = receipt.toJson();

				jsonArray.add(itemObj);
			}

		}

		jsonObj.put("receipts", jsonArray);

		return jsonObj.toString(2);
	}

}

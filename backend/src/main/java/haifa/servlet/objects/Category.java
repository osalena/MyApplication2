package haifa.servlet.objects;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public class Category {
	
	private String idCategory;
	private String CategoryName;
	

	

	public Category(String id) {
		this.idCategory = id;
	}
	

	public Category(String id, String title) {
		this.idCategory = id;
		this.CategoryName = title;
	}

	
	public String getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(String id) {
		this.idCategory = id;
	}
	

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String title) {
		this.CategoryName = title;
	}
	
	

	
	public static String toJson(List<Category> list) {

		JSONObject jsonObj = new JSONObject();

		if (list == null) {
			return null;
		}

		if (list.size() == 0) {
			return null;
		}

		JSONArray jsonArray = new JSONArray();

		for (Category f : list) {

			if (f != null) {

				JSONObject fObj = new JSONObject();

				fObj.put("id", f.getIdCategory());
				fObj.put("title", f.getCategoryName());


				jsonArray.add(fObj);
			}

		}

		jsonObj.put("category", jsonArray);

		return jsonObj.toString(2);
	}

}

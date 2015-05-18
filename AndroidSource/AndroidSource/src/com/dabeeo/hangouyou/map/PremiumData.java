package com.dabeeo.hangouyou.map;

import org.json.JSONObject;


public class PremiumData {

	public String text;
	public String imageUrl;
	public boolean isText = false;

	public void setJSONObject(JSONObject object) {
		try {
			if (object.has("image")) {
				imageUrl = object.getString("image");
				isText = false;
			} else {
				text = object.getString("text");
				isText = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.thirdyearjavamaps.classes;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jdk.nashorn.api.scripting.JSObject;

public class UtilityTools {

	private static final String ADDRESS = "address";
	private static final String LATLNG = "latlng";
	private static final String DISTANCE = "distance";
	private static final String UPDATE = "update";
	private static List<String> searchResult = null;

	@SuppressWarnings("unchecked")
	public static JSONObject SearchApartment(HttpServletRequest request,
			JSONObject json, DB db) {
		try {
			String address = request.getParameter(ADDRESS);
			String latlng = request.getParameter(LATLNG);
			String distance = request.getParameter(DISTANCE);
			String update = request.getParameter(UPDATE);

			System.out.print(ADDRESS + ":" + address + " ");
			System.out.print(LATLNG + ":" + latlng + " ");
			System.out.println(DISTANCE + ":" + distance);

			if (update != null && !update.isEmpty()) {
				String id = request.getParameter("id");
				String city = request.getParameter("city");

				String str = latlng + "," + id;
				String[] strs = str.split(",");
				float lat = new Float(strs[0]).floatValue();
				float lng = new Float(strs[1]).floatValue();
				int id_num = new Integer(strs[2]).intValue();

				db.updateApartment(id_num, lat, lng, address, city);

			} else {

				if (address != null && !address.isEmpty()) {
					searchResult = (List<String>) db.searchByAddress(address);
				} else if (latlng != null && !latlng.isEmpty()) {
					String str = latlng + "," + distance;
					String[] strs = str.split(",");
					float lat = new Float(strs[0]).floatValue();
					float lng = new Float(strs[1]).floatValue();
					float dst = new Float(strs[2]).floatValue();

					searchResult = (List<String>) db.searchByDistance(lat, lng,
							dst);

				}
			}

		} catch (Exception e) {
		} finally {
			try {
				if (searchResult != null && searchResult.size() > 0) {
					JSONArray jarr;

					jarr = ListToJSONArray(searchResult);

					json.put("result", "success");
					json.put("data", jarr);
				} else {
					json.put("result", "error");
					json.put("message", "No entries found.");
				}
			} catch (JSONException e) {
			}
		}

		return json;
	}

	private static JSONArray ListToJSONArray(List history) throws JSONException {
		JSONArray jarr = new JSONArray();
		for (Object dict : history) {
			Iterator it = ((Map) dict).entrySet().iterator();
			JSONObject jobj = new JSONObject();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				jobj.put((String) pair.getKey(), pair.getValue());
				it.remove();
			}
			jarr.put(jobj);
		}
		return jarr;
	}
}

package com.chattynotes.linkpreview;

import java.net.URL;

public class SearchUrls {

	private final static String HTTP_PROTOCOL = "http://";
	private final static String HTTPS_PROTOCOL = "https://";


	//______________________________________________________________________________________________
	//_________________________________________ CUSTOM METHODS _____________________________________
	//______________________________________________________________________________________________
	/* search for string and match 1 url only */
	public static String getSingleURL (String text) {
		String[] splitString = (text.split(" "));
		for (String urlString : splitString) {
			if(!(urlString.toUpperCase().equals(HTTP_PROTOCOL) && urlString.toUpperCase().equals(HTTPS_PROTOCOL)))
				try {
					URL url = new URL(urlString);
					return url.toString();
				} catch (Exception ignored) {
				}
		}
		return null;
	}

}

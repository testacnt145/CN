package com.chattynotes.linkpreview;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextCrawler {

	private final static String HTTP_PROTOCOL = "http://";
	private final static String HTTPS_PROTOCOL = "https://";

	/** Gets content from a html tag */
	private static String getTagContent(String tag, String content) {
		String pattern = "<" + tag + "(.*?)>(.*?)</" + tag + ">";
		String result = "", currentMatch;
		List<String> matches = Regex.pregMatchAll(content, pattern, 2);
		int matchesSize = matches.size();
		for (int i = 0; i < matchesSize; i++) {
			currentMatch = stripTags(matches.get(i));
			if (currentMatch.length() >= 120) {
				result = extendedTrim(currentMatch);
				break;
			}
		}
		if (result.equals("")) {
			String matchFinal = Regex.pregMatch(content, pattern, 2);
			result = extendedTrim(matchFinal);
		}
		result = result.replaceAll("&nbsp;", "");
		return htmlDecode(result);
	}

	/** Gets images from the html code */
	private static List<String> getImages(Document doc) {
		List<String> matches = new ArrayList<>();
		Elements media = doc.select("[src]");
		for (Element srcElement : media) {
			if (srcElement.tagName().equals("img")) {
				matches.add(srcElement.attr("abs:src"));
				break; //add only 1 image
			}
		}
		return matches;
	}

	/** Transforms from html to normal string */
	private static String htmlDecode(String content) {
		return Jsoup.parse(content).text();
	}

	/** Crawls the code looking for relevant information */
	private static String crawlCode(String content) {
		String result;
		String resultSpan;
		String resultParagraph;
		String resultDiv;

		resultSpan = getTagContent("span", content);
		resultParagraph = getTagContent("p", content);
		resultDiv = getTagContent("div", content);

		if (resultParagraph.length() > resultSpan.length()
				&& resultParagraph.length() >= resultDiv.length())
			result = resultParagraph;
		else if (resultParagraph.length() > resultSpan.length()
				&& resultParagraph.length() < resultDiv.length())
			result = resultDiv;
		else
			result = resultParagraph;

		return htmlDecode(result);
	}

	/** Returns the canonical url */
	private static String canonicalPage(String url) {
		String canonical = "";
		if (url.startsWith(HTTP_PROTOCOL)) {
			url = url.substring(HTTP_PROTOCOL.length());
		} else if (url.startsWith(HTTPS_PROTOCOL)) {
			url = url.substring(HTTPS_PROTOCOL.length());
		}
		int urlLength = url.length();
		for (int i = 0; i < urlLength; i++) {
			if (url.charAt(i) != '/')
				canonical += url.charAt(i);
			else
				break;
		}
		return canonical;
	}

	/** Strips the tags from an element */
	private static String stripTags(String content) {
		return Jsoup.parse(content).text();
	}

	/** Verifies if the url is an image */
	private boolean isImage(String url) {
		return url.matches(Regex.IMAGE_PATTERN);
	}

	/**
	 * Returns meta tags from html code
	 */
	private static HashMap<String, String> getMetaTags(String content) {

		HashMap<String, String> metaTags = new HashMap<>();
		metaTags.put("url", "");
		metaTags.put("title", "");
		metaTags.put("description", "");
		metaTags.put("image", "");

		List<String> matches = Regex.pregMatchAll(content,
				Regex.METATAG_PATTERN, 1);

		for (String match : matches) {
			final String lowerCase = match.toLowerCase();
			if (lowerCase.contains("property=\"og:url\"")
					|| lowerCase.contains("property='og:url'")
					|| lowerCase.contains("name=\"url\"")
					|| lowerCase.contains("name='url'"))
				updateMetaTag(metaTags, "url", separeMetaTagsContent(match));
			else if (lowerCase.contains("property=\"og:title\"")
					|| lowerCase.contains("property='og:title'")
					|| lowerCase.contains("name=\"title\"")
					|| lowerCase.contains("name='title'"))
				updateMetaTag(metaTags, "title", separeMetaTagsContent(match));
			else if (lowerCase
					.contains("property=\"og:description\"")
					|| lowerCase
					.contains("property='og:description'")
					|| lowerCase.contains("name=\"description\"")
					|| lowerCase.contains("name='description'"))
				updateMetaTag(metaTags, "description", separeMetaTagsContent(match));
			else if (lowerCase.contains("property=\"og:image\"")
					|| lowerCase.contains("property='og:image'")
					|| lowerCase.contains("name=\"image\"")
					|| lowerCase.contains("name='image'"))
				updateMetaTag(metaTags, "image", separeMetaTagsContent(match));
		}
		return metaTags;
	}

	private static void updateMetaTag(HashMap<String, String> metaTags, String url, String value) {
		if (value != null && (value.length() > 0)) {
			metaTags.put(url, value);
		}
	}

	/** Gets content from metatag */
	private static String separeMetaTagsContent(String content) {
		String result = Regex.pregMatch(content, Regex.METATAG_CONTENT_PATTERN, 1);
		return htmlDecode(result);
	}

	/** Removes extra spaces and trim the string */
	static String extendedTrim(String content) {
		return content.replaceAll("\\s+", " ").replace("\n", " ")
				.replace("\r", " ").trim();
	}

	//______________________________________________________________________________________________
	public static SourceContent parseContent(String _html, String _url) {
		//_html = extendedTrim(_html); //[bug_: content.replaceAll was causing crash]
		try {
			SourceContent sourceContent = new SourceContent();
			sourceContent.setFinalUrl(extendedTrim(_url));
			//https://jsoup.org/cookbook/input/parse-document-from-string
			//[bug_: image not parsing]
			//Document doc = Jsoup.parse(_html);
			//[fixed_: by passing baseUri]
			Document doc = Jsoup.parse(_html, _url);
			HashMap<String, String> metaTags = getMetaTags(_html);
			sourceContent.setMetaTags(metaTags);
			sourceContent.setTitle(metaTags.get("title"));
			sourceContent.setDescription(metaTags.get("description"));
			if (sourceContent.getTitle().equals("")) {
				String matchTitle = Regex.pregMatch(sourceContent.getHtmlCode(), Regex.TITLE_PATTERN, 2);
				if (!matchTitle.equals(""))
					sourceContent.setTitle(htmlDecode(matchTitle));
			}
			if (sourceContent.getDescription().equals(""))
				sourceContent.setDescription(crawlCode(sourceContent.getHtmlCode()));
			sourceContent.setDescription(sourceContent.getDescription().replaceAll(Regex.SCRIPT_PATTERN, ""));

			//if (imageQuantity != NONE) {
				if (!metaTags.get("image").equals(""))
					sourceContent.getImages().add(metaTags.get("image"));
				else
					sourceContent.setImages(getImages(doc));
			//	}
			//}
			sourceContent.setSuccess(true);
			String[] finalLinkSet = sourceContent.getFinalUrl().split("&");
			sourceContent.setUrl(finalLinkSet[0]);
			sourceContent.setCannonicalUrl(canonicalPage(sourceContent.getFinalUrl()));
			sourceContent.setDescription(stripTags(sourceContent.getDescription()));
			return sourceContent;
		} catch (Exception ignored) {
			return null;
		}
	}
}

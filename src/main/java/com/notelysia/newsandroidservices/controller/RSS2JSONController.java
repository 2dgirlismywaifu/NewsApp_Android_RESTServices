package com.notelysia.newsandroidservices.controller;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@RestController
public class RSS2JSONController {
    @RequestMapping(value = "/rss/convertIntoJson", params = {"url"},
                method = RequestMethod.GET,
                produces = "application/json")
        public String getRssFeedAsJson(@RequestParam(value = "url") String url) throws IOException {
            InputStream xml = getInputStreamForURLData(url);
            byte[] byteArray = IOUtils.toByteArray(xml);
            String xmlString = new String(byteArray);
            JSONObject xmlToJsonObject = XML.toJSONObject(xmlString);
            //JSON Path
            xmlToJsonObject = xmlToJsonObject
                    .getJSONObject("rss")
                    .getJSONObject("channel");
            String jsonString = xmlToJsonObject.toString();
            byte[] jsonStringAsByteArray = jsonString.getBytes(StandardCharsets.UTF_8);
                return new String(jsonStringAsByteArray, StandardCharsets.UTF_8);
            }
        public static InputStream getInputStreamForURLData(String Url) {
            URL url;
            HttpURLConnection httpConnection;
            InputStream content = null;
            try {
                url = new URL(Url);
                System.out.println("URL::" + Url);
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                httpConnection = (HttpURLConnection) conn;
                content = httpConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }
}

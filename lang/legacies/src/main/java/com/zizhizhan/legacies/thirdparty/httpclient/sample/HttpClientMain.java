package com.zizhizhan.legacies.thirdparty.httpclient.sample;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpClientMain {

	public static void main(String[] args) {
		HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());

		// GetMethod method = new
		// GetMethod("http://127.0.0.1/httpclient/redirect.php");
		PostMethod method = new PostMethod("http://127.0.0.1/httpclient/redirect.php");

		try {
			int statusCode = client.executeMethod(method);
			if (statusCode == 200 || statusCode == 500) {
				System.out.println(method.getStatusLine());
				Header[] headers = method.getRequestHeaders();
				System.out.println("\nRequest Header: ");
				for (Header h : headers) {
					System.out.println(h.getName() + ": " + h.getValue());
				}
				headers = method.getResponseHeaders();
				System.out.println("\nResponse Header: ");
				for (Header h : headers) {
					System.out.println(h.getName() + ": " + h.getValue());
				}
				headers = method.getResponseFooters();
				System.out.println("\nResponse Footer: ");
				for (Header h : headers) {
					System.out.println(h.getName() + ": " + h.getValue());
				}

				System.out.println("\nResponse Body: ");
				String resp = new String(method.getResponseBody(), "utf-8");
				System.out.println(resp);
				System.out.println();

			} else {
				System.out.println("Error: " + method.getStatusLine());
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}

	}

}

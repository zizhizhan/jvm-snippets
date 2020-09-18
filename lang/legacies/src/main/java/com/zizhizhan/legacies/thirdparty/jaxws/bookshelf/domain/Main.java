package com.zizhizhan.legacies.thirdparty.jaxws.bookshelf.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zizhizhan.compress.lzma.Lzma;

public class Main {
	
	private static final String UTF8 = "UTF-8";

	public static void main(String[] args) throws IOException {
		BookShelf bookShelf = new BookShelf("Y:/ebooks/Bookshelf");
		List<Document> books = bookShelf.search();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File jsonfile = new File(".books/json_final.lzma");
		if (!jsonfile.exists()) {
			Lzma.encode(gson.toJson(books).getBytes(UTF8), jsonfile);
		} else {
			Type type = new TypeToken<List<Document>>() {}.getType();
			
			byte[] result = Lzma.decode(jsonfile);		
			List<Document> list = gson.fromJson(new String(result, UTF8), type);
			for(Document doc : list){
				System.out.println(doc);
			}
		}
	}

	public static void normal(String[] args) throws IOException {
		BookShelf bookShelf = new BookShelf("Y:/ebooks/Bookshelf");
		List<Document> books = bookShelf.search();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		File jsonfile = new File(".books/json.data");
		if (!jsonfile.exists()) {
			Writer out = new OutputStreamWriter(new FileOutputStream(jsonfile), "utf-8");
			out.write(gson.toJson(books));
			out.close();
		} else {
			Type type = new TypeToken<List<Document>>() {
			}.getType();
			List<Document> list = gson.fromJson(new InputStreamReader(new FileInputStream(jsonfile), "utf-8"), type);
			System.out.println(list);
		}
	}

}

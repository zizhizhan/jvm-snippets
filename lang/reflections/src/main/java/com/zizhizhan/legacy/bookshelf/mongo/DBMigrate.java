package com.zizhizhan.legacy.bookshelf.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/*
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class DBMigrate {

	public static void file2db() throws UnknownHostException, MongoException {
		List<Document> books = AllBooks.get();
		
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("bookshelf");
		DBCollection collection = db.getCollection("books");
		
		List<DBObject> objects = new ArrayList<DBObject>();

		for(Document doc : books){
			objects.add(BasicDBObjectBuilder.start()
				.add("name", doc.getName())
				.add("authors", doc.getAuthors())
				.add("translators", doc.getTranslators())	
				.add("tags", doc.getTags())
				.add("isbn", doc.getIsbn())
				.add("version", doc.getVersion())	
				.add("description", doc.getDescription())	
				.add("publishers", doc.getPublishers())
				.add("rating", doc.getStarRating())
				.add("type", doc.getType())
				.add("path", doc.getPath())
				.add("size", doc.getSize())
				.add("lastModified", doc.getLastModified())
				.add("createdTime", doc.getCreatedTime())
				.add("accessTime", doc.getAccessTime())	
				.get());
		}
		
		collection.insert(objects);
	
		

	}

	public static void testdb() {
		try {
			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("bookshelf");
			DBCollection collection = db.getCollection("books");
			long count = collection.getCount();
			System.out.format("count: %d\n", count);

			DBCursor cursor = collection.find();
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnknownHostException, MongoException {
		testdb();
	}

}

*/
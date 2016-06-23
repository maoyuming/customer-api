package com.duantuke.api.util.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lz.mongo.MongoDelegate;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

/**
 * 业务日志代理.
 * <p>
 * 用于业务日志的MongoDB持久化.
 *
 * @author zhaoshb
 * @since 0.0.1
 */
@Component
public class AccessLogDelegate {

	private static final String DB_NAME = "duantuke";

	private static final String COL_NAME = "access_log";

	@Autowired
	private MongoDelegate mongoDelegate = null;

	public void saveAccessLog(AccessLog accessLog) {
		this.getAccessLogCollection().insertOne(accessLog.toDocument());
	}

	public void batchSaveAccessLog(List<AccessLog> accessLogList) {
		this.getAccessLogCollection().insertMany(this.toDocList(accessLogList));
	}

	/**
	 * 查询业务日志.
	 *
	 * @param filter
	 *            过滤器，编写参照{code Filters}，字段名称参照{code AccessLog}
	 * @param sort
	 *            排序条件<br>
	 *            排序写法:new Document("borough", 1).append("address.zipcode", 1)
	 *            升降序说明:1 for ascending and -1 for descending.
	 * @return 业务日志列表.
	 *
	 * @see com.mongodb.client.model.Filters
	 */
	public List<AccessLog> find(Bson filter, Bson sort) {
		return this.find(filter, sort, -1, -1);
	}

	/**
	 * 查询业务日志.
	 *
	 * @param filter
	 *            过滤器，编写参照{code Filters}，字段名称参照{code AccessLog}
	 * @param sort
	 *            排序条件<br>
	 *            排序写法:new Document("borough", 1).append("address.zipcode", 1)
	 *            升降序说明:1 for ascending and -1 for descending.
	 * @param skip
	 *            偏移量(-1不设置).
	 * @param limits
	 *            返回数量(-1不设置).
	 * @return 业务日志列表.
	 *
	 * @see com.mongodb.client.model.Filters
	 */
	public List<AccessLog> find(Bson filter, Bson sort, int skip, int limit) {
		FindIterable<Document> docIter = this.getAccessLogCollection().find(filter);
		if (sort != null) {
			docIter.sort(sort);
		}
		if (skip != -1) {
			docIter.skip(skip);
		}
		if (limit != -1) {
			docIter.limit(limit);
		}
		return this.transfer(docIter);
	}

	/**
	 * 删除业务日志.
	 *
	 * @param filter
	 *            过滤器.
	 *
	 * @see com.mongodb.client.model.Filters
	 */
	public void delete(Bson filter) {
		this.getAccessLogCollection().deleteMany(filter);
	}

	private List<AccessLog> transfer(FindIterable<Document> docIter) {
		List<AccessLog> AccessLogList = new ArrayList<AccessLog>();
		docIter.forEach(new BlockAction(AccessLogList));

		return AccessLogList;
	}

	private List<Document> toDocList(List<AccessLog> AccessLogList) {
		List<Document> docList = new ArrayList<Document>();
		AccessLogList.forEach(AccessLog -> docList.add(AccessLog.toDocument()));

		return docList;
	}

	private MongoCollection<Document> getAccessLogCollection() {
		return this.getMongoClient().getDatabase(AccessLogDelegate.DB_NAME).getCollection(AccessLogDelegate.COL_NAME);
	}

	private MongoClient getMongoClient() {
		return this.getMongoDelegate().getMongoClient();
	}

	private MongoDelegate getMongoDelegate() {
		return this.mongoDelegate;
	}

	private class BlockAction implements Block<Document> {

		private List<AccessLog> accessLogList = null;

		public BlockAction(List<AccessLog> accessLogList) {
			super();
			this.accessLogList = accessLogList;
		}

		@Override
		public void apply(Document t) {
			this.getAccessLogList().add(AccessLog.toAccessLog(t));
		}

		private List<AccessLog> getAccessLogList() {
			return this.accessLogList;
		}

	}

}

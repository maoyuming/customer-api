package com.fangbaba.api.util.pushlog;
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
 * @author he
 * 推送信息mongo存储
 */
@Component
public class PushLogUtil {

	private static final String DB_NAME = "lezhu";

	private static final String COL_NAME = "push_log";

	@Autowired
	private MongoDelegate mongoDelegate = null;

	public void savePushLog(PushLog pushLog) {
		this.getPushLogCollection().insertOne(pushLog.toDocument());
	}

	/*public void batchSaveBigLog(List<PushLog> pushLogList) {
		this.getPushLogCollection().insertMany(this.toDocList(pushLogList));
	}*/

	/**
	 * 查询业务日志.
	 *
	 * @param filter
	 *            过滤器，编写参照{code Filters}，字段名称参照{code PushLog}
	 * @param sort
	 *            排序条件<br>
	 *            排序写法:new Document("borough", 1).append("address.zipcode", 1)
	 *            升降序说明:1 for ascending and -1 for descending.
	 * @return 业务日志列表.
	 *
	 * @see com.mongodb.client.model.Filters
	 */
	public List<PushLog> find(Bson filter, Bson sort) {
		return this.find(filter, sort, -1, -1);
	}

	/**
	 * 查询业务日志.
	 *
	 * @param filter
	 *            过滤器，编写参照{code Filters}，字段名称参照{code PushLog}
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
	public List<PushLog> find(Bson filter, Bson sort, int skip, int limit) {
		FindIterable<Document> docIter = this.getPushLogCollection().find(filter);
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
		this.getPushLogCollection().deleteMany(filter);
	}

	private List<PushLog> transfer(FindIterable<Document> docIter) {
		List<PushLog> pushLogList = new ArrayList<PushLog>();
		docIter.forEach(new BlockAction(pushLogList));

		return pushLogList;
	}

	/*private List<Document> toDocList(List<PushLog> pushLogList) {
		List<Document> docList = new ArrayList<Document>();
		pushLogList.forEach(pushLog -> docList.add(pushLog.toDocument()));

		return docList;
	}*/

	private MongoCollection<Document> getPushLogCollection() {
		return this.getMongoClient().getDatabase(DB_NAME).getCollection(COL_NAME);
	}

	private MongoClient getMongoClient() {
		return this.getMongoDelegate().getMongoClient();
	}

	private MongoDelegate getMongoDelegate() {
		return this.mongoDelegate;
	}

	private class BlockAction implements Block<Document> {

		private List<PushLog> pushLogList = null;

		public BlockAction(List<PushLog> pushLogList) {
			super();
			this.pushLogList = pushLogList;
		}

		@Override
		public void apply(Document t) {
			this.getPushLogList().add(PushLog.toPushLog(t));
		}

		private List<PushLog> getPushLogList() {
			return this.pushLogList;
		}

	}

}


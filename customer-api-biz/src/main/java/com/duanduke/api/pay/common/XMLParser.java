package com.duanduke.api.pay.common;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.security.utils.XMLUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * User: rizenguo Date: 2014/11/1 Time: 14:06
 */
public class XMLParser {

	private static Logger log = LoggerFactory.getLogger(Signature.class);

	public static Map<String, Object> getMapFromXML(String xmlString)
			throws JDOMException, IOException {

		// 这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
		SAXBuilder builder = new SAXBuilder(false);
		Map<String, Object> map = new HashMap<String, Object>();
		StringReader read = new StringReader(xmlString);
		Document doc = builder.build(read);
		Element el = doc.getRootElement();
		List<Element> list = el.getChildren();
		for (Element element : list) {
			map.put(element.getName(), element.getValue());
		}
		return map;
	}

	/**
	 * xmlResult 转换为退款所需要的参数
	 * 
	 * @param refundReturnStr
	 *            退款请求返回的数据
	 * @return RefundResData
	 */
	public static RefundResData getRefundResData(String xmlResult)
			throws IOException, SAXException, ParserConfigurationException {
		RefundResData rfrd = new RefundResData();
		Element root = null;
		try {
			root = getElementRoot(xmlResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (root != null) {
			rfrd.setReturn_code(root.getChildText("return_code"));// return_code
			rfrd.setReturn_msg(root.getChildText("return_msg"));// return_msg
			// 在上面 return_code 为 SUCCESS时才有下面信息
			rfrd.setAppid(root.getChildText("appid"));// appid
			rfrd.setMch_id(root.getChildText("mch_id"));// mch_id
			rfrd.setNonce_str(root.getChildText("nonce_str"));// nonce_str
			rfrd.setSign(root.getChildText("sign"));// sign
			rfrd.setResult_code(root.getChildText("result_code"));// result_code
			rfrd.setTransaction_id(root.getChildText("transaction_id"));// transaction_id
			rfrd.setOut_trade_no(root.getChildText("out_trade_no"));// out_trade_no
			rfrd.setRefund_id(root.getChildText("refund_id"));// refund_id
			rfrd.setRefund_fee(root.getChildText("refund_fee"));// refund_fee
			rfrd.setCoupon_refund_fee(root.getChildText("coupon_refund_fee"));// coupon_refund_fee
			// 有错误才返回下面信息
			rfrd.setErr_code(root.getChildText("err_code"));// err_code
			rfrd.setErr_code_des(root.getChildText("err_code_des"));// err_code_des
		}
		return rfrd;
	}

	/**
	 * 从refundReturnStr里面解析出退款订单数据
	 * 
	 * @param refundReturnStr
	 *            退款请求返回的数据
	 * @return RefundResData
	 */
	public static ScanPayQueryResData getQueryResData(String xmlResult)
			throws IOException, SAXException, ParserConfigurationException {
		ScanPayQueryResData spqr = new ScanPayQueryResData();
		Element root = null;
		try {
			root = getElementRoot(xmlResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (root != null) {
			spqr.setReturn_code(root.getChildText("return_code"));
			spqr.setReturn_msg(root.getChildText("return_msg"));
			// 在上面 return_code 为 SUCCESS时才有下面信息
			spqr.setAppid(root.getChildText("appid"));
			spqr.setMch_id(root.getChildText("mch_id")); // mch_id
			spqr.setNonce_str(root.getChildText("nonce_str")); // mch_id
			spqr.setSign(root.getChildText("sign")); // mch_id
			spqr.setResult_code(root.getChildText("result_code")); // mch_id
			spqr.setOpenid(root.getChildText("openid")); // mch_id
			spqr.setIs_subscribe(root.getChildText("is_subscribe"));// result_code
			spqr.setTotal_fee(root.getChildText("total_fee"));// total_fee
			spqr.setTransaction_id(root.getChildText("transaction_id"));// transaction_id
			spqr.setOut_trade_no(root.getChildText("out_trade_no"));// out_trade_no
			spqr.setTrade_state(root.getChildText("trade_state"));// trade_state
			// 有错误才返回下面信息
			spqr.setErr_code(root.getChildText("err_code")); // err_code
			spqr.setErr_code_des(root.getChildText("err_code_des"));// err_code_des
		}
		return spqr;
	}

	/**
	 * 退款查询结果
	 */
	public static RefundQueryResData getRefundQueryResData(String xmlResult)
			throws IOException, SAXException, ParserConfigurationException {
		RefundQueryResData res = new RefundQueryResData();
		Element root = null;
		try {
			root = getElementRoot(xmlResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (root != null) {
			res.setResult_code(root.getChildText("result_code"));
			res.setReturn_code(root.getChildText("return_code"));
			res.setReturn_msg(root.getChildText("return_msg"));
			// 在上面 return_code 为 SUCCESS时才有下面信息
			res.setOut_refund_no(root.getChildText("out_refund_no_0"));
			res.setOut_trade_no(root.getChildText("out_trade_no"));
			String rc = root.getChildText("refund_count");
			if (rc != null) {
				res.setRefund_count(Integer.valueOf(rc).intValue());
			}
			res.setRefund_fee(root.getChildText("refund_fee"));
			res.setRefund_id(root.getChildText("refund_id_0"));
			res.setAppid(root.getChildText("appid"));
			res.setMch_id(root.getChildText("mch_id")); // mch_id
			res.setNonce_str(root.getChildText("nonce_str")); // mch_id
			res.setSign(root.getChildText("sign")); // mch_id
			res.setResult_code(root.getChildText("result_code")); // mch_id
			res.setTransaction_id(root.getChildText("transaction_id"));// transaction_id
			res.setOut_trade_no(root.getChildText("out_trade_no"));// out_trade_no
			// 有错误才返回下面信息
			res.setErr_code(root.getChildText("err_code")); // err_code
			res.setErr_code_des(root.getChildText("err_code_des"));// err_code_des
		}
		return res;
	}

	public static Element getElementRoot(String xmlResult) throws Exception {
		Element root = null;
		if (xmlResult != null) {
			try {
				Document doc = StringtoXML(xmlResult);
				root = doc.getRootElement();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return root;
	}

	
	public static Document StringtoXML(String xmlstring) throws JDOMException,
			IOException {
		SAXBuilder builder = new SAXBuilder(false);
		StringReader read = new StringReader(xmlstring);
		Document doc = builder.build(read);
		return doc;
	}
}

/**
 * 
 */
package com.iwork.plugs.sms.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author lee 短信发送结果集
 */
public class MsgResults {
	private HashMap<String, String> ok;
	private HashMap<String, String> err;
	private HashMap<String, Double> fee;
	int msgCount; // 折合短信数

	public MsgResults() {
		ok = new HashMap<String, String>();
		err = new HashMap<String, String>();
		fee = new HashMap<String, Double>();
		msgCount = 0;
	}

	public void put(String num, String result) {
		if (result.length() == 0)
			ok.put(num, result);
		else
			err.put(num, result);
	}

	public void addFee(String num, double price) {
		fee.put(num, price);
	}

	public void addMsgCount(int count) {
		msgCount += count;
	}

	public int getCount() {
		return (ok.size() + err.size());
	}

	public int getErrCount() {
		return err.size();
	}

	public int getOkCount() {
		return ok.size();
	}

	public int getMsgCount() {
		return msgCount;
	}

	public String getErrStr() {
		String ret = "";
		Iterator iter = err.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String num = String.valueOf(entry.getKey());
			String result = String.valueOf(entry.getValue());
			ret += num + ": " + result + ";\n";
		}
		return ret;
	}

	public double getFee() {
		double ret = 0;
		Iterator iter = fee.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String num = String.valueOf(entry.getKey());
			double price = (Double) (entry.getValue());
			ret += price;
		}
		return ret;
	}

	public void clear() {
		ok.clear();
		err.clear();
	}
}

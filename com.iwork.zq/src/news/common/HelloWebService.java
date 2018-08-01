package news.common;

import java.lang.reflect.Proxy;
import java.util.List;

import news.server.WebServiceClient;
import news.server.WebServicePortType;
import news.service.Comnotice;
import news.service.Cxdd;
import news.service.Gpgsxq;
import news.service.Qygg;
import news.service.Qyjbxx;
import news.service.Qysjk;
import news.service.Qytop;
import news.service.Ssxx;
import news.service.Zjjg;

import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.client.XFireProxy;

public class HelloWebService {
	public static WebServiceClient wclient;
	public static WebServicePortType service;
	public static XFireProxy proxy;
	public static Client client;
	static {
		wclient = new WebServiceClient();
		service = wclient.getWebServiceHttpPort();
		proxy = (XFireProxy) Proxy.getInvocationHandler(service);
		client = proxy.getClient();
		client.addOutHandler(new WebserviceClientHandler("admin", "123"));
	}

	public static List<Ssxx> getSsxx(int pageNo,int pageSize) {
		List<Ssxx> list = service.getList(pageNo, pageSize).getSsxx();
		return list;
	}

	public static Ssxx getSsxxById(int infoid) {
		Ssxx ssxx = service.getSsxxById(infoid);
		return ssxx;
	}

	public static List<Cxdd> getCxdd(String string) {

		List<Cxdd> list = service.getListCxdd1(string).getCxdd();
		return list;
	}
	public static List<Cxdd> getCxdd() {

		List<Cxdd> list = service.getListCxdd().getCxdd();
		return list;
	}
	public static Cxdd getCxddById(int infoid) {

		Cxdd cxdd = service.getCxddById(infoid);
		return cxdd;
	}
	
	
	public static int getCxddCount(String string){
		int total=service.cxddCount1(string);
		return total;
	}
	
	public static int getCxddCount(){
		int total=service.cxddCount();
		return total;
	}
	/**实时信息总数
	 * @return
	 */
	public static int getSsxxCount() {

		int total = service.ssxxCount();
		return total;
	}
	
	public static int getZjjgCount() {

		int total = service.zjjgCount();
		return total;
	}
	
	/**企业数据库总数
	 * @return
	 */
	public static int getQysjkCount() {

		int total = service.qysjkCount();
		return total;
	}
	/**企业数据库
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public static List<Qysjk> getQysjkList(int pageNo,int pageSize) {

		List<Qysjk> list = service.getQysjk1(pageNo, pageSize).getQysjk();
		return list;
	}
	/**企业基本信息
	 * @param qyid
	 * @return
	 */
	public static List<Qyjbxx> getQyjbxxList(int qyid) {

		List<Qyjbxx> list = service.getQyjbxx(qyid).getQyjbxx();
		return list;
	}
	/**企业高管
	 * @param qyid
	 * @return
	 */
	public static List<Qygg> getQyggList(int qyid) {

		List<Qygg> list = service.getQygg(qyid).getQygg();
		return list;
	}
	/**企业前十股东
	 * @param qyid
	 * @return
	 */
	public static List<Qytop> getQytopList(int qyid) {

		List<Qytop> list = service.getQytop(qyid).getQytop();
		return list;
	}

	public static Qysjk getQysjk(int infoid) {
		Qysjk qysjk = service.getQysjk(infoid);
		return qysjk;
	}

	public static Zjjg getZjjgById(int infoid) {
			Zjjg zjjg = service.getZjjgById(infoid);
		return zjjg;
	}

	public static List getListZjjg(int pageNo, int pageSize) {
		List<Zjjg> list = service.getListZjjg(pageNo, pageSize).getZjjg();
		return list;
	}
	
	public static List<Cxdd> getCxdd(int pageNo,int pageSize,String string) {

		List<Cxdd> list = service.getListCxdd2(pageNo, pageSize, string).getCxdd();
		return list;
	}
	public static List<Gpgsxq> getGpgsxq(String zqServer){
		List<Gpgsxq> list = service.getListGpgsxq(zqServer).getGpgsxq();
		return list;
	}
	
	public static List<Comnotice> getComnotice(String zqServer){
		List<Comnotice> list = service.getListComnotice(zqServer).getComnotice();
		return list;
	}
}

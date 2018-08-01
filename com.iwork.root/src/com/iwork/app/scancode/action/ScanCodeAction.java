package com.iwork.app.scancode.action;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code93Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.WidthCodedPainter;
import com.iwork.app.scancode.service.ScanCodeService;
import com.iwork.commons.util.ObjectUtil;
import com.iwork.connection.ConnectionAPI;
import com.iwork.core.util.ResponseUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.swetake.util.Qrcode;
import org.apache.log4j.Logger;
public class ScanCodeAction extends ActionSupport{
	private static Logger logger = Logger.getLogger(ScanCodeAction.class);

	private ScanCodeService scanCodeService;
	private String type;	
	private String codeType;	
	private String codestr;
	private String n_left;
	protected ByteArrayInputStream inputStream;
	/**
	 * 扫码
	 */
	public void doScanCode(){
		String str = scanCodeService.doScanCode(type, codestr);
		ResponseUtil.write(str);
	} 
	
	/**
	 * 加载条形码/二维码
	 */
	public String showQrCode(){
		if(type!=null){
			HttpServletRequest request = ServletActionContext.getRequest();
			Map pageParams = request.getParameterMap();
			String content = scanCodeService.showQrCode(type,pageParams);
			      try {
			          Qrcode qrcodeHandler = new Qrcode(); 
			          qrcodeHandler.setQrcodeErrorCorrect('M'); 
			          qrcodeHandler.setQrcodeEncodeMode('B'); 
			          qrcodeHandler.setQrcodeVersion(7); 
			          byte[] contentBytes = content.getBytes("gb2312"); 
			          BufferedImage bufImg = new BufferedImage(140, 140, 
			                  BufferedImage.TYPE_INT_RGB);
			          Graphics2D gs = bufImg.createGraphics();
			          gs.setBackground(Color.WHITE); 
			          gs.clearRect(0, 0, 140, 140); 

			          // 设定图像颜色> BLACK 
			          gs.setColor(Color.BLACK); 

			          // 设置偏移量 不设置可能导致解析出错 
			          int pixoff = 2; 
			          // 输出内容> 二维码 
			          if (contentBytes.length > 0 && contentBytes.length < 120) { 
			              boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes); 
			              for (int i = 0; i < codeOut.length; i++) { 
			                  for (int j = 0; j < codeOut.length; j++) { 
			                      if (codeOut[j][i]) { 
			                          gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3); 
			                      } 
			                  } 
			              } 
			          }
			          gs.dispose(); 
			          bufImg.flush(); 
			          ByteArrayOutputStream output = new ByteArrayOutputStream();  
			          ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);  
			          // 生成二维码QRCode图片 
			          ImageIO.write(bufImg, "JPEG", imageOut);
			          imageOut.close();  
			          ByteArrayInputStream input = new ByteArrayInputStream(output  
			                  .toByteArray());  
			          this.setInputStream(input);  
			      } catch (Exception e) {
			          logger.error(e,e); 
			      } 
			   }
		return SUCCESS;  
	}
	
	/**
	 * 加载条形码/二维码
	 */
	public String showBarCode(){
		if(type!=null){
			try{
			HttpServletRequest request = ServletActionContext.getRequest();
			Map pageParams = request.getParameterMap();
			String content = "150811001286";
			JBarcode jBarcode = new JBarcode(Code93Encoder.getInstance(), WidthCodedPainter.getInstance(), BaseLineTextPainter.getInstance());
			jBarcode .setShowCheckDigit(false);
			jBarcode .setCheckDigit(true);
			jBarcode .setShowText(false);
			jBarcode .setBarHeight(10);
			BufferedImage bufferedImage = jBarcode .createBarcode(content);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageOutputStream imageOut = ImageIO.createImageOutputStream(output); 
			ImageIO.write(bufferedImage , "JPEG", imageOut);
			imageOut.close();
			inputStream = new ByteArrayInputStream(output.toByteArray()); 
		} catch (Exception e) {
	          logger.error(e,e); 
	      }
		}
		return SUCCESS;
	}
	
	public void showTestFormInfo(){
		if(codestr!=null){
			String rate = "";
			//调用SAP SDK
			String uuid = "c8aa4fc844cd410cb3ab005d2b4ed7d4";
			HashMap hash = new HashMap();
			hash.put("EXCURR", codestr);
			List<HashMap> params = ConnectionAPI.getInstance().getList(uuid, hash);
			if(params!=null&&params.size()>0){
				HashMap map = params.get(0);
				if(map!=null){
					rate = ObjectUtil.getString(map.get("RATE"));
					
				}
			}
//			if(codestr.equals("CNY")){
//				rate = "1";
//			}else if(codestr.equals("COP")){
//				rate = "3";
//			}else if(codestr.equals("USD")){
//				rate = "11";
//			}
			ResponseUtil.write(rate);
			
		}
		
	}
	
	
	public void showTreeJson(){
		String xml = "";
		if(n_left==null || "".equals(n_left)){
		      xml = "<?xml version='1.0' encoding='utf-8'?>" +
		          "  <rows>" +
		          "    <page>1</page>" +
		          "    <total>1</total>" +
		          "    <records>1</records>" +
		          "    <row>" +
		          "      <cell>1</cell>" +
		          "      <cell>现金</cell>" +
		          "      <cell>100</cell>" +
		          "      <cell>400.00</cell>" +
		          "      <cell>250.00</cell>" +
		          "      <cell>150.00</cell>" +
		          "      <cell>0</cell>" +
		          "      <cell>1</cell>" +
		          "      <cell>8</cell>" +
		          "      <cell>false</cell>" +
		          "      <cell>false</cell>" +
		          "    </row>" +
		          "    <row>" +
		          "      <cell>5</cell>" +
		          "      <cell>银行</cell>" +
		          "      <cell>200</cell>" +
		          "      <cell>1500.00</cell>" +
		          "      <cell>1000.00</cell>" +
		          "      <cell>500.00</cell>" +
		          "      <cell>0</cell>" +
		          "      <cell>9</cell>" +
		          "      <cell>14</cell>" +
		          "      <cell>false</cell>" +
		          "      <cell>false</cell>" +
		          "    </row>" +
		          "    <row>" +
		          "      <cell>8</cell>" +
		          "      <cell>固定资产</cell>" +
		          "      <cell>300</cell>" +
		          "      <cell>0.00</cell>" +
		          "      <cell>1000.00</cell>" +
		          "      <cell>-1000.00</cell>" +
		          "      <cell>0</cell>" +
		          "      <cell>15</cell>" +
		          "      <cell>16</cell>" +
		          "      <cell>true</cell>" +
		          "      <cell>false</cell>" +
		          "    </row>" +
		          "  </rows>";
		    }else if("1".equals(n_left)){//二级数据
		      xml = "<?xml version='1.0' encoding='utf-8'?>" +
		          "  <rows>" +
		          "    <page>1</page>" +
		          "    <total>1</total>" +
		          "    <records>1</records>" +
		          "    <row>" +
		          "      <cell>2</cell>" +
		          "      <cell>现金 1</cell>" +
		          "      <cell>1</cell>" +
		          "      <cell>300.00</cell>" +
		          "      <cell>200.00</cell>" +
		          "      <cell>100.00</cell>" +
		          "      <cell>1</cell>" +
		          "      <cell>2</cell>" +
		          "      <cell>5</cell>" +
		          "      <cell>false</cell>" +
		          "      <cell>false</cell>" +
		          "    </row>" +
		          "    <row>" +
		          "      <cell>4</cell>" +
		          "      <cell>现金 2</cell>" +
		          "      <cell>2</cell>" +
		          "      <cell>100.00</cell>" +
		          "      <cell>50.00</cell>" +
		          "      <cell>50.00</cell>" +
		          "      <cell>1</cell>" +
		          "      <cell>6</cell>" +
		          "      <cell>7</cell>" +
		          "      <cell>true</cell>" +
		          "      <cell>false</cell>" +
		          "    </row>" +
		          "  </rows>";
		    }else if("2".equals(n_left)){//三级数据
		      xml = "<?xml version='1.0' encoding='utf-8'?>" +
		          "  <rows>" +
		          "    <page>1</page>" +
		          "    <total>1</total>" +
		          "    <records>1</records>" +
		          "    <row>" +
		          "      <cell>3</cell>" +
		          "      <cell>现金子项 1</cell>" +
		          "      <cell>1</cell>" +
		          "      <cell>300.00</cell>" +
		          "      <cell>200.00</cell>" +
		          "      <cell>100.00</cell>" +
		          "      <cell>2</cell>" +
		          "      <cell>3</cell>" +
		          "      <cell>4</cell>" +
		          "      <cell>true</cell>" +
		          "      <cell>false</cell>" +
		          "    </row>" +
		          "  </rows>";
		    }
		ResponseUtil.write(xml);
		
	} 
	
	public String getCodestr() {
		return codestr;
	}
	public void setCodestr(String codestr) {
		this.codestr = codestr;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public void setScanCodeService(ScanCodeService scanCodeService) {
		this.scanCodeService = scanCodeService;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getN_left() {
		return n_left;
	}

	public void setN_left(String nLeft) {
		n_left = nLeft;
	}
	
	
	

}

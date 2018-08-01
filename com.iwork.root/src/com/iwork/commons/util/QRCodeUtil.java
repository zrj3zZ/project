package com.iwork.commons.util;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage; 
import java.io.File; 
import java.io.IOException;
import javax.imageio.ImageIO; 
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;
import org.apache.log4j.Logger;
import com.swetake.util.Qrcode; 
public class QRCodeUtil {
	private static Logger logger = Logger.getLogger(QRCodeUtil.class);
	
	 /**
     * 生成二维码(QRCode)图片
     * @paramcontent
     * @paramimgPath
     */ 
    public void encoderQRCode(String content, String imgPath) { 
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
           File imgFile = new File(imgPath); 
           // 生成二维码QRCode图片 
           ImageIO.write(bufImg, "png", imgFile); 
       } catch (Exception e) {
           logger.error(e); 
       } 
    }

	
    /**
     * 解码二维码
     * @paramimgPath
     * @returnString
     */ 
    public String decoderQRCode(String imgPath) { 
       // QRCode 二维码图片的文件 
       File imageFile = new File(imgPath); 
 
       BufferedImage bufImg = null; 
       String decodedData = null; 
       try { 
           bufImg = ImageIO.read(imageFile); 
 
           QRCodeDecoder decoder = new QRCodeDecoder(); 
           decodedData = new String(decoder.decode(new J2SEImage(bufImg))); 
 
           // try { 
           // "gb2312")); 
           // } catch (Exception e) { 
           // // TODO: handle exception 
           // } 
       } catch (IOException e) { 
           logger.error(e); 
       } catch (DecodingFailedException dfe) { 
           logger.error(dfe); 
       } 
       return decodedData; 
    } 
    
    class J2SEImage implements QRCodeImage { 
        BufferedImage bufImg; 
  
        public J2SEImage(BufferedImage bufImg) { 
            this.bufImg = bufImg; 
        } 
  
        public int getWidth() { 
            return bufImg.getWidth(); 
        } 
  
        public int getHeight() { 
            return bufImg.getHeight(); 
        } 
  
        public int getPixel(int x, int y) { 
            return bufImg.getRGB(x, y); 
        } 
  
     } 
}

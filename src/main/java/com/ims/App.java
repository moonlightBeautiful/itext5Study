package com.ims;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws IOException, DocumentException {
        // 待加水印的pdf文件
        PdfReader reader = new PdfReader("test01.pdf");
        // 加完水印的pdf文件
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("test02.pdf"));
        // 文本水印 
        String text = "这是一段测试文字";
        // 图片水印  
        Image img = Image.getInstance("barcode.png");
        //pdf页面
        PdfContentByte pdfContent;
        // 待加水印的pdf文件页数
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            // 获取pdf页面 stamper.getUnderContent(pageNum) 位于水印的下方
            pdfContent = stamper.getOverContent(i);  //pdfContent位于水印的上方
            // 设置水印状态
            PdfGState pdfGState = new PdfGState();
            pdfGState.setFillOpacity(0.3f);  // 设置透明度
            pdfContent.setGState(pdfGState);
            // 设置水印字体
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            pdfContent.setFontAndSize(baseFont, 32);

            /**
             * 添加文本水印
             */
            pdfContent.beginText();
            // 设置颜色 默认为黑色
            pdfContent.setColorFill(BaseColor.BLACK);
            // 开始写入文字水印
            pdfContent.showTextAligned(Element.ALIGN_MIDDLE, text, 180, 340, 45);
            pdfContent.showTextAligned(Element.ALIGN_MIDDLE, UUID.randomUUID().toString(), 140, 240, 45);
            pdfContent.endText();

            /**
             * 添加图片水印
             */
            img.setAbsolutePosition(150, 100);
            img.setRotationDegrees(45);// 旋转 角度
            img.scalePercent(50);// 设定显示比例 scalePercent(50)表示显示的大小为原尺寸的50% scalePercent(int percentX, int percentY)则图像高宽的显示比例
            pdfContent.addImage(img);
            /**
             * 使用模板添加照片水印
             */
            PdfTemplate pdfTemplate = pdfContent.createTemplate(100, 30);
            pdfTemplate.addImage(img, img.getWidth(), 0, 0, img.getHeight(), 0, 0);
            Font font = new Font(baseFont);
            Phrase phrase = new Phrase("我要和图片在一起", font);
            ColumnText.showTextAligned(pdfTemplate, Element.ALIGN_CENTER, phrase, 2, 2, 0);
            img = Image.getInstance(pdfTemplate);
            img.setAbsolutePosition(150, 100);
            img.setRotationDegrees(45);// 旋转 角度
            img.scalePercent(50);// 设定显示比例 scalePercent(50)表示显示的大小为原尺寸的50% scalePercent(int percentX, int percentY)则图像高宽的显示比例
            pdfContent.addImage(img);
        }
        stamper.close();
        reader.close();
        ;
    }
}

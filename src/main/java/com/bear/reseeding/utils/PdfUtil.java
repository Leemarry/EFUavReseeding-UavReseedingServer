package com.bear.reseeding.utils;

import org.docx4j.Docx4J;
import org.docx4j.XmlUtils;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Administrator
 */
public class PdfUtil {

	/**
	 * docx文档转换为PDF然后保存本地
	 * @param in
	 * @param outputFile
	 * @param params
	 * @throws Exception
	 */
	public static void convertDocxToPdf(InputStream in, File outputFile, Map<String,String> params) throws Exception {
		WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.load(in);
		if (params!=null&&!params.isEmpty()) {
			MainDocumentPart documentPart = wmlPackage.getMainDocumentPart();
			cleanDocumentPart(documentPart);
			documentPart.variableReplace(params);
		}
		setFontMapper(wmlPackage);
		FileOutputStream fos = new FileOutputStream(outputFile);
		Docx4J.toPDF(wmlPackage, fos);
		fos.close();
	}


	/**
	 * 清除文档空白占位符
	 * @param documentPart
	 * @return {@link boolean}
	 */
	public static boolean cleanDocumentPart(MainDocumentPart documentPart) throws Exception {
		if (documentPart == null) {
			return false;
		}
		Document document = documentPart.getContents();
		String wmlTemplate =
			XmlUtils.marshaltoString(document, true, false, Context.jc);
		document = (Document) XmlUtils.unwrap(DocxVariableClearUtil.doCleanDocumentPart(wmlTemplate, Context.jc));
		documentPart.setContents(document);
		return true;
	}
	/**
	 * 设置字体样式
	 * @param mlPackage
	 */
	private static void setFontMapper(WordprocessingMLPackage mlPackage) throws Exception {
		Mapper fontMapper = new IdentityPlusMapper();
		fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
		fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
		fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
		fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
		fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
		fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
		fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
		fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
		fontMapper.put("宋体扩展", PhysicalFonts.get("simsun-extB"));
		fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
		fontMapper.put("仿宋_GB2312", PhysicalFonts.get("FangSong_GB2312"));
		fontMapper.put("幼圆", PhysicalFonts.get("YouYuan"));
		fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
		fontMapper.put("华文中宋", PhysicalFonts.get("STZhongsong"));
		mlPackage.setFontMapper(fontMapper);
	}
}

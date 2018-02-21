package com.digiwin.mutilangutil.controller;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.internal.IEnterpriseSessionSecurity;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.occa.infostore.internal.IInternalInfoObject;
import com.crystaldecisions.sdk.occa.infostore.internal.InfoStoreFactory;
import com.crystaldecisions.sdk.occa.security.internal.ISecuritySession;
import com.crystaldecisions.sdk.plugin.desktop.folder.IFolder;
import com.digiwin.v6.util.StringUtil;
import com.sap.sl.sdk.authoring.cms.CmsResourceService;
import com.sap.sl.sdk.framework.SlContext;
import com.sap.sl.sdk.framework.cms.CmsSessionService;
import com.sap.translation.core.doc.ITMgrDocument;
import com.sap.translation.core.doc.ITMgrPropertyInfo;
import com.sap.translation.core.doc.PropertyStatus;
import com.sap.translation.core.engine.IEntityInfo;
import com.sap.translation.core.engine.ITMgrEngine;
import com.sap.translation.core.engine.IValueData;
import com.sap.translation.core.file.FileDocumentFactory;
import com.sap.translation.core.file.TMgrMergeReport;
import com.sap.translation.exception.TranslationException;
import com.sap.translation.sdk.TranslatableEntity;
import com.sap.translation.sdk.TranslationSDKManager;

@Controller
public class MultiLangTransController {
	private static Logger log = LogManager.getLogger(MultiLangTransController.class);
	
	private static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//private static String hostName = "BI6221A";
	private static String hostName = "DD06VM009";
	
	private static String userName = "Administrator";
	//private static String sourceServer = "10.40.41.41";
	//private static String sourceBiVer = "BI6";
	private static String sourceBiVer = "BI7";
	private static String sourceServer = "10.40.41.43";
	private static String sourcePwd = "Digiwin99";

	private static String dbServer = "10.40.41.157";
	//private static String dbName = "DicLib_154";
	private static String dbName = "DicLib";
	private static String dbUser = "sa";
	private static String dbPwd = "!QAZ2wsx";
	
	private static String unxTempPath = "C:\\sdk\\temp_unx\\"+sourceBiVer+"\\"+sourceServer+"\\";
	private static String webiTempPath = "C:\\sdk\\temp_webi\\"+sourceBiVer+"\\"+sourceServer+"\\";
	
	private static LinkedList<String> filterChList = new LinkedList<String>();
	
	private static Map<String, ITMgrEngine> tempEngineMap = new HashMap<String, ITMgrEngine>();
	
	public static void main(String[] args) throws Exception {
		try{
			
			filterChList = new LinkedList<String>();
			filterChList.add("一");
			filterChList.add("二");
			filterChList.add("三");
			filterChList.add("四");
			filterChList.add("五");
			filterChList.add("六");
			filterChList.add("七");
			filterChList.add("八");
			filterChList.add("九");
			filterChList.add("十");
			
			MultiLangTransController multiLangTransController = new MultiLangTransController();
			//multiLangTransController.proDicLib();
			//multiLangTransController.breakStr("");
			//multiLangTransController.checkMultiDic();
			
			//若資料夾不存在將自動創建
			multiLangTransController.createTempFolder(unxTempPath);
			multiLangTransController.createTempFolder(webiTempPath);
			
			//定義欲取得的物件類型
			Map<Integer, String> infoObjTypeMap = new HashMap<Integer, String>();
			//infoObjTypeMap.put(1, "Universe");
			infoObjTypeMap.put(2, "Webi");
			//根據物件類型，取得對應infoObjs
			IInfoObjects infoObjs = null;
			for (Integer objType : infoObjTypeMap.keySet()) {
				infoObjs = multiLangTransController.getCmsInfoObjs(objType, false, null);
				/*LinkedList<String> cValList = new LinkedList<String>();
				cValList.add("AYeOAwy_mRJDujKCZJowdTs");
				cValList.add("AVKMDbcIz7tIq4qQC0klXvM");
				cValList.add("ASFJemoeWZJKvAmu46NNusg");
				cValList.add("AYr.uByCRiVCsZFn5zzIhDk");
				cValList.add("AUv8pLdTeK5DkspOGdA04Aw");
				cValList.add("AdMkxzK9KrtIh728rAxCuMo");
				cValList.add("Ad4x0PwEBuZNt10Z3KMI4Uc");
				cValList.add("AUGRL.LZoaNBoLhxWJJhfSQ");
				cValList.add("AT8VmUCI7uBGv1BdSobpnQw");
				cValList.add("AQi1WVH_IWZFp3Zo6xgVf9U");
				cValList.add("AayngHhsqYhMrM1w7rtw4G4");
				//infoObjs = multiLangTransController.getCmsInfoObjs(objType, true, cValList);*/
				//Webi
				/*LinkedList<String> cValList = new LinkedList<String>();
				cValList.add("AUqxzIjuO1ZAlXxf6auqeoA");
				cValList.add("Aao_PtxiAmtAlUT7wzGDBv8");
				cValList.add("AXkunu4kwxVFq7IAhplLe7o");
				cValList.add("ARdXyPFfaF1FrfBpOsAlJb0");
				cValList.add("AX50Jk5.VgxCrIKt5_Hpgcc");
				cValList.add("AXRmk2g3bAhAkEd88EHPpEQ");
				cValList.add("AbupZHkYrFpBmbINnYCtFB0");
				cValList.add("Abdigsd_4E1JkKZow4sTWfQ");
				cValList.add("Acz2y0Mq2zVLhqJl5T6dYOM");
				cValList.add("ATMiYeJ_YzFBhSn6H3B2vjg");
				cValList.add("AZIYIoKe50FEu3mB4SP279o");
				infoObjs = multiLangTransController.getCmsInfoObjs(objType, true, cValList);*/
				
				//LinkedList<String> cValList = new LinkedList<String>();
				//cValList.add("AQaEcpjESq9LkaunePPc5sM");
				//cValList.add("Afdvu7S.0y1OtDjmDnm1yC8");
				//cValList.add("ARAHUf9V2aFOkGgGqlqT80U");
				//cValList.add("AZRGJnh1VGRIll7qii5YI3Q");
				//cValList.add("AeuXgCuudGVMjTtkN9GyoaA");
				//cValList.add("AWWhNR6CVPJMmfEBYUAJWdg");
				//cValList.add("AQ0q67y3o1VOv7_fh.Fz3qU");
				//cValList.add("AQ0q67y3o1VOv7_fh.Fz3qU");
				//cValList.add("AT3PGm3Ts81Ei_TGyQipwBI");
				//infoObjs = multiLangTransController.getCmsInfoObjs(objType, true, cValList);
				
				//Unx須取得allUnxPathMap
				Map<IInternalInfoObject, String> allUnxPathMap = null;
				if(objType==1){
					allUnxPathMap = multiLangTransController.getAllUnxPath(infoObjs);
				}
				/*
				 * 主方法
				 * */
				multiLangTransController.loadObjToDb(objType, infoObjs, allUnxPathMap);	
				multiLangTransController.addObjLangToCms(objType, infoObjs, allUnxPathMap);
			}				
		} catch(Exception e) {
			log.error("Controller main Error", e);
			throw e;
		}
	}
	
	private Map<String, Map<String, String>> getObjInfo() throws Exception{
		
		Map<String, Map<String, String>> objInfoMap = new HashMap<String, Map<String, String>>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			StringBuffer strSql = new StringBuffer(
				" Select * "
				+ " From obj_info "
				+ " Where 1=1 "
			);
			//log.debug("strSql: "+strSql);	
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection("jdbc:sqlserver://"+dbServer+":1433;databaseName="+dbName+";user="+dbUser+";password="+dbPwd+";sendStringParametersAsUnicode=true;");	
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSql.toString());
			while(rs.next()){
				Map<String, String> thisMap = new HashMap<String, String>();
				thisMap.put("src_bi_ver", rs.getString("src_bi_ver"));
				thisMap.put("obj_id", rs.getString("obj_id"));
				thisMap.put("obj_type", rs.getString("obj_type"));
				thisMap.put("obj_path", rs.getString("obj_path"));
				thisMap.put("dtl_key", rs.getString("dtl_key"));
				thisMap.put("dtl_type", rs.getString("dtl_type"));
				thisMap.put("dtl_type_class", rs.getString("dtl_type_class"));
				thisMap.put("src_lng_str", rs.getString("src_lng_str"));
				thisMap.put("src_loc", rs.getString("src_loc"));
				thisMap.put("src_loc_abb", rs.getString("src_loc_abb"));
				thisMap.put("tgt_loc", rs.getString("tgt_loc"));
				thisMap.put("tgt_loc_abb", rs.getString("tgt_loc_abb"));
				thisMap.put("tgt_lng_str", rs.getString("tgt_lng_str"));
				thisMap.put("obj_key_id", rs.getString("obj_key_id"));
				objInfoMap.put(rs.getString("src_bi_ver")+"@@"+rs.getString("obj_id")+"@@"+rs.getString("dtl_key")+"@@"+rs.getString("dtl_type_class")+"@@"+rs.getString("tgt_loc_abb"), thisMap);
			}	
		} catch (Exception e) {
			log.error("Controller getObjInfo Error", e);
			throw e;
		}  finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeConnection(con);
		}
		return objInfoMap;
	}
	
	private void addObjLangToCms(int objType, IInfoObjects infoObjs, Map<IInternalInfoObject, String> allUnxPathMap) throws Exception{
		Connection con = null;
		PreparedStatement pStmtY = null;
		PreparedStatement pStmtN = null;
		PreparedStatement pStmtP = null;
		IEnterpriseSession enterpriseSession = null;
		try{
			ITMgrEngine engine = null;
			Map<String, ITMgrEngine> engineMap = new HashMap<String, ITMgrEngine>();
			if(tempEngineMap.size()>0){
				engineMap.putAll(tempEngineMap);
			} else {
				engineMap = getCmsEngine(infoObjs, allUnxPathMap);
			}
			log.debug(engineMap);
			log.debug("engineMap size:"+engineMap.size());
			
			enterpriseSession = getEnterpriseSession();
			ISecuritySession userSession = ((IEnterpriseSessionSecurity) enterpriseSession).getSecuritySession();
			IInfoStore infostore = (IInfoStore) InfoStoreFactory.getFactory().makeOCCA("", userSession);
			Map<String, String> objPathCuid = new HashMap<String, String>();
			for(int ifoNum=0; ifoNum<infoObjs.size(); ifoNum++){
				IInternalInfoObject infoObj = (IInternalInfoObject) infoObjs.get(ifoNum);
				objPathCuid.putAll(getObjPath(objType, infostore, infoObj));
			}
			enterpriseSession.logoff();
			
			Map<String, Map<String, String>> dicLibDataMap = getDicLibData();
			Map<String, Map<String, String>> objInfoMap = getObjInfo();
			
			int zhCNNotMappedCount = 0;
			int enUSNotMappedCount = 0;
			int totalStrCount = 0;
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection("jdbc:sqlserver://"+dbServer+":1433;databaseName="+dbName+";user="+dbUser+";password="+dbPwd+";sendStringParametersAsUnicode=true;");
			
			StringBuffer sqlInDicObjMap = new StringBuffer(" Insert Into dic_obj_map (obj_id, dic_key_id, obj_key_id, created_user, created_date )");
			sqlInDicObjMap.append(" Values (?, ?, ?, ?, ?)");
			pStmtY = con.prepareStatement(sqlInDicObjMap.toString());
			
			StringBuffer sqlInStrNoMap = new StringBuffer(" Insert Into str_no_map (obj_id, src_lng_str, obj_key_id, tgt_loc, tgt_loc_abb, created_user, created_date )");
			sqlInStrNoMap.append(" Values (?, ?, ?, ?, ?, ?, ?)");
			pStmtN = con.prepareStatement(sqlInStrNoMap.toString());
			
			StringBuffer sqlInStrPartMap = new StringBuffer(" Insert Into str_part_map (obj_id, obj_key_id, part_trans_str, tgt_loc, tgt_loc_abb, created_user, created_date )");
			sqlInStrPartMap.append(" Values (?, ?, ?, ?, ?, ?, ?)");
			pStmtP = con.prepareStatement(sqlInStrPartMap.toString());
			
			for(String objCuid : engineMap.keySet()){
				engine = engineMap.get(objCuid);

				
				log.debug("objCuid:"+objCuid);		
				/*
				 * 主體物件架構說明：
				 * 1.語言別
				 * 2.物件名稱
				 * 3.物建內容：一個物件，會有多個內容，例如：
				 * -root有Name、Description
				 * -UNX的維度、計量會有Name、Description、Format
				 * */
				//定義Locale
				Map<String, Locale> transLocalesMap = new HashMap<String, Locale>();
				transLocalesMap.put("TW", new Locale("zh", "TW"));
				transLocalesMap.put("CN", new Locale("zh", "CN"));
				transLocalesMap.put("US", new Locale("en", "US"));
				//定義langLabel
				LinkedList<Map<String, String>> langLabelListMap = new LinkedList<Map<String, String>>();
				Map<String, String> langLabelMap = new HashMap<String, String>();
				langLabelMap.put("loc", "CN");
				langLabelMap.put("locAbb", "zh_CN");
				langLabelListMap.add(langLabelMap);
				langLabelMap = new HashMap<String, String>();
				langLabelMap.put("loc", "US");
				langLabelMap.put("locAbb", "en_US");
				langLabelListMap.add(langLabelMap);
				
				//初始話tmgr，避免tmgr無法覆蓋之狀況
				log.debug("init tmgr...");
				engine.removeAvailableLocale(Locale.TAIWAN);
				engine.removeAvailableLocale(Locale.CHINA);
				engine.removeAvailableLocale(Locale.US);
				engine.save();	
				//只有Webi需要發佈至CMS
				if(objType==2){
					exportToCms(engine, objCuid);
				}
				
				//設定翻譯語系
				engine.addAvailableLocale(Locale.TAIWAN);
				engine.setModifiedLocale(Locale.TAIWAN);
				engine.setLocaleVisible(Locale.TAIWAN,true);
				engine.setFallbackLocale(Locale.TAIWAN);
				engine.addAvailableLocale(Locale.CHINA);
				engine.setModifiedLocale(Locale.CHINA);
				engine.setLocaleVisible(Locale.CHINA,true);
				
				log.debug("init mapping db...");
				LinkedList<String> delObjCuidList = new LinkedList<String>();
				delObjCuidList.add(objCuid);
				Map<String, LinkedList<String>> tgtTableMapList = new HashMap<String, LinkedList<String>>();
				tgtTableMapList.put("dic_obj_map", delObjCuidList);
				tgtTableMapList.put("str_no_map", delObjCuidList);
				tgtTableMapList.put("str_part_map", delObjCuidList);
				initTableData(tgtTableMapList);

				log.debug("===============");
				log.debug("obj_id:"+objCuid+"；str not mapping:");
				for(int i=0; i<engine.getEntities().size(); i++){
					IEntityInfo thisInfo = engine.getEntityInfo(engine.getEntities().get(i));
					Object thisEntity = engine.getEntities().get(i);
					String objId = thisInfo.getEntityId();
					//objId為null時，表示非實體物件
					if(null!=objId){
						//取得物件原始與細內容
						ITMgrPropertyInfo[] pInfos = thisInfo.getLocalizedProperties();
						//log.debug(pInfos.length);
						for(int j=0; j<pInfos.length; j++){
							//針對org語系取得內容
							String orgStr = pInfos[j].getValue(thisEntity, null);
							//log.debug(orgStr);
							Map<String, Map<String, String>> orgStrMap = new HashMap<String, Map<String, String>>();
							Map<String, String> orgStrDtlMap = new HashMap<String, String>();
							
							if(null!=orgStr){
								orgStr = orgStr.trim();
							}
							if(!StringUtil.isEmptyOrSpace(orgStr)){
								//切割orgStr
								LinkedList<String> orgStrList = breakStr(orgStr);
								//判斷是否存在任一中文字
								boolean isOrgStrHasChs = false;
								for(int k=0; k<orgStrList.size(); k++){
									if(isChinese(orgStrList.get(k).charAt(0))){
										isOrgStrHasChs = true;
										break;
									}
								}
								//如果不存在任何中文字，就不進行翻譯
								if(isOrgStrHasChs){
									//log.debug("org:"+orgStr);
									//log.debug(objId);
									Map<String, Map<String, String>> tgtLangObjInfoMap = new HashMap<String, Map<String, String>>();
									String thisObjInfoMapKey = null;
									String tempPromptId = null;
									int promptNum = 1;
									String newObjInfoMapKey = null;	
									//走仿語言別，取得目標objInfoMap，並以語言別建置tgtLangObjInfoMap
									for(int n=0; n<langLabelListMap.size(); n++){
										thisObjInfoMapKey = sourceBiVer+"@@"+objCuid+"@@"+objId+"@@"+(j+1)+"@@"+"NULL";
										//一般物件會在正常的迴圈內，進行累加已取得正確的dtl_type_class
										if(!StringUtil.isEmptyOrSpace(objInfoMap.get(thisObjInfoMapKey))){
											tgtLangObjInfoMap.put(langLabelListMap.get(n).get("locAbb"), objInfoMap.get(thisObjInfoMapKey));
										} else {
											//特殊物件，如prompt，僅會以index=3的方式進行處理
											//故必須對此有獨立的index處理機制
											//log.debug("null key:"+thisObjInfoMapKey);
											if(!objId.equalsIgnoreCase(tempPromptId)){
												promptNum = 1;
											} else {
												promptNum++;
											}
											newObjInfoMapKey = sourceBiVer+"@@"+objCuid+"@@"+objId+"@@"+promptNum+"@@"+"NULL";
											tgtLangObjInfoMap.put(langLabelListMap.get(n).get("locAbb"), objInfoMap.get(newObjInfoMapKey));
											tempPromptId = objId;
											//if(!StringUtil.isEmptyOrSpace(objInfoMap.get(newObjInfoMapKey))){
											//	log.debug("(o) key is already set.");
											//} else {
											//	log.debug("(x) key still null!!");
											//}
										}
									}
									int mapCount = 0;
									for(int m=0; m<orgStrList.size(); m++){
										String thisOrgStr = orgStrList.get(m);
										//走訪語言別
										for(int n=0; n<langLabelListMap.size(); n++){
											String loc = langLabelListMap.get(n).get("loc");
											String locAbb = langLabelListMap.get(n).get("locAbb");
											//以thisOrgStr作為Mapping依據
											Map<String, String> mappedDicLibDataMap = dicLibDataMap.get(thisOrgStr+"@@"+loc+"@@"+locAbb);
											//取得objKeyId，方可Mapping回物件對應表
											String objKeyId = null;
											if(!StringUtil.isEmptyOrSpace(tgtLangObjInfoMap.get(locAbb))){
												objKeyId = tgtLangObjInfoMap.get(locAbb).get("obj_key_id");
											}/* else {
												log.debug(mappedDicLibDataMap);
											}*/
											
											//實作與翻譯檔的Mapping
											if(!StringUtil.isEmptyOrSpace(mappedDicLibDataMap)){
												//log.debug(tgtLangObjInfoMap.get(locAbb).get("obj_key_id"));	
												//如果有Mapping到，就放入dic_obj_map
												pStmtY.setObject(1, objCuid);
												pStmtY.setObject(2, mappedDicLibDataMap.get("dic_key_id"));
												pStmtY.setObject(3, objKeyId);
												pStmtY.setObject(4, "12");
												pStmtY.setObject(5, sdFormat.format(new Date()));
												pStmtY.addBatch();
												//InitMap，避免存到缺少語系的資料
												orgStrDtlMap = new HashMap<String, String>();
												orgStrMap = new HashMap<String, Map<String, String>>();
												
												orgStr = orgStr.replaceFirst(thisOrgStr, mappedDicLibDataMap.get("tgt_lng_str"));
												orgStrDtlMap.put("str", orgStr);
												orgStrMap.put(loc, orgStrDtlMap);
												
												if(!"zh_CN".equalsIgnoreCase(locAbb)){
													mapCount++;
												}
											} else {
												orgStrDtlMap = new HashMap<String, String>();
												orgStrMap = new HashMap<String, Map<String, String>>();
												//若有任何一組詞彙，是沒有被翻譯到的，就代表非完整翻譯
												//log.debug("not mapping dtl str:"+thisOrgStr);
												if("zh_CN".equalsIgnoreCase(locAbb)){
													zhCNNotMappedCount++;
												} else {
													enUSNotMappedCount++;
													orgStrDtlMap.put("isTrans", "N");
													//log.debug("not mapping dtl str:"+thisOrgStr);
													//如果英文Mapping不到，就放入str_no_map
													//目前尚未處裡除了英文以外，其他詞彙的Mapping
													pStmtN.setObject(1, objCuid);
													pStmtN.setObject(2, thisOrgStr);
													pStmtN.setObject(3, objKeyId);
													pStmtN.setObject(4, loc);
													pStmtN.setObject(5, locAbb);
													pStmtN.setObject(6, "12");
													pStmtN.setObject(7, sdFormat.format(new Date()));
													pStmtN.addBatch();
												}
												//找不到對應關係就用原始語系
												orgStrDtlMap.put("str", pInfos[j].getValue(thisEntity, null));
												orgStrMap.put(loc, orgStrDtlMap);
												
											}
											totalStrCount++;
										}
									}
									for(int n=0; n<langLabelListMap.size(); n++){
										String loc = langLabelListMap.get(n).get("loc");
										String locAbb = langLabelListMap.get(n).get("locAbb");
										if(orgStrMap.containsKey(loc)){
											pInfos[j].setValue(thisEntity, transLocalesMap.get(loc), orgStrMap.get(loc).get("str"));
											//log.debug("===========");
											//log.debug("str:"+orgStrMap.get(loc).get("str"));
											if(!"N".equalsIgnoreCase(orgStrMap.get(loc).get("isTrans"))){
												if(orgStrList.size()==mapCount){
													pInfos[j].setStatus(thisEntity, transLocalesMap.get(loc), PropertyStatus.TRANSLATED);
												} else {
													pInfos[j].setStatus(thisEntity, transLocalesMap.get(loc), PropertyStatus.NEEDS_REVIEW_ADAPTATION);
													pStmtP.setObject(1, objCuid);
													pStmtP.setObject(2, tgtLangObjInfoMap.get(locAbb).get("obj_key_id"));
													pStmtP.setObject(3, orgStrMap.get(loc).get("str"));
													pStmtP.setObject(4, loc);
													pStmtP.setObject(5, locAbb);
													pStmtP.setObject(6, "12");
													pStmtP.setObject(7, sdFormat.format(new Date()));
													pStmtP.addBatch();
												}
											} else {
												pInfos[j].setStatus(thisEntity, transLocalesMap.get(loc), PropertyStatus.NEEDS_TRANSLATION);
											}
											//log.debug("===========");
										} else {
											pInfos[j].setValue(thisEntity, transLocalesMap.get(loc), pInfos[j].getValue(thisEntity, null));
											pInfos[j].setStatus(thisEntity, transLocalesMap.get(loc), PropertyStatus.NEEDS_TRANSLATION);
										}
									}
								} else {
									//不進行翻譯也算是一種翻譯
									for(int n=0; n<langLabelListMap.size(); n++){
										String loc = langLabelListMap.get(n).get("loc");
										pInfos[j].setValue(thisEntity, transLocalesMap.get(loc), pInfos[j].getValue(thisEntity, null));
										pInfos[j].setStatus(thisEntity, transLocalesMap.get(loc), PropertyStatus.TRANSLATED);
									}
								}
								pInfos[j].setValue(thisEntity, transLocalesMap.get("TW"), pInfos[j].getValue(thisEntity, null));
								pInfos[j].setStatus(thisEntity, transLocalesMap.get("TW"), PropertyStatus.TRANSLATED);
							}
							

							//空白或空值無須翻譯
//							if(!StringUtil.isEmptyOrSpace(orgStr)){
//								for(int k=0; k<langLabelListMap.size(); k++){
//									String loc = langLabelListMap.get(k).get("loc");
//									String locAbb = langLabelListMap.get(k).get("locAbb");
//									Map<String, String> mappedDicLibDataMap = dicLibDataMap.get(orgStr+"_"+loc+"_"+locAbb);
//									if(!StringUtil.isEmptyOrSpace(mappedDicLibDataMap)){
//										pInfos[j].setValue(thisEntity, transLocalesMap.get(loc), mappedDicLibDataMap.get("tgt_lng_str"));
//										pInfos[j].setStatus(thisEntity, transLocalesMap.get(loc), PropertyStatus.TRANSLATED);
//										/*log.debug("===========");
//										log.debug("orgStr:"+orgStr);
//										log.debug("Mapped Val:"+mappedDicLibDataMap.get("src_lng_str"));
//										log.debug("===========");*/
//									} else {
//										log.debug(orgStr);
//									}
//								}
//							}
						}
					}
				}
				//commit:dic_obj_map
				try{
					int[] commitCounts = pStmtY.executeBatch();
					log.debug("addObjLangToCms commit:dic_obj_map:"+commitCounts.length);
				}
				catch (BatchUpdateException bue) {
					int[] commitCounts = bue.getUpdateCounts();
					 for (int c = 0; c < commitCounts.length; c++) {
				    	 int commitIndex = commitCounts[c];
				    	 if(commitIndex!=1){
				    		// log.debug(debugLists.get(i));
				    	 }
				     }
					 log.debug(bue.getMessage());
				}
				pStmtY.clearBatch();
				//commit:str_no_map
				try{
					int[] commitCounts = pStmtN.executeBatch();
					log.debug("addObjLangToCms commit:str_no_map:"+commitCounts.length);
				}
				catch (BatchUpdateException bue) {
					int[] commitCounts = bue.getUpdateCounts();
					 for (int c = 0; c < commitCounts.length; c++) {
				    	 int commitIndex = commitCounts[c];
				    	 if(commitIndex!=1){
				    		// log.debug(debugLists.get(i));
				    	 }
				     }
					 log.debug(bue.getMessage());
				}
				pStmtN.clearBatch();
				//commit:str_part_map
				try{
					int[] commitCounts = pStmtP.executeBatch();
					log.debug("addObjLangToCms commit:str_part_map:"+commitCounts.length);
				}
				catch (BatchUpdateException bue) {
					int[] commitCounts = bue.getUpdateCounts();
					 for (int c = 0; c < commitCounts.length; c++) {
				    	 int commitIndex = commitCounts[c];
				    	 if(commitIndex!=1){
				    		// log.debug(debugLists.get(i));
				    	 }
				     }
					 log.debug(bue.getMessage());
				}
				pStmtP.clearBatch();
				log.debug("===============");
				engine.save();
				//只有Webi需要發佈至CMS
				if(objType==2){
					exportToCms(engine, objCuid);
				}
				log.debug("export success!");
			}
			log.debug("totalStrCount:"+totalStrCount/2);
			log.debug("zhCNNotMappedCount:"+zhCNNotMappedCount);
			log.debug("enUSNotMappedCount:"+enUSNotMappedCount);
		} catch (Exception e) {
			log.error("Controller loadSubObjToDb Error", e);
			//throw e;
		}  finally {
			closePStatement(pStmtP);
			closePStatement(pStmtN);
			closePStatement(pStmtY);
			closeConnection(con);
			if(null!=enterpriseSession)
				enterpriseSession.logoff();
		}
	}
	//將CMS物件資訊，讀取並儲存至DB(obj_info)，即產生物件結構表
	private void loadObjToDb(int objType, IInfoObjects infoObjs, Map<IInternalInfoObject, String> allUnxPathMap) throws Exception{
		Connection con = null;
		PreparedStatement pStmt = null;
		IEnterpriseSession enterpriseSession = null;
		try{
			ITMgrEngine engine = null;
			tempEngineMap = new HashMap<String, ITMgrEngine>();
			Map<String, ITMgrEngine> engineMap = getCmsEngine(infoObjs, allUnxPathMap);
			tempEngineMap.putAll(engineMap);
			log.debug(engineMap);
			log.debug("engineMap size:"+engineMap.size());
			
			enterpriseSession = getEnterpriseSession();
			ISecuritySession userSession = ((IEnterpriseSessionSecurity) enterpriseSession).getSecuritySession();
			IInfoStore infostore = (IInfoStore) InfoStoreFactory.getFactory().makeOCCA("", userSession);
			Map<String, String> objPathCuid = new HashMap<String, String>();
			for(int ifoNum=0; ifoNum<infoObjs.size(); ifoNum++){
				IInternalInfoObject infoObj = (IInternalInfoObject) infoObjs.get(ifoNum);
				objPathCuid.putAll(getObjPath(objType, infostore, infoObj));
			}
			enterpriseSession.logoff();
			
			for(String objCuid : engineMap.keySet()){
				engine = engineMap.get(objCuid);	
				
				Locale[] vLocales = engine.getVisibleLocales();		
				Locale[] locales = new Locale[vLocales.length+1];
				for(int i=0; i<vLocales.length; i++){
					locales[i] = vLocales[i];
				}	
				locales[vLocales.length] = null;

				Map<String, String> existObjInfo = new HashMap<String, String>();
				existObjInfo.put("objId", objCuid);
				existObjInfo.put("objType", String.valueOf(objType));
				existObjInfo.put("objPath", objPathCuid.get(objCuid));
				log.debug("objCuid:"+objCuid);
				
				log.debug("init obj_info db...");
				LinkedList<String> delObjCuidList = new LinkedList<String>();
				delObjCuidList.add(objCuid);
				Map<String, LinkedList<String>> tgtTableMapList = new HashMap<String, LinkedList<String>>();
				tgtTableMapList.put("obj_info", delObjCuidList);
				initTableData(tgtTableMapList);
				
				/*
				 * 主體物件架構說明：
				 * 1.語言別
				 * 2.物件名稱
				 * 3.物建內容：一個物件，會有多個內容，例如：
				 * -root有Name、Description
				 * -UNX的維度、計量會有Name、Description、Format
				 * */
				StringBuffer sqlInDtlInfo = new StringBuffer(" Insert Into obj_info (src_bi_ver, obj_id, obj_type, obj_path, dtl_key, dtl_type, dtl_type_class, src_lng_str, src_loc, src_loc_abb, tgt_loc, tgt_loc_abb, tgt_lng_str, obj_key_id, ");
				sqlInDtlInfo.append(" created_user, created_date) ");
				sqlInDtlInfo.append(" Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				con = DriverManager.getConnection("jdbc:sqlserver://"+dbServer+":1433;databaseName="+dbName+";user="+dbUser+";password="+dbPwd+";sendStringParametersAsUnicode=true;");	
				pStmt = con.prepareStatement(sqlInDtlInfo.toString());
					
				for(int j=0; j<engine.getEntities().size(); j++){
					IEntityInfo thisInfo = engine.getEntityInfo(engine.getEntities().get(j));
					Object thisEntity = engine.getEntities().get(j);
						
					//Map<String, LinkedList<String>> localObjValMap = getObjDtlData(thisInfo, locales);
					Map<String, LinkedList<String>> localObjValMap = getObjDtlDataB(thisInfo, thisEntity, locales);
					
					String objId = thisInfo.getEntityId();
					String dtlType = null;
					
					if(objType==1){
						if(null!=objId){
							dtlType = objId.substring(0, objId.indexOf(":"));
						}
					} else if(objType==2){
						//避免root時，無Parent而出錯
						if(null!=thisInfo.getParent()){
							dtlType = thisInfo.getParent().getText();
							if(null==dtlType){
								dtlType = "root";
							}
						} else {
							dtlType = "root";
						}
					}
					for(String langKey : localObjValMap.keySet()){
						pStmt.setObject(1, sourceBiVer);
						pStmt.setObject(2, existObjInfo.get("objId"));
						pStmt.setObject(3, existObjInfo.get("objType"));
						pStmt.setObject(4, existObjInfo.get("objPath"));
						pStmt.setObject(5, objId);
						pStmt.setObject(6, dtlType);
						pStmt.setObject(7, 0);
						pStmt.setObject(8, null);
						pStmt.setObject(9, "TW");
						pStmt.setObject(10, "zh_TW");
						pStmt.setObject(11, "NULL");
						pStmt.setObject(12, "NULL");
						pStmt.setObject(13, null);
							
						LinkedList<String> thisObjValMap = localObjValMap.get(langKey);
						for(int i=0; i<thisObjValMap.size(); i++){
							pStmt.setObject(7, (i+1));
							pStmt.setObject(8, localObjValMap.get("org").get(i));
							if(!"org".equalsIgnoreCase(langKey)){
								String srcLngStr = null;
								if(!StringUtil.isEmptyOrSpace(localObjValMap.containsKey("zh_TW"))){
									try{
										//srcLngStr = localObjValMap.get("zh_TW").get(i);
										srcLngStr = localObjValMap.get("org").get(i);
									} catch (Exception e) {
										log.error(e);
									}
								}
								pStmt.setObject(8, srcLngStr);
								if("en_US".equalsIgnoreCase(langKey)){
									pStmt.setObject(11, "US");
								}
								if("zh_CN".equalsIgnoreCase(langKey)){
									pStmt.setObject(11, "CN");
								}
								if("zh_TW".equalsIgnoreCase(langKey)){
									pStmt.setObject(11, "TW");
								}
								pStmt.setObject(12, langKey);
								pStmt.setObject(13, thisObjValMap.get(i));
							}		
							pStmt.setObject(14, UUID.randomUUID().toString());
							String nowDatetime = sdFormat.format(new Date());		
							pStmt.setObject(15, "12");
							pStmt.setObject(16, nowDatetime);
							pStmt.addBatch();
						}
					}
				}
				try{
					int[] commitCounts = pStmt.executeBatch();
					log.debug("loadSubObjToDb commitCounts:"+commitCounts.length);
				}
				catch (BatchUpdateException bue) {
					int[] commitCounts = bue.getUpdateCounts();
					 for (int i = 0; i < commitCounts.length; i++) {
				    	 int commitIndex = commitCounts[i];
				    	 if(commitIndex!=1){
				    		// log.debug(debugLists.get(i));
				    	 }
				     }
					 log.debug(bue.getMessage());
				}
				pStmt.clearBatch();
			}
		} catch (Exception e) {
			log.error("Controller loadObjToDb Error", e);
			//throw e;
		}  finally {
			closePStatement(pStmt);
			closeConnection(con);
			if(null!=enterpriseSession)
				enterpriseSession.logoff();
		}
	}
	
	private void initTableData(Map<String, LinkedList<String>> tgtTableMapList){
		Connection con = null;
		Statement stmt = null;
		try{
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection("jdbc:sqlserver://10.40.41.157:1433;databaseName=DicLib;user=sa;password=!QAZ2wsx;sendStringParametersAsUnicode=true;");
			stmt = con.createStatement();
			
			StringBuffer initTableSql = new StringBuffer();
			for(String tableName : tgtTableMapList.keySet() ){
				initTableSql = new StringBuffer(
					"Delete From "+tableName+" Where obj_id In ( "
				);
				LinkedList<String> objCuidList = tgtTableMapList.get(tableName);
				for(int i=0; i<objCuidList.size(); i++){
					initTableSql.append("'"+objCuidList.get(i)+"'");
					if((i+1)==objCuidList.size()){
						initTableSql.append(" ) ");
					} else {
						initTableSql.append(", ");
					}
				}
				log.debug("initTableSql:"+initTableSql);
				stmt.executeUpdate(initTableSql.toString());
			}
			log.debug("initTableData success!");	
		} catch (Exception e) {
			log.error("Controller initTableData Error", e);
			//throw e;
		}  finally {
			closeStatement(stmt);
			closeConnection(con);
		}
	}
	
	private void checkMultiDic(){
		
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		
		String nowDatetime = sdFormat.format(new Date());
		
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection("jdbc:sqlserver://10.40.41.157:1433;databaseName=DicLib;user=sa;password=!QAZ2wsx;sendStringParametersAsUnicode=true;");
			stmt = con.createStatement();

			//初始化multi_trans_id欄位為Null
			StringBuffer strDicLibInitSql = new StringBuffer(
				" Update dic_lib_data Set multi_trans_id = null, last_updated_user = '12', last_updated_date = '"+nowDatetime+"' "
			);
			log.debug("strDicLibInitSql:"+strDicLibInitSql);
			stmt.executeUpdate(strDicLibInitSql.toString());
			
			//查詢具備一個已上翻譯的項目
			StringBuffer strDicLibSql = new StringBuffer(
				" Select * From dic_lib_data "+
					" Where src_lng_str In ( "+
						" Select src_lng_str "+
						" From dic_lib_data "+
						" Group By src_lng_str "+
						" Having Count(src_lng_str)>1 "+
					")"
			);
			log.debug("strDicLibSql: "+strDicLibSql);
			rs = stmt.executeQuery(strDicLibSql.toString());
			LinkedList<Map<String, String>> dicLibRsListMap = new LinkedList<Map<String, String>>();
			while(rs.next()){
				Map<String, String> thisRsMap = new HashMap<String, String>();
				thisRsMap.put("seq_id", rs.getString("seq_id"));
				thisRsMap.put("src_lng_str", rs.getString("src_lng_str"));
				thisRsMap.put("src_loc", rs.getString("src_loc"));
				thisRsMap.put("src_loc_abb", rs.getString("src_loc_abb"));
				thisRsMap.put("tgt_loc", rs.getString("tgt_loc"));
				thisRsMap.put("tgt_loc_abb", rs.getString("tgt_loc_abb"));
				thisRsMap.put("tgt_lng_str", rs.getString("tgt_lng_str"));
				thisRsMap.put("dic_key_id", rs.getString("dic_key_id"));
				thisRsMap.put("multi_trans_id", rs.getString("multi_trans_id"));
				dicLibRsListMap.add(thisRsMap);
			}
			
			StringBuffer sqlUpDicLibData = new StringBuffer(
				" Update dic_lib_data Set multi_trans_id = ?, last_updated_user = '12', last_updated_date = '"+nowDatetime+"'"
					+ " Where seq_id = ? ");
			log.debug("sqlUpDicLibData: "+sqlUpDicLibData);
			pStmt = con.prepareStatement(sqlUpDicLibData.toString());
			
			int totalCount = 0;
			
			Map<String, String> tempStrKeyMap = new HashMap<String, String>();
			for(int i=0; i<dicLibRsListMap.size(); i++){
				Map<String, String> thisRsMap = dicLibRsListMap.get(i);
				String seqId = thisRsMap.get("seq_id");
				String srcLngStr = thisRsMap.get("src_lng_str");
				String srcLoc = thisRsMap.get("src_loc");
				String srcLocAbb = thisRsMap.get("src_loc_abb");
				String tgtLoc = thisRsMap.get("tgt_loc");
				String tgtLocAbb = thisRsMap.get("tgt_loc_abb");
				String thisStrKey = srcLngStr+"@@"+srcLoc+"@@"+srcLocAbb+"@@"+tgtLoc+"@@"+tgtLocAbb;
				pStmt.setObject(2, seqId);
				if(tempStrKeyMap.containsKey(thisStrKey)){
					pStmt.setObject(1, tempStrKeyMap.get(thisStrKey));
				} else {
					String multiTransId = UUID.randomUUID().toString();
					pStmt.setObject(1, multiTransId);
					tempStrKeyMap = new HashMap<String, String>(); 
					tempStrKeyMap.put(thisStrKey, multiTransId);
				}
				pStmt.addBatch();
				totalCount++;
			}
			
			log.debug("totalCount:"+totalCount);
			log.debug("Start Time:"+sdFormat.format(new Date()));
			try{
				int[] commitCounts = pStmt.executeBatch();
				log.debug("checkMultiDic commitCounts:"+commitCounts.length);
			}
			catch (BatchUpdateException bue) {
				int[] commitCounts = bue.getUpdateCounts();
				 for (int i = 0; i < commitCounts.length; i++) {
			    	 int commitIndex = commitCounts[i];
			    	 if(commitIndex!=1){
			    		// log.debug(debugLists.get(i));
			    	 }
			     }
				 log.debug(bue.getMessage());
			}
			pStmt.clearBatch();
			log.debug("End Time:"+sdFormat.format(new Date()));

		} catch (Exception e) {
			log.error("Controller checkMultiDic Error", e);
			//throw e;
		}  finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closePStatement(pStmt);
			closeConnection(con);
		}
		
	}
	
	private void proDicLib(){
		
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection("jdbc:sqlserver://10.40.41.157:1433;databaseName=DicLib;user=sa;password=!QAZ2wsx;sendStringParametersAsUnicode=true;");
			//條件化查詢
			StringBuffer strDicLibSql = new StringBuffer(
				" Select * "
				+ " From dic_lib_data_unpro "
				+ " Where 1=1 "
			);
			log.debug("strDicLibSql: "+strDicLibSql);	
			stmt = con.createStatement();
			rs = stmt.executeQuery(strDicLibSql.toString());
			LinkedList<Map<String, String>> dicLibRsListMap = new LinkedList<Map<String, String>>();
			while(rs.next()){
				Map<String, String> thisRsMap = new HashMap<String, String>();
				thisRsMap.put("src_lng_str", rs.getString("src_lng_str"));
				thisRsMap.put("tgt_lng_str", rs.getString("tgt_lng_str"));
				thisRsMap.put("tgt_loc", rs.getString("tgt_loc"));
				thisRsMap.put("tgt_loc_abb", rs.getString("tgt_loc_abb"));
				dicLibRsListMap.add(thisRsMap);
			}
			
			/*Map<String, String> thisRsMap = new HashMap<String, String>();
			thisRsMap.put("tgt_loc", "US");
			thisRsMap.put("tgt_loc_abb", "en_US");
			thisRsMap.put("src_lng_str", "一對一");
			thisRsMap.put("tgt_lng_str", "One By One");
			dicLibRsListMap.add(thisRsMap);*/
			
			StringBuffer sqlInDicLibData = new StringBuffer(" Insert Into dic_lib_data (src_lng_str, src_loc, src_loc_abb, tgt_loc, tgt_loc_abb, tgt_lng_str, dic_key_id, created_user, created_date )");
			sqlInDicLibData.append(" Values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pStmt = con.prepareStatement(sqlInDicLibData.toString());
			
			int totalCount = 0;
			
			LinkedList<String> existDataKey = new LinkedList<String>();
	
			for(int rsNum=0; rsNum<dicLibRsListMap.size(); rsNum++){
				
				Map<String, String> dicLibRsMap = dicLibRsListMap.get(rsNum);
				String srcLngStr = dicLibRsMap.get("src_lng_str");
				String srcLoc = "TW";
				String srcLocAbb = "zh_TW";
				String tgtLoc = dicLibRsMap.get("tgt_loc");
				String tgtLocAbb = dicLibRsMap.get("tgt_loc_abb");
				String tgtLngStr = dicLibRsMap.get("tgt_lng_str");
					
				//1.處理來源詞彙
				LinkedList<String> proSrcChStrList = new LinkedList<String>();
				LinkedList<String> proSrcEnStrList = new LinkedList<String>();
				String srcChStr = "";
				String srcEnStr = "";
				//去除中文字中的空白
				srcLngStr = srcLngStr.replace(" ", "");
				char[] srcCharArr = null;
				if(!StringUtil.isEmptyOrSpace(srcLngStr)){
					srcCharArr = srcLngStr.toCharArray();
				}
				for (int i=0; i<srcCharArr.length; i++) {
					char thisSrcChar = srcCharArr[i];
					boolean isLastIndex = (i+1)==srcCharArr.length;
					//僅儲存中文
					if(isChinese(thisSrcChar)){
						//必須是有效的詞彙才會被存入
						if(isValidChar(String.valueOf(thisSrcChar), srcLngStr)){
							srcChStr += thisSrcChar;
						}
						//如果是最後一個，就必須把當前結果存入
						if(isLastIndex){
							srcChStr = srcChStr.trim();
							proSrcChStrList.add(srcChStr);
						}
					} else {
						//如果是英文或空白，不切割中文字，也不繼續除存當前字串
						//因為中文字於前方已去除空白，故無須擔心空白造成的影響
						if(!isAZSpace(String.valueOf(thisSrcChar))){
							//非中文、非英文項目，則表示為特殊符號，故把先前的去除空白後，存入proSrcChStrList
							if(!StringUtil.isEmptyOrSpace(srcChStr)){
								srcChStr = srcChStr.trim();
								proSrcChStrList.add(srcChStr);
							}
							srcChStr = "";
						}
					}
					//僅儲存英文和空白
					//存入proSrcEnStrList，以作為後續去除依據
					//針對英文+中文的複合字，必須取英文的部分，來做為proTgtEnStrList英文內容去除依據
					if(isAZSpace(String.valueOf(thisSrcChar))){
						srcEnStr += thisSrcChar;
						if(isLastIndex){
							srcEnStr = srcEnStr.trim();
							proSrcEnStrList.add(srcEnStr);
						}
					} else {
						//非目標[英文]項目，則把先前的去除空白後，存入proSrcEnStrList
						if(!StringUtil.isEmptyOrSpace(srcEnStr)){
							srcEnStr = srcEnStr.trim();
							proSrcEnStrList.add(srcEnStr);
						}
						srcEnStr = "";
					}
				}
				//2.處理目標詞彙
				LinkedList<String> proTgtEnStrList = new LinkedList<String>();
				String tgtEnStr = "";
				char[] tgtCharArr = null;
				if(!StringUtil.isEmptyOrSpace(tgtLngStr)){
					tgtCharArr = tgtLngStr.toCharArray();
				}
				if(null!=tgtCharArr){
					for (int i=0; i<tgtCharArr.length; i++) {
						char thisTgtChar = tgtCharArr[i];
						boolean isLastIndex = (i+1)==tgtCharArr.length;
						//僅儲存英文和空白
						if(isAZSpace(String.valueOf(thisTgtChar))){
							tgtEnStr += thisTgtChar;
							//如果是最後一個，就必須把當前結果存入
							if(isLastIndex){
								tgtEnStr = tgtEnStr.trim();
								//判斷來源字串中，英文的部分，若不存在才可放入目的字串中
								if(!proSrcEnStrList.contains(tgtEnStr)){
									//只有一個字，不存入
									if(tgtEnStr.length()>1){
										proTgtEnStrList.add(tgtEnStr);
									}
								}
							}
						} else {
							//非目標項目，則把先前的去除空白後，存入proSrcChStrList
							if(!StringUtil.isEmptyOrSpace(tgtEnStr)){
								tgtEnStr = tgtEnStr.trim();
								//判斷來源字串中，英文的部分，若不存在才可放入目的字串中
								if(!proSrcEnStrList.contains(tgtEnStr)){
									//只有一個字，不存入
									if(tgtEnStr.length()>1){
										proTgtEnStrList.add(tgtEnStr);
									}
								}
							}
							tgtEnStr = "";
						}
						//如果全部字串已走訪完成(結束)，且proTgtEnStrList有多個內容(只有一個就不管)
						if(isLastIndex && proTgtEnStrList.size()>1){
							//如果中文項目，只有一個，則打掉所有英文List，直接存為一筆
							if(proSrcChStrList.size()==1){
								String finTgtEnStr = "";
								for(int j=0; j<proTgtEnStrList.size(); j++){
									String thisTgtEnStr = proTgtEnStrList.get(j);
									finTgtEnStr += thisTgtEnStr+" ";
								}
								finTgtEnStr = finTgtEnStr.trim();
								proTgtEnStrList = new LinkedList<String>();
								proTgtEnStrList.add(finTgtEnStr);
							}
						}
					}
				}
				//3.走訪proSrcEnStrList，取得待處理的內容，將中英文重疊的部分，取代掉
				for(int i=0; i<proSrcEnStrList.size(); i++){
					String thisReplaceEnStr = proSrcEnStrList.get(i);
					//如果proTgtEnStrList字串內，存在應處理的內容，必須把他取代掉
					for(int j=0; j<proTgtEnStrList.size(); j++){
						String thisEnStr = proTgtEnStrList.get(j);
						thisEnStr = thisEnStr.replace(thisReplaceEnStr, "");
						//去除連續兩個的空白
						thisEnStr = thisEnStr.replace("  ", " ");
						thisEnStr = thisEnStr.trim();
						proTgtEnStrList.remove(j);
						proTgtEnStrList.addFirst(thisEnStr);
					}
				}
				//TODO:srcLngStr太多，就不翻譯
				//4.如果有任何一個中文字過多，則一整個List都不要翻譯
				if(!StringUtil.isEmptyOrSpace(proSrcChStrList)){
					for(int i=0; i<proSrcChStrList.size(); i++){
						if(proSrcChStrList.get(i).length()>15){
							proSrcChStrList = new LinkedList<String>();
						}
					}
				}
				String nowDatetime = sdFormat.format(new Date());
				if(proSrcChStrList.size()==proTgtEnStrList.size()){
					log.debug("=========");
					log.debug("org ch:"+srcLngStr);
					log.debug("org en:"+tgtLngStr);
					for(int i=0; i<proSrcChStrList.size();i++){
						String thisMappedSrcChStr = proSrcChStrList.get(i);
						String thisMappedTgtEnStr = proTgtEnStrList.get(i);
						log.debug("ch:"+thisMappedSrcChStr);
						log.debug("en:"+thisMappedTgtEnStr);
						if(!StringUtil.isEmptyOrSpace(thisMappedSrcChStr)&&!StringUtil.isEmptyOrSpace(thisMappedTgtEnStr)){
							String dataKey = thisMappedSrcChStr+"@@"+thisMappedTgtEnStr;
							String dicKeyId = UUID.randomUUID().toString();
							//針對不重複的資料進行記錄
							if(!existDataKey.contains(dataKey)){
								pStmt.setObject(1, thisMappedSrcChStr);
								pStmt.setObject(2, srcLoc);
								pStmt.setObject(3, srcLocAbb);
								pStmt.setObject(4, tgtLoc);
								pStmt.setObject(5, tgtLocAbb);
								pStmt.setObject(6, thisMappedTgtEnStr);
								pStmt.setObject(7, dicKeyId);
								pStmt.setObject(8, "12");
								pStmt.setObject(9, nowDatetime);
								pStmt.addBatch();
								existDataKey.add(dataKey);
								totalCount++;
							}
						}
					}
					log.debug("=========");
				}
			}
			log.debug("totalCount:"+totalCount);
			String nowDatetime = sdFormat.format(new Date());
			log.debug("Start Time:"+nowDatetime);
			try{
				int[] commitCounts = pStmt.executeBatch();
				log.debug("proDicLib commitCounts:"+commitCounts.length);
			}
			catch (BatchUpdateException bue) {
				int[] commitCounts = bue.getUpdateCounts();
				 for (int i = 0; i < commitCounts.length; i++) {
			    	 int commitIndex = commitCounts[i];
			    	 if(commitIndex!=1){
			    		// log.debug(debugLists.get(i));
			    	 }
			     }
				 log.debug(bue.getMessage());
			}
			pStmt.clearBatch();
			nowDatetime = sdFormat.format(new Date());
			log.debug("End Time:"+nowDatetime);

		
		} catch (Exception e) {
			log.error("Controller proDicLib Error", e);
			//throw e;
		}  finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closePStatement(pStmt);
			closeConnection(con);
		}
		
	}
	
	/*
	 * InfoObject Utils
	 * */
	//1.取得物件於CMS中的位置資訊(資料夾路徑)
	private Map<String, String> getObjPath(int objType, IInfoStore infostore, IInternalInfoObject infoObj) throws Exception{
		Map<String, String> objPathMap =  new HashMap<String, String>();
		try{
			String thisParentCuid = infoObj.getParentCUID();
			String objPath = "";
			if(objType==1){
				String strQueryParent = "Select * from CI_APPOBJECTS Where SI_CUID='"+thisParentCuid+"'";
				IInternalInfoObject pObj = (IInternalInfoObject) infostore.query(strQueryParent).get(0);
				IFolder ifolder = (IFolder)pObj;
				if(ifolder.getPath()!= null){
					String path[] = ifolder.getPath();
					for(int fi=0; fi<path.length; fi++){
						if("語意層".equalsIgnoreCase(path[fi])){
							objPath = CmsResourceService.UNIVERSES_ROOT + "/" + objPath;
							break;
						}
						if("Root Folder 95".equalsIgnoreCase(path[fi])){
							objPath = CmsResourceService.UNIVERSES_ROOT;
							break;
						}
						objPath = path[fi] + "/" + objPath;
					}
				}
				if(!"語意層".equalsIgnoreCase(ifolder.getTitle())){
					objPath = objPath + ifolder.getTitle() + "/" + infoObj.getTitle();
				} else {
					objPath = objPath + "/" + infoObj.getTitle();
				}
			} else if(objType==2){
				String strQueryParent = "Select * From ci_infoobjects Where SI_CUID = '"+thisParentCuid+"' ";
				IInternalInfoObject pObj = (IInternalInfoObject) infostore.query(strQueryParent).get(0);
				IFolder ifolder = (IFolder)pObj;
				if(ifolder.getPath()!= null){
					String path[] = ifolder.getPath();
					for(int fi=0; fi<path.length; fi++){
						objPath = path[fi] + "/" + objPath;
					}
				}
				objPath = "/" + objPath + ifolder.getTitle() + "/" + infoObj.getTitle() + ".wid";
			}
			objPathMap.put(infoObj.getCUID(), objPath);
		} catch(Exception e) {
			log.error("Controller getObjPath Error", e);
			//throw e;
		}
		return objPathMap;
	}
	//2.取得實體InfoObjects
	private IInfoObjects getCmsInfoObjs(int objType, boolean isCustom, LinkedList<String> cValList) throws Exception{
		IInfoObjects InfoObjects = null;
		IEnterpriseSession enterpriseSession = null;
		try{
			//BoPlatformApiService boApi = new BoPlatformApiService();
			//JSONObject paramObj = new JSONObject();
			enterpriseSession = getEnterpriseSession();

			ISecuritySession userSession = ((IEnterpriseSessionSecurity) enterpriseSession).getSecuritySession();
			IInfoStore infostore = (IInfoStore) InfoStoreFactory.getFactory().makeOCCA("", userSession);
			
			String strQuery = "";
			if(objType==1){
				//BI6Test
				//strQuery = "Select * From ci_appobjects Where SI_SPECIFIC_KIND = 'DSL.Universe' and SI_CUID = 'AYTz09BA0lZMrorhEYlCXng' ";
				//Real
				strQuery = "Select * From ci_appobjects Where SI_SPECIFIC_KIND = 'DSL.Universe' ";
				if(isCustom){
					strQuery += " and SI_CUID In ( ";
					for(int i=0; i<cValList.size(); i++){
						strQuery += " '"+cValList.get(i)+"', ";
					}
					strQuery = strQuery.substring(0, strQuery.lastIndexOf(","));
					strQuery += " ) ";
				}
			} else if(objType==2){
				//BI7Test
				//strQuery = "Select * From ci_infoobjects Where SI_KIND='Webi' and SI_CUID = 'AfaTBmh4lJJCo9vYvDBCG_g' ";
				//BI6Test
				//strQuery = "Select * From ci_infoobjects Where SI_KIND='Webi' and SI_CUID = 'AQAKCqdGrS5PsNduXQkgLhQ' ";
				//Real
				strQuery = "Select * From ci_infoobjects Where SI_KIND='Webi' ";
				if(isCustom){
					strQuery += " and SI_CUID In ( ";
					for(int i=0; i<cValList.size(); i++){
						strQuery += " '"+cValList.get(i)+"', ";
					}
					strQuery = strQuery.substring(0, strQuery.lastIndexOf(","));
					strQuery += " ) ";
				}
			}
			log.debug("getCmsInfoObj strQuery:"+strQuery);
			InfoObjects = (IInfoObjects) infostore.query(strQuery);
		} catch(Exception e) {
			log.error("Controller getCmsInfoObjs Error", e);
			throw e;
		} finally {
			if(null!=enterpriseSession)
				enterpriseSession.logoff();
		}
		return InfoObjects;
	}
	//3.將InfoObject轉換為ITMgrEngine，如此才可應用於後續的解析
	private Map<String, ITMgrEngine> getCmsEngine(IInfoObjects infoObjs, Map<IInternalInfoObject, String> allUnxPath) throws Exception{
		Map<String, ITMgrEngine> engineMap = new HashMap<String, ITMgrEngine>();
		IEnterpriseSession enterpriseSession = null;
		try{
			//BoPlatformApiService boApi = new BoPlatformApiService();
			//JSONObject paramObj = new JSONObject();
			//paramObj.put(BusinessObjectConst.BO_TRUSTED_FILE_FOLDER_PATH, InitServlet.TRUST_FILE_PATH);
			//paramObj.put("hostName", hostName);
			//paramObj.put("userName", userName);
			//enterpriseSession = boApi.getTrustedEnterpriseSession(paramObj);
			//enterpriseSession = CrystalEnterprise.getSessionMgr().logon("Administrator",sourcePwd,sourceServer,"secEnterprise");
			enterpriseSession = CrystalEnterprise.getSessionMgr().logon("Administrator","Digiwin11","10.40.41.41","secEnterprise");
			TranslationSDKManager manager = new TranslationSDKManager(enterpriseSession);
			//Webi
			int reLoginNum = 0;
			log.debug("infoObjs Size:"+infoObjs.size());
			if(null!=infoObjs&&null==allUnxPath){
				for(int ifoNum=0; ifoNum<infoObjs.size(); ifoNum++){
					IInternalInfoObject infoObj = (IInternalInfoObject) infoObjs.get(ifoNum);
					try{
						log.debug("Now ifoNum:"+ifoNum);
						InputStream translations = manager.extractTranslations(infoObj.getCUID());
						engineMap.put(infoObj.getCUID(), ITMgrEngine.Factory.createInstance(getTmgrDoc(translations,infoObj,1)));
						reLoginNum = 0;
					} catch(TranslationException e) {
						log.debug("error infoObj:"+infoObj.getCUID()+"；Name:"+infoObj.getTitle());
						log.debug(e);
						if(e.toString().contains("org.apache.axis2.AxisFault")){
							if(reLoginNum<51){
								enterpriseSession.logoff();
								enterpriseSession = CrystalEnterprise.getSessionMgr().logon("Administrator","Digiwin11","10.40.41.41","secEnterprise");
								manager = new TranslationSDKManager(enterpriseSession);
								reLoginNum++;
								log.debug("reLogin...");
								log.debug("reLogin Times:"+reLoginNum);
								ifoNum--;
							}
						}
					}
				}
			//Unx
			} else {
				Map<String, String> allUnxFilePath = retrieveAllUnxSave(allUnxPath);
				for(String infoObjCuid : allUnxFilePath.keySet()){
					String thisUnxPath = allUnxFilePath.get(infoObjCuid);
					File filepath = new File(thisUnxPath);
					log.debug("["+filepath.exists()+"]"+"thisUnxPath:"+thisUnxPath);
					engineMap.put(infoObjCuid, ITMgrEngine.Factory.createInstance(manager.loadArtifact(TranslatableEntity.BUSINESSLAYER, filepath)));
				}
			}
		} catch(TranslationException e) {
			log.error("Controller getCmsEngine getErrorCode:", e.getErrorCode());
			log.error("Controller getCmsEngine Error", e);
		} finally {
			if(null!=enterpriseSession)
				enterpriseSession.logoff();
		}
		return engineMap;
	}
	//4.解析物件語系資料
	private Map<String, LinkedList<String>> getObjDtlData(IEntityInfo thisInfo, Locale[] locales) throws Exception{
		Map<String, LinkedList<String>> localObjValMap = new HashMap<String, LinkedList<String>>();
		try{
			for(int i=0; i<locales.length; i++){
				Locale thisLocale = locales[i];
				String thisLocaleName = "org";
				if(null!=thisLocale){
					thisLocaleName = thisLocale.getLanguage()+"_"+thisLocale.getCountry();
				}
				LinkedList<String> thisLocalObjValList = new LinkedList<String>();
				if(null!=thisInfo.getValueDatas(thisLocale)){
					IValueData[] ivdatas = thisInfo.getValueDatas(thisLocale);
					for(int k=0; k<ivdatas.length; k++){
						if(!StringUtil.isEmptyOrSpace(ivdatas[k].getValue())){
							thisLocalObjValList.add(ivdatas[k].getValue());
						}
					}
				}
				localObjValMap.put(thisLocaleName, thisLocalObjValList);
			}
		} catch (Exception e) {
			log.error("Controller getObjDtlData Error", e);
			throw e;
		}
		return localObjValMap;
	}
	
	private Map<String, LinkedList<String>> getObjDtlDataB(IEntityInfo thisInfo, Object thisEntity, Locale[] locales) throws Exception{
		Map<String, LinkedList<String>> localObjValMap = new HashMap<String, LinkedList<String>>();
		try{
			ITMgrPropertyInfo[] pInfos = thisInfo.getLocalizedProperties();
			for(int i=0; i<locales.length; i++){
				Locale thisLocale = locales[i];
				String thisLocaleName = "org";
				if(null!=thisLocale){
					thisLocaleName = thisLocale.getLanguage()+"_"+thisLocale.getCountry();
				}
				LinkedList<String> thisLocalObjValList = new LinkedList<String>();
				for(int j=0; j<pInfos.length; j++){
					if(!StringUtil.isEmptyOrSpace(pInfos[j].getValue(thisEntity, thisLocale))){
						thisLocalObjValList.add(pInfos[j].getValue(thisEntity, thisLocale));
					}
				}
				localObjValMap.put(thisLocaleName, thisLocalObjValList);
			}
		} catch (Exception e) {
			log.error("Controller getObjDtlDataB Error", e);
			throw e;
		}
		return localObjValMap;
	}
	
	/*
	 * Dictionary Utils
	 * */
	//1.取得字典庫內所有資料
	private Map<String, Map<String, String>> getDicLibData() throws Exception{
		
		Map<String, Map<String, String>> dicLibDataMap = new HashMap<String, Map<String, String>>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			StringBuffer strSql = new StringBuffer(
				" Select * "
				+ " From dic_lib_data "
				+ " Where 1=1 "
			);
			//log.debug("strSql: "+strSql);	
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection("jdbc:sqlserver://"+dbServer+":1433;databaseName="+dbName+";user="+dbUser+";password="+dbPwd+";sendStringParametersAsUnicode=true;");	
			stmt = con.createStatement();
			rs = stmt.executeQuery(strSql.toString());
			while(rs.next()){
				Map<String, String> thisMap = new HashMap<String, String>();
				thisMap.put("seq_id", rs.getString("seq_id"));
				thisMap.put("src_lng_str", rs.getString("src_lng_str"));
				thisMap.put("src_loc", rs.getString("src_loc"));
				thisMap.put("src_loc_abb", rs.getString("src_loc_abb"));
				thisMap.put("tgt_loc", rs.getString("tgt_loc"));
				thisMap.put("tgt_loc_abb", rs.getString("tgt_loc_abb"));
				thisMap.put("tgt_lng_str", rs.getString("tgt_lng_str"));
				thisMap.put("dic_key_id", rs.getString("dic_key_id"));
				thisMap.put("multi_trans_id", rs.getString("multi_trans_id"));
				dicLibDataMap.put(rs.getString("src_lng_str")+"@@"+rs.getString("tgt_loc")+"@@"+rs.getString("tgt_loc_abb"), thisMap);
			}	
		} catch (Exception e) {
			log.error("Controller getDicLibData Error", e);
			throw e;
		}  finally {
			closeResultSet(rs);
			closeStatement(stmt);
			closeConnection(con);
		}
		return dicLibDataMap;
	}
	//2.判斷是否為中文
	private boolean isChinese(char c){
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);     
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS){
			return true;
        }  
        return false;
    }
	//3.判斷是否為有效語句
	private boolean isValidChar(String c, String str){
		if(!filterChList.contains(c)){
			return true;
		} else {
			int thisCharCount = 0;
			for(int i=0; i<str.length(); i++){
				if(c.equalsIgnoreCase(String.valueOf(str.charAt(i)))){
					thisCharCount++;
				}
			}
			if(thisCharCount>1){
				return true;
			}
		}
		return false;
	}
	//4.判斷是否由A`X或空白組成
	private boolean isAZSpace(String c){
		String regexAZ = "[a-zA-Z]";
		if(c.matches(regexAZ)||StringUtil.isEmptyOrSpace(c)){
			return true;
		}
		if("'".equalsIgnoreCase(c)){
			return true;
		}
		return false;
	}
	//5.斷句處理
	private LinkedList<String> breakStr(String bStr) throws Exception{
		LinkedList<String> breakStrList = new LinkedList<String>();
		try{
			if(!StringUtil.isEmptyOrSpace(bStr)){
				//1.處理來源詞彙
				String chStr = "";	
				//2.去除文字中的空白
				bStr = bStr.replace(" ", "");
				char[] bStrCharArr = null;
				//3.轉換為個別字串
				if(!StringUtil.isEmptyOrSpace(bStr)){
					bStrCharArr = bStr.toCharArray();
				}
				//4.走訪字串，並評估是否去除
				for (int i=0; i<bStrCharArr.length; i++) {
					char thisSrcChar = bStrCharArr[i];
					boolean isLastIndex = (i+1)==bStrCharArr.length;
					//僅儲存中文
					if(isChinese(thisSrcChar)){
						//必須是有效的詞彙才會被存入
						if(isValidChar(String.valueOf(thisSrcChar), chStr)){
							chStr += thisSrcChar;
						}
						//如果是最後一個，就必須把當前結果存入
						if(isLastIndex){
							chStr = chStr.trim();
							breakStrList.add(chStr);
						}
					} else {
						//如果是英文或空白，不切割中文字，也不繼續除存當前字串
						//因為中文字於前方已去除空白，故無須擔心空白造成的影響
						if(!isAZSpace(String.valueOf(thisSrcChar))){
							//非中文、非英文項目，則表示為特殊符號，故把先前的去除空白後，存入proSrcChStrList
							if(!StringUtil.isEmptyOrSpace(chStr)){
								chStr = chStr.trim();
								breakStrList.add(chStr);
							}
							chStr = "";
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Controller breakStr Error", e);
			throw e;
		}
		return breakStrList;
	}
	
	
	/*
	 * Unx Utils
	 * */
	//1.取得所有Unx於CMS中的路徑
	private Map<IInternalInfoObject, String> getAllUnxPath(IInfoObjects infoObjs) throws Exception{
		Map<IInternalInfoObject, String> allUnxPathMap = new HashMap<IInternalInfoObject, String>();
		IEnterpriseSession enterpriseSession = null;
		try{
			enterpriseSession = getEnterpriseSession();
			ISecuritySession userSession = ((IEnterpriseSessionSecurity) enterpriseSession).getSecuritySession();
			IInfoStore infostore = (IInfoStore) InfoStoreFactory.getFactory().makeOCCA("", userSession);
			for(int ifoNum=0; ifoNum<infoObjs.size(); ifoNum++){
				IInternalInfoObject infoObj = (IInternalInfoObject) infoObjs.get(ifoNum);
				Map<String, String> objPathMap = getObjPath(1, infostore, infoObj);
				allUnxPathMap.put(infoObj, objPathMap.get(infoObj.getCUID()));
			}
			
		} catch(Exception e) {
			log.error("Controller getAllUnxPath Error", e);
			throw e;
		} finally {
			if(null!=enterpriseSession)
				enterpriseSession.logoff();
		}
		return allUnxPathMap;
	}
	//2.擷取Unx至本機，並取得於本機中的檔案錄鏡
	private Map<String, String> retrieveAllUnxSave(Map<IInternalInfoObject, String> allUnxPathMap) throws Exception{
		Map<String, String> localUnxPathMap = new HashMap<String, String>();
		SlContext context = null;
		IEnterpriseSession enterpriseSession = null;	
		try{
			enterpriseSession = getEnterpriseSession();
			context = SlContext.create();
			context.getService(CmsSessionService.class).setSession(enterpriseSession);
			CmsResourceService service = context.getService(CmsResourceService.class);
			for(IInternalInfoObject infoObj : allUnxPathMap.keySet()){
				try{
					Thread.sleep(1000);
					String path = service.retrieveUniverse(allUnxPathMap.get(infoObj), unxTempPath, true);
					localUnxPathMap.put(infoObj.getCUID(), path);
				} catch (Exception e) {
					log.error("error infoObj:"+infoObj.getCUID()+"；Name:"+infoObj.getTitle());
					throw e;
				}
			}
			 //Gets the LocalResourceService.class service
			 //LocalResourceService service = context.getService(LocalResourceService.class);
		} catch (Exception e) {
			log.error("Controller retrieveAllUnxSave Error", e);
			//throw e;
		} finally {
			if(null!=enterpriseSession)
				enterpriseSession.logoff();
			if(null!=context)
				context.close();
		}
		return localUnxPathMap;
	}
	
	/*
	 * Webi Utils
	 * */
	//1.將InfoObject寫入成為tmgr檔案，並儲存於本機端
	private static ITMgrDocument getTmgrDoc(InputStream inputstream, IInfoObject infoObj, int objProMode) throws TranslationException	{
		ITMgrDocument tmgrdoc = null;
		TranslationSDKManager manager = new TranslationSDKManager();
		try {
			log.debug("Start Loading: "+infoObj.getTitle()+"_"+infoObj.getCUID()+ ".tmgr file under "+webiTempPath);
			String fullFilePath = webiTempPath+infoObj.getTitle()+"_"+infoObj.getCUID()+".tmgr";
			File fileInPath = new File(fullFilePath);
			//處理模式1：當已存在時，不再重新擷取
			if(objProMode==1){
				if(!fileInPath.exists()){
					log.debug("Start Creating tmgr...");
					FileOutputStream os = new FileOutputStream(webiTempPath+infoObj.getTitle()+"_"+infoObj.getCUID()+".tmgr");
					byte[] buffer = new byte[1024];
					int bytesRead;
					//read from is to buffer
					while((bytesRead = inputstream.read(buffer)) !=-1){
						os.write(buffer, 0, bytesRead);
					}
					//flush OutputStream to write any buffered data to file
					os.flush();
					os.close();
					log.debug("Created Success");
				}
			} else if(objProMode==2){
			//處理模式2：無論有無存在，都重新擷取
				log.debug("Start Creating tmgr...");
				FileOutputStream os = new FileOutputStream(webiTempPath+infoObj.getTitle()+"_"+infoObj.getCUID()+".tmgr");
				byte[] buffer = new byte[1024];
				int bytesRead;
				//read from is to buffer
				while((bytesRead = inputstream.read(buffer)) !=-1){
					os.write(buffer, 0, bytesRead);
				}
				//flush OutputStream to write any buffered data to file
				os.flush();
				os.close();
				log.debug("Created Success");
			}
			//Gets the Translation Manager Document
			tmgrdoc = manager.loadArtifact(TranslatableEntity.TRANSMGR,new File(webiTempPath+infoObj.getTitle()+"_"+infoObj.getCUID()+".tmgr"));
			log.debug("Loaded Success");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (java.io.IOException e1) {
			e1.printStackTrace();
		} catch (SDKException e) {
			e.printStackTrace();
		}
		return tmgrdoc;
	}
	//2.Webi必須實做export動作，才可更新物件
	private void exportToCms(ITMgrEngine engine, String objCuid) throws Exception{
		IEnterpriseSession enterpriseSession = null;
		try{
			enterpriseSession = getEnterpriseSession();
			TranslationSDKManager manager = new TranslationSDKManager(enterpriseSession);
			ByteArrayOutputStream tmgrStream = new ByteArrayOutputStream();
			FileDocumentFactory.exportToTMgr(engine,tmgrStream);
			InputStream saveResult = manager.saveTranslations(objCuid,tmgrStream,true);
			log.debug("exportToCms cuid is:" + objCuid);
			TMgrMergeReport mergeResult = new TMgrMergeReport(saveResult);
			if(mergeResult.hasConflict()){
				System.out.println("Conflict has occurred");
			} else {
				System.out.println("Translations are saved");
			}
		} catch (Exception e) {
			log.error("Error Cuid:"+objCuid);
			log.error("Controller exportToCms Error", e);
			//throw e;
		}  finally {
			if(null!=enterpriseSession)
				enterpriseSession.logoff();
		}
	}

	/*
	 * Comn Tools
	 * */
	//1.若不存在路徑時，自動建立路徑資料夾
	private void createTempFolder(String tgtTempPath){
		File tempFolder = new File(tgtTempPath.substring(0, tgtTempPath.lastIndexOf("\\")));
    	if(!tempFolder.exists()){
    		tempFolder.mkdirs();
    	}
	}
	//2.取得getEnterpriseSession
	private IEnterpriseSession getEnterpriseSession() throws Exception{
		IEnterpriseSession enterpriseSession = null;
		try{
			//BoPlatformApiService boApi = new BoPlatformApiService();
			//JSONObject paramObj = new JSONObject();
			//paramObj.put(BusinessObjectConst.BO_TRUSTED_FILE_FOLDER_PATH, InitServlet.TRUST_FILE_PATH);
			//paramObj.put("hostName", hostName);
			//paramObj.put("userName", userName);
			//enterpriseSession = boApi.getTrustedEnterpriseSession(paramObj);
			enterpriseSession = CrystalEnterprise.getSessionMgr().logon("Administrator",sourcePwd,sourceServer,"secEnterprise");
		} catch (Exception e) {
			log.error("Controller getCmsInfoObjs Error", e);
			throw e;
		}
		return enterpriseSession;
	}

	private static void closeConnection(Connection pConn) {
		if (pConn != null) {
			try {
				pConn.close();
				pConn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				rs=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
	
	private static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
				stmt=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	

	private static void closePStatement(PreparedStatement pStmt) {
		if (pStmt != null) {
			try {
				pStmt.close();
				pStmt=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/*
	 * 測試或註解程式
	 * */
	@RequestMapping(value="/test", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String testJSON (@RequestBody String data) throws Exception {	
		JSONObject loadMainObjToDbJSONObject = new JSONObject();
		try{
			//LinkedList<String> allUnxPathList = getAllUnxPath();
			//retrieveAllUnxSave(allUnxPathList);
			//retrieveAllUnxSave(null);
			loadMainObjToDbJSONObject.put("success", "Y");
			return loadMainObjToDbJSONObject.toString();		
		} catch(Exception e) {
			loadMainObjToDbJSONObject.put("success", "N");
			log.error("Controller loadMainObjToDb Error", e);
			throw e;
		}
	}
	
//	private void loadObjMapToDicDb(LinkedList<Map<String, String>> tMapList) throws Exception{
//	
//	Connection con = null;
//	PreparedStatement pStmtMain = null;
//	PreparedStatement pStmtSub = null;
//	Statement stmt = null;
//	ResultSet rs = null;
//	
//	try{
//		
//		con = DriverManager.getConnection("jdbc:sqlserver://10.40.41.157:1433;databaseName=DicLib;user=sa;password=!QAZ2wsx;sendStringParametersAsUnicode=true;");
//		
//		//條件化查詢
//		StringBuffer strObjInfoSql = new StringBuffer(
//			" Select * "
//			+ " From obj_info "
//			+ " Where 1=1 "
//		);
//		Map<String, LinkedList<String>> tColValMapList = new HashMap<String, LinkedList<String>>();
//		for(int i=0; i<tMapList.size(); i++){
//			Map<String, String> tMap = tMapList.get(i);
//			for(String colName : tMap.keySet()){
//				LinkedList<String> tColValList = new LinkedList<String>();
//				if(tColValMapList.containsKey(colName)){
//					tColValList = tColValMapList.get(colName);
//				}
//				tColValList.add(tMap.get(colName));
//				tColValMapList.put(colName, tColValList);
//			}
//		}
//		for(String colName : tColValMapList.keySet()){
//			LinkedList<String> tColValList = tColValMapList.get(colName);
//			strObjInfoSql.append(" And "+colName+" In ( ");
//			for(int i=0; i<tColValList.size(); i++){
//				strObjInfoSql.append(tColValList.get(i));
//				if((i+1)!=tColValList.size()){
//					strObjInfoSql.append(", ");
//				}
//			}
//			strObjInfoSql.append(" ) ");
//		}
//		log.debug("strObjInfoSql: "+strObjInfoSql);	
//		stmt = con.createStatement();
//		rs = stmt.executeQuery(strObjInfoSql.toString());
//		//取得obj_info內的DB資料
//		LinkedList<Map<String, String>> dbObjInfoListMap = new LinkedList<Map<String, String>>();
//		Map<String, LinkedList<Map<String, String>>> repeatMapList = new HashMap<String, LinkedList<Map<String, String>>>();
//		while(rs.next()){
//			String src_bi_ver = rs.getString("src_bi_ver");
//			String obj_id = rs.getString("obj_id");
//			String obj_type = rs.getString("obj_type");
//			String obj_path = rs.getString("obj_path");
//			String dtl_key = rs.getString("dtl_key");
//			String dtl_type = rs.getString("dtl_type");
//			String dtl_type_class = rs.getString("dtl_type_class");
//			String src_lng_str = rs.getString("src_lng_str");
//			String src_loc = rs.getString("src_loc");
//			String src_loc_abb = rs.getString("src_loc_abb");
//			String tgt_loc = rs.getString("tgt_loc");
//			String tgt_loc_abb = rs.getString("tgt_loc_abb");
//			String tgt_lng_str = rs.getString("tgt_lng_str");	
//			
//			Map<String, String> dbObjInfoMap = new HashMap<String, String>();
//			dbObjInfoMap.put("src_bi_ver", src_bi_ver);
//			dbObjInfoMap.put("obj_id", obj_id);
//			dbObjInfoMap.put("obj_type", obj_type);
//			dbObjInfoMap.put("obj_path", obj_path);
//			dbObjInfoMap.put("dtl_key", dtl_key);
//			dbObjInfoMap.put("dtl_type", dtl_type);
//			dbObjInfoMap.put("dtl_type_class", dtl_type_class);
//			dbObjInfoMap.put("src_lng_str", src_lng_str);
//			dbObjInfoMap.put("src_loc", src_loc);
//			dbObjInfoMap.put("src_loc_abb", src_loc_abb);
//			dbObjInfoMap.put("tgt_loc", tgt_loc);
//			dbObjInfoMap.put("tgt_loc_abb", tgt_loc_abb);
//			dbObjInfoMap.put("tgt_lng_str", tgt_lng_str);
//			dbObjInfoListMap.add(dbObjInfoMap);
//			
//			String repMapKey = "@@"+src_lng_str+"_@@"+tgt_lng_str;
//			if(repeatMapList.containsKey(repMapKey)){
//				LinkedList<Map<String, String>> repeatList = repeatMapList.get(repMapKey);
//			}
//		}
//		
//		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		StringBuffer sqlInDicLibData = new StringBuffer(" Insert Into dic_lib_data (src_lng_str, src_loc, src_loc_abb, tgt_loc, tgt_loc_abb, tgt_lng_str, created_user, created_date )");
//		sqlInDicLibData.append(" Values (?, ?, ?, ?, ?, ?, ?, ?)");
//		pStmtMain = con.prepareStatement(sqlInDicLibData.toString());
//		
//		StringBuffer sqlInDicLibDataRep = new StringBuffer(" Insert Into dic_lib_data_rep (map_id, rep_num, src_lng_str, src_loc, src_loc_abb, tgt_loc, tgt_loc_abb, tgt_lng_str, seq_id, created_user, created_date )");
//		sqlInDicLibDataRep.append(" Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//		pStmtSub = con.prepareStatement(sqlInDicLibData.toString());
//		
//		//走訪物件對應表的資料
//		for(int i=0; i<dbObjInfoListMap.size(); i++){
//			Map<String, String> dbObjMap = dbObjInfoListMap.get(i);
//			
//			String map_id = UUID.randomUUID().toString();
//			String nowDatetime = sdFormat.format(new Date());
//			
//			Map<String, String> dicOrgExistMap = getDicOrgExist(dbObjMap);
//			if(StringUtil.isEmptyOrSpace(dicOrgExistMap)){
//				//沒有重複的資料，才能放進字典庫
//				pStmtMain.setObject(1, dbObjMap.get("src_lng_str"));
//				pStmtMain.setObject(2, dbObjMap.get("src_loc"));
//				pStmtMain.setObject(3, dbObjMap.get("src_loc_abb"));
//				pStmtMain.setObject(4, dbObjMap.get("tgt_loc"));
//				pStmtMain.setObject(5, dbObjMap.get("tgt_loc_abb"));
//				pStmtMain.setObject(6, dbObjMap.get("tgt_lng_str"));
//				pStmtMain.setObject(7, "12");
//				pStmtMain.setObject(8, nowDatetime);
//				pStmtMain.addBatch();
//			} else {
//				//org於dic_lib_data內已有資料
//				//如果存在map_id，代表org於dic_lib_data_rep，也存在相同資料
//				//rep_num預設為1
//				int rep_num = 1;
//				if(dicOrgExistMap.containsKey("tgtStrExistRep")){
//					//有map_id，代表str是沒有重複的
//					//沒有重複才要存，有重複則不管
//					if(dicOrgExistMap.containsKey("map_id")){
//						//因為dic_lib_data_rep已有資料，必須用既有id進行Mapping
//						//rep_num必須取出來再+1
//						map_id = dicOrgExistMap.get("map_id");
//						rep_num = Integer.valueOf(dicOrgExistMap.get("rep_num"))+1;
//						pStmtSub.setObject(1, map_id);
//						pStmtSub.setObject(2, rep_num);
//						pStmtSub.setObject(3, dbObjMap.get("src_lng_str"));
//						pStmtSub.setObject(4, dbObjMap.get("src_loc"));
//						pStmtSub.setObject(5, dbObjMap.get("src_loc_abb"));
//						pStmtSub.setObject(6, dbObjMap.get("tgt_loc"));
//						pStmtSub.setObject(7, dbObjMap.get("tgt_loc_abb"));
//						pStmtSub.setObject(8, dbObjMap.get("tgt_lng_str"));
//						pStmtSub.setObject(9, dicOrgExistMap.get("seq_id"));
//						pStmtSub.setObject(10, "12");
//						pStmtSub.setObject(11, nowDatetime);
//						pStmtSub.addBatch();
//					}
//				} else {
//					//dic_lib_data_rep完全不存在重複資料，直接
//					//取得重複資訊的seq_id，並將現有資訊填入DB
//					pStmtSub.setObject(1, map_id);
//					pStmtSub.setObject(2, rep_num);
//					pStmtSub.setObject(3, dbObjMap.get("src_lng_str"));
//					pStmtSub.setObject(4, dbObjMap.get("src_loc"));
//					pStmtSub.setObject(5, dbObjMap.get("src_loc_abb"));
//					pStmtSub.setObject(6, dbObjMap.get("tgt_loc"));
//					pStmtSub.setObject(7, dbObjMap.get("tgt_loc_abb"));
//					pStmtSub.setObject(8, dbObjMap.get("tgt_lng_str"));
//					pStmtSub.setObject(9, dicOrgExistMap.get("seq_id"));
//					pStmtSub.setObject(10, "12");
//					pStmtSub.setObject(11, nowDatetime);
//					pStmtSub.addBatch();
//				}
//			}
//		}
//	} catch (Exception e) {
//		log.error("Controller loadSubObjToDb Error", e);
//		//throw e;
//	}  finally {
//		closePStatement(pStmtMain);
//		closePStatement(pStmtSub);
//		closeResultSet(rs);
//		closeStatement(stmt);
//		closeConnection(con);
//	}
//}
	//檢查org是否已存在於字典庫
//		private Map<String, String> getDicOrgExist(Map<String, String> tMap) throws Exception{
//			
//			Map<String, String> existDataMap = new HashMap<String, String>();
//			
//			Connection con = null;
//			Statement stmt = null;
//			ResultSet rs = null;
//			
//			try{
//				
//				con = DriverManager.getConnection("jdbc:sqlserver://"+dbServer+":1433;databaseName="+dbName+";user="+dbUser+";password="+dbPwd+";sendStringParametersAsUnicode=true;");	
//				
//				//查詢dic_lib_data內，目標org是否已存在目標語系的資料
//				StringBuffer strSqlDicLibData = new StringBuffer(
//					" Select * "
//					+ " From dic_lib_data "
//					+ " Where 1=1 "
//				);
//				if(!StringUtil.isEmptyOrSpace(tMap)){
//					strSqlDicLibData.append(" And src_lng_str = '"+tMap.get("src_lng_str")+"' ");
//					strSqlDicLibData.append(" And src_loc = '"+tMap.get("src_loc")+"' ");
//					strSqlDicLibData.append(" And src_loc_abb = '"+tMap.get("src_loc_abb")+"' ");
//					strSqlDicLibData.append(" And tgt_loc = '"+tMap.get("tgt_loc")+"' ");
//					strSqlDicLibData.append(" And tgt_loc_abb = '"+tMap.get("tgt_loc_abb")+"' ");
//				} else {
//					log.error("Controller getDicOrgExist Error: tMap is null");
//					return null;
//				}
//				log.debug("strSqlDicLibData: "+strSqlDicLibData);	
//				stmt = con.createStatement();
//				rs = stmt.executeQuery(strSqlDicLibData.toString());
//				if(rs.next()){
//					existDataMap.put("seq_id", rs.getString("seq_id"));
//					existDataMap.put("src_lng_str", rs.getString("src_lng_str"));
//					existDataMap.put("src_loc", rs.getString("src_loc"));
//					existDataMap.put("src_loc_abb", rs.getString("src_loc_abb"));
//					existDataMap.put("tgt_loc", rs.getString("tgt_loc"));
//					existDataMap.put("tgt_loc_abb", rs.getString("tgt_loc_abb"));
//					existDataMap.put("tgt_lng_str", rs.getString("tgt_lng_str"));
//				}
//				//如果目標語系資料已存在
//				if(!StringUtil.isEmptyOrSpace(existDataMap)){
//					existDataMap.put("tgtStrExistRep", "Y");
//					//查詢dic_lib_data_rep內，目標org是否已存在目標語系資料
//					StringBuffer strSqlDicLibDataRep = new StringBuffer(
//						" Select * "
//						+ " From dic_lib_data_rep "
//						+ " Where 1=1 "
//					);
//					strSqlDicLibDataRep.append(" And src_lng_str = '"+existDataMap.get("src_lng_str")+"' ");
//					strSqlDicLibDataRep.append(" And src_loc = '"+existDataMap.get("src_loc")+"' ");
//					strSqlDicLibDataRep.append(" And src_loc_abb = '"+existDataMap.get("src_loc_abb")+"' ");
//					strSqlDicLibDataRep.append(" And tgt_loc = '"+existDataMap.get("tgt_loc")+"' ");
//					strSqlDicLibDataRep.append(" And tgt_loc_abb = '"+existDataMap.get("tgt_loc_abb")+"' ");
//					log.debug("strSqlDicLibDataRep: "+strSqlDicLibDataRep);	
//					stmt = con.createStatement();
//					rs = stmt.executeQuery(strSqlDicLibDataRep.toString());
//					//如果已存在目標語系資料，則判斷DB內，是否已儲存對應的翻譯文字
//					while(rs.next()){
//						//如果翻譯後的文字，是不同的，將儲存map_id，以定義，後續mapping方式
//						if(!existDataMap.get("tgt_lng_str").equalsIgnoreCase(rs.getString("tgt_lng_str"))){
//							//map_id一定會是相同的
//							existDataMap.put("map_id", rs.getString("map_id"));
//							//rep_num用於得知目前已存數量
//							existDataMap.put("rep_num", rs.getString("rep_num"));
//						}
//					}
//				}
//			} catch (Exception e) {
//				log.error("Controller getDicOrgExist Error", e);
//				throw e;
//			} finally {
//				if(rs != null){
//					try { rs.close();} catch (SQLException e1) { }
//				}
//				if(stmt != null){
//					try { stmt.close();} catch (SQLException e1) { }
//				}
//				
//				if(con != null){
//					try { con.close();} catch (SQLException e1) { }
//				}
//			}
//			return existDataMap;
//		}
	
	
}

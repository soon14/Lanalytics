package com.ai.analy.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.IRecommendGoodsService;
import com.ai.analy.utils.CacheUtil;
import com.ai.analy.utils.CommonUtils;
import com.ai.analy.utils.DataConvertUtil;
import com.ai.analy.utils.DateUtils;
import com.ai.analy.vo.recommendgds.GdsAndBrandIdVO;
import com.ai.analy.vo.recommendgds.GdsRelationDegreeVO;
import com.ai.analy.vo.recommendgds.Goods4RecomVO;
import com.ai.analy.vo.recommendgds.GoodsBrandVO;
import com.ai.analy.vo.recommendgds.PageInfo;
import com.ai.analy.vo.recommendgds.UserGdsRelationDegreeVO;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RecommendGoodsServiceImpl extends BaseDao implements IRecommendGoodsService{
    
    private static final Logger log = Logger.getLogger(RecommendGoodsServiceImpl.class);
    
    /**单品与单品相关关系，在redis中的key*/
//    private static final String REDIS_KEY_GDS_MATRIX="GDS_MATRIX";
    
    /**猜你喜欢接口，默认推荐10个单品*/
    private static final int DEFAULT_GDS_NUM=10;
    
    /**商品推荐接口，默认推荐10个单品*/
    private static final int DEFAULT_RELATED_GDS_NUM=20;
    
    private static final String DEFAULT_FORMAT="JSON";

    /**用户00000000是一个全局用户， 用来做默认用户ID*/
    private static final String DEFAULT_USERID="00000000";
    
    /**用户-商品、商品、商品 关系存放在redis的n库*/
    private static final int CACHE_DB_INDEX=4;
    
    /**分隔符号：冒号:*/
    private static final String SEPARATOR_COLON=":";
    
    /**猜你喜欢推荐商品列表缓存时间：120分钟*/
    private static final int RECOMM_GDS_CACHE_MINUTES=120;
    
    /**服务响应状态：0000-正常*/
    private static final String SERVICE_STATE_0000="0000";
    
    /**
     * 根据用户id获取推荐商品列表
     * @param userId    用户的ID
     * @param format    返回数据的格式。默认JSON,目前仅支持JSON格式。
     * @param gdsNumStr 返回商品数量。默认返回10个商品。
     * @param willQueryCatgList 是否要查询商品分类
     * @return
     * @author liangyi
     */
    @Override
    public String recommendGoods(String userId,String format,String gdsNumStr,boolean willQueryCatgList){
        String resJson = null;
        
        try {
            Map<String,Object> responseMsg=null;
            //校验参数
            if (format==null) {
				format=DEFAULT_FORMAT;
			}
            responseMsg=validParam(userId, format, gdsNumStr);
            if (responseMsg!=null) {
                return CommonUtils.toJson(responseMsg);
            }
            
            //先从缓存中获取数据,若缓存数据是两个小时内的，则取缓存中的商品列表返回
    	    String resultKeyInRedis="RECOMM_GDS_"+userId+"_"+format+"_"+gdsNumStr;
    	    resJson=getRecomGdsJsonFromCache(resultKeyInRedis);
    	    if (resJson!=null) {
				return resJson;
			}
            
            //需要查询出来的商品
            int gdsNum=gdsNumStr!=null?Integer.valueOf(gdsNumStr):DEFAULT_GDS_NUM;
            
            List<GdsRelationDegreeVO> gdsRelationResultList=recommendGoodsList(userId, gdsNum);
            
            if (gdsRelationResultList.size()<gdsNum) {
				//列表不足的，用默认用户的计算结果数据补充。
            	gdsNum=gdsNum-gdsRelationResultList.size();
            	List<GdsRelationDegreeVO> buchongGdsRelationResultList=recommendGoodsList(DEFAULT_USERID, gdsNum);
            	gdsRelationResultList.addAll(buchongGdsRelationResultList);
			}
            
            //从数据库查询出推荐的单品的信息
            GdsAndBrandIdVO gdsAndBrandIdVO=getGoods4RecomVOs(gdsRelationResultList);
            
            //推荐的单品信息列表
            List<Goods4RecomVO> goods4RecomList=gdsAndBrandIdVO.getGoods4RecomList();
            
            //把所有信息放到map中，转成json
            responseMsg=new HashMap<>();
            responseMsg.put("serviceState", "0000");
            responseMsg.put("serviceMsg", "正常");
            responseMsg.put("userId", userId);
            responseMsg.put("itemCount", goods4RecomList.size());
            responseMsg.put("goodsList", goods4RecomList);
            
            //查询推荐的商品分类
            if (willQueryCatgList) {
            	List<GoodsBrandVO> catgList=getCatgList(gdsAndBrandIdVO.getBrandIdsInStr());
            	responseMsg.put("catgList", catgList);
			}
            
            resJson=CommonUtils.toJson(responseMsg);
            
            //把返回的json存入缓存
            addRecomGdsJsonToCache(resultKeyInRedis, resJson);
            
        } catch (Exception e) {
        	log.error("根据用户id获取推荐商品列表异常", e);
            resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
        }
        
        return resJson;
    }
    
    /**
     * 根据用户id获取推荐商品列表。分页查询
     * @param userId	用户的ID
     * @param format	返回数据的格式。默认JSON,目前仅支持JSON格式。
     * @param gdsNumStr	查询商品总个数。默认100，最大5000。
     * @param willQueryCatgList	是否要查询商品分类
     * @param pageNoStr	分页查询时所查询的页数。
     * @param pageSizeStr	分页查询时每页显示的数据条数。
     * @return
     */
    public String recommendGoodsPaging(String userId,String format,String gdsNumStr,boolean willQueryCatgList,String pageNoStr,String pageSizeStr){
    	
    	String resJson=null;
    	
    	int gdsNum=0;
    	try {
    		gdsNum=Integer.valueOf(gdsNumStr);
		} catch (NumberFormatException e) {
			gdsNum=100;
		}
    	if (gdsNum>5000) {
    		gdsNum=5000;
		}
    	
    	int pageNo=0;
    	try {
    		pageNo=Integer.valueOf(pageNoStr);
		} catch (NumberFormatException e) {
			pageNo=1;
		}
    	
    	int pageSize=0;
    	try {
    		pageSize=Integer.valueOf(pageSizeStr);
		} catch (NumberFormatException e) {
			pageSize=10;
		}
    	
    	try {
    		//先获取包含所有推荐商品的json
        	String allGdsJson=recommendGoods(userId, format, ""+gdsNum, false);
        	
        	if (StringUtils.isBlank(allGdsJson)) {
    			resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
    			return resJson;
    		}
        	
        	//若serviceState不是0000-正常，则直接返回
        	JSONObject jsonObject=JSONObject.parseObject(allGdsJson);
        	
        	String serviceState=jsonObject.getString("serviceState");
        	if (!SERVICE_STATE_0000.equals(serviceState)) {
    			return allGdsJson;
    		}
        	
        	//解析goodsList
        	JSONArray goodsList = jsonObject.getJSONArray("goodsList");
        	if (goodsList==null||goodsList.size()<=0) {
        		Map<String,Object> responseMsg=getEmpeyRespMapForPagingQuery(userId, pageNo, pageSize);
                resJson=CommonUtils.toJson(responseMsg);
                return resJson;
    		}
        	
        	//解析allGdsJson，根据分页获取商品，再组成json
        	int goodsListSize=goodsList.size();
        	
        	int fromIndex = (pageNo-1)*pageSize;
        	int toIndex = pageNo*pageSize;
        	
        	if (fromIndex>goodsListSize) {
        		Map<String,Object> responseMsg=getEmpeyRespMapForPagingQuery(userId, pageNo, pageSize);
                resJson=CommonUtils.toJson(responseMsg);
                return resJson;
			}
        	if (toIndex>goodsListSize) {
        		toIndex=goodsListSize;
			}
        	
        	List<Object> subGoodsList = goodsList.subList(fromIndex, toIndex);
        	
        	Map<String,Object> responseMsg=new HashMap<>();
            responseMsg.put("serviceState", "0000");
            responseMsg.put("serviceMsg", "正常");
            responseMsg.put("userId", userId);
            responseMsg.put("itemCount", subGoodsList.size());
            responseMsg.put("goodsList", subGoodsList);
            responseMsg.put("pageInfo", new PageInfo(goodsListSize, pageNo, pageSize));
        	
            resJson=CommonUtils.toJson(responseMsg);
		} catch (Exception e) {
        	log.error("根据用户id分页获取推荐商品列表异常", e);
            resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
        }
    	
    	return resJson;
    	
    }
    
    private Map<String,Object> getEmpeyRespMapForPagingQuery(String userId,int pageNo,int pageSize){
    	Map<String,Object> responseMsg=new HashMap<>();
        responseMsg.put("serviceState", "0000");
        responseMsg.put("serviceMsg", "正常");
        responseMsg.put("userId", userId);
        responseMsg.put("itemCount", 0);
        responseMsg.put("goodsList", Collections.emptyList());
        responseMsg.put("pageInfo", new PageInfo(0, pageNo, pageSize));
        
        return responseMsg;
    }
    
    /**
     * 校验参数
     * @param userId
     * @param format
     * @param gdsNumStr
     * @return
     */
    protected Map<String, Object> validParam(String userId,String format,String gdsNumStr){
        if (userId==null) {
            return CommonUtils.buildAErrorMsgMap("1001","参数校验错误");
        }
        if (format!=null&&!DEFAULT_FORMAT.equalsIgnoreCase(format)) {
            return CommonUtils.buildAErrorMsgMap("1001","参数校验错误");
        }
        if (gdsNumStr!=null&&!CommonUtils.isNumeric(gdsNumStr)) {
            return CommonUtils.buildAErrorMsgMap("1001","参数校验错误");
        }
        return null;
    }
    
    
    /**
     * 从redis中取推荐商品json。若json是在2小时内，则取redis中的json返回
     * @param resultKeyInRedis
     * @return
     */
    private String getRecomGdsJsonFromCache(String resultKeyInRedis){
    	
    	try {
    		String cacheTimeStr=CacheUtil.getItem(CACHE_DB_INDEX, resultKeyInRedis, "TIME");
    	    if (cacheTimeStr!=null) {
    			//判断时间是否在两个小时内
    	    	Timestamp cacehTime=DateUtils.getTimestamp(cacheTimeStr, DateUtils.YYYYMMDDHHMMSS);
    	    	Timestamp nMinutesEarlier=DateUtils.getNMinutes(-RECOMM_GDS_CACHE_MINUTES);
    	    	
    	    	if (cacehTime.after(nMinutesEarlier)) {
    				//如果缓存还在两小时内，则从缓存中获取商品列表返回
    	    		return CacheUtil.getItem(CACHE_DB_INDEX, resultKeyInRedis, "JSON");
    			}
    		}
		} catch (Exception e) {
			log.error("从redis取商品推荐json异常", e);
		}
    	
	    return null;
    }
    
    /**
     * 把商品推荐json存入redis
     * @param resultKeyInRedis
     * @param resJson
     */
    private void addRecomGdsJsonToCache(String resultKeyInRedis,String resJson){
    	try {
    		//把返回的json存入缓存
            CacheUtil.addItem(CACHE_DB_INDEX, resultKeyInRedis, "JSON", resJson);
            //把当前时间存入缓存
            CacheUtil.addItem(CACHE_DB_INDEX, resultKeyInRedis, "TIME", DateUtils.getDateString(DateUtils.YYYYMMDDHHMMSS));
		} catch (Exception e) {
			log.error("把商品推荐json存入redis异常", e);
		}
    }
    
    /**
     * 获取推荐给用户的商品id
     * @param userId 用户id
     * @param gdsNum 查询商品的个数
     * @return
     * @throws Exception
     */
    private List<GdsRelationDegreeVO> recommendGoodsList(String userId,int gdsNum) throws Exception{
    	 //从redis中获取  用户->行为商品列表
        List<UserGdsRelationDegreeVO> userGdsList=getUserGdsList(userId);
        
        //获取用户已购买的商品
    	Map<String, Object> buyGdsOfUserMap=getBuyGdsOfUserMap(userId);
    	
    	//积分商城单品id集合
    	List<String> jifenSkuIds = getJifenMallSiteSkuIds();
        
        //从redis获取推荐给用户的商品列表
        List<GdsRelationDegreeVO> gdsRelationResultList=getGdsRelationResultList(buyGdsOfUserMap, jifenSkuIds, userGdsList, gdsNum);
        
        return gdsRelationResultList;
    }
    
    /**
     * 从redis中获取用户对应商品的列表
     * @param userId
     * @return
     */
    private List<UserGdsRelationDegreeVO> getUserGdsList(String userId){
    	//用户向量 USERID:用户id   
    	//例如USERID:10000   =>   277196:182.777,276932:28.940,70246:19.840,67446:11.263,
    	
    	String key="USERID:"+userId;
    	String userGdsStr=CacheUtil.getItem(CACHE_DB_INDEX,key);
    	
    	if (userGdsStr==null) {
			return new ArrayList<>();
		}
    	
    	String[] skuIdDegrees=userGdsStr.split(",");
    	List<UserGdsRelationDegreeVO> resultList=new ArrayList<>(skuIdDegrees.length);
    	
    	for (String skuIdDegree : skuIdDegrees) {
    		
    		if (StringUtils.isBlank(skuIdDegree)) {
				continue;
			}
    		
			int index=skuIdDegree.indexOf(SEPARATOR_COLON);
			String skuId=skuIdDegree.substring(0,index);
	        String degreeStr=skuIdDegree.substring(index+1,skuIdDegree.length());
	        double degree=0;
	        try {
	        	degree=Double.valueOf(degreeStr);
			} catch (Exception e) {
				log.error("转换用户--商品权值为double类型出错，degreeStr="+degreeStr, e);
			}
	        
	        UserGdsRelationDegreeVO vo=new UserGdsRelationDegreeVO();
	        vo.setUserId(userId);
	        vo.setSrcSkuId(skuId);
	        vo.setUserDegree(degree);
	        
	        resultList.add(vo);
		}
    	
    	return resultList;
    	
    }
    
    /**
     * 获取用户已购买的商品
     */
    private Map<String, Object> getBuyGdsOfUserMap(String userId){
    	Map<String, Object> buyGdsOfUserMap=new HashMap<>();
    	
    	String key="ORDERED:"+userId;
    	String buyGdsStr=CacheUtil.getItem(CACHE_DB_INDEX,key);
    	
    	if (buyGdsStr==null) {
			return null;
		}
    	
    	String[] buySkuIds=buyGdsStr.split(",");//已购买的商品
    	
    	for (String buySkuIdDegree : buySkuIds) {
    		
    		if (StringUtils.isBlank(buySkuIdDegree)) {
				continue;
			}
    		
    		String buySkuId=null;
    		
    		int index=buySkuIdDegree.indexOf(SEPARATOR_COLON);
    		
    		if (index<=0) {
    			buySkuId=buySkuIdDegree;
			}else {
				buySkuId=buySkuIdDegree.substring(0,index);
			}
    		
    		buyGdsOfUserMap.put(buySkuId, buySkuId);
		}
    	
    	return buyGdsOfUserMap;
    }
    
    private List<GdsRelationDegreeVO> getGdsRelationResultList(Map<String, Object> buyGdsOfUserMap, List<String> jifenSkuIds, List<UserGdsRelationDegreeVO> userGdsList,int gdsNum) throws Exception{
    	
    	//给用户推荐的单品map：存入map中去重
        Map<String, GdsRelationDegreeVO> recommendGoodsMap=new HashMap<>();
        
        //从redis中找到各个行为商品的相关商品
        for (UserGdsRelationDegreeVO userGds : userGdsList) {
        	
        	String srcSkuId=userGds.getSrcSkuId();
        	
        	//得到用户对商品列表字符串， 以逗号隔开 
        	//例如SKUSKU:10000   =>   277196:182.777,276932:28.940,70246:19.840,67446:11.263,
        	String gdsToGdsStr=CacheUtil.getItem(CACHE_DB_INDEX,"SKUSKU:"+srcSkuId);
    		if (StringUtils.isBlank(gdsToGdsStr)) {
    			continue;
    		}
        	
    		//解析
        	String[] skuIdDegrees=gdsToGdsStr.split(",");
        	
        	for (String skuIdDegree : skuIdDegrees) {
        		
        		if (StringUtils.isBlank(skuIdDegree)) {
    				continue;
    			}
        		
    			int index=skuIdDegree.indexOf(SEPARATOR_COLON);
    			String targetSkuId=skuIdDegree.substring(0,index);
    			
    			//过滤已购买的商品
    			if (isSrcInMap(targetSkuId, buyGdsOfUserMap)) {
    				continue;
    			}
    			
    			//过滤积分商城单品
    			if(jifenSkuIds != null && jifenSkuIds.contains(targetSkuId)){
    				continue;
    			}
    			
    	        String degreeStr=skuIdDegree.substring(index+1,skuIdDegree.length());
    	        double gdsDegree=0;
    	        try {
    	        	gdsDegree=Double.valueOf(degreeStr);
    			} catch (Exception e) {
    				log.error("转换商品--商品权值为double类型出错，degreeStr="+degreeStr, e);
    			}
    	        
    	        GdsRelationDegreeVO gdsRelation=new GdsRelationDegreeVO(userGds.getUserId(),srcSkuId, targetSkuId, gdsDegree);
                
                //计算用户与目标单品的相关度
                double userGdsDegree=userGds.getUserDegree()*gdsDegree;
                
                gdsRelation.setUserGdsDegree(userGdsDegree);
                
                
                //相同商品分数累加
				GdsRelationDegreeVO voInMap=recommendGoodsMap.get(targetSkuId);
				if (voInMap==null) {
					recommendGoodsMap.put(targetSkuId, gdsRelation);
				}else{
					//将分数累加
					double userGdsDegreeSum=voInMap.getUserGdsDegree()+gdsRelation.getGdsDegree();
					voInMap.setUserGdsDegree(userGdsDegreeSum);
				}
    		}
		}
        
        //跟用户有关系的商品列表
        List<GdsRelationDegreeVO> recommendGoodsList=new ArrayList<>(recommendGoodsMap.size());
        for (Map.Entry<String, GdsRelationDegreeVO> entry: recommendGoodsMap.entrySet()) {
        	recommendGoodsList.add(entry.getValue());
		}
        //按分数高低排序，返回
        Collections.sort(recommendGoodsList);
        
        //给用户推荐的单品列表
        List<GdsRelationDegreeVO> resultList=null;
        
        if (gdsNum<recommendGoodsMap.size()) {
			//找出相关度最大的几个单品
        	resultList=recommendGoodsList.subList(0, gdsNum);
		}else {
			resultList=recommendGoodsList;
		}
        
        return resultList;
    }
    
    /**
     * 从缓存中获取商品相关度，并计算用于与推荐商品的相关度
     * @param srcSkuId
     * @return
     * @throws Exception
     */

    private String getSkuIdsForQuery(List<GdsRelationDegreeVO> gdsRelationList){
    	StringBuilder sbBuilder=new StringBuilder();
    	for (GdsRelationDegreeVO gdsRelationDegreeVO : gdsRelationList) {
    		sbBuilder.append(gdsRelationDegreeVO.getTargetSkuId()).append(",");
		}
    	sbBuilder.deleteCharAt(sbBuilder.length()-1);
    	return sbBuilder.toString();
    }
    
    protected GdsAndBrandIdVO getGoods4RecomVOs(List<GdsRelationDegreeVO> gdsRelationList){
    	
    	if (gdsRelationList==null||gdsRelationList.size()<=0) {
			return GdsAndBrandIdVO.emptyVO();
		}
    	
    	String  skuIdsForQuery=getSkuIdsForQuery(gdsRelationList);
        
        StringBuilder sqlBuilder=new StringBuilder();
        sqlBuilder.append("SELECT gds_id as gdsId,sku_name as gdsName,pic_id as mainPicId, brand_id as brandId,")
        .append("status as gdsStatus,sku_id as skuId, agent_price as guidePrice,sku_prop as skuProps")
        .append(" FROM t_goods_sku ")
        .append(" where sku_id in (").append(skuIdsForQuery).append(")");
        
        String sql=sqlBuilder.toString();
        log.info(sql);
        
        List<Map<String, Object>> queryResultList= getJdbcTemplate().queryForList(sql);
        if (queryResultList==null||queryResultList.size()<=0) {
        	return GdsAndBrandIdVO.emptyVO();
		}
        
        StringBuilder brandIds=new StringBuilder();
        List<Goods4RecomVO> goods4RecomList=new ArrayList<>(queryResultList.size());
        Map<String, Goods4RecomVO> goods4RecomMap=new HashMap<>();
        
        for (Map<String, Object> dataMap : queryResultList) {
        	Goods4RecomVO goods4RecomVO=new Goods4RecomVO();
			
			String gdsId = (String) dataMap.get("gdsId");
			String gdsName = (String) dataMap.get("gdsName");     
			String mainPicId = (String) dataMap.get("mainPicId");   
			Integer gdsStatus = (Integer)dataMap.get("gdsStatus");
			String skuId = (String) dataMap.get("skuId");
			Long guidePrice=(Long) dataMap.get("guidePrice");
			
			if (gdsId==null||guidePrice==null) {
				continue;
			}
			
			String skuPropsStr = (String) dataMap.get("skuProps");//商品属性，要转为map
			
			List<Map<String,String>> skuProps=DataConvertUtil.ConvertProps(skuPropsStr);
			
			goods4RecomVO.setGdsId(CommonUtils.trimToEmpty(gdsId));
			goods4RecomVO.setGdsName(CommonUtils.trimToEmpty(gdsName));
			goods4RecomVO.setMainPicId(CommonUtils.trimToEmpty(mainPicId));
			goods4RecomVO.setGdsStatus(""+gdsStatus);
			goods4RecomVO.setSkuId(CommonUtils.trimToEmpty(skuId));
			goods4RecomVO.setGuidePrice(guidePrice);
			goods4RecomVO.setSkuProps(skuProps);
			
			goods4RecomMap.put(skuId, goods4RecomVO);
			
			String brandId = (String) dataMap.get("brandId");
			if (StringUtils.isNotEmpty(brandId)) {
				brandIds.append(brandId).append(",");
			}
		}
        
        if (brandIds.length()>0&&brandIds.lastIndexOf(",")==brandIds.length()-1) {
            brandIds.deleteCharAt(brandIds.length()-1);
        }
        
        //把goods4RecomList按GdsRelationDegreeVO的userGdsDegree大到小排序
        for (GdsRelationDegreeVO gdsRelationDegreeVO : gdsRelationList) {
        	//gdsRelationList是经过排序的
        	Goods4RecomVO goods4RecomVO=goods4RecomMap.get(gdsRelationDegreeVO.getTargetSkuId());
        	if (goods4RecomVO==null) {
				continue;
			}
        	goods4RecomList.add(goods4RecomVO);
		}
        
        
        GdsAndBrandIdVO result=new GdsAndBrandIdVO(goods4RecomList, brandIds.toString());
        return result;
    }
    
    private List<GoodsBrandVO> getCatgList(String brandIdsInStr) throws Exception{
    	if (StringUtils.isBlank(brandIdsInStr)) {
			return Collections.emptyList();
		}
    	
    	//查询推荐的分类
    	StringBuilder sqlBuilder=new StringBuilder();
    	sqlBuilder.append("select  b1.brand_id as catgId3,b1.brand_name as catgName3, ");
    	sqlBuilder.append("bb.catgId2,bb.catgName2, ");
    	sqlBuilder.append("bb.catgId1,bb.catgName1 ");
    	sqlBuilder.append("from t_goods_brand b1 ");
    	sqlBuilder.append("left join ");
    	sqlBuilder.append("(select b2.brand_id as catgId2,b2.brand_name as catgName2,b3.brand_id as catgId1,b3.brand_name as catgName1 ");
    	sqlBuilder.append("from t_goods_brand b2 left join t_goods_brand b3 on b2.catg_parent=b3.brand_id) bb ");
    	sqlBuilder.append("on b1.catg_parent=bb.catgId2 ");
    	sqlBuilder.append("where  b1.brand_id in ");
    	sqlBuilder.append("(");
    	sqlBuilder.append(brandIdsInStr);
    	sqlBuilder.append(")");
    	
    	String sql=sqlBuilder.toString();
    	
    	log.info(sql);
    	
    	List<GoodsBrandVO> resultList=queryList(sql, null, new RowMapper<GoodsBrandVO>() {
    		
			@Override
			public GoodsBrandVO mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				GoodsBrandVO vo=new GoodsBrandVO();
				
				String catgId1 = rs.getString("catgId1");
				String catgName1 = rs.getString("catgName1");
				String catgId2 = rs.getString("catgId2");
				String catgName2 = rs.getString("catgName2");
				String catgId3 = rs.getString("catgId3");
				String catgName3 = rs.getString("catgName3");
				
				vo.setCatgId1(CommonUtils.trimToEmpty(catgId1));
				vo.setCatgId2(CommonUtils.trimToEmpty(catgId2));
				vo.setCatgId3(CommonUtils.trimToEmpty(catgId3));
				vo.setCatgName1(CommonUtils.trimToEmpty(catgName1));
				vo.setCatgName2(CommonUtils.trimToEmpty(catgName2));
				vo.setCatgName3(CommonUtils.trimToEmpty(catgName3));
				
				return vo;
			}
		});
    	
    	if (resultList==null) {
    		resultList=Collections.emptyList();
		}
        
    	return resultList;
    }
    
    private boolean isSrcInMap(String src,Map<String,Object> map){
    	if (map!=null&&map.get(src)!=null) {
			return true;
		}
    	return false;
    }
    
    /**
     * 查询积分商城单品id集合
     * @return
     */
    private List<String> getJifenMallSiteSkuIds(){
    	String sql = "select sku_id from t_goods_sku"
    			+" where catlog_id in(select catlog_id from t_gds_catlog2site where site_id = 2)";
    	
    	List<String> skuIds = queryList(sql, null, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getString("sku_id");
			}
		});
    	
    	return skuIds;
    }
    
    
    /**
     * 商品推荐接口
     * @param skuId    	源单品id
     * @param userId    用户的ID
     * @param skuNumStr 返回商品数量。默认返回20个商品。
     * @return
     * @author liangyi
     */
    @Override
    public String recommendRelatedGds(String skuId,String userId,String skuNumStr){
        String resJson = null;
        
        try {
            Map<String,Object> responseMsg=null;
            //校验参数
            if (StringUtils.isBlank(skuId)) {
            	return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1001","参数校验错误"));
            }
            if (skuNumStr!=null&&!CommonUtils.isNumeric(skuNumStr)) {
                return CommonUtils.toJson(CommonUtils.buildAErrorMsgMap("1001","参数校验错误"));
            }
            
            //先从缓存中获取数据,若缓存数据是两个小时内的，则取缓存中的商品列表返回
    	    String resultKeyInRedis="RECOMM_RELATED_GDS_"+skuId+"_"+userId+"_"+skuNumStr;
    	    resJson=getRecomGdsJsonFromCache(resultKeyInRedis);
    	    if (resJson!=null) {
				return resJson;
			}
            
            //需要查询出来的商品
            int gdsNum=skuNumStr!=null?Integer.valueOf(skuNumStr):DEFAULT_RELATED_GDS_NUM;
            
            //先从redis中查出单品相关的单品
        	String srcSkuId=skuId;
        	List<GdsRelationDegreeVO> gdsRelationResultList=getSku2SkuList(srcSkuId,gdsNum);
            
            //如果不够，再用userId查猜你喜欢接口，去补
            if (gdsRelationResultList.size()<gdsNum&&StringUtils.isNotEmpty(userId)) {
        		gdsNum=gdsNum-gdsRelationResultList.size();
            	List<GdsRelationDegreeVO> buChongGdsRelationResultList=recommendGoodsList(userId, gdsNum);
            	gdsRelationResultList.addAll(buChongGdsRelationResultList);
			}
            
            //列表还是不足的，用默认用户的计算结果数据补充。
            if (gdsRelationResultList.size()<gdsNum) {
            	gdsNum=gdsNum-gdsRelationResultList.size();
            	List<GdsRelationDegreeVO> buchongGdsRelationResultList=recommendGoodsList(DEFAULT_USERID, gdsNum);
            	gdsRelationResultList.addAll(buchongGdsRelationResultList);
			}
            
            //从数据库查询出推荐的单品的信息
            GdsAndBrandIdVO gdsAndBrandIdVO=getGoods4RecomVOs(gdsRelationResultList);
            
            //推荐的单品信息列表
            List<Goods4RecomVO> goods4RecomList=gdsAndBrandIdVO.getGoods4RecomList();
            
            //把所有信息放到map中，转成json
            responseMsg=new HashMap<>();
            responseMsg.put("serviceState", "0000");
            responseMsg.put("serviceMsg", "正常");
            responseMsg.put("srcSkuId", srcSkuId);
            responseMsg.put("itemCount", goods4RecomList.size());
            responseMsg.put("goodsList", goods4RecomList);
            
            resJson=CommonUtils.toJson(responseMsg);
            
            //把返回的json存入缓存
            addRecomRelatedGdsJsonToCache(resultKeyInRedis, resJson);
            
        } catch (Exception e) {
        	log.error("根据用户id获取推荐商品列表异常", e);
            resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
        }
        
        return resJson;
    }
    
    /**
     * 把商品推荐json存入redis
     * @param resultKeyInRedis
     * @param resJson
     */
    private void addRecomRelatedGdsJsonToCache(String resultKeyInRedis,String resJson){
    	try {
    		//把返回的json存入缓存
            CacheUtil.addItem(CACHE_DB_INDEX, resultKeyInRedis, "JSON", resJson);
            //把当前时间存入缓存
            CacheUtil.addItem(CACHE_DB_INDEX, resultKeyInRedis, "TIME", DateUtils.getDateString(DateUtils.YYYYMMDDHHMMSS));
		} catch (Exception e) {
			log.error("把商品推荐json存入redis异常", e);
		}
    }
    
    private List<GdsRelationDegreeVO> getSku2SkuList(String srcSkuId,int gdsNum){
    	//得到商品对商品列表字符串， 以逗号隔开 
    	//例如SKUSKU:10000   =>   277196:182.777,276932:28.940,70246:19.840,67446:11.263,
    	String gdsToGdsStr=CacheUtil.getItem(CACHE_DB_INDEX,"SKUSKU:"+srcSkuId);
		if (StringUtils.isBlank(gdsToGdsStr)) {
			return new ArrayList<GdsRelationDegreeVO>(gdsNum);
		}
    	
		//解析
    	String[] skuIdDegrees=gdsToGdsStr.split(",");
    	
    	List<GdsRelationDegreeVO> relatedGdsList=new ArrayList<>(gdsNum);
    	for (String skuIdDegree : skuIdDegrees) {
    		
    		if (StringUtils.isBlank(skuIdDegree)) {
				continue;
			}
    		
			int index=skuIdDegree.indexOf(SEPARATOR_COLON);
			String targetSkuId=skuIdDegree.substring(0,index);
			
	        String degreeStr=skuIdDegree.substring(index+1,skuIdDegree.length());
	        double gdsDegree=0;
	        try {
	        	gdsDegree=Double.valueOf(degreeStr);
			} catch (Exception e) {
				log.error("转换商品--商品权值为double类型出错，degreeStr="+degreeStr, e);
			}
	        
	        GdsRelationDegreeVO gdsRelation=new GdsRelationDegreeVO(null,srcSkuId, targetSkuId, gdsDegree);
	        double userGdsDegree=gdsDegree;//用于排序
            gdsRelation.setUserGdsDegree(userGdsDegree);
            
            relatedGdsList.add(gdsRelation);
            
            if (relatedGdsList.size()>=gdsNum) {
				break;
			}
		}
    	
    	Collections.sort(relatedGdsList);
    	return relatedGdsList;
    }
    
    /**
     * 根据skuId获取推荐商品列表。分页查询
     * @param skuId
     * @param userId
     * @param skuNumStr
     * @param pageNoStr
     * @param pageSizeStr
     * @return
     * @author liangyi
     */
    @Override
    public String recommendRelatedGdsPaging(String skuId,String userId,String skuNumStr,String pageNoStr,String pageSizeStr){
    	
    	String resJson=null;
    	
    	int gdsNum=0;
    	try {
    		gdsNum=Integer.valueOf(skuNumStr);
		} catch (NumberFormatException e) {
			gdsNum=100;
		}
    	if (gdsNum>5000) {
    		gdsNum=5000;
		}
    	
    	int pageNo=0;
    	try {
    		pageNo=Integer.valueOf(pageNoStr);
		} catch (NumberFormatException e) {
			pageNo=1;
		}
    	
    	int pageSize=0;
    	try {
    		pageSize=Integer.valueOf(pageSizeStr);
		} catch (NumberFormatException e) {
			pageSize=10;
		}
    	
    	try {
    		//先获取包含所有推荐商品的json
        	String allGdsJson=recommendRelatedGds(skuId, userId, ""+gdsNum);
        	
        	if (StringUtils.isBlank(allGdsJson)) {
    			resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
    			return resJson;
    		}
        	
        	//若serviceState不是0000-正常，则直接返回
        	JSONObject jsonObject=JSONObject.parseObject(allGdsJson);
        	
        	String serviceState=jsonObject.getString("serviceState");
        	if (!SERVICE_STATE_0000.equals(serviceState)) {
    			return allGdsJson;
    		}
        	
        	//解析goodsList
        	JSONArray goodsList = jsonObject.getJSONArray("goodsList");
        	if (goodsList==null||goodsList.size()<=0) {
        		Map<String,Object> responseMsg=new HashMap<>();
                responseMsg.put("serviceState", "0000");
                responseMsg.put("serviceMsg", "正常");
                responseMsg.put("srcSkuId", skuId);
                responseMsg.put("itemCount", 0);
                responseMsg.put("goodsList", Collections.emptyList());
                responseMsg.put("pageInfo", new PageInfo(0, pageNo, pageSize));
        		
        		resJson=CommonUtils.toJson(responseMsg);
                return resJson;
    		}
        	
        	//解析allGdsJson，根据分页获取商品，再组成json
        	int goodsListSize=goodsList.size();
        	
        	int fromIndex = (pageNo-1)*pageSize;
        	int toIndex = pageNo*pageSize;
        	
        	if (fromIndex>goodsListSize) {
        		Map<String,Object> responseMsg=new HashMap<>();
                responseMsg.put("serviceState", "0000");
                responseMsg.put("serviceMsg", "正常");
                responseMsg.put("srcSkuId", skuId);
                responseMsg.put("itemCount", 0);
                responseMsg.put("goodsList", Collections.emptyList());
                responseMsg.put("pageInfo", new PageInfo(0, pageNo, pageSize));
        		
                resJson=CommonUtils.toJson(responseMsg);
                return resJson;
			}
        	if (toIndex>goodsListSize) {
        		toIndex=goodsListSize;
			}
        	
        	List<Object> subGoodsList = goodsList.subList(fromIndex, toIndex);
        	
        	Map<String,Object> responseMsg=new HashMap<>();
            responseMsg.put("serviceState", "0000");
            responseMsg.put("serviceMsg", "正常");
            responseMsg.put("srcSkuId", skuId);
            responseMsg.put("itemCount", subGoodsList.size());
            responseMsg.put("goodsList", subGoodsList);
            responseMsg.put("pageInfo", new PageInfo(goodsListSize, pageNo, pageSize));
        	
            resJson=CommonUtils.toJson(responseMsg);
		} catch (Exception e) {
        	log.error("根据用户id分页获取推荐商品列表异常", e);
            resJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}";
        }
    	
    	return resJson;
    
    }

    
}

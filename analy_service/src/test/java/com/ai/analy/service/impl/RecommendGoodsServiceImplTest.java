package com.ai.analy.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ClassUtils;

import com.ai.analy.vo.recommendgds.GdsAndBrandIdVO;
import com.ai.analy.vo.recommendgds.GdsRelationDegreeVO;
import com.ai.analy.vo.recommendgds.Goods4RecomVO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RecommendGoodsServiceImplTest {
	
	@Before
	public void before(){
		
	}
	
	/**
	 * 
	 * 
	 * @author liangyi
	 */
	//@Test
	public void testValidParam(){
		RecommendGoodsServiceImpl svImpl=new RecommendGoodsServiceImpl();
		Map<String, Object> result=svImpl.validParam(null, null, null);
		assertNotNull(result);
		
		result=svImpl.validParam("11", null, null);
		assertNull(result);
		
		result=svImpl.validParam("11", "json", null);
		assertNull(result);
		
		result=svImpl.validParam("11", "JSON", null);
		assertNull(result);
		
		result=svImpl.validParam("11", "JSON", "AAAA");
		assertNotNull(result);
		
		result=svImpl.validParam("11", "JSON", "100");
		assertNull(result);
	}

	/**
	 * 
	 * 
	 * @author liangyi
	 */
	//@Test
	public void testGetGoods4RecomVOs() {
		
		List<GdsRelationDegreeVO> gdsRelationList=new ArrayList<>();
		GdsRelationDegreeVO vo1=new GdsRelationDegreeVO();
		vo1.setTargetSkuId("1");
		vo1.setUserGdsDegree(1);
		GdsRelationDegreeVO vo2=new GdsRelationDegreeVO();
		vo2.setTargetSkuId("2");
		vo2.setUserGdsDegree(2);
		GdsRelationDegreeVO vo3=new GdsRelationDegreeVO();
		vo3.setTargetSkuId("3");
		vo3.setUserGdsDegree(3);
		gdsRelationList.add(vo1);
		gdsRelationList.add(vo2);
		gdsRelationList.add(vo3);
		Collections.sort(gdsRelationList);
		
		RecommendGoodsServiceImpl svImpl=new RecommendGoodsServiceImpl();
		RecommendGoodsServiceImpl svImplSpy = spy(svImpl);
		
		Map<String, Object> map1=new HashMap<>();
		map1.put("gdsId", "1");
		map1.put("skuId", "1");
		map1.put("guidePrice", 1L);
		Map<String, Object> map2=new HashMap<>();
		map2.put("gdsId", "2");
		map2.put("skuId", "2");
		map2.put("guidePrice", 2L);
		Map<String, Object> map3=new HashMap<>();
		map3.put("gdsId", "3");
		map3.put("skuId", "3");
		map3.put("guidePrice", 3L);
		List<Map<String, Object>> queryResultList=new ArrayList<>();
		queryResultList.add(map1);
		queryResultList.add(map2);
		queryResultList.add(map3);
		
		JdbcTemplate jdbcTemplate=mock(JdbcTemplate.class);
		when(jdbcTemplate.queryForList(anyString())).thenReturn(queryResultList);
		when(svImplSpy.getJdbcTemplate()).thenReturn(jdbcTemplate);
		
		
		GdsAndBrandIdVO resultVO=svImplSpy.getGoods4RecomVOs(gdsRelationList);
		assertNotNull(resultVO);
		
		List<Goods4RecomVO> Goods4RecomList=resultVO.getGoods4RecomList();
		assertNotNull(Goods4RecomList);
		assertTrue(Goods4RecomList.size()==3);
		
		//验证排序
		assertTrue("3".equals(Goods4RecomList.get(0).getGdsId()));
		assertTrue("2".equals(Goods4RecomList.get(1).getGdsId()));
		assertTrue("1".equals(Goods4RecomList.get(2).getGdsId()));
		
	}
	
	/**
	 * 
	 * 
	 * @author liangyi
	 */
	//@Test
	public void testRecommendGoodsPaging(){
		
		String userId="userId";
		String format="JSON";
		String gdsNumStr="100";
		boolean willQueryCatgList=false;
		String pageNoStr="1";
		String pageSizeStr="5";
		
		RecommendGoodsServiceImpl svImpl=new RecommendGoodsServiceImpl();
		RecommendGoodsServiceImpl svImplSpy = spy(svImpl);
		
		when(svImplSpy.recommendGoods(anyString(), anyString(), anyString(), anyBoolean())).thenReturn(null);
		
		//
		String resJson=svImplSpy.recommendGoodsPaging(userId, format, gdsNumStr, willQueryCatgList, pageNoStr, pageSizeStr);
		assertTrue("{\"serviceState\":\"2001\",\"serviceMsg\":\"服务异常\"}".equals(resJson));
		
		String mockRespJson="{\"serviceState\":\"2001\",\"serviceMsg\":\"测试异常\"}";
		when(svImplSpy.recommendGoods(anyString(), anyString(), anyString(), anyBoolean())).thenReturn(mockRespJson);
		resJson=svImplSpy.recommendGoodsPaging(userId, format, gdsNumStr, willQueryCatgList, pageNoStr, pageSizeStr);
		assertTrue(mockRespJson.equals(resJson));
		
		
		//从testRecommendGoodsPaging.txt文件中的去json
		String fileName="test_data/testRecommendGoodsPaging.txt";
		mockRespJson=read(fileName);
		when(svImplSpy.recommendGoods(anyString(), anyString(), anyString(), anyBoolean())).thenReturn(mockRespJson);
		
		resJson=svImplSpy.recommendGoodsPaging(userId, format, gdsNumStr, willQueryCatgList, "1", pageSizeStr);
		try {
			JSONObject jsonObject=JSONObject.parseObject(resJson);
			String serviceState=jsonObject.getString("serviceState");
			assertTrue("0000".equals(serviceState));
			
			JSONObject pageInfo=jsonObject.getJSONObject("pageInfo");
			assertNotNull(pageInfo);
			assertTrue("1".equals(pageInfo.getString("pageNo")));
			assertTrue("5".equals(pageInfo.getString("pageSize")));
			assertTrue("10".equals(pageInfo.getString("totalCount")));
			
			JSONArray goodsList = jsonObject.getJSONArray("goodsList");
			assertTrue(goodsList!=null&&goodsList.size()==5);
			
			assertTrue("1".equals(goodsList.getJSONObject(0).getString("gdsId")));
			assertTrue("2".equals(goodsList.getJSONObject(1).getString("gdsId")));
			assertTrue("3".equals(goodsList.getJSONObject(2).getString("gdsId")));
			assertTrue("4".equals(goodsList.getJSONObject(3).getString("gdsId")));
			assertTrue("5".equals(goodsList.getJSONObject(4).getString("gdsId")));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		//第二页
		resJson=svImplSpy.recommendGoodsPaging(userId, format, gdsNumStr, willQueryCatgList, "2", pageSizeStr);
		try {
			JSONObject jsonObject=JSONObject.parseObject(resJson);
			String serviceState=jsonObject.getString("serviceState");
			assertTrue("0000".equals(serviceState));
			
			JSONObject pageInfo=jsonObject.getJSONObject("pageInfo");
			assertNotNull(pageInfo);
			assertTrue("2".equals(pageInfo.getString("pageNo")));
			assertTrue("5".equals(pageInfo.getString("pageSize")));
			assertTrue("10".equals(pageInfo.getString("totalCount")));
			
			JSONArray goodsList = jsonObject.getJSONArray("goodsList");
			assertTrue(goodsList!=null&&goodsList.size()==5);
			
			assertTrue("6".equals(goodsList.getJSONObject(0).getString("gdsId")));
			assertTrue("7".equals(goodsList.getJSONObject(1).getString("gdsId")));
			assertTrue("8".equals(goodsList.getJSONObject(2).getString("gdsId")));
			assertTrue("9".equals(goodsList.getJSONObject(3).getString("gdsId")));
			assertTrue("10".equals(goodsList.getJSONObject(4).getString("gdsId")));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		//第3页
		resJson=svImplSpy.recommendGoodsPaging(userId, format, gdsNumStr, willQueryCatgList, "3", pageSizeStr);
		try {
			JSONObject jsonObject=JSONObject.parseObject(resJson);
			String serviceState=jsonObject.getString("serviceState");
			assertTrue("0000".equals(serviceState));
			
			JSONObject pageInfo=jsonObject.getJSONObject("pageInfo");
			assertNotNull(pageInfo);
			assertTrue("3".equals(pageInfo.getString("pageNo")));
			assertTrue("5".equals(pageInfo.getString("pageSize")));
			assertTrue("10".equals(pageInfo.getString("totalCount")));
			
			JSONArray goodsList = jsonObject.getJSONArray("goodsList");
			assertTrue(goodsList!=null&&goodsList.size()==0);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		
		//按每页查询3条记录测试
		//第一页
		resJson=svImplSpy.recommendGoodsPaging(userId, format, gdsNumStr, willQueryCatgList, "1", "3");
		try {
			JSONObject jsonObject=JSONObject.parseObject(resJson);
			String serviceState=jsonObject.getString("serviceState");
			assertTrue("0000".equals(serviceState));
			
			JSONObject pageInfo=jsonObject.getJSONObject("pageInfo");
			assertNotNull(pageInfo);
			assertTrue("1".equals(pageInfo.getString("pageNo")));
			assertTrue("3".equals(pageInfo.getString("pageSize")));
			assertTrue("10".equals(pageInfo.getString("totalCount")));
			
			JSONArray goodsList = jsonObject.getJSONArray("goodsList");
			assertTrue(goodsList!=null&&goodsList.size()==3);
			
			assertTrue("1".equals(goodsList.getJSONObject(0).getString("gdsId")));
			assertTrue("2".equals(goodsList.getJSONObject(1).getString("gdsId")));
			assertTrue("3".equals(goodsList.getJSONObject(2).getString("gdsId")));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
		//第4页
		resJson=svImplSpy.recommendGoodsPaging(userId, format, gdsNumStr, willQueryCatgList, "4", "3");
		try {
			JSONObject jsonObject=JSONObject.parseObject(resJson);
			String serviceState=jsonObject.getString("serviceState");
			assertTrue("0000".equals(serviceState));
			
			JSONObject pageInfo=jsonObject.getJSONObject("pageInfo");
			assertNotNull(pageInfo);
			assertTrue("4".equals(pageInfo.getString("pageNo")));
			assertTrue("3".equals(pageInfo.getString("pageSize")));
			assertTrue("10".equals(pageInfo.getString("totalCount")));
			
			JSONArray goodsList = jsonObject.getJSONArray("goodsList");
			assertTrue(goodsList!=null&&goodsList.size()==1);
			
			assertTrue("10".equals(goodsList.getJSONObject(0).getString("gdsId")));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public static String read(String fileName) {
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        InputStream is = classLoader.getResourceAsStream(fileName);
		
		StringBuilder result = new StringBuilder();
		try {
			InputStreamReader inputStreamReader=new InputStreamReader(is);
			
			BufferedReader br = new BufferedReader(inputStreamReader);
			String temp = br.readLine();
			while (temp != null) {
				result.append(temp);
				temp = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
 


}

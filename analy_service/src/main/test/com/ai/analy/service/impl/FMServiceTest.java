package com.ai.analy.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ai.analy.constants.Constants;
import com.ai.analy.service.interfaces.FlowMapService;
import com.ai.analy.service.interfaces.GoodsAnalyService;
import com.ai.analy.service.interfaces.SearchRankingService;
import com.ai.analy.service.interfaces.UrlDefinitionService;
import com.ai.analy.service.interfaces.VisitorStructureService;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.comm.UrlDefinition;
import com.ai.analy.vo.flow.FlowMapVo;
import com.ai.analy.vo.flow.FlowNextVo;
import com.ai.analy.vo.flow.FlowSourceVo;
import com.ai.analy.vo.flow.VisitorDataInterpretVo;
import com.ai.analy.vo.flow.structure.FlowUserTypeVo;
import com.ai.analy.vo.flow.structure.StructureSectionVo;
import com.ai.analy.vo.goods.GoodsOverviewVo;
import com.ai.analy.vo.search.FlowSearchOverviewVo;
import com.ai.analy.vo.search.SearchRankingShopVo;
import com.ai.analy.vo.search.SearchRankingVo;

public class FMServiceTest {

	private static final String configFile = "analyServiceContext.xml";
	private static ClassPathXmlApplicationContext context = null;
	private static FlowMapService flowService = null;
	private static VisitorStructureService vsService = null;
	private static GoodsAnalyService gdsService = null;
	private static SearchRankingService srService = null;
	private static UrlDefinitionService urlService = null;

	public static void init() {
		context = new ClassPathXmlApplicationContext(new String[] { configFile });
		if (context != null) {
			flowService = (FlowMapService) context.getBean("flowMapService");
			vsService = (VisitorStructureService) context.getBean("visitorStructureService");
			gdsService = (GoodsAnalyService) context.getBean("goodsAnalyService");
			srService = (SearchRankingService) context.getBean("searchRankingService");
			srService = (SearchRankingService) context.getBean("searchRankingService");
			urlService = (UrlDefinitionService) context.getBean("urlDefinitionService");
		}
	}
	
	public static void testQuery() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setShopId("ZBCU4740");
		param.setDateFrom(df.parse("2014-10-15"));
		param.setDateTo(df.parse("2015-11-04"));
		
		
		List<FlowMapVo> list = flowService.queryFlowMap(param);
		System.out.println(list);
	}
	
	
	public static void testQueyFlowSource() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setShopId("0771A8");
		param.setSource(1);
		param.setDateFrom(df.parse("2014-10-15"));
		param.setDateTo(df.parse("2015-10-16"));
		param.setPageNo(1);
		param.setPageSize(100);
		
		PageBean<FlowSourceVo> list = flowService.queryFlowSource(param);
		System.out.println(list.getRecordList());
	}
	
	public static void testQueyNext() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setShopId("0771A8");
		param.setSource(1);
		param.setDateFrom(df.parse("2014-10-15"));
		param.setDateTo(df.parse("2015-10-16"));
		param.setPageNo(1);
		param.setPageSize(100);
		
		PageBean<FlowNextVo> list = flowService.queryFlowNext(param);
		System.out.println(list.getRecordList());
	}
	
	public static void testQueyFlowNext() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setShopId("0771A8");
		param.setSource(1);
		param.setPageNo(1);
		param.setPageSize(100);
		param.setDateFrom(df.parse("2014-10-15"));
		param.setDateTo(df.parse("2015-10-16"));
		
		
		PageBean<FlowNextVo> list = flowService.queryFlowNext(param);
		System.out.println(list.getRecordList());
	}
	
	
	public static void testGetSumUV() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setShopId("0771A8");
		param.setSource(1);
		param.setDateFrom(df.parse("2014-10-15"));
		param.setDateTo(df.parse("2015-10-16"));
		param.setTarget("");
		
		Integer sumUV = flowService.getSumUV(param);
		System.out.println(sumUV);
	}
	
	public static void testQueryFlowUserType() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setSource(1);
		param.setDateFrom(df.parse("2014-10-15"));
		param.setDateTo(df.parse("2015-11-16"));
		param.setUserType(Constants.UserType.QueryType.BUY_LEVEL);
		param.setShopId("ZBCU3151");
		List<FlowUserTypeVo>  list1 = vsService.queryFlowUserType(param);
		System.out.println(list1);
	}
	
	
	
	public static void testQueryFlowUserRegionT() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setSource(1);
		param.setDateFrom(df.parse("2014-10-15"));
		param.setDateTo(df.parse("2015-10-16"));
		param.setUserType(Constants.UserType.QueryType.WHITE_LIST);
		param.setShopId("ZBCU3151");
		List<StructureSectionVo>  list1 = vsService.queryFlowUserRegionT(param);
		System.out.println(list1);
	}
	
	public static void testQueryGoodsOverview() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setShopId("GXCU2473");
		param.setDateFrom(df.parse("2015-10-22"));
		param.setDateTo(df.parse("2015-10-23"));
		
		param.setPreDateFrom(df.parse("2015-10-21"));
		param.setPreDateTo(df.parse("2015-10-22"));
		GoodsOverviewVo  list = gdsService.queryGoodsOverview(param);
		System.out.println(list);
	}
	
	public static void testGetHotKeyWords() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setDateFrom(df.parse("2015-10-30"));
		param.setDateTo(df.parse("2015-10-30"));
		
		param.setPreDateFrom(df.parse("2015-10-29"));
		param.setPreDateTo(df.parse("2015-10-29"));
		
		param.setPageNo(1);
		param.setPageSize(10);
		PageBean<SearchRankingVo> pageBean = srService.getHotKeyWords(param);
		System.out.println(pageBean.getRecordList());
	}
	
	public static void testGetHotKeyWordsShop() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setDateFrom(df.parse("2015-10-28"));
		param.setDateTo(df.parse("2015-10-30"));
		param.setShopId("0771A8");
		param.setPageNo(1);
		param.setPageSize(10);
		PageBean<SearchRankingShopVo> pageBean = srService.getHotKeyWordsShop(param);
		System.out.println(pageBean.getRecordList());
	}
	
	public static void testGetFlowSearchOverviw() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setDateFrom(df.parse("2014-10-28"));
		param.setDateTo(df.parse("2015-10-30"));
		param.setShopId("0771A8");
		FlowSearchOverviewVo flowSearchOverviewVo = srService.getFlowSearchOverviw(param);
		System.out.println(flowSearchOverviewVo);
	}
	
	public static void testQueryVisitorOverviewTop() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		QueryParamsVo param = new QueryParamsVo();
		param.setDateFrom(df.parse("2015-10-28"));
		param.setDateTo(df.parse("2015-11-04"));
		param.setShopId("ZBCU3213");
		VisitorDataInterpretVo data = vsService.queryVisitorOverviewTop(param);
		System.out.println(data);
	}
	
	public static void testSave() throws ParseException {
		UrlDefinition urlDefinition = new UrlDefinition();
		urlDefinition.setCode("code");
		urlDefinition.setName("name");
		urlDefinition.setUrl("xxxx/xxx/xxx.html");
		urlDefinition.setEnable("true");
		urlService.save(urlDefinition);
		
		
	}
	
	public static void testUpdate() throws ParseException {
		UrlDefinition urlDefinition = new UrlDefinition();
//		urlDefinition = urlService.getByCode("code");
//		System.out.println(urlDefinition);
//		urlDefinition.setName("hello world !!!");
//		urlService.update(urlDefinition);
//		urlDefinition = urlService.getByCode("code");
//		System.out.println(urlDefinition);
		urlDefinition.setName("店铺");
		PageBean<UrlDefinition> pageBean = urlService.getPageBean(urlDefinition);
		System.out.println(pageBean);
	}
	
	public static void main(String[] args) throws ParseException {
		
		init();
		
//		testGetSumUV();
//		testQueyFlowNext();
//		testQueyFlowSource();
//		testQueyNext();
//		testQueryFlowUserType();
//		testQueryFlowUserRegionT();
//		testGetFlowSourceOrderMom();
// 		testQuery();
//		testQueryGoodsOverview();
//		testGetHotKeyWords();
//		testGetHotKeyWordsShop();
//		testGetFlowSearchOverviw(); 
//		testQueryVisitorOverviewTop();
		
//		testSave();
//		testUpdate();

	}

}

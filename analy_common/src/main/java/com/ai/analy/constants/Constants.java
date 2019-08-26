package com.ai.analy.constants;


public class Constants {
	/*
	public static class PAGE_NAME{
		
		//店铺首页
		public static String SHOP_HOME_PAGE = "商城首页";
		//商品详情页
		public static String SHOP_GOODS_PAGE = "商品详情页";
		
		public static String TARGE_PAGE3 = "商城搜索页";
		public static String TARGE_PAGE4 = "积分商城首页";
		public static String TARGE_PAGE5 = "积分商城商品页";
		public static String TARGE_PAGE6 = "积分商城搜索页";
		
		public static Map<String,PageInfoVo> pages = new LinkedHashMap<String,PageInfoVo>();
		static{
			pages.put(SHOP_HOME_PAGE, new PageInfoVo(SHOP_HOME_PAGE,"商城首页"));
			pages.put(SHOP_GOODS_PAGE, new PageInfoVo(SHOP_GOODS_PAGE,"商品详情页"));
			pages.put(TARGE_PAGE3, new PageInfoVo(TARGE_PAGE3,"商城搜索页"));
			pages.put(TARGE_PAGE4, new PageInfoVo(TARGE_PAGE4,"积分商城首页"));
			pages.put(TARGE_PAGE5, new PageInfoVo(TARGE_PAGE5,"积分商城商品页"));
			pages.put(TARGE_PAGE6, new PageInfoVo(TARGE_PAGE6,"积分商城搜索页"));
		}
		
		public static PageInfoVo getPageInfo(String key){
			PageInfoVo vo = null;
			if(pages.containsKey(key)){
				vo = pages.get(key);
			}
			return vo;
		}
	}
	*/
	
	public static class UserType {
		// 白名单用户
		public static final String USER_WHITE_LIST = "bw_1";
		// 非白名单用户
		public static String USER_BLACK_LIST = "bw_0";
		// 连锁渠道用户
		public static String USER_CHAIN = "cc_1";
		// 非连锁渠道用户
		public static String USER_NOT_CHAIN = "cc_0";
		// 新用户
		public static String USER_NEW = "sr_0";
		// 老用户
		public static String USER_OLD = "sr_1";
		// 外链入口
		public static String USER_OUTSIDE_LINK = "rt_0";
		// 自主访问
		public static String USER_INSIDE_LINK = "rt_1";
		// 购买力等级
		public static String USER_BUY_LEVEL1 = "1";
		public static String USER_BUY_LEVEL2 = "2";
		public static String USER_BUY_LEVEL3 = "3-5";
		public static String USER_BUY_LEVEL4 = "6-10";
		public static String USER_BUY_LEVEL5 = "10+";
		
		// PC端
		public static String USER_PC = "1";
		// 无线端
		public static String USER_WIRELESS = "2";
		
		// 访客类结构类型
		public static class QueryType {
			// 用户类型查询, 01：黑白名单，02：连锁渠道用户，03：新老访客，04：购买力分级，05：外链入口与自主访问 ，06：PC端与无线端
			public static String WHITE_LIST = "01";
			public static String CHAIN = "02";
			public static String NEW_AND_OLD = "03";
			public static String BUY_LEVEL = "04";
			public static String LINK_TYPE = "05";
			public static String PC_AND_WIRELESS = "06";
		}
		
		public static String getUserTypeName(String userType){
			String name = "";
			
			if(Constants.UserType.USER_WHITE_LIST.equals(userType))
				name = "白名单用户";
			
			if(Constants.UserType.USER_BLACK_LIST.equals(userType))
				name = "非白名单用户";
			
			if(Constants.UserType.USER_CHAIN.equals(userType))
				name = "连锁渠道用户";
			
			if(Constants.UserType.USER_NOT_CHAIN.equals(userType))
				name = "非连锁渠道用户";
			
			if(Constants.UserType.USER_NEW.equals(userType))
				name = "新用户";
			
			if(Constants.UserType.USER_OLD.equals(userType))
				name = "老用户";
			
			if (Constants.UserType.USER_OUTSIDE_LINK.equals(userType))
				name ="外链入口";
				
			if (Constants.UserType.USER_INSIDE_LINK.equals(userType))
				name ="自主访问";
			
			if (("0" + com.ai.analy.constants.Constants.UserType.USER_PC).equals(userType))
				name = "PC端";
			
			if (("0" + com.ai.analy.constants.Constants.UserType.USER_WIRELESS).equals(userType)) 
				name = "无线端";
			
			if (com.ai.analy.constants.Constants.UserType.USER_BUY_LEVEL1.equals(userType))
				name = "1件";
				
			
			if (com.ai.analy.constants.Constants.UserType.USER_BUY_LEVEL2.equals(userType))
				name = "2件";
			
			
			if (com.ai.analy.constants.Constants.UserType.USER_BUY_LEVEL3.equals(userType))
				name = "3-5件";
			
			
			if (com.ai.analy.constants.Constants.UserType.USER_BUY_LEVEL4.equals(userType))
				name = "6-10件";
			
			
			if (com.ai.analy.constants.Constants.UserType.USER_BUY_LEVEL5.equals(userType))
				name = "10件以上";
			
			
			return name;
		}
	}
	
    public static class REDIS_KEY{
    	
    	public static String URL_KEY_PRE ="URLINC:";
    }
    
	// 排序字段
	public static class OrderTypeName {
		public static String ORDER_BY_UV = "uv";
		public static String ORDER_BY_PAY_COUNT = "payCount";
		public static String ORDER_BY_PAY_MONEY = "payMoney";
	}
	
	// 排序方式
	public static class OrderWay {
		public static String ORDER_DESC = "desc";
		public static String ORDER_ASC = "asc";
	}
	
	// 商品状态
	public static class GoodsStatus {
        // 商品待上架状态
        public static String GDS_STATUS_WAIT = "0";
        // 商品已上架状态
        public static String GDS_STATUS_ACTIVE = "11";
        // 商品已下架状态
        public static String GDS_STATUS_INACTIVE = "22";
        // 商品删除状态
        public static String GDS_STATUS_IS_DELETE = "99";
        
        public static String getGoodsStatusName(String state) {
        	if (GoodsStatus.GDS_STATUS_WAIT.equals(state)) {
        		return "待上架";
        	}
        	if (GoodsStatus.GDS_STATUS_ACTIVE.equals(state)) {
        		return "已上架";
        	}
        	if (GoodsStatus.GDS_STATUS_INACTIVE.equals(state)) {
        		return "已下架";
        	}
        	if (GoodsStatus.GDS_STATUS_IS_DELETE.equals(state)) {
        		return "已删除";
        	}
        	return "";
        }
	}
	
}
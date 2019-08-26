package com.ai.analy.vo.comm;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ai.analy.vo.BaseVo;

public class QueryParamsVo extends BaseVo{
	private static final long serialVersionUID = -8008017595814522481L;

	//开始时间
	private Date dateFrom;
	//结束时间
	private Date dateTo;
	//来源 0:全部，1：PC端 2：无线
	private int source;
	//上期开始时间
	private Date preDateFrom;
	//上期结束时间 
	private Date preDateTo;
	
	//统计对象 例如：店铺首页 ，商品详情页 等等，值取PageInfoVo.pageCode
	private String target;
	//状态
	private String status;
	//参数shopId
	private String paramShopId;
	// 用户类型查询, 01：黑白名单，02：连锁渠道用户，03：新老访客，04：购买力分级
	private String userType;
	// 排序类型
	private String orderTypeName;
	// 排序方式 ：降序，升序
	private String orderWay;
	//单品id
	private String skuId;
	// 商品名称
	private String gdsName;
	// 商品类型(对应商品gds_type_id)
	private String catId;
	// 分类类型   1:商品类目  2:终端品牌 3：价格区间  
	private String categoryType;
	//分类等级
	private int categoryLevel;
	//商品分类
	private String categoryId;
	//ISBN
	private String skuIsbn;
	//促销名称
	private String promName;
	//促销类型
	private String promTypeCode;
	//站点
	private String siteId;
	//促销Id
	private String promId;
	//url
	private String url;
	//模板
	private String  templateId;
	//模板位置
	private String placeId;
	//广告id
	private String adId;
	//广告语
	private String adTitle;
	//开始时间
	private String firstDateFrom;
	//结束时间
	private String firstDateTo;
	//开始时间
	private String secondDateFrom;
	//结束时间
	private String secondDateTo;
	
	// 只查询数量
	private boolean queryCountOnly = false;
	
	private String provinceCode;
	private String cityCode;
	
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getGdsName() {
		return gdsName;
	}
	public void setGdsName(String gdsName) {
		this.gdsName = gdsName;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public Date getPreDateFrom() {
		return preDateFrom;
	}
	public void setPreDateFrom(Date preDateFrom) {
		this.preDateFrom = preDateFrom;
	}
	public Date getPreDateTo() {
		return preDateTo;
	}
	public void setPreDateTo(Date preDateTo) {
		this.preDateTo = preDateTo;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getDateFromStr() {
		return new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
	}
	public String getDateToStr() {
		return new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
	}
	public String getPreDateFromStr() {
		return new SimpleDateFormat("yyyy-MM-dd").format(preDateFrom);
	}
	public String getPreDateToStr() {
		return new SimpleDateFormat("yyyy-MM-dd").format(preDateTo);
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	public String getOrderWay() {
		return orderWay;
	}
	public void setOrderWay(String orderWay) {
		this.orderWay = orderWay;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public boolean isQueryCountOnly() {
		return queryCountOnly;
	}
	public void setQueryCountOnly(boolean queryCountOnly) {
		this.queryCountOnly = queryCountOnly;
	}
	public int getCategoryLevel() {
		return categoryLevel;
	}
	public void setCategoryLevel(int categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getSkuIsbn() {
		return skuIsbn;
	}
	public void setSkuIsbn(String skuIsbn) {
		this.skuIsbn = skuIsbn;
	}
	public String getPromName() {
		return promName;
	}
	public void setPromName(String promName) {
		this.promName = promName;
	}
	public String getPromTypeCode() {
		return promTypeCode;
	}
	public void setPromTypeCode(String promTypeCode) {
		this.promTypeCode = promTypeCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getParamShopId() {
		return paramShopId;
	}
	public void setParamShopId(String paramShopId) {
		this.paramShopId = paramShopId;
	}
	public String getPromId() {
		return promId;
	}
	public void setPromId(String promId) {
		this.promId = promId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getAdTitle() {
		return adTitle;
	}
	public void setAdTitle(String adTitle) {
		this.adTitle = adTitle;
	}
	public String getFirstDateFrom() {
		return firstDateFrom;
	}
	public void setFirstDateFrom(String firstDateFrom) {
		this.firstDateFrom = firstDateFrom;
	}
	public String getFirstDateTo() {
		return firstDateTo;
	}
	public void setFirstDateTo(String firstDateTo) {
		this.firstDateTo = firstDateTo;
	}
	public String getSecondDateFrom() {
		return secondDateFrom;
	}
	public void setSecondDateFrom(String secondDateFrom) {
		this.secondDateFrom = secondDateFrom;
	}
	public String getSecondDateTo() {
		return secondDateTo;
	}
	public void setSecondDateTo(String secondDateTo) {
		this.secondDateTo = secondDateTo;
	}
	public String getAdId() {
		return adId;
	}
	public void setAdId(String adId) {
		this.adId = adId;
	}
}
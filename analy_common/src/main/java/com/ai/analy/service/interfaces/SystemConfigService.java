package com.ai.analy.service.interfaces;

import java.util.List;

import com.ai.analy.vo.comm.PlaceInfoVo;
import com.ai.analy.vo.comm.ShopInfoVo;
import com.ai.analy.vo.comm.SiteInfoVo;
import com.ai.analy.vo.comm.TemplateInfoVo;

/**
 * 系统配置服务接口
 * 
 * @author limb
 * @date 2016年9月5日
 */
public interface SystemConfigService {
	/**
	 * 查询有效店铺列表
	 * 
	 * @return
	 */
	public List<ShopInfoVo> getShopList();

	/**
	 * 查询站点列表
	 * 
	 * @return
	 */
	public List<SiteInfoVo> getSiteList();

	/**
	 * 查询模板列表
	 * 
	 * @return
	 */
	public List<TemplateInfoVo> getTemplateList(int siteId);

	/**
	 * 查询模板位置列表
	 * 
	 * @param templateId
	 * @return
	 */
	public List<PlaceInfoVo> getPlaceList(int templateId);
}

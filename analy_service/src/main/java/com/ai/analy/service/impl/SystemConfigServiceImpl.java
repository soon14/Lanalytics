package com.ai.analy.service.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.SystemConfigService;
import com.ai.analy.vo.comm.PlaceInfoVo;
import com.ai.analy.vo.comm.ShopInfoVo;
import com.ai.analy.vo.comm.SiteInfoVo;
import com.ai.analy.vo.comm.TemplateInfoVo;

/**
 * 系统配置接口服务实现
 * 
 * @author limb
 * @date 2016年9月5日
 */
public class SystemConfigServiceImpl extends BaseDao implements SystemConfigService {

	@Override
	public List<ShopInfoVo> getShopList() {
		String sql = "select id as shopId, shop_name as shopName from t_shop_info";

		List<ShopInfoVo> shopList = queryList(sql, null, new BeanPropertyRowMapper<ShopInfoVo>(ShopInfoVo.class));

		return shopList;
	}

	@Override
	public List<SiteInfoVo> getSiteList() {
		String sql = "select id, site_name as siteName, site_url as siteUrl, status from t_cms_site";

		return queryList(sql, null, new BeanPropertyRowMapper<SiteInfoVo>(SiteInfoVo.class));
	}

	@Override
	public List<TemplateInfoVo> getTemplateList(int siteId) {
		String sql = "select id, site_id as siteId, template_name as templateName, status from t_cms_template where site_id = "
				+ siteId;

		return queryList(sql, null, new BeanPropertyRowMapper<TemplateInfoVo>(TemplateInfoVo.class));
	}

	@Override
	public List<PlaceInfoVo> getPlaceList(int templateId) {
		String sql = "select id, site_id as siteId, template_id as templateId, place_name as placeName, status from t_cms_place where template_id="
				+ templateId;

		return queryList(sql, null, new BeanPropertyRowMapper<PlaceInfoVo>(PlaceInfoVo.class));
	}
}

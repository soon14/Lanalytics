package com.ai.analy.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.ai.analy.dao.BaseDao;
import com.ai.analy.service.interfaces.UrlDefinitionService;
import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.UrlDefinition;
import com.alibaba.dubbo.common.utils.StringUtils;

public class UrlDefinitionServiceImpl extends BaseDao implements UrlDefinitionService {

	public int save(UrlDefinition urlDefinition) {
		StringBuffer sql = new StringBuffer("insert into t_url_def(url, code, name, enable, create_time) values(?,?,?,?,?)");
		List<Object> params = new ArrayList<Object>();
		params.add(urlDefinition.getUrl());
		params.add(urlDefinition.getCode());
		params.add(urlDefinition.getName());
		params.add(urlDefinition.getEnable());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		params.add(df.format(new Date()));
		return update(sql.toString(), params);
	}

	public int delete(Long idx) {
		StringBuffer sql = new StringBuffer("delete from t_url_def where idx=?");
		List<Object> params = new ArrayList<Object>();
		params.add(idx);
		return update(sql.toString(), params);
	}

	public int update(UrlDefinition urlDefinition) {
		if (urlDefinition == null) {
			return 0;
		}
		UrlDefinition dbUrlDefin = getByIdx(urlDefinition.getIdx());
		if (dbUrlDefin == null) {
			return 0;
		}
		if (StringUtils.isNotEmpty(urlDefinition.getUrl())) {
			dbUrlDefin.setUrl(urlDefinition.getUrl());
		}
		if (StringUtils.isNotEmpty(urlDefinition.getName())) {
			dbUrlDefin.setName(urlDefinition.getName());
		}
		if (StringUtils.isNotEmpty(urlDefinition.getEnable())) {
			dbUrlDefin.setEnable(urlDefinition.getEnable());
		}
		StringBuffer sql = new StringBuffer("update t_url_def t set t.url=?, t.name=?, t.enable=? , t.update_time=? where t.idx=?");
		List<Object> params = new ArrayList<Object>();
		params.add(dbUrlDefin.getUrl());
		params.add(dbUrlDefin.getName());
		params.add(dbUrlDefin.getEnable());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		params.add(df.format(new Date()));
		params.add(dbUrlDefin.getIdx());
		return update(sql.toString(), params);
	}

	public PageBean<UrlDefinition> getPageBean(UrlDefinition urlDefinition) {
		if (urlDefinition == null) {
			return null;
		}
		
		StringBuffer sql = new StringBuffer("select t.idx, t.url, t.name, t.code, t.enable from  t_url_def t where 1=1");
		List<Object> params = new ArrayList<Object>();
		if (StringUtils.isNotEmpty(urlDefinition.getUrl())) {
			sql.append(" and t.url like ?");
			params.add("%" + urlDefinition.getUrl() + "%");
		}
		if (StringUtils.isNotEmpty(urlDefinition.getName())) {
			sql.append(" and t.name like ?");
			params.add("%" + urlDefinition.getName() + "%");
		}
		if (StringUtils.isNotEmpty(urlDefinition.getCode())) {
			sql.append(" and t.code=?");
			params.add(urlDefinition.getCode());
		}
		
		int count = queryCount(sql.toString(), params);
		
		sql.append(" order by t.idx asc limit ?, ?");
		params.add((urlDefinition.getPageNo() - 1) * urlDefinition.getPageSize());
		params.add(urlDefinition.getPageSize());
		
		List<UrlDefinition> list = queryList(sql.toString(), params, new UrlDefinitionRowMapper());
		
		PageBean<UrlDefinition> pageBean = new PageBean<UrlDefinition>(urlDefinition.getPageNo(), urlDefinition.getPageSize(), list, count);
		
		return pageBean;
	}

	public UrlDefinition getByIdx(Long idx) {
		StringBuffer sql = new StringBuffer("select t.idx, t.url, t.name, t.code, t.enable from  t_url_def t where 1=1");
		List<Object> params = new ArrayList<Object>();
		if (idx != null) {
			sql.append(" and t.idx = ?");
			params.add(idx);
		}
		
		List<UrlDefinition> list = queryList(sql.toString(), params, new UrlDefinitionRowMapper());
		
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}
	
	protected class UrlDefinitionRowMapper implements RowMapper<UrlDefinition> {
		public UrlDefinition mapRow(ResultSet rs, int rowNum) throws SQLException {
			UrlDefinition url = new UrlDefinition();
			url.setIdx(rs.getLong("idx"));
			url.setCode(rs.getString("code"));
			url.setName(rs.getString("name"));
			url.setUrl(rs.getString("url"));
			url.setEnable(rs.getString("enable"));
			return url;
		}
	}

	public List<UrlDefinition> findAll() {
		
		StringBuffer sql = new StringBuffer("select t.idx, t.url, t.name, t.code, t.enable from  t_url_def t where t.name <> '' order by t.name asc");
		
		List<UrlDefinition> list = queryList(sql.toString(), null, new UrlDefinitionRowMapper());
		
		return list;
	}

}

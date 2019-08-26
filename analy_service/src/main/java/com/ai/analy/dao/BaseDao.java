package com.ai.analy.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.alibaba.dubbo.common.utils.StringUtils;

public abstract class BaseDao {

	protected static final Logger log = Logger.getLogger(BaseDao.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public <T> List<T> queryList(String sql, List<Object> params, RowMapper<T> rowMapper) {
		if (StringUtils.isEmpty(sql)) {
			return new ArrayList<T>();
		}
		if (params == null) {
			params = new ArrayList<Object>();
		}
		log.info("sql : " + sql + " params:" + params);

		List<T> list = jdbcTemplate.query(sql, params.toArray(), rowMapper);

		return list;
	}

	public <T> T queryOne(String sql, List<Object> params, RowMapper<T> rowMapper) {
		if (StringUtils.isEmpty(sql)) {
			return null;
		}
		if (params == null) {
			params = new ArrayList<Object>();
		}
		log.info("sql : " + sql + " params:" + params);

		T object = (T) jdbcTemplate.queryForObject(sql, params.toArray(), rowMapper);

		return object;
	}

	public int queryCount(String sql, List<Object> params) {
		if (StringUtils.isEmpty(sql)) {
			return 0;
		}
		if (params == null) {
			params = new ArrayList<Object>();
		}
		StringBuilder sb = new StringBuilder("select count(*) from (");
		sb.append(sql);
		sb.append(" ) as _tn");
		return this.getInt(sb.toString(), params);
	}

	public int getInt(String sql, List<Object> params) {
		if (StringUtils.isEmpty(sql)) {
			return 0;
		}
		if (params == null) {
			params = new ArrayList<Object>();
		}
		if (log.isInfoEnabled()) {
			log.info("sql : " + sql + " params:" + params);
		}
		return jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
	}

	public long getLong(String sql, List<Object> params) {
		if (StringUtils.isEmpty(sql)) {
			return 0L;
		}
		if (params == null) {
			params = new ArrayList<Object>();
		}
		if (log.isInfoEnabled()) {
			log.info("sql : " + sql + " params:" + params);
		}
		return jdbcTemplate.queryForObject(sql, params.toArray(), Long.class);
	}
	
	/**
	 * 更新(insert, delete, update)
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	protected int update(String sql, List<Object> params) {
		if (StringUtils.isEmpty(sql)) {
			return 0;
		}
		if (log.isInfoEnabled()) {
			log.info("sql : " + sql + " params:" + params);
		}
		return this.getJdbcTemplate().update(sql, params.toArray());
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}

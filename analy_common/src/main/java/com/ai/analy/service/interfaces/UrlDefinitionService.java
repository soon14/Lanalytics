package com.ai.analy.service.interfaces;

import java.util.List;

import com.ai.analy.vo.comm.PageBean;
import com.ai.analy.vo.comm.UrlDefinition;

public interface UrlDefinitionService {
	
	public int save(UrlDefinition urlDefinition);
	
	public int delete(Long idx);
	
	public UrlDefinition getByIdx(Long idx);
	
	public int update(UrlDefinition urlDefinition);
	
	public PageBean<UrlDefinition> getPageBean(UrlDefinition urlDefinition);
	
	public List<UrlDefinition> findAll();

}

package com.ai.analy.service.interfaces;

import java.util.List;

import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.FlowDateInterpretVo;
import com.ai.analy.vo.flow.VisitorDataInterpretVo;
import com.ai.analy.vo.flow.structure.FlowUserDaysVo;
import com.ai.analy.vo.flow.structure.FlowUserTypeVo;
import com.ai.analy.vo.flow.structure.StructureSectionVo;

/**
 * 访客结构服务接口
 * 
 * @author huangcm
 * @date 2015-10-20
 */
public interface VisitorStructureService {

	/**
	 * 地域分布查询
	 * 
	 * @author huangcm
	 * @date 2015-10-20
	 * @param param
	 * @return
	 */
	public List<StructureSectionVo> queryFlowUserRegionT(QueryParamsVo param);

	/**
	 * 访客类结构
	 * 
	 * 
	 * @author huangcm
	 * @date 2015-10-20
	 * @param param
	 * @return
	 */
	public List<FlowUserTypeVo> queryFlowUserType(QueryParamsVo param);

	/**
	 * 最近30天浏览频次分布
	 * 
	 * @author huangcm
	 * @date 2015-10-20
	 * @param param
	 * @return
	 */
	public List<FlowUserDaysVo> queryFlowUser30Days(QueryParamsVo param);

	/**
	 * 访客类结构-pc端和无线端
	 * 
	 * @author huangcm
	 * @date 2015-10-20
	 * @param param
	 * @return
	 */
	public List<FlowUserTypeVo> queryFlowTypePCAndWireless(QueryParamsVo param);
	
	/**
	 * 7天流量概况解读
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-22
	 * @param param
	 * @return
	 */
	public FlowDateInterpretVo queryFlowOverviewTop(QueryParamsVo param);
	
	/**
	 * 7天访客结构解读
	 *
	 *
	 * @author huangcm
	 * @date 2015-10-22
	 * @param param
	 * @return
	 */
	public VisitorDataInterpretVo queryVisitorOverviewTop(QueryParamsVo param);
	
	

}

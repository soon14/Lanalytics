/**
 * FlowAnalyController  流量分析-访客结构
 */
package com.ai.analy.controller.busi;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.analy.constants.Constants;
import com.ai.analy.service.interfaces.VisitorStructureService;
import com.ai.analy.system.AppBaseController;
import com.ai.analy.vo.comm.ExcelDataVo;
import com.ai.analy.vo.comm.QueryParamsVo;
import com.ai.analy.vo.flow.VisitorDataInterpretVo;
import com.ai.analy.vo.flow.structure.FlowUserDaysVo;
import com.ai.analy.vo.flow.structure.FlowUserTypeVo;
import com.ai.analy.vo.flow.structure.StructureSectionVo;

@Controller
@RequestMapping(value = "/flowAnaly")
public class FlowAnalyStructureController extends AppBaseController{
	
	@Autowired
	private VisitorStructureService visitorStructureService;//访客结构服务
	
	/**********************************************************************
	 * 访客结构界面处理逻辑
	 **********************************************************************/
	/**
	 * 访客结构界面
	 * @return
	 */
	@RequestMapping(value = "/flowstructure")
	public String  flowstructure(Model model){
		
		//请求参数
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		Date dateTo = cd.getTime();
		Date dateFrom = new Date(dateTo.getTime()-6*24*60*60*1000L);
		QueryParamsVo params = getQueryParams(df.format(dateFrom),df.format(dateTo),"","");
		
		//查询解读数据
		VisitorDataInterpretVo vo = visitorStructureService.queryVisitorOverviewTop(params);
		
		//计算时间范围
		if(vo.getTimeList()!= null && vo.getTimeList().size()>0){
			Map<Integer,Integer> timeMap = new LinkedHashMap<Integer,Integer>(); 
			
			List<String> timeList = vo.getTimeList();
			
			int curTime = -1;
			for(int i=0;i<timeList.size();i++){
				int iTime = Integer.valueOf(timeList.get(i));
				boolean b = false;
				int index = 0;
				
				if(curTime > iTime) continue;
				
				for(int j=i+1;j<timeList.size();j++){
					index++;
					int jTime = Integer.valueOf(timeList.get(j));
					if(iTime == jTime-index){
						timeMap.put(iTime, jTime+1);
						curTime = jTime+1;
						b = true;
					}
				}
				
				if(b == false){
			        timeMap.put(iTime, iTime+1);
				}
			}
			
			String timeSegment = "";
			int index = 0;
			for (Map.Entry<Integer,Integer> entry : timeMap.entrySet()) {
				if(index > 0)timeSegment +="，";
				timeSegment += String.format("%02d",entry.getKey())+":00-" + String.format("%02d",entry.getValue())+":00";
				index++;
			}
			
			vo.setTimeSegment(timeSegment);
			
		}else{
			vo.setTimeSegment("暂无数据");
		}
		
		//返回数据
		model.addAttribute("data", vo);
		return "busi/flowanaly/flowstructure";
	}
	
	/**
	 * 获取地域分布数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowSectionDate")
	public String getFlowSectionDate(Model model){
		//请求参数
		QueryParamsVo params = this.getQueryParams();
		//查询
		List<StructureSectionVo> flowMapList = visitorStructureService.queryFlowUserRegionT(params);
		//返回数据
		model.addAttribute("data", flowMapList);
		return "busi/flowanaly/structure/section";
	}
	
	/**
	 * 获取类型结构数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowStructureDate")
	public String getFlowStructureDate(Model model){
		
		QueryParamsVo params = this.getQueryParams();
		
		Map<String,Object> userStru = new HashMap<String,Object>();
		
		//连锁非连锁
		params.setUserType(Constants.UserType.QueryType.CHAIN);
		List<FlowUserTypeVo> listChainScales =  visitorStructureService.queryFlowUserType(params);
		
		//名单制非名单制客户
		params.setUserType(Constants.UserType.QueryType.WHITE_LIST);
		List<FlowUserTypeVo> listNonScales =  visitorStructureService.queryFlowUserType(params);
		
		//渠道购买力度分布
		params.setUserType(Constants.UserType.QueryType.BUY_LEVEL);
		List<FlowUserTypeVo> listBuyLevelScales =  visitorStructureService.queryFlowUserType(params);
		
		userStru.put("listChainScales", listChainScales);
		userStru.put("listNonScales", listNonScales);
		userStru.put("listBuyLevelScales", listBuyLevelScales);
		
		model.addAttribute("data", this.toJsonString(userStru));
		
		return "busi/flowanaly/structure/structure";
	}
	
	/**
	 * 获取类型结构数据
	 * @return
	 */
	@RequestMapping(value = "/getFlowRateDate")
	public String getFlowRateDate(Model model){

		//请求参数
		QueryParamsVo params = this.getQueryParams();
		
		//查询数据
		List<FlowUserDaysVo> flowUserDaysList = visitorStructureService.queryFlowUser30Days(params);
		
		//返回界面
		model.addAttribute("data", flowUserDaysList);
		return "busi/flowanaly/structure/rate";
	}
	
	/**
	 * 导出类型结构数据
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportFlowRateData.xls")
	public String exportFlowRateData(ModelMap model) {
		// 请求参数
		QueryParamsVo params = this.getQueryParams();
		params.setPageNo(0);

		//查询数据
		List<FlowUserDaysVo> flowUserDaysList = visitorStructureService.queryFlowUser30Days(params);
		ExcelDataVo excelData = new ExcelDataVo();

		excelData.getTitleList().add("排名");
		excelData.getTitleList().add("浏览频次");
		excelData.getTitleList().add("访客数");
		excelData.getTitleList().add("占比");
		excelData.getTitleList().add("下单转化率");
		
		DecimalFormat df = new DecimalFormat("#0.00");
		for (int i=0;i<flowUserDaysList.size();i++) {
			FlowUserDaysVo flowUserDaysVo = flowUserDaysList.get(i);
			List<String> rowData = new ArrayList<String>();
			rowData.add(String.valueOf(i+1));
			rowData.add(flowUserDaysVo.getVisitType());
			rowData.add(String.valueOf(flowUserDaysVo.getUv()));
			rowData.add(df.format(flowUserDaysVo.getUvPercent() * 100) + "%");
			rowData.add(df.format(flowUserDaysVo.getOrderRate() * 100) + "%");
			
			excelData.getDataList().add(rowData);
		}

		String fileName = "最近30天浏览频次分布";
		excelData.setFileName(fileName);
		
		model.addAttribute("excelData", excelData);

		return "";
	}
}
package com.ai.analy.vo.comm;

import java.util.List;

import com.ai.analy.vo.BaseVo;

public class MenuDisplayVO extends BaseVo implements Comparable<MenuDisplayVO>{
    private static final long serialVersionUID = 1L;

    private int menuId;

    private String menuTitle;

    private String menuType;

    private String menuPic;

    private String menuDesc;

    private String menuUrl;

    private int parentMenuId;
    
    ///排序
    private long menuOrder;
    
    //是否展开标记 ，值为1则收缩，其它值为不展开
    private String openFlag;

    // 菜单列表
    private List<MenuDisplayVO> menuList;

    // 子目录
    private List<MenuDisplayVO> catList;

    public String getMenuUrl() {
        return menuUrl;
    }

    public int getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(int parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuPic() {
        return menuPic;
    }

    public void setMenuPic(String menuPic) {
        this.menuPic = menuPic;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public List<MenuDisplayVO> getCatList() {
        return catList;
    }

    public void setCatList(List<MenuDisplayVO> catList) {
        this.catList = catList;
    }

    public List<MenuDisplayVO> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuDisplayVO> menuList) {
        this.menuList = menuList;
    }

	public long getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(long menuOrder) {
		this.menuOrder = menuOrder;
	}
	
	
	public String getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(String openFlag) {
		this.openFlag = openFlag;
	}

	@Override
	public int compareTo(MenuDisplayVO o) {
		if(this.getMenuOrder() > o.getMenuOrder()){
			return 1;
		} else if(this.getMenuOrder() < o.getMenuOrder()){
			return -1;
		} else {
			return 0;
		}
		
	}
}

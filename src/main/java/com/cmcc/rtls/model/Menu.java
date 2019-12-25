package com.cmcc.rtls.model;

import java.io.Serializable;
import lombok.Data;

/**
 * permission_menu
 * @author 
 */
@Data
public class Menu implements Serializable {
    /**
     * 菜单id编号
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 父级菜单id
     */
    private Integer pid;

    /**
     * 权限访问路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单类型1-表示一级菜单2-表示子菜单3-表示非菜单
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;
}
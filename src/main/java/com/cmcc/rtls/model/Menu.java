package com.cmcc.rtls.model;

import java.io.Serializable;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;

/**
 * permission_menu
 * @author 
 */
@Data
@Table(name = "permission_menu")
public class Menu implements Serializable {
    /**
     * 菜单id编号
     */
    @Column(name = "id",type = MySqlTypeConstant.INT,length = 11,isKey = true,isAutoIncrement = true,comment = "菜单id编号")
    private Integer id;

    /**
     * 标题
     */
    @Column(name = "title",type = MySqlTypeConstant.VARCHAR,length = 255,comment = "标题")
    private String title;

    /**
     * 父级菜单id
     */
    @Column(name = "pid",type = MySqlTypeConstant.INT,length = 11,defaultValue = "0",comment = "父级菜单id")
    private Integer pid;

    /**
     * 权限访问路径
     */
    @Column(name = "url",type = MySqlTypeConstant.VARCHAR,length = 255,comment = "访问路径")
    private String url;

    /**
     * 请求方式
     */
    @Column(name = "method",type = MySqlTypeConstant.VARCHAR,length = 111,comment = "请求方式")
    private String method;

    /**
     * 菜单图标
     */
    @Column(name = "icon",type = MySqlTypeConstant.VARCHAR,length = 111,defaultValue = "",comment = "菜单图标")
    private String icon;

    /**
     * 菜单类型1-表示一级菜单2-表示子菜单3-表示非菜单
     */
    @Column(name = "type",type = MySqlTypeConstant.INT,length = 111,defaultValue = "3",comment = "菜单类型1-表示一级菜单2-表示子菜单3-表示非菜单")
    private Integer type;

    /**
     * 备注
     */
    @Column(name = "remark",type = MySqlTypeConstant.VARCHAR,length = 255,comment = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}
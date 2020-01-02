package com.cmcc.rtls.model;

import java.io.Serializable;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;

/**
 * databasesource
 * @author 
 */
@Data
public class DatabaseSource implements Serializable {
    /**
     * 数据源的id
     */
    private String datasourceId;

    /**
     * 连接信息
     */
    private String url;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 暂留字段
     */
    private String code;

    /**
     * 数据库类型
     */
    private String databasetype;

    private static final long serialVersionUID = 1L;
}
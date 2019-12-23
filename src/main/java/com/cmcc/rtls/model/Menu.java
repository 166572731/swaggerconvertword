package com.cmcc.rtls.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @PackageName:com.cmcc.rtls.model
 * @ClassName:Menu
 * @Description:
 * @Author 陈磊
 * @Date 2019/12/23
 */
@Data
public class Menu implements Serializable {
    private int id;
    private String title;
    private int pid;
    private String url;
    private String method;
    private String icon;
    private int type;
    private String remark;
}

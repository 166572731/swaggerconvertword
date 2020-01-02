package com.cmcc.rtls.mapper.impl;

import com.cmcc.rtls.mapper.MenuMapper;
import com.cmcc.rtls.model.Menu;
import com.gitee.sunchenbin.mybatis.actable.manager.common.BaseMysqlCRUDManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @PackageName:com.cmcc.rtls.mapper.impl
 * @ClassName:MenuMapperImpl
 * @Description:
 * @Author 陈磊
 * @Date 2019/12/26
 */
@Repository
public class MenuMapperImpl implements MenuMapper  {
    @Autowired
    private BaseMysqlCRUDManager baseMysqlCRUDManager;
    @Override
    public int insert(Menu record) {
        return baseMysqlCRUDManager.save(record);
    }

    @Override
    public void synchronization(Menu record) {
        baseMysqlCRUDManager.delete(record);
    }
}

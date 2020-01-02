package com.cmcc.rtls.mapper.impl;

import com.cmcc.rtls.mapper.DatabaseSourceMapper;
import com.cmcc.rtls.model.DatabaseSource;
import com.gitee.sunchenbin.mybatis.actable.manager.common.BaseMysqlCRUDManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @PackageName:com.cmcc.rtls.mapper.impl
 * @ClassName:DatabaseSourceMapperImpl
 * @Description:
 * @Author 陈磊
 * @Date 2019/12/27
 */
@Repository
public class DatabaseSourceMapperImpl implements DatabaseSourceMapper {
    @Autowired
    private BaseMysqlCRUDManager baseMysqlCRUDManager;
    @Override
    public List<DatabaseSource> get() {
        return baseMysqlCRUDManager.query("select * from database_source",DatabaseSource.class);
    }

    @Override
    public int insert(DatabaseSource databaseSource) {
        return baseMysqlCRUDManager.save(databaseSource);
    }

}

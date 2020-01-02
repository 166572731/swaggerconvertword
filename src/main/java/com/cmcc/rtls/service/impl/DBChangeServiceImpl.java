package com.cmcc.rtls.service.impl;

import com.cmcc.rtls.config.DBContextHolder;
import com.cmcc.rtls.config.DynamicDataSource;
import com.cmcc.rtls.mapper.DatabaseSourceMapper;
import com.cmcc.rtls.model.DatabaseSource;
import com.cmcc.rtls.service.DBChangeService;
import com.gitee.sunchenbin.mybatis.actable.dao.system.CreateMysqlTablesMapper;
import com.gitee.sunchenbin.mybatis.actable.manager.system.SysMysqlCreateTableManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author : JCccc
 * @CreateTime : 2019/10/22
 * @Description :
 **/
@Slf4j
@Service
public class DBChangeServiceImpl implements DBChangeService {

    @Autowired
    DatabaseSourceMapper dataSourceMapper;
    @Autowired
    private DynamicDataSource dynamicDataSource;
    @Autowired
    private SysMysqlCreateTableManager sysMysqlCreateTableManager;

    @Override
    public List<DatabaseSource> get() {
        return dataSourceMapper.get();
    }

    @Override
    public boolean changeDb(String datasourceId) throws Exception {

        //默认切换到主数据源,进行整体资源的查找
        DBContextHolder.clearDataSource();

        List<DatabaseSource> dataSourcesList = dataSourceMapper.get();

        for (DatabaseSource dataSource : dataSourcesList) {
            if (dataSource.getDatasourceId().equals(datasourceId)) {
                System.out.println("需要使用的的数据源已经找到,datasourceId是：" + dataSource.getDatasourceId());
                //创建数据源连接&检查 若存在则不需重新创建
                dynamicDataSource.createDataSourceWithCheck(dataSource);
                //切换到该数据源
                DBContextHolder.setDataSource(dataSource.getDatasourceId());
                return true;
            }
        }
        return false;

    }

    @Override
    public int insert(DatabaseSource databaseSource) {
        return dataSourceMapper.insert(databaseSource);
    }

    @Override
    public boolean changeDbByFrom(DatabaseSource databaseSource) throws Exception {
        //默认切换到主数据源,进行整体资源的查找
        DBContextHolder.clearDataSource();
        //创建数据源连接&检查 若存在则不需重新创建
        dynamicDataSource.createDataSourceWithCheck(databaseSource);
        //切换到该数据源
        DBContextHolder.setDataSource(databaseSource.getUrl());
        sysMysqlCreateTableManager.createMysqlTable();
        return true;

    }

}
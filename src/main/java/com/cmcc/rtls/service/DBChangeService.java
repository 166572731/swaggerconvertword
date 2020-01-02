package com.cmcc.rtls.service;

import com.cmcc.rtls.model.DatabaseSource;
import java.util.List;
 
/**
 * @Author : JCccc
 * @CreateTime : 2019/10/22
 * @Description :
 **/
 
public interface DBChangeService {
 
    List<DatabaseSource> get();
 
    boolean changeDb(String datasourceId) throws Exception;

    int insert(DatabaseSource databaseSource);

    boolean changeDbByFrom(DatabaseSource databaseSource) throws Exception;
 
}
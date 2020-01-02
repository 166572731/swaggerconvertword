package com.cmcc.rtls.mapper;

import com.cmcc.rtls.model.DatabaseSource;
import com.cmcc.rtls.model.Menu;

import java.util.List;

public interface DatabaseSourceMapper {
    List<DatabaseSource> get();
    int insert(DatabaseSource databaseSource);
}
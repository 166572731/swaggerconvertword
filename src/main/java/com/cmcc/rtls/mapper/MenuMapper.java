package com.cmcc.rtls.mapper;

import com.cmcc.rtls.model.Menu;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuMapper {
    int insert(Menu record);
    void synchronization();
}
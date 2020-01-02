package com.cmcc.rtls.service;

import com.cmcc.rtls.model.Menu;

public interface MenuService {
    int insert(Menu menu);
    void synchronization(Menu menu);
}

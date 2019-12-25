package com.cmcc.rtls.service.impl;

import com.cmcc.rtls.mapper.MenuMapper;
import com.cmcc.rtls.model.Menu;
import com.cmcc.rtls.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @PackageName:com.cmcc.rtls.service.impl
 * @ClassName:MenuServiceImpl
 * @Description:
 * @Author 陈磊
 * @Date 2019/12/24
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuMapper menuMapper;

    @Override
    public int insert(Menu menu) {
        return menuMapper.insert(menu);
    }

    @Override
    public void synchronization() {
        menuMapper.synchronization();
    }
}

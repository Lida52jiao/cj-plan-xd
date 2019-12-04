package com.yj.bj.service.plan.impl;

import com.yj.bj.entity.BindEntity;
import com.yj.bj.mapper.BindMapper;
import com.yj.bj.service.BaseServiceImpl;
import com.yj.bj.service.plan.BindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by bin on 2017/11/6.
 */
@Service
public class BindServiceImpl extends BaseServiceImpl<BindEntity> implements BindService {
    @Autowired
    private BindMapper bindMapper;
}

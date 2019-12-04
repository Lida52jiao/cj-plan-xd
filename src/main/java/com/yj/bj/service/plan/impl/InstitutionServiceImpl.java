package com.yj.bj.service.plan.impl;


import com.yj.bj.entity.InstitutionEntity;
import com.yj.bj.mapper.InstitutionMapper;
import com.yj.bj.service.BaseServiceImpl;
import com.yj.bj.service.plan.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bin on 2018/3/14.
 */
@Service
public class InstitutionServiceImpl extends BaseServiceImpl<InstitutionEntity> implements InstitutionService {
    @Autowired
    private InstitutionMapper institutionMapper;
}

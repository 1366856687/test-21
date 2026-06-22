package com.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.dao.inter.PbocDataDao;
import com.test.domain.entity.PbocData;
import com.test.service.TestService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


@Service
public class TestServiceImpl implements TestService {
    @Resource
    private PbocDataDao pbocDataDao;

    public String test(){
        System.out.println(JSON.toJSONString(pbocDataDao.selectList(new QueryWrapper<PbocData>().lambda()
                .eq(PbocData::getVariableName,"12"))));
        return "143";
    }
}

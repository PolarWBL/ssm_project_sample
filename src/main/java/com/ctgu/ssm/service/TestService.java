package com.ctgu.ssm.service;

import com.ctgu.ssm.mapper.TestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Boliang Weng
 */
@Service
public class TestService {
    @Resource
    public TestMapper testMapper;

    @Transactional(rollbackFor = Exception.class)
    public void bachImport(){
        for (int i = 0; i < 5; i++) {
            if (i == 3) {
                throw new RuntimeException("主动制造的异常");
            }
            testMapper.insertSample();
        }
    }
}

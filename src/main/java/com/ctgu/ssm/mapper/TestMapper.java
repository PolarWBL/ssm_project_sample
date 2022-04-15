package com.ctgu.ssm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.ssm.entity.Test;

/**
 * @author Boliang Weng
 */
public interface TestMapper extends BaseMapper<Test> {
    public void insertSample();
}

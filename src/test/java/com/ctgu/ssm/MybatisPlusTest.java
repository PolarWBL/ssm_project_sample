package com.ctgu.ssm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctgu.ssm.entity.Test;
import com.ctgu.ssm.mapper.TestMapper;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MybatisPlusTest {
    @Resource
    private TestMapper testMapper;
    @org.junit.Test
    public void mybatisPlusTest(){
        Test test = new Test();
        test.setContent("mybatisPlus 测试");
        int result = testMapper.insert(test);
        System.out.println("result: [" + result + "]");
    }
    @org.junit.Test
    public void mybatisPlusTest1(){
        Test test = testMapper.selectById(2);
        System.out.println(test);
    }
    @org.junit.Test
    public void mybatisPlusTest2(){
        int result = testMapper.deleteById(1);
        System.out.println("result: [" + result + "]");
    }
    @org.junit.Test
    public void mybatisPlusTest3(){
        QueryWrapper<Test> wrapper = new QueryWrapper<>();
//        wrapper.eq("id",5);
        wrapper.gt("id", 5);
        List<Test> testList = testMapper.selectList(wrapper);
        for (Test test : testList) {
            System.out.println(test);
        }
    }
}

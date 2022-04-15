package com.ctgu.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author Boliang Weng
 */
@TableName("test")//表的名称
public class Test {
    @TableId(type = IdType.AUTO)//标识主键
    @TableField("id")//说明属性对应的表的字段
    private Integer id;
    @TableField("content")//如果字段名和属性名相同或者符合驼峰命名转换的规则则可以省略@TableField注解
    private String content;

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.shanzhu.purchase.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.shanzhu.purchase.config.LocalDateTimeConverter;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * @Author ChenYuTeng
 * @Date 2024/12/17 15:50
 * @description: 导出操作记录
 */
public class DcTake {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "操作记录")
    private String name;

    @ApiModelProperty(value = "采购时间")
    private LocalDateTime time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "DcTake{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}

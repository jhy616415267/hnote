package me.mingshan.facade.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: mingshan
 * @Date: Created in 21:50 2018/4/29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Folder implements Serializable {
    private static final long serialVersionUID = -2582952414547485569L;

    private Long id;
    private String name;
    private Integer level;
    private Long pid;
    private Long uid;
    private Integer isDeleted;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;

    /**
     * 批量删除
     */
    private List<Long> ids;
}
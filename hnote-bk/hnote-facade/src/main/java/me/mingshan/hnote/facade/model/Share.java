package me.mingshan.hnote.facade.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: mingshan
 * @Date: Created in 23:10 2018/5/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Share implements Serializable {
    private static final long serialVersionUID = 4265612278662010439L;

    private Long id;
    private Long noteId;
    private String code;
    private String viewPassword;
    private Integer likeNum;
    private Integer commentNum;
    private Integer reportNum;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtModified;
    // 乐观锁版本号
    private Integer version;

    private Note note;
}

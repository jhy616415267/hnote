package me.mingshan.web.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: mingshan
 * @Date: Created in 15:31 2018/5/21
 */
@Data
public class CreateNoteVO implements Serializable {
    private static final long serialVersionUID = -1018368608378910342L;

    private String title;
    private String content;
    private String author;
    private String source;
    private Long uid;
    private Long typeId;
    private Long folderId;
}

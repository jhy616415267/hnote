package me.mingshan.web.controller;

import io.swagger.annotations.*;
import me.mingshan.common.annotation.Authorization;
import me.mingshan.facade.model.Tag;
import me.mingshan.facade.service.TagService;
import me.mingshan.web.exception.ServerException;
import me.mingshan.web.model.ResultModel;
import me.mingshan.web.vo.TagVO;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mingshan
 * @Date: Created in 19:42 2018/5/1
 */
@Api(value = "tags")
@RestController
@RequestMapping("/api/tags")
public class TagController extends BaseController {
    @Autowired
    private TagService tagService;

    @Autowired
    private Mapper mapper;

    /**
     * Get tags by note id.
     * @param nid
     * @return
     */
    @RequestMapping(value = "filters", method = RequestMethod.GET)
    @ApiOperation(value="Get tags by nid", httpMethod="GET", notes="")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<List<TagVO>> getTagsByNid(@RequestParam Long nid) {
        List<TagVO> tagsVOs = new ArrayList<>();
        List<Tag> tags = tagService.findAllByNid(nid);
        for (Tag tag : tags) {
            TagVO tagVO = mapper.map(tag, TagVO.class);
            tagsVOs.add(tagVO);
        }

        return new ResponseEntity<>(tagsVOs, HttpStatus.OK);
    }

    /**
     * Get folder by id.
     *
     * @param id The folder id.
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value="Get tag by id", httpMethod="GET", notes="")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<TagVO> getNoteById(@PathVariable Long id) {
        Tag tag = tagService.findById(id);
        if (tag == null) {
            ResultModel result = new ResultModel();
            result.setCode(1021);
            result.setMessage("Tag with id " + id + " not found");
            logger.info("Tag with id {} not found", id);
            throw new ServerException(result, HttpStatus.NOT_FOUND);
        }

        TagVO tagVO = mapper.map(tag, TagVO.class);
        return new ResponseEntity<>(tagVO, HttpStatus.OK);
    }

    /**
     * Create tag.
     *
     * @param tagVO
     * @param nid
     * @param ucBuilder
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value="create tag", httpMethod="POST", notes="Create tag")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<Void> createNote(@ApiParam(required=true, value="Folder", name="Folder")
                                           @RequestBody TagVO tagVO, @RequestParam Long nid, UriComponentsBuilder ucBuilder) {
        Tag tag = mapper.map(tagVO, Tag.class);

        tagService.insert(tag, nid);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/notes/{id}").buildAndExpand(tag.getId()).toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

}

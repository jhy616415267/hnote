package me.mingshan.hnote.web.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import me.mingshan.hnote.common.annotation.Authorization;
import me.mingshan.hnote.common.exception.ServerException;
import me.mingshan.hnote.common.model.ResultModel;
import me.mingshan.hnote.facade.model.Note;
import me.mingshan.hnote.facade.service.NoteService;
import me.mingshan.hnote.facade.service.SearchClient;
import me.mingshan.hnote.web.config.Constants;
import me.mingshan.hnote.web.vo.*;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: mingshan
 * @Date: Created in 14:34 2018/4/30
 */
@Api(value = "notes")
@RestController
@RequestMapping("/api/notes")
public class NoteController extends BaseController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private Mapper mapper;

    @Autowired
    private SearchClient searchClient;

    /**
     * Get all note info by pagination.
     *
     * @param pageNumber
     * @param pageSize
     * @param fid
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/filters", method = RequestMethod.GET)
    @ApiOperation(value="Get all notes by pagination.", httpMethod="GET", notes="Get notes")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<SearchResultVO<Note>> listAllNotes(@RequestParam Integer pageNumber,
                                                             @RequestParam Integer pageSize,
                                                             @RequestParam Long fid,
                                                             @RequestParam String sort,
                                                             @RequestParam String sortType) {
        logger.info("page = " + pageNumber + "per_page = " + pageSize + "fid = " + fid);

        PageInfo<Note> pageInfo = noteService.findAll(pageNumber, pageSize, fid, sort, sortType);
        List<Note> notes = pageInfo.getList();
        // 总记录数
        Long total = pageInfo.getTotal();
        if(notes.isEmpty()){
            // You many decide to return HttpStatus.NOT_FOUND
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        SearchResultVO<Note> model = new SearchResultVO<>();
        model.setItems(notes);
        model.setTotal(total);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }


    /**
     * Get the lastest notes.
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/lastest", method = RequestMethod.GET)
    @ApiOperation(value="Get the lastest notes by pagination.", httpMethod="GET", notes="Get notes")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<SearchResultVO<Note>> listLastestNotes(@RequestParam Integer pageNumber,
                                                                 @RequestParam Integer pageSize,
                                                                 @RequestParam String sort,
                                                                 @RequestParam String sortType,
                                                                 HttpServletRequest request) {
        logger.info("page = " + pageNumber + "per_page = " + pageSize);
        Long userId = (Long) request.getAttribute(Constants.CURRENT_USER_ID);
        PageInfo<Note> pageInfo = noteService.findLastestNotes(userId, pageNumber, pageSize, sort, sortType);
        List<Note> notes = pageInfo.getList();
        // 总记录数
        Long total = pageInfo.getTotal();
        if(notes.isEmpty()){
            // You many decide to return HttpStatus.NOT_FOUND
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        SearchResultVO<Note> model = new SearchResultVO<>();
        model.setItems(notes);
        model.setTotal(total);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Get notes by fuzzy search.
     *
     * @param pageNumber
     * @param pageSize
     * @param token
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value="Get notes by fuzzy search.", httpMethod="GET", notes="Get notes")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<SearchResultVO<Note>> listNotesByFuzzySearch(@RequestParam Integer pageNumber,
                                                                       @RequestParam Integer pageSize,
                                                                       @RequestParam String token,
                                                                       @RequestParam String sort,
                                                                       @RequestParam String sortType) {
        logger.info("page = " + pageNumber + "per_page = " + pageSize);

        List<Note> notes = searchClient.search(token, Note.class);

        // 总记录数
        Long total = Long.valueOf(notes.size());
        if(notes.isEmpty()){
            // You many decide to return HttpStatus.NOT_FOUND
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        SearchResultVO<Note> model = new SearchResultVO<>();
        model.setItems(notes);
        model.setTotal(total);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Get notes by tid.
     *
     * @param pageNumber
     * @param pageSize
     * @param tid
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/tags/{tid}", method = RequestMethod.GET)
    @ApiOperation(value="Get notes by tid.", httpMethod="GET", notes="Get notes")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<SearchResultVO<Note>> listNotesByTid(@RequestParam Integer pageNumber,
                                                               @RequestParam Integer pageSize,
                                                               @PathVariable Long tid,
                                                               @RequestParam String sort,
                                                               @RequestParam String sortType) {
        logger.info("page = " + pageNumber + ",per_page = " + pageSize + ",tid = " + tid);

        PageInfo<Note> pageInfo = noteService.findByTid(tid, pageNumber, pageSize, sort, sortType);
        List<Note> notes = pageInfo.getList();
        // 总记录数
        Long total = pageInfo.getTotal();
        if(notes.isEmpty()){
            // You many decide to return HttpStatus.NOT_FOUND
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        SearchResultVO<Note> model = new SearchResultVO<>();
        model.setItems(notes);
        model.setTotal(total);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Get note by id.
     *
     * @param id The note id.
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value="Get note by id", httpMethod="GET", notes="")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Note note = noteService.findById(id);
        if (note == null) {
            ResultModel result = new ResultModel();
            result.setCode(1021);
            result.setMessage("Note with id " + id + " not found");
            logger.info("Note with id {} not found", id);
            throw new ServerException(result, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(note, HttpStatus.OK);
    }


    /**
     * Create a note.
     * @param noteVO
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value="create a note", httpMethod="POST", notes="Create a note")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<CreatedNoteVO> createNote(@ApiParam(required=true, value="笔记信息", name="Note")
                                                    @RequestBody CreateNoteVO noteVO) {
        Note note = mapper.map(noteVO, Note.class);
        logger.info("Create note: {}", note);
        Long id = noteService.insert(note);
        CreatedNoteVO vo = new CreatedNoteVO();
        vo.setId(id);
        return new ResponseEntity<>(vo, HttpStatus.CREATED);
    }

    /**
     * Deletes note by id.
     * @param:  * @param null
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value="Delete note", httpMethod="DELETE", notes="Delete book by id")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<ResultModel> deleteNote(@ApiParam(required=true, value="Note ID", name="id")
                                                  @PathVariable("id") Long id) {
        logger.info("Fetching & Deleting Note with id " + id);
        try {
            noteService.delete(id);
        } catch (RuntimeException e) {
            ResultModel result = new ResultModel();
            result.setCode(1024);
            result.setMessage("Unable to delete note with id " + id);
            logger.info("Unable to delete note with id " + id);
            throw new ServerException(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Delete tag by id.
     * @param:  * @param null
     */
    @RequestMapping(value = "/{id}/tags/{tid}", method = RequestMethod.DELETE)
    @ApiOperation(value="Delete tag", httpMethod="DELETE", notes="Delete tag by id")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<ResultModel> deleteTag(@ApiParam(required=true, value="Note ID", name="id")
                                                  @PathVariable("id") Long id, @PathVariable("tid") Long tid) {
        logger.info("Fetching & Deleting tag with id " + tid);
        try {
            noteService.deleteByNidTid(id, tid);
        } catch (RuntimeException e) {
            ResultModel result = new ResultModel();
            result.setCode(1024);
            result.setMessage("Unable to delete tag with id " + tid);
            logger.info("Unable to delete tag with id " + tid);
            throw new ServerException(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Update note.
     *
     * @param updateNoteVo
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ApiOperation(value="Update note", httpMethod="PUT", notes="Update note by id")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<ResultModel> updateNote(@RequestBody UpdateNoteVo updateNoteVo) {
        try {
            Note note = mapper.map(updateNoteVo, Note.class);
            noteService.update(note);
        } catch (RuntimeException e) {
            ResultModel result = new ResultModel();
            result.setCode(1024);
            result.setMessage("Unable to update note: " + updateNoteVo);
            logger.info("Unable to  update note: " + updateNoteVo);
            throw new ServerException(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Update folder of note.
     *
     * @param folderVO
     * @return
     */
    @RequestMapping(value = "/folder", method = RequestMethod.PUT)
    @ApiOperation(value="Update folder of note", httpMethod="PUT", notes="Update folder of note")
    @Authorization
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "authorization", required = true, dataType = "String",
                    paramType = "header")
    })
    public ResponseEntity<ResultModel>  updateFolder(@RequestBody UpdateNoteFolderVO folderVO) {
        try {

            noteService.updateFolder(folderVO.getFolderId(), folderVO.getId());
        } catch (RuntimeException e) {
            ResultModel result = new ResultModel();
            result.setCode(1024);
            result.setMessage("Unable to update folder of note: " + folderVO);
            logger.info("Unable to  update folder of note: " + folderVO);
            throw new ServerException(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

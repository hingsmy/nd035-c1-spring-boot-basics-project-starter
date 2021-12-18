package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM notes WHERE userid = #{userId}")
    List<Note> getAllNotes(User user);

    @Select("SELECT * FROM notes WHERE noteid = #{noteId}")
    Note getNote(Integer noteId);

    @Insert("INSERT INTO notes (noteid, notetitle, notedescription, userid) VALUES(#{noteId}, #{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("UPDATE notes SET notetitle = #{noteTitle}, noteDescription = #{noteDescription} WHERE noteid = #{noteId}")
    int update(Note note);

    @Delete("DELETE FROM notes WHERE noteid = #{id}")
    void delete(Integer id);
}

package com.dosol.qwer.repository.note;

import com.dosol.qwer.domain.note.Note;
import com.dosol.qwer.domain.user.User;
import com.dosol.qwer.repository.note.search.search.NoteSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long>, NoteSearch {

}

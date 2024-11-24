package com.dosol.qwer.service.note;

import com.dosol.qwer.config.auth.CustomUserDetails;
import com.dosol.qwer.domain.note.Note;
import com.dosol.qwer.domain.user.User;
import com.dosol.qwer.dto.note.NoteDTO;
import com.dosol.qwer.dto.note.PageRequestDTO;
import com.dosol.qwer.dto.note.PageResponseDTO;
import com.dosol.qwer.repository.note.NoteRepository;
import com.dosol.qwer.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageResponseDTO<NoteDTO> list(PageRequestDTO pageRequestDTO) {
        // 현재 로그인된 사용자 정보 가져오기
        User currentUser = getCurrentUser();

        // 검색 조건 및 페이징 정보 추출
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("noteNum");

        // 본인의 노트를 검색 조건과 함께 조회
        Page<Note> result = noteRepository.searchAll(types, keyword, pageable, currentUser.getUserNum());

        // ModelMapper를 사용해 엔티티 -> DTO 변환
        List<NoteDTO> dtoList = result.getContent().stream()
                .map(note -> modelMapper.map(note, NoteDTO.class))
                .collect(Collectors.toList());

        // PageResponseDTO 생성
        return PageResponseDTO.<NoteDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public Long register(NoteDTO noteDTO) {
        User currentUser = getCurrentUser();

        // DTO -> Entity 변환
        Note note = modelMapper.map(noteDTO, Note.class);
        note.setUser(currentUser);

        // 저장
        Note savedNote = noteRepository.save(note);
        return savedNote.getNoteNum();
    }

    @Override
    public NoteDTO readOne(Long noteNum) {
        Note note = noteRepository.findById(noteNum)
                .orElseThrow(() -> new IllegalArgumentException("Note not found: " + noteNum));

        User currentUser = getCurrentUser();

        // 본인의 노트만 읽을 수 있도록 제한
        if (!note.getUser().equals(currentUser)) {
            throw new SecurityException("You are not authorized to view this note");
        }

        // 엔티티 -> DTO 변환
        return modelMapper.map(note, NoteDTO.class);
    }

    @Override
    public void modify(NoteDTO noteDTO) {
        Note note = noteRepository.findById(noteDTO.getNoteNum())
                .orElseThrow(() -> new IllegalArgumentException("Note not found: " + noteDTO.getNoteNum()));

        User currentUser = getCurrentUser();
        if (!note.getUser().equals(currentUser)) {
            throw new SecurityException("You are not authorized to modify this note");
        }

        // 변경사항 적용
        note.change(noteDTO.getTitle(), noteDTO.getContent());
        noteRepository.save(note);
    }

    @Override
    public void remove(Long noteNum) {
        Note note = noteRepository.findById(noteNum)
                .orElseThrow(() -> new IllegalArgumentException("Note not found: " + noteNum));

        User currentUser = getCurrentUser();
        if (!note.getUser().equals(currentUser)) {
            throw new SecurityException("You are not authorized to delete this note");
        }

        noteRepository.delete(note);
    }

    private User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByNickname(userDetails.getNickname())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

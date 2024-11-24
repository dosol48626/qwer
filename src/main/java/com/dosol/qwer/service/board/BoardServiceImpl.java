package com.dosol.qwer.service.board;

import com.dosol.qwer.config.auth.CustomUserDetails;
import com.dosol.qwer.domain.board.Board;
import com.dosol.qwer.domain.user.User;
import com.dosol.qwer.dto.board.BoardDTO;
import com.dosol.qwer.dto.board.PageRequestDTO;
import com.dosol.qwer.dto.board.PageResponseDTO;
import com.dosol.qwer.repository.board.BoardRepository;
import com.dosol.qwer.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    private final Map<String, Set<Long>> userViewHistory = new HashMap<>();

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("boardNum");

        // Page 객체 가져오기
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        // ModelMapper를 사용해 엔티티 -> DTO 변환
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());

        // PageResponseDTO 생성
        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public Long register(BoardDTO boardDTO) {
        // 현재 로그인된 사용자 가져오기
        User currentUser = getCurrentUser();

        // DTO -> Entity 변환
        Board board = modelMapper.map(boardDTO, Board.class);
        board.setUser(currentUser);

        // 저장
        Board savedBoard = boardRepository.save(board);
        return savedBoard.getBoardNum();
    }

    @Override
    public BoardDTO readOne(Long boardNum) {
        Board board = boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("Board not found: " + boardNum));

        // 현재 로그인된 사용자 ID 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자별 조회 기록 초기화
        userViewHistory.putIfAbsent(username, new HashSet<>());

        // 조회수 증가 조건: 사용자가 해당 게시글을 처음 조회하는 경우
        if (!userViewHistory.get(username).contains(boardNum)) {
            board.updateVisitCount(); // 조회수 증가
            boardRepository.save(board); // 변경된 조회수 저장
            userViewHistory.get(username).add(boardNum); // 조회 기록 추가
        }

        // 엔티티 -> DTO 변환
        return modelMapper.map(board, BoardDTO.class);
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = boardRepository.findById(boardDTO.getBoardNum())
                .orElseThrow(() -> new IllegalArgumentException("Board not found: " + boardDTO.getBoardNum()));

        // 현재 로그인된 사용자 확인
        User currentUser = getCurrentUser();
        if (!board.getUser().equals(currentUser)) {
            throw new SecurityException("You are not authorized to modify this board");
        }

        // 변경사항 적용
        board.change(boardDTO.getTitle(), boardDTO.getContent()); // change 메서드 활용
        boardRepository.save(board); // 수정된 데이터 저장
    }


    @Override
    public void remove(Long boardNum) {
        Board board = boardRepository.findById(boardNum)
                .orElseThrow(() -> new IllegalArgumentException("Board not found: " + boardNum));

        User currentUser = getCurrentUser();
        if (!board.getUser().equals(currentUser)) {
            throw new SecurityException("You are not authorized to delete this board");
        }

        boardRepository.delete(board);
    }

    private User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByNickname(userDetails.getNickname())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

package com.example.board.service;

import com.example.board.dto.BoardDTO;
import com.example.board.entity.BoardEntity;
import com.example.board.entity.MemberEntity;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<BoardDTO> findAll(){
        List<BoardEntity> boardEntityList = boardRepository.findAll(Sort.by(Sort.Direction.DESC,"boardCategory","boardId"));

        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    public BoardDTO findByBoardId(Long boardId){
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardId);
        if(optionalBoardEntity.isPresent()){
            //DB에 값이 존재하면 Entity를 DTO로 변환
            return BoardDTO.toBoardDTO(optionalBoardEntity.get());
        }else{
            return null;
        }
    }

    public void write(BoardDTO boardDTO, String mId, MultipartFile file) throws Exception{

        // 1. 프로젝트에 첨부파일을 저장해놓고 끌어올예정, 프로젝트의 경로 가져와야함
        // 소스 코드안에 파일 첨부 x
        String savePath = "C:\\Users\\HKIT\\Downloads\\FileUpload\\";

        //고유 식별자, 임의로 파일이름을 바꿔주기 위한 값
        UUID uuid = UUID.randomUUID();

        //파일 이름 짓기
        String fileName = uuid + "_" + file.getOriginalFilename();
        //new File(어떤 경로에, 어떤 파일을 저장할지) saveFile에 명시
        File saveFile = new File(savePath,fileName);
        //saveFile에 명시한 내용을 가지고 파일 업로드처리
        file.transferTo(saveFile);
        String fileDownPath = savePath + fileName;

        //게시글 작성시 최초 값 초기화
        boardDTO.setBoardMemberWriter(memberRepository.findByMemberId(mId).orElse(null));
        boardDTO.setBoardCommentCnt(0l);
        boardDTO.setBoardDislike(0l);
        boardDTO.setBoardLike(0l);
        boardDTO.setBoardViewCnt(0l);
        boardDTO.setBoardDislike(0l);
        boardDTO.setFileName(fileName);
        boardDTO.setFilePath(fileDownPath);

        BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    public void deleteByBoardDTO(BoardDTO boardDTO){
        boardRepository.deleteById(boardDTO.getBoardId());
    }

    public void update(BoardDTO exDTO, BoardDTO boardDTO){
        exDTO.setBoardTitle(boardDTO.getBoardTitle());
        exDTO.setBoardContent(boardDTO.getBoardContent());
        BoardEntity boardEntity = BoardEntity.toBoardEntity(exDTO);
        boardRepository.save(boardEntity);
    }

    public void like(BoardDTO boardDTO){
        boardDTO.setBoardLike(boardDTO.getBoardLike()+1);
        BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    public void disLike(BoardDTO boardDTO){
        boardDTO.setBoardDislike(boardDTO.getBoardDislike()+1);
        BoardEntity boardEntity = BoardEntity.toBoardEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    public Page<BoardDTO> paging(Pageable pageable){
        int page = pageable.getPageNumber() -1;
        int pageLimit = 15;

        Page<BoardEntity> boardEntityPage = boardRepository.findAll
                (PageRequest.of(page,pageLimit,Sort.by(Sort.Direction.DESC,"boardCategory","boardId")));

        Page<BoardDTO> boardDTOPage = boardEntityPage.map(
                postPage -> BoardDTO.toBoardDTO(postPage)
        );

        return boardDTOPage;
    }


    public void updateViewCnt(Long id){
        Optional<BoardEntity> boardEntity = boardRepository.findById(id);
        if(boardEntity.isPresent()){
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity.get());
            boardDTO.setBoardViewCnt(boardDTO.getBoardViewCnt() + 1);
            boardRepository.save(BoardEntity.toBoardEntity(boardDTO));
        }
    }

    public List<BoardDTO> findByBoardWriter(String boardWriter){
        List<BoardEntity> boardEntityList = boardRepository.findAllByBoardMemberWriter_MemberNic(boardWriter);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity : boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    public int findByBoardWriteCnt(String memberNic) {
       /*회원 닉네임으로 게시글 찾기*/
        List<BoardEntity> boardEntityList = boardRepository.findAllByBoardMemberWriter_MemberNic(memberNic);
        return boardEntityList.size();
    }
}

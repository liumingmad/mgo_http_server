package com.ming.mgo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ming.mgo.dto.SgfDTO;
import com.ming.mgo.dto.SgfSaveRequest;
import com.ming.mgo.entity.Sgf;
import com.ming.mgo.repository.SgfRepository;

@Service
public class SgfService {
    @Autowired
    private SgfRepository sgfRepository;

    public Page<SgfDTO> searchByPlayerName(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timeMs"));
        Page<Sgf> sgfPage = sgfRepository.searchByKeyword(keyword, pageable);
        return sgfPage.map(SgfDTO::from);
    }

    public Optional<Sgf> getById(Long id) {
        return sgfRepository.findById(id);
    }

    public Sgf save(SgfSaveRequest request) {
        Sgf sgf = new Sgf();
        sgf.setTimeMs(System.currentTimeMillis());
        sgf.setBName(request.getBName());
        sgf.setWName(request.getWName());
        sgf.setBLevel(request.getBLevel());
        sgf.setWLevel(request.getWLevel());
        sgf.setSgf(request.getSgf());
        return sgfRepository.save(sgf);
    }
}

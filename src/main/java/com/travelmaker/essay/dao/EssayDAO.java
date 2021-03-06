package com.travelmaker.essay.dao;

import com.travelmaker.essay.domain.EssayDTO;
import com.travelmaker.essay.domain.EssaySearchFilter;

import java.util.List;

public interface EssayDAO {

   public EssayDTO readOneByBno(int bno);

   public EssayDTO readOne(int rno);

   public List<EssayDTO> readAll(int seq);

   public List<EssayDTO> readAll(EssaySearchFilter essaySearchFilter);

   public EssayDTO create(EssayDTO essayDTO);

   public EssayDTO update(EssayDTO essayDTO);

   public void delete(int rno);
}

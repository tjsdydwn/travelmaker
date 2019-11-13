package com.travelmaker.essay.ifs;

import com.travelmaker.essay.domain.EssaySearchFilter;
import com.travelmaker.model.network.Header;

import java.util.List;

public interface EssayApiInterface<Request, Response> {

    Header<Response> create(Header<Request> request);

    String upload(int rno, Request request);

    Header<List<Response>> readAll();

    Header<List<Response>> readAll(EssaySearchFilter essaySearchFilter);

    Header<Response> readOne(int rno);

    Header<Response> update(Header<Request> request);

    Header delete(int rno);


}

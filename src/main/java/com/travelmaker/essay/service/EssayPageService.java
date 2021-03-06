package com.travelmaker.essay.service;

import com.travelmaker.essay.dao.EssayDAO;
import com.travelmaker.essay.domain.EssayDTO;
import com.travelmaker.essay.ifs.EssayPageInterface;
import com.travelmaker.user.dao.UserDAO;
import com.travelmaker.util.fileIO.UploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;

@Service(value = "essayPageService")
public class EssayPageService implements EssayPageInterface {

    @Autowired
    EssayDAO essayDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ServletContext servletContext;
    
    @Autowired
    UploadService uploadService;

    @Override
    public EssayDTO readOne(int rno) {
    	String temp = "essayTxt";
        EssayDTO essayDTO = essayDAO.readOne(rno);
        essayDTO.setContent(readFile(essayDTO.getFileName(), temp));
        essayDTO.setUserDTO(userDAO.getUserDTO(essayDTO.getSeq()));

        return essayDTO;
    }

    public String readFile(String fileName, String temp) {
    	return uploadService.downloadTxt(fileName, temp);
    }
//    private String readFile(String fileName) {
//        String dirPath = servletContext.getRealPath("/resources/storage/essay");
//        BufferedReader br = null;
//        StringBuffer sb = new StringBuffer();
//        try {
//            String line = null;
//            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(dirPath, fileName))));
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return sb.toString();
//    }
}

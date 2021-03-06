package com.travelmaker.util.websockethandler;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.travelmaker.alarm.domain.AlarmDTO;
import com.travelmaker.alarm.service.AlarmServiceImpl;
import com.travelmaker.cash.dao.CashDAO;
import com.travelmaker.cash.domain.CashDTO;
import com.travelmaker.comment.dao.CommentDAO;
import com.travelmaker.essay.dao.EssayDAO;
import com.travelmaker.friend.domain.FriendDTO;
import com.travelmaker.friend.service.FriendServiceImpl;
import com.travelmaker.purchase.domain.PurchaseDTO;
import com.travelmaker.purchase.service.PurchaseImpleService;
import com.travelmaker.route.dao.RouteDAO;
import com.travelmaker.user.domain.UserDTO;
import com.travelmaker.user.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebsocketHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(WebsocketHandler.class);
    private Map<WebSocketSession, String> sessionList = new HashMap<WebSocketSession, String>();

    @Autowired
    private FriendServiceImpl friendServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private AlarmServiceImpl alarmServiceImpl;

    @Autowired
    private PurchaseImpleService purchaseImpleService;

    @Autowired
    private CashDAO cashDAO;

    @Autowired
    private EssayDAO essayDAO;

    @Autowired
    private RouteDAO routeDAO;

    @Autowired
    private CommentDAO commentDAO;

    private JsonParser parser = new JsonParser();

    // 클라이언트와 연결 이후에 실행되는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // System.out.println("**********afterConnectionEstablished************");
        // System.out.println("[EchoHandler]");
        // System.out.println("{" + session.getId() + "} 연결됨");
        // System.out.println("[HTTP 받아오고]");
        Map<String, Object> map = session.getAttributes();

        if (map.get("SPRING_SECURITY_CONTEXT") != null) { // 로그인 경우
            // 원래 값
            String object2String = map.get("SPRING_SECURITY_CONTEXT").toString();
            // 추출값
            String loginId = null;
            Pattern pattern = Pattern.compile("Username[:] (.*?)[;] ");
            Matcher matcher = pattern.matcher(object2String);
            while (matcher.find()) {
                loginId = matcher.group(1);
                if (matcher.group(1) == null)
                    break;
            }
            // System.out.println("로그인 세션 : "+loginId);
            // System.out.println("[세션MAP에 저장]");
            sessionList.put(session, loginId);
        }
        // System.out.println("세션 MAP : " + sessionList.size());

        /*
         * Iterator<WebSocketSession> mapIter = sessionList.keySet().iterator();
         * while(mapIter.hasNext()){ WebSocketSession key = mapIter.next(); Object value
         * = sessionList.get( key );
         * System.out.println("[Key : "+key+"] [value : "+value+"]"); }
         */
    }

    // 클라이언트가 서버로 메시지를 전송했을 때 실행되는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // System.out.println("***********handleTextMessage***********");
        // System.out.println("{" + session.getId() + "}로 부터 {" + message.getPayload() +
        // "} 받음");
        Iterator<WebSocketSession> mapIter = sessionList.keySet().iterator();

        String packHeader = message.getPayload(); //스트링 전체 패킷
        JsonElement element = parser.parse(packHeader); //Json파싱
        String header = element.getAsJsonObject().get("header").getAsString(); //헤더
        JsonElement data = element.getAsJsonObject().get("data");

        String targetId = null; //상대방
        String sendUserId = null; // 보내는자
        String responseMessage = null; //응답메세지
        int dataseq = -1;

        System.out.println(header);

        // 동행신청 알람
        if (header.compareTo("friend") == 0) {
            int fno = data.getAsJsonObject().get("fno").getAsInt();
            sendUserId = data.getAsJsonObject().get("username").getAsString();
            /* 큰글 */
            FriendDTO friendDTO = friendServiceImpl.getFriendDTO(fno);
            UserDTO userDTO = userServiceImpl.getUserDTO(friendDTO.getSeq());

            targetId = userDTO.getId() + "%" + userDTO.getRegisterMethod();
            dataseq = fno;
            responseMessage = sendUserId + " 님이 동행신청을 했습니다";
        }

        if (header.compareTo("pur") == 0) {
            System.out.println("pur");
            int bno = data.getAsJsonObject().get("bno").getAsInt();
            PurchaseDTO purchaseDTO = purchaseImpleService.getPurchaseDTO(String.valueOf(bno));
            targetId = purchaseDTO.getUsername();
            dataseq = bno;
            sendUserId = data.getAsJsonObject().get("username").getAsString();

            System.out.println("DTO : " + purchaseDTO.toString());

            if (purchaseDTO.getCon() == 1) {
                System.out.println("A");
                header = "purA";
            } else if (purchaseDTO.getCon() == 2) {
                System.out.println("B");
                header = "purB";
            }
            responseMessage = sendUserId + " 님이 대리구매 요청을 했습니다";
        }

        if (header.compareTo("cash") == 0) {
            System.out.println("cash");
            int cno = data.getAsJsonObject().get("cno").getAsInt();
            int seq = data.getAsJsonObject().get("seq").getAsInt();
            String status = data.getAsJsonObject().get("status").getAsString();
            sendUserId = data.getAsJsonObject().get("username").getAsString();
            //타겟 아이디를 잡아옵시다.
            CashDTO cashDTO = cashDAO.readOneByCno(cno);
            int targetSeq = cashDTO.getRequestUserSeq() == seq ? cashDTO.getSellerSeq() : cashDTO.getRequestUserSeq();
            UserDTO userDTO = userServiceImpl.getUserDTO(targetSeq);
            targetId = userDTO.getId() + "%" + userDTO.getRegisterMethod();
            dataseq = cno;

            if (status.compareTo("pay") == 0) {
                responseMessage = sendUserId + " 님이 결제를 완료했습니다.";
            } else if (status.compareTo("start") == 0) {
                responseMessage = sendUserId + " 님의 선물을 위해 여행을 떠났습니다.";
            } else if (status.compareTo("end") == 0) {
                responseMessage = sendUserId + " 님이 배송을 완료했습니다.";
            } else if (status.compareTo("done") == 0) {
                responseMessage = sendUserId + " 님이 선물을 수령했습니다.";
            }
        }

        if (header.compareTo("cmt") == 0) {

            int rno = data.getAsJsonObject().get("rno").getAsInt();
            int cno = data.getAsJsonObject().get("cno").getAsInt();
            String category = data.getAsJsonObject().get("category").getAsString();
            String action = data.getAsJsonObject().get("action").getAsString();
            sendUserId = data.getAsJsonObject().get("username").getAsString();
            dataseq = rno;
            int targetSeq = 0;

            if (action.compareTo("cmt") == 0) {
                if (category.compareTo("essay") == 0) {
                    targetSeq = essayDAO.readOne(rno).getSeq();
                } else {
                    targetSeq = routeDAO.getRoute(rno).getSeq();
                }
                UserDTO userDTO = userServiceImpl.getUserDTO(targetSeq);
                targetId = userDTO.getId() + "%" + userDTO.getRegisterMethod();
                responseMessage = sendUserId + " 님이 댓글을 남기셨습니다.";
            } else if (!(action.compareTo("cmt") == 0)) {
                targetSeq = commentDAO.readOne(cno).getSeq();
                UserDTO userDTO = userServiceImpl.getUserDTO(targetSeq);
                targetId = userDTO.getId() + "%" + userDTO.getRegisterMethod();

                if (action.compareTo("like") == 0) {
                    responseMessage = sendUserId + " 님이 댓글을 좋아합니다.";
                } else if (action.compareTo("unlike") == 0) {
                    responseMessage = sendUserId + " 님이 댓글을 싫어합니다.";
                } else if (action.compareTo("re") == 0) {
                    responseMessage = sendUserId + " 님이 댓글에 대한 답글을 작성했습니다.";
                }
            }
        }

        //알람 생성
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setHeader(header);
        alarmDTO.setDataSeq(dataseq);
        alarmDTO.setSendUserFid(sendUserId);
        alarmDTO.setSendUserId(sendUserId.split("%")[0]);
        alarmDTO.setRequestUserFid(targetId);
        alarmDTO.setRequestUserId(targetId.split("%")[0]);
        alarmDTO.setContent(responseMessage);
        System.out.println("1." + alarmDTO.toString());
        alarmServiceImpl.addAlarm(alarmDTO);

        //ResponsePackage
        ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.setHeader(header);
        responsePacket.setData(responseMessage);
        responsePacket.setNo(dataseq); //대행 : fno //대리구매 : bno //결제 cno

        while (mapIter.hasNext()) {
            WebSocketSession key = mapIter.next();
            Object value = sessionList.get(key);
            //System.out.println(key + " : " + value);
            //System.out.println("Message:" + message);
            //System.out.println("MessagePayload : " + message.getPayload());
            if (targetId.equals(value)) {
                key.sendMessage(new TextMessage(responsePacket.toString()));
            }
        }

    }

    // 클라이언트와 연결을 끊었을 때 실행되는 메소드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // System.out.println("**************afterConnectionClosed*************");
        sessionList.remove(session);
        // System.out.println("{" + session.getId() + "} 연결끊김");
        // System.out.println("연결종료 후 MAP Size : "+sessionList.size());
    }
}
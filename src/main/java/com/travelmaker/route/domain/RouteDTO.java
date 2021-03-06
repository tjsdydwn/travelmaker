package com.travelmaker.route.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDTO {
	private int bno; //rno 따라다님
	private int rno; //웹에서
	private int seq;
	private String nickname; //세션에서?
	private String title; // 처음 저장할때
	private String content; //웹에서
	private String imageName; // 처음 저장할때
	private int likes; //디폴트
	private int views; //디폴트
	private String hashtag; //웹에서
	private String dateWrite; //디폴트
	private String dateUpdate; //디폴트
	private String dateFrom;
	private String dateTo;
	private int cmt; //디폴트
	private int fixed; //웹에서
	private int isDomestic; //웹&DB 비교
}
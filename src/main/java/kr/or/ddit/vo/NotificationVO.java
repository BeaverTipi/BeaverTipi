package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="notifId")
public class NotificationVO implements Serializable{
	private String notifId;
	private String mbrCd;
	private String notifTitle;
	private String notifMsg;
	private String notifDt;
	private String notifTypeCd;
	private String notifTypeGroupCd;
	private String notifRefUrl;
	private boolean notifReadYn;
	private boolean notifDelYn;
}

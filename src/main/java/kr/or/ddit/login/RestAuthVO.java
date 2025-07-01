package kr.or.ddit.login;

import java.io.Serializable;

import lombok.Data;

@Data
public class RestAuthVO implements Serializable{
	private String username;
	private String password;
}
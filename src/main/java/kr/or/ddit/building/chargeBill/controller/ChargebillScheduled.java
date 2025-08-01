package kr.or.ddit.building.chargeBill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import kr.or.ddit.building.chargeBill.service.PaymentsReceiptService;

@Component
public class ChargebillScheduled {

	@Autowired
	PaymentsReceiptService service;
	
	@Scheduled(cron = "0 0 0 * * *")
	public void run() {
		service.modifyOverdue();
	}
	
	
	@PostConstruct
	public void init() {
	    service.modifyOverdue(); 
	}
	
}

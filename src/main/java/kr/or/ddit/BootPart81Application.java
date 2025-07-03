package kr.or.ddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySources;
@EnableScheduling
@EnableEncryptableProperties
@SpringBootApplication
public class BootPart81Application {

	public static void main(String[] args) {
		SpringApplication.run(BootPart81Application.class, args);
	}

}

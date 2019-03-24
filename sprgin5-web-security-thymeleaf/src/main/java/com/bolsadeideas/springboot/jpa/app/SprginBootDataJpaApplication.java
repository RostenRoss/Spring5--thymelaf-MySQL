package com.bolsadeideas.springboot.jpa.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.jpa.app.service.IUploadFileServiceImpl;

@SpringBootApplication
public class SprginBootDataJpaApplication implements CommandLineRunner{
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Autowired
	IUploadFileServiceImpl iUploadFileServiceImpl;
	public static void main(String[] args) {
		SpringApplication.run(SprginBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		iUploadFileServiceImpl.deleteAll();//borra la carpeta uploads al iniciar el proyecto
		iUploadFileServiceImpl.init();//crea la carpeta de uploads al iniciar el proyecto
		String password="123";
		
		for(int i=0;i<2;i++) {
			String bcrypPass=passwordEncoder.encode(password);
			System.out.println(bcrypPass);
		}
	}

}

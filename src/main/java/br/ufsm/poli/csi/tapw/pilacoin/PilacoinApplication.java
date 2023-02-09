package br.ufsm.poli.csi.tapw.pilacoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "br.ufsm.poli.csi.tapw.pilacoin")
@EntityScan("br.ufsm.poli.csi.tapw.pilacoin")
@SpringBootApplication(scanBasePackages = "br.ufsm.poli.csi.tapw.pilacoin")
public class PilacoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(PilacoinApplication.class, args);
	}

}

package cn.net.insurance.account.cms.connector;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@MapperScan({"cn.net.insurance.account.cms.connector.mapper"})
@ComponentScan(value = {"cn.net.insurance"})
@EnableFeignClients(value = {"cn.net.insurance.account"})
@EnableDiscoveryClient
@EnableAsync
public class InsuranceAccountCmsConnectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsuranceAccountCmsConnectorApplication.class, args);
    }

}

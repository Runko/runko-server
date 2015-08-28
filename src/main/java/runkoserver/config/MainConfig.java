/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.config;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig {

    @Bean
    public BasicDataSource dataSource() throws URISyntaxException {
        URI dbUri = new URI(System.getenv("postgres://mboghjrckryzcv:veqou3IR0Q5ueF4UjXVuSe_8c2@ec2-54-197-238-19.compute-1.amazonaws.com:5432/d4vebfk3meqoiu"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);

        return basicDataSource;
    }
}
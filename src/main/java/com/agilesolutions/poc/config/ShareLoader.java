package com.agilesolutions.poc.config;

import com.agilesolutions.poc.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShareLoader implements InitializingBean {

    private final WalletRepository walletRepository;

    @Override
    public void afterPropertiesSet() throws Exception {

        insert into share(id, company, quantity) values (1, 'AAPL', 100);
        insert into share(id, company, quantity) values (2, 'AMZN', 300);
        insert into share(id, company, quantity) values (3, 'META', 300);
        insert into share(id, company, quantity) values (4, 'MSFT', 400);
        insert into share(id, company, quantity) values (5, 'NVDA', 200);

    }
}

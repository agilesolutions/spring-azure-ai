package com.agilesolutions.poc.tools;

import com.agilesolutions.poc.model.Share;
import com.agilesolutions.poc.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletTools {

    private WalletRepository walletRepository;

    @Tool(description = "Number of shares for each company in my wallet")
    public List<Share> getNumberOfShares() {
        return (List<Share>) walletRepository.findAll();
    }
}
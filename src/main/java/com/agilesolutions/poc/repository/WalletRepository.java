package com.agilesolutions.poc.repository;

import com.agilesolutions.poc.model.Share;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Share, Long> {
}

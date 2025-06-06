package com.capgemini.hospital_management_system.repository;

import com.capgemini.hospital_management_system.model.Block;
import com.capgemini.hospital_management_system.model.BlockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, BlockId> {
}

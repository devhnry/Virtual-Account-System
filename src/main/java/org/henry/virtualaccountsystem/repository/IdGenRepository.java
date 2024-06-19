package org.henry.virtualaccountsystem.repository;

import org.henry.virtualaccountsystem.entity.IdGen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdGenRepository extends JpaRepository<IdGen, Long> {
}

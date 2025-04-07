package com.carddemo.repository;

import com.carddemo.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardId(Long cardId);
    Page<Transaction> findByCardId(Long cardId, Pageable pageable);
    
    List<Transaction> findByCardIdAndType(Long cardId, String type);
    Page<Transaction> findByCardIdAndType(Long cardId, String type, Pageable pageable);

    // Customized search method with filter support
    @Query("SELECT t FROM Transaction t WHERE " +
           "(:startDate IS NULL OR t.transactionDate >= :startDate) AND " +
           "(:endDate IS NULL OR t.transactionDate <= :endDate) AND " +
           "(:type IS NULL OR t.type = :type)")
    List<Transaction> findTransactionsByFilter(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("type") String type
    );
    
    // Customized search method with filter support and pagination
    @Query("SELECT t FROM Transaction t WHERE " +
           "(:startDate IS NULL OR t.transactionDate >= :startDate) AND " +
           "(:endDate IS NULL OR t.transactionDate <= :endDate) AND " +
           "(:type IS NULL OR t.type = :type)")
    Page<Transaction> findTransactionsByFilterWithPagination(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("type") String type,
        Pageable pageable
    );
}
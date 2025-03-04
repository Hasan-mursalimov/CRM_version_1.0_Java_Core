package services;


import models.Deal;

import java.time.LocalDate;

public interface DealService {

    void save(Long id, String title, Long clientId, Long userId, Double amount, Deal.Status status, LocalDate createdDate, LocalDate closedDate);
    void updateDeal(Long id, String title, Double amount, Deal.Status status);
    void deleteDeal(Long id);
}

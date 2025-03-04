package services;


import models.Deal;
import repositories.DealRepository;

import java.time.LocalDate;

/**
 * Сервис для управления сделками.
 * Предоставляет методы для сохранения сделок.
 */
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;

    /**
     * Конструктор сервиса.
     *
     * @param dealRepository репозиторий для работы со сделками
     * @throws NullPointerException если dealRepository равен null
     */
    public DealServiceImpl(DealRepository dealRepository) {
        if (dealRepository == null) {
            throw new NullPointerException("DealRepository не может быть null");
        }
        this.dealRepository = dealRepository;
    }

    /**
     * Сохраняет сделку.
     *
     * @param id          уникальный идентификатор сделки
     * @param title       название сделки
     * @param clientId    идентификатор клиента
     * @param userId      идентификатор пользователя
     * @param amount      сумма сделки
     * @param status      статус сделки
     * @param createdDate дата создания сделки
     * @param closedDate  дата закрытия сделки
     * @throws IllegalArgumentException если любой из параметров некорректен
     */
    @Override
    public void save(Long id, String title, Long clientId, Long userId, Double amount, Deal.Status status, LocalDate createdDate, LocalDate closedDate) {
        if (id == null || title == null || clientId == null || userId == null || amount == null || status == null || createdDate == null) {
            throw new IllegalArgumentException("Ни один из параметров не может быть null");
        }
        if (title.trim().isEmpty() || amount < 0) {
            throw new IllegalArgumentException("Название сделки не может быть пустым, а сумма должна быть положительной");
        }

        Deal deal = new Deal(id, title, clientId, userId, amount, status, createdDate, closedDate);
        dealRepository.save(deal);
    }

    @Override
    public void updateDeal(Long id, String title, Double amount, Deal.Status status) {
        if (id == null || title == null || amount == null || status == null) {
            throw new IllegalArgumentException("Ни один из параметров не может быть null");
        }
        // Логика обновления сделки
    }

    @Override
    public void deleteDeal(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор сделки не может быть null");
        }
        // Логика удаления сделки

    }
}

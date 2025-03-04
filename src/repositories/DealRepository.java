package repositories;


import models.Deal;

public interface DealRepository extends CrudRepository<Deal> {
    void updateDeal(Deal updatedDeal);
}

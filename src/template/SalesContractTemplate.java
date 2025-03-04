package template;


import models.Client;

public class SalesContractTemplate extends DocumentTemplate{
    @Override
    protected void addHeader() {
        System.out.println("=== Договор продаж ===");
    }

    @Override
    protected void addClientDetails(Client client) {
        System.out.println("Клиент: " + client.getName());
        System.out.println("Email: " + client.getEmail());
        System.out.println("Телефон: " + client.getPhone());
        System.out.println("Адрес: " + client.getAddress());
    }

    @Override
    protected void addTerms() {
        System.out.println("Условия:");
        System.out.println("1. Оплата в течение 30 дней.");
        System.out.println("2. Доставка в течение 5 рабочих дней.");
    }

    @Override
    protected void addFooter() {
        System.out.println("Подпись: _______________");
    }
}

package template;


import models.Client;

public abstract class DocumentTemplate {
    // Шаблонный метод
    public final void createDocument(Client client) {
        addHeader();
        addClientDetails(client);
        addTerms();
        addFooter();
        saveDocument();
    }

    // Шаги, которые должны быть реализованы в подклассах
    protected abstract void addHeader();
    protected abstract void addClientDetails(Client client);
    protected abstract void addTerms();
    protected abstract void addFooter();

    // Общий метод для сохранения документа
    protected void saveDocument() {
        System.out.println("Документ сохранен.");
    }
}

package cd.gbc.inventoryservice.service;

public interface InventoryService {
    public boolean isInStock(String skuCode, Integer quantity);
}

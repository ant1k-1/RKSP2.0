package orderservice.service;


import orderservice.client.BookDto;
import orderservice.client.InventoryClient;
import orderservice.entity.Order;
import orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    public Order placeOrder(Long bookId, int quantity) {
        BookDto book = inventoryClient.getBookById(bookId);
        if (book != null && book.getStock() >= quantity) {
            // Здесь можно было бы обновить сток в InventoryService (потребуется метод там)
            Order order = new Order();
            order.setBookId(bookId);
            order.setQuantity(quantity);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Недостаточно товара на складе");
        }
    }
}

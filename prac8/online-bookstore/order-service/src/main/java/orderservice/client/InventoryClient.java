// order-service/src/main/java/com/example/orderservice/client/InventoryClient.java
package orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "inventory-service")
public interface InventoryClient {
    @GetMapping("/books/{id}")
    BookDto getBookById(@PathVariable("id") Long id);
}

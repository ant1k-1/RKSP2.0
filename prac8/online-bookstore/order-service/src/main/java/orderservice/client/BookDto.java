// order-service/src/main/java/com/example/orderservice/client/BookDto.java
package orderservice.client;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private int stock;
}

package kz.tansh;

import kz.tansh.proto.v5.Book;
import kz.tansh.proto.v5.Library;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class L5_Collections {

  public static void main(String[] args) {
    Book book1 = Book.newBuilder().setName("Harry Potter v1").setAuthor("J K Rouling").build();
    Book book2 = book1.toBuilder().setName("Harry Potter v2").build();
    Book book3 = book1.toBuilder().setName("Harry Potter v3").build();

    // единичная вставка
    Library library = Library.newBuilder()
                             .setName("CityLib #1")
                             .addBooks(book1)
                             .addBooks(book2)
                             .addBooks(book3)
                             .build();

    // use of Array
    Library library2 = Library.newBuilder()
                              .setName("CityLib #2")
                              .addAllBooks(List.of(book1, book2, book3))
                              .build();

    log.info("{}", library);
    log.info("{}", library2);
  }
}

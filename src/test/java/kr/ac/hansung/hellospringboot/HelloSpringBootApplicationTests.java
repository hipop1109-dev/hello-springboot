package kr.ac.hansung.hellospringboot;

import kr.ac.hansung.hellospringboot.model.Product;
import kr.ac.hansung.hellospringboot.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HelloSpringBootApplicationTests {

    @Autowired
    private ProductService productService;

    @Test
    void contextLoads() {
    }

    @Test
    void testProductSearchAndPaging() {
        // Save test products
        Product p1 = new Product();
        p1.setName("Apple iPad");
        p1.setPrice(800000);
        p1.setDescription("Tablet");
        productService.createProduct(p1);

        Product p2 = new Product();
        p2.setName("Apple iPhone");
        p2.setPrice(1200000);
        p2.setDescription("Phone");
        productService.createProduct(p2);

        Product p3 = new Product();
        p3.setName("Samsung Galaxy");
        p3.setPrice(1100000);
        p3.setDescription("Phone");
        productService.createProduct(p3);

        // Test getAllProducts with Pageable
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> allProducts = productService.getAllProducts(pageable);
        assertThat(allProducts.getContent()).hasSize(2);

        // Test searchProducts with Pageable
        Page<Product> appleProducts = productService.searchProducts("Apple", PageRequest.of(0, 10));
        assertThat(appleProducts.getContent()).hasSize(2);
        assertThat(appleProducts.getContent()).extracting(Product::getName).containsExactlyInAnyOrder("Apple iPad", "Apple iPhone");
    }

}

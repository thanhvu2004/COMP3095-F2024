package ca.gbc.productservice.controller;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<ProductResponse> getAllProducts() throws InterruptedException {
//        Thread.sleep(5000); // Simulate a delay of 5 seconds
//        return productService.getAllProducts();
//    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateProduct(@PathVariable String id, @RequestBody ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
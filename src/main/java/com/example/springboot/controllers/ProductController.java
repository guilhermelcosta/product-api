package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ProductModel> save(@RequestBody @Valid ProductRecordDto productRecordDto) {

        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel); /*Faz a conversao de DTO para Model*/

        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> obj = productRepository.findById(id);
        return !obj.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(obj.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductRecordDto productRecordDto) {

        Optional<ProductModel> obj = productRepository.findById(id);

        if (obj.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");

        var productModel = obj.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id) {

        Optional<ProductModel> obj = productRepository.findById(id);

        if (obj.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");

        productRepository.delete(obj.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
    }
}

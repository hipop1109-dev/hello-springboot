package kr.ac.hansung.hellospringboot.controller;

import kr.ac.hansung.hellospringboot.dto.ProductDto;
import kr.ac.hansung.hellospringboot.model.Product;
import kr.ac.hansung.hellospringboot.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String listProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Product> productPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            productPage = productService.searchProducts(keyword.trim(), pageable);
            model.addAttribute("keyword", keyword.trim());
        } else {
            productPage = productService.getAllProducts(pageable);
        }

        model.addAttribute("productPage", productPage);
        return "products/list";
    }

    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        ProductDto productDto = new ProductDto(
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getDescription()
        );

        model.addAttribute("product", productDto);
        model.addAttribute("productId", id);
        return "products/edit";
    }

    @PostMapping("/products/{id}/edit")
    public String editProduct(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("product") ProductDto productDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "products/edit";
        }

        productService.updateProduct(id, productDto);
        redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        return "redirect:/products";
    }
}

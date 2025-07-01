package com.eewms.controller;

import com.eewms.dto.ProductFormDTO;
import com.eewms.dto.ProductDetailsDTO;
import com.eewms.exception.InventoryException;
import com.eewms.services.IProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"/products", "/product-list"})
@RequiredArgsConstructor
public class ProductController {
    private final IProductServices productService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAll());
        return "product-list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("productForm", new ProductFormDTO());
        return "products/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ProductFormDTO productForm,
                       BindingResult br,
                       RedirectAttributes ra) {
        if (br.hasErrors()) {
            return "products/form";
        }
        try {
            if (productForm.getId() == null) productService.create(productForm);
            else productService.update(productForm.getId(), productForm);
            ra.addFlashAttribute("success", "Lưu sản phẩm thành công");
        } catch (InventoryException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "products/form";
        }
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("product", productService.getById(id));
            return "products/detail";
        } catch (InventoryException e) {
            return "redirect:/products";
        }
    }
}
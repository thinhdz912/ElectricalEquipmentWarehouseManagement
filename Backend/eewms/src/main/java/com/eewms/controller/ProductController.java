package com.eewms.controller;

import com.eewms.dto.ProductFormDTO;
import com.eewms.dto.ProductDetailsDTO;
import com.eewms.constant.SettingType;
import com.eewms.exception.InventoryException;
import com.eewms.services.IProductServices;
import com.eewms.services.ISettingServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping({"/products", "/product-list"})
@RequiredArgsConstructor
public class ProductController {
    private final IProductServices productService;
    private final ISettingServices settingService;
    @GetMapping
    public String list(Model model) throws InventoryException {
        model.addAttribute("products", productService.getAll());
        model.addAttribute("productDTO", new ProductFormDTO());
        model.addAttribute("units",      settingService.getByType(SettingType.UNIT));
        model.addAttribute("brands",     settingService.getByType(SettingType.BRAND));
        model.addAttribute("categories", settingService.getByType(SettingType.CATEGORY));
        return "product-list";
    }

    // xử lý submit modal form
    @PostMapping
    public String create(@ModelAttribute("productDTO") ProductFormDTO dto,
                         RedirectAttributes ra) {
        try {
            productService.create(dto);
            ra.addFlashAttribute("success", "Thêm sản phẩm thành công");
        } catch (InventoryException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/products";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute ProductFormDTO productForm,
            BindingResult br,
            RedirectAttributes ra,
            Model model) {
        if (br.hasErrors()) {
            return "products/form";
        }
        try {
            if (productForm.getId() == null)
                productService.create(productForm);
            else
                productService.update(productForm.getId(), productForm);
            ra.addFlashAttribute("success", "Lưu thành công");
            return "redirect:/products";
        } catch (InventoryException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "products/form";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        try {
            ProductDetailsDTO dto = productService.getById(id);
            model.addAttribute("product", dto);
            return "products/detail";
        } catch (InventoryException ex) {
            return "redirect:/products";
        }
    }
}

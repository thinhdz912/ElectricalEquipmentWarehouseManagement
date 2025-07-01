package com.eewms.services.impl;

import com.eewms.dto.*;
import com.eewms.entities.*;
import com.eewms.exception.InventoryException;
import com.eewms.repository.*;
import com.eewms.services.IProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServicesImpl implements IProductServices {
    private final ProductRepository productRepo;
    private final SettingRepository settingRepo;
    private final ImagesRepository imageRepo;

    @Override
    @Transactional
    public ProductDetailsDTO create(ProductFormDTO dto) throws InventoryException {
        if (productRepo.existsByCode(dto.getCode())) {
            throw new InventoryException("Mã sản phẩm đã tồn tại");
        }
        return saveOrUpdate(null, dto);
    }

    @Override
    @Transactional
    public ProductDetailsDTO update(Integer id, ProductFormDTO dto) throws InventoryException {
        if (!productRepo.existsById(id)) {
            throw new InventoryException("Sản phẩm không tồn tại");
        }
        return saveOrUpdate(id, dto);
    }

    private ProductDetailsDTO saveOrUpdate(Integer id, ProductFormDTO dto) throws InventoryException {
        Setting unit = settingRepo.findById(dto.getUnitId())
                .orElseThrow(() -> new InventoryException("Đơn vị không tồn tại"));
        Setting brand = settingRepo.findById(dto.getBrandId())
                .orElseThrow(() -> new InventoryException("Thương hiệu không tồn tại"));
        Setting category = settingRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new InventoryException("Danh mục không tồn tại"));

        Product product = Product.builder()
                .id(id)
                .code(dto.getCode())
                .name(dto.getName())
                .originPrice(dto.getOriginPrice())
                .listingPrice(dto.getListingPrice())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .quantity(dto.getQuantity())
                .unit(unit)
                .brand(brand)
                .category(category)
                .build();

        Product saved = productRepo.save(product);

        imageRepo.deleteByProductId(saved.getId());
        List<Image> images = dto.getImages() == null ? List.of()
                : dto.getImages().stream()
                .map(url -> Image.builder().imageUrl(url).product(saved).build())
                .toList();
        imageRepo.saveAll(images);

        List<ImageDTO> imgDtos = images.stream()
                .map(img -> ImageDTO.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        return ProductDetailsDTO.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .name(saved.getName())
                .originPrice(saved.getOriginPrice())
                .listingPrice(saved.getListingPrice())
                .description(saved.getDescription())
                .status(saved.getStatus())
                .quantity(saved.getQuantity())
                .unitName(unit.getName())
                .brandName(brand.getName())
                .categoryName(category.getName())
                .images(imgDtos)
                .build();
    }

    @Override
    @Transactional
    public void delete(Integer id) throws InventoryException {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new InventoryException("Sản phẩm không tồn tại"));
        imageRepo.deleteByProductId(id);
        productRepo.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailsDTO getById(Integer id) throws InventoryException {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new InventoryException("Sản phẩm không tồn tại"));
        List<ImageDTO> imgDtos = imageRepo.findByProductId(id).stream()
                .map(img -> ImageDTO.builder().id(img.getId()).imageUrl(img.getImageUrl()).build())
                .collect(Collectors.toList());
        return ProductDetailsDTO.builder()
                .id(p.getId())
                .code(p.getCode())
                .name(p.getName())
                .originPrice(p.getOriginPrice())
                .listingPrice(p.getListingPrice())
                .description(p.getDescription())
                .status(p.getStatus())
                .quantity(p.getQuantity())
                .unitName(p.getUnit().getName())
                .brandName(p.getBrand().getName())
                .categoryName(p.getCategory().getName())
                .images(imgDtos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDetailsDTO> getAll() throws InventoryException {
        return productRepo.findAll().stream()
                .map(p -> {
                    try {
                        return getById(p.getId());
                    } catch (InventoryException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
package com.eewms.services.impl;

import com.eewms.dto.*;
import com.eewms.entities.*;
import com.eewms.exception.InventoryException;
import com.eewms.repository.ImagesRepository;
import com.eewms.constant.SettingType;
import com.eewms.repository.ProductRepository;
import com.eewms.repository.SettingRepository;
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

    private SettingDTO mapSetting(Setting s) {
        return SettingDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .type(s.getType())
                .priority(s.getPriority())
                .description(s.getDescription())
                .status(s.getStatus())
                .build();
    }

    private List<ImageDTO> mapImages(List<Image> imgs) {
        return imgs.stream().map(i -> ImageDTO.builder()
                        .id(i.getId())
                        .imageUrl(i.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDetailsDTO create(ProductFormDTO dto) throws InventoryException {
        if (productRepo.existsByCode(dto.getCode()))
            throw new InventoryException("Mã sản phẩm đã tồn tại");
        return saveOrUpdate(null, dto);
    }

    @Override
    @Transactional
    public ProductDetailsDTO update(Integer id, ProductFormDTO dto) throws InventoryException {
        if (!productRepo.existsById(id))
            throw new InventoryException("Sản phẩm không tồn tại");
        return saveOrUpdate(id, dto);
    }

    private ProductDetailsDTO saveOrUpdate(Integer id, ProductFormDTO dto) throws InventoryException {
        Setting u = settingRepo.findById(dto.getUnitId())
                .orElseThrow(() -> new InventoryException("Đơn vị không tồn tại"));
        Setting c = settingRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new InventoryException("Danh mục không tồn tại"));
        Setting b = settingRepo.findById(dto.getBrandId())
                .orElseThrow(() -> new InventoryException("Thương hiệu không tồn tại"));

        Product p = Product.builder()
                .id(id)
                .code(dto.getCode())
                .name(dto.getName())
                .originPrice(dto.getOriginPrice())
                .listingPrice(dto.getListingPrice())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .quantity(dto.getQuantity())
                .unit(u)
                .category(c)
                .brand(b)
                .build();

        Product saved = productRepo.save(p);
        imageRepo.deleteByProductId(saved.getId());
        List<Image> imgs = dto.getImages()==null? List.of() : dto.getImages().stream()
                .map(url -> Image.builder().imageUrl(url).product(saved).build())
                .collect(Collectors.toList());
        imageRepo.saveAll(imgs);

        return ProductDetailsDTO.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .name(saved.getName())
                .originPrice(saved.getOriginPrice())
                .listingPrice(saved.getListingPrice())
                .description(saved.getDescription())
                .status(saved.getStatus())
                .quantity(saved.getQuantity())
                .unit(mapSetting(u))
                .category(mapSetting(c))
                .brand(mapSetting(b))
                .images(mapImages(imgs))
                .build();
    }

    @Override
    @Transactional
    public void delete(Integer id) throws InventoryException {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new InventoryException("Sản phẩm không tồn tại"));
        imageRepo.deleteByProductId(id);
        productRepo.delete(p);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailsDTO getById(Integer id) throws InventoryException {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new InventoryException("Sản phẩm không tồn tại"));
        return ProductDetailsDTO.builder()
                .id(p.getId())
                .code(p.getCode())
                .name(p.getName())
                .originPrice(p.getOriginPrice())
                .listingPrice(p.getListingPrice())
                .description(p.getDescription())
                .status(p.getStatus())
                .quantity(p.getQuantity())
                .unit(mapSetting(p.getUnit()))
                .category(mapSetting(p.getCategory()))
                .brand(mapSetting(p.getBrand()))
                .images(mapImages(imageRepo.findByProductId(id)))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDetailsDTO> getAll() throws InventoryException {
        return productRepo.findAll().stream()
                .map(p -> {
                    try { return getById(p.getId()); }
                    catch (InventoryException e) { throw new RuntimeException(e); }
                }).collect(Collectors.toList());
    }

    @Override
    public List<SettingDTO> getSettingOptions(SettingType type) {
        return settingRepo.findByType(type).stream()
                .map(this::mapSetting)
                .collect(Collectors.toList());
    }
}
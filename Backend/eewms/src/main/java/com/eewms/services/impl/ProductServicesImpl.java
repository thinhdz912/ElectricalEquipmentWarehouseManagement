package com.eewms.services.impl;

import com.eewms.dto.ImageDTO;
import com.eewms.dto.ProductCreateDTO;
import com.eewms.dto.ProductDetailsDTO;
import com.eewms.dto.ProductUpdateDTO;
import com.eewms.entities.Image;
import com.eewms.entities.Product;
import com.eewms.entities.Setting;
import com.eewms.exception.InventoryException;
import com.eewms.repository.ImagesRepository;
import com.eewms.repository.ProductRepository;
import com.eewms.repository.SettingRepository;
import com.eewms.services.IProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServicesImpl implements IProductServices {

    private final ProductRepository productRepo;
    private final SettingRepository settingRepo;
    private final ImagesRepository imageRepo;

    @Override
    public ProductDetailsDTO create(ProductCreateDTO dto) throws InventoryException {
        // Validate: code đã tồn tại chưa
        if (productRepo.existsByCode(dto.getCode())) {
            throw new InventoryException("Mã sản phẩm đã tồn tại");
        }

        // Lấy các setting liên quan (unit, brand, category)
        Setting unit = settingRepo.findById(dto.getUnitId())
                .orElseThrow(() -> new InventoryException("Đơn vị không tồn tại"));
        Setting brand = settingRepo.findById(dto.getBrandId())
                .orElseThrow(() -> new InventoryException("Thương hiệu không tồn tại"));
        Setting category = settingRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new InventoryException("Danh mục không tồn tại"));

        // Tạo đối tượng Product
        Product product = Product.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .originPrice(dto.getOriginPrice())
                .listingPrice(dto.getListingPrice())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .unit(unit)
                .brand(brand)
                .category(category)
                .build();

        // Lưu Product vào DB
        Product saved = productRepo.save(product);

        // Lưu ảnh nếu có
        List<Image> images = dto.getImages() != null
                ? dto.getImages().stream()
                .map(url -> Image.builder()
                        .imageUrl(url)
                        .product(saved)
                        .build())
                .toList()
                : List.of();
        imageRepo.saveAll(images);

        // Convert list image sang DTO
        List<ImageDTO> imageDTOs = images.stream()
                .map(img -> ImageDTO.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .build())
                .toList();

        // Trả về ProductDetailDTO
        return ProductDetailsDTO.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .name(saved.getName())
                .originPrice(saved.getOriginPrice())
                .listingPrice(saved.getListingPrice())
                .description(saved.getDescription())
                .status(saved.getStatus())
                .unitName(unit.getName())
                .brandName(brand.getName())
                .categoryName(category.getName())
                .images(imageDTOs)
                .build();
    }

    @Override
    public ProductDetailsDTO update(Integer id, ProductUpdateDTO dto) throws InventoryException {
        // Lấy product theo ID
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new InventoryException("Sản phẩm không tồn tại"));

        // Lấy setting liên quan
        Setting unit = settingRepo.findById(dto.getUnitId())
                .orElseThrow(() -> new InventoryException("Đơn vị không tồn tại"));
        Setting brand = settingRepo.findById(dto.getBrandId())
                .orElseThrow(() -> new InventoryException("Thương hiệu không tồn tại"));
        Setting category = settingRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new InventoryException("Danh mục không tồn tại"));

        // Cập nhật thông tin
        product.setCode(dto.getCode());
        product.setName(dto.getName());
        product.setOriginPrice(dto.getOriginPrice());
        product.setListingPrice(dto.getListingPrice());
        product.setDescription(dto.getDescription());
        product.setStatus(dto.getStatus());
        product.setUnit(unit);
        product.setBrand(brand);
        product.setCategory(category);

        // Lưu lại product
        Product saved = productRepo.save(product);

        // Xóa toàn bộ ảnh cũ
        imageRepo.deleteByProductId(id);

        // Lưu ảnh mới nếu có
        List<Image> images = dto.getImages() != null
                ? dto.getImages().stream()
                .map(url -> Image.builder()
                        .imageUrl(url)
                        .product(saved)
                        .build())
                .toList()
                : List.of();
        imageRepo.saveAll(images);

        // Convert list image sang DTO
        List<ImageDTO> imageDTOs = images.stream()
                .map(img -> ImageDTO.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .build())
                .toList();

        // Trả lại DTO chi tiết
        return ProductDetailsDTO.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .name(saved.getName())
                .originPrice(saved.getOriginPrice())
                .listingPrice(saved.getListingPrice())
                .description(saved.getDescription())
                .status(saved.getStatus())
                .unitName(unit.getName())
                .brandName(brand.getName())
                .categoryName(category.getName())
                .images(imageDTOs)
                .build();
    }

    @Override
    public void delete(Integer id) throws InventoryException {
        // Kiểm tra sản phẩm có tồn tại không
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new InventoryException("Sản phẩm không tồn tại"));

        // Xoá toàn bộ ảnh liên quan trước
        imageRepo.deleteByProductId(id);

        // Xoá product
        productRepo.delete(product);
    }

    @Override
    public ProductDetailsDTO getById(Integer id) throws InventoryException {
        // Tìm product
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new InventoryException("Sản phẩm không tồn tại"));

        // Lấy danh sách ảnh theo productId
        List<Image> images = imageRepo.findByProductId(product.getId());

        // Convert ảnh sang DTO
        List<ImageDTO> imageDTOs = images.stream()
                .map(img -> ImageDTO.builder()
                        .id(img.getId())
                        .imageUrl(img.getImageUrl())
                        .build())
                .toList();

        // Trả về DTO chi tiết
        return ProductDetailsDTO.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .originPrice(product.getOriginPrice())
                .listingPrice(product.getListingPrice())
                .description(product.getDescription())
                .status(product.getStatus())
                .unitName(product.getUnit().getName())
                .brandName(product.getBrand().getName())
                .categoryName(product.getCategory().getName())
                .images(imageDTOs)
                .build();
    }

    @Override
    public List<ProductDetailsDTO> getAll() {
        // Lấy tất cả sản phẩm
        List<Product> products = productRepo.findAll();

        return products.stream().map(product -> {
            // Lấy ảnh theo product
            List<Image> images = imageRepo.findByProductId(product.getId());

            // Convert ảnh sang DTO
            List<ImageDTO> imageDTOs = images.stream()
                    .map(img -> ImageDTO.builder()
                            .id(img.getId())
                            .imageUrl(img.getImageUrl())
                            .build())
                    .toList();

            // Build DTO cho từng sản phẩm
            return ProductDetailsDTO.builder()
                    .id(product.getId())
                    .code(product.getCode())
                    .name(product.getName())
                    .originPrice(product.getOriginPrice())
                    .listingPrice(product.getListingPrice())
                    .description(product.getDescription())
                    .status(product.getStatus())
                    .unitName(product.getUnit().getName())
                    .brandName(product.getBrand().getName())
                    .categoryName(product.getCategory().getName())
                    .images(imageDTOs)
                    .build();
        }).toList();
    }
}

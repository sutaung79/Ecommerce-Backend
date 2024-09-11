package com.ecom.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecom.app.api.response.ProductResponse;
import com.ecom.app.dto.CartDTO;
import com.ecom.app.dto.ProductDTO;
import com.ecom.app.entities.Cart;
import com.ecom.app.entities.Category;
import com.ecom.app.entities.Product;
import com.ecom.app.exceptions.APIException;
import com.ecom.app.exceptions.ResourceNotFoundException;
import com.ecom.app.repositories.CartRepository;
import com.ecom.app.repositories.CategoryRepository;
import com.ecom.app.repositories.ProductRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private CategoryRepository categoryRepo;

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private CartService cartService;


	@Autowired
	private ModelMapper modelMapper;


	@Value("${project.image}")
	private String path;


	
	@Override
	public ProductDTO addProduct(Long categoryId, Product product) {

	    Category category = categoryRepo.findById(categoryId)
	            .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

	    
	    List<Product> products = category.getProducts();
	    if (products == null) {
	        products = List.of(); 
	    }

	    
	    boolean isProductNotPresent = products.stream()
	            .noneMatch(p -> p.getProductName().equalsIgnoreCase(product.getProductName())
	                    && p.getDescription().equalsIgnoreCase(product.getDescription()));

	    if (isProductNotPresent) {
	        product.setImage("default.png");
	        product.setCategory(category);

	        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
	        product.setSpecialPrice(specialPrice);
	        Product savedProduct = productRepo.save(product);

	        return modelMapper.map(savedProduct, ProductDTO.class);
	    } else {
	        throw new APIException("Product already exists !!!");
	    }
	}


	@Override
	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Product> pageProducts = productRepo.findAll(pageDetails);

		List<Product> products = pageProducts.getContent();

		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();

		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());

		return productResponse;
	}


	
	@Override
	public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
	                                        String sortOrder) {

	    Category category = categoryRepo.findById(categoryId)
	            .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

	    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();

	    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

	    Page<Product> pageProducts = productRepo.findByCategory(category, pageDetails);

	    List<Product> products = pageProducts.getContent();

	    if (products.isEmpty()) {
	        throw new APIException(category.getCategoryName() + " category doesn't contain any products!!!");
	    }

	    List<ProductDTO> productDTOs = products.stream()
	            .map(p -> modelMapper.map(p, ProductDTO.class))
	            .collect(Collectors.toList());

	    ProductResponse productResponse = new ProductResponse();
	    productResponse.setContent(productDTOs);
	    productResponse.setPageNumber(pageProducts.getNumber());
	    productResponse.setPageSize(pageProducts.getSize());
	    productResponse.setTotalElements(pageProducts.getTotalElements());
	    productResponse.setTotalPages(pageProducts.getTotalPages());
	    productResponse.setLastPage(pageProducts.isLast());

	    return productResponse;
	}


	@Override
	public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Product> pageProducts = productRepo.findByProductNameContainingIgnoreCase(keyword, pageDetails);

		List<Product> products = pageProducts.getContent();
		
		if (products.size() == 0) {
			throw new APIException("Products not found with keyword: " + keyword);
		}

		List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProductDTO.class))
				.collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();

		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());

		return productResponse;
	}

	@Override
	public ProductDTO updateProduct(Long productId, Product product) {
		Product productFromDB = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		if (productFromDB == null) {
			throw new APIException("Product not found with productId: " + productId);
		}

		product.setImage(productFromDB.getImage());
		product.setProductId(productId);
		product.setCategory(productFromDB.getCategory());

		double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
		product.setSpecialPrice(specialPrice);

		Product savedProduct = productRepo.save(product);

		List<Cart> carts = cartRepo.findCartsByProductId(productId);

		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
		    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		    
		    if (cartDTO == null) {
		        throw new APIException("Failed to map Cart to CartDTO");
		    }

		    List<ProductDTO> products = cart.getCartItems().stream()
		        .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
		        .collect(Collectors.toList());

		    cartDTO.setProducts(products);

		    return cartDTO;
		}).collect(Collectors.toList());

			
		cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

		return modelMapper.map(savedProduct, ProductDTO.class);
	}

	
	
	@Override
	public String deleteProduct(Long productId) {

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		List<Cart> carts = cartRepo.findCartsByProductId(productId);

		carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

		productRepo.delete(product);

		return "Product with productId: " + productId + " deleted successfully !!!";
	}

}

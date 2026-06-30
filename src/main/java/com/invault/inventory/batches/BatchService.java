package com.invault.inventory.batches;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.invault.inventory.batches.dto.BatchRequestDTO;
import com.invault.inventory.batches.dto.BatchResponseDTO;
import com.invault.inventory.common.exception.BadRequestException;
import com.invault.inventory.common.exception.ResourceNotFoundException;
import com.invault.inventory.products.Product;
import com.invault.inventory.products.ProductRepository;

@Service
@Transactional
public class BatchService {

    private final BatchRepository batchRepository;
    private final ProductRepository productRepository;
    private final BatchMapper batchMapper;

    public BatchService(
            BatchRepository batchRepository,
            ProductRepository productRepository,
            BatchMapper batchMapper) {

        this.batchRepository = batchRepository;
        this.productRepository = productRepository;
        this.batchMapper = batchMapper;
    }

    @Transactional(readOnly = true)
    public List<BatchResponseDTO> findAll() {
        List<Batch> batches = batchRepository.findAllByOrderByBatchCodeAsc();
        return batchMapper.toResponseDTOList(batches);
    }

    @Transactional(readOnly = true)
    public List<BatchResponseDTO> findByProductId(Long productId) {
        validateProductExists(productId);

        List<Batch> batches = batchRepository.findByProductIdOrderByBatchCodeAsc(productId);
        return batchMapper.toResponseDTOList(batches);
    }

    @Transactional(readOnly = true)
    public BatchResponseDTO findById(Long id) {
        Batch batch = findBatchEntityById(id);
        return batchMapper.toResponseDTO(batch);
    }

    public BatchResponseDTO create(BatchRequestDTO requestDTO) {
        Product product = findActiveProductById(requestDTO.getProductId());

        String normalizedBatchCode = normalizeBatchCode(requestDTO.getBatchCode());
        BigDecimal normalizedQuantity = normalizeQuantity(requestDTO.getQuantity());

        validateBatchCodeIsUnique(normalizedBatchCode, null);

        Batch batch = batchMapper.toEntity(requestDTO, product);

        // Relacionamos el lote con el producto real cargado desde base de datos.
        batch.setProduct(product);

        // Normalizamos los campos principales antes de guardar.
        batch.setBatchCode(normalizedBatchCode);
        batch.setQuantity(normalizedQuantity);
        batch.setStatus(requestDTO.getStatus());
        batch.setNotes(normalizeText(requestDTO.getNotes()));

        Batch savedBatch = batchRepository.save(batch);
        return batchMapper.toResponseDTO(savedBatch);
    }

    public BatchResponseDTO update(Long id, BatchRequestDTO requestDTO) {
        Batch batch = findBatchEntityById(id);
        Product product = findActiveProductById(requestDTO.getProductId());

        String normalizedBatchCode = normalizeBatchCode(requestDTO.getBatchCode());
        BigDecimal normalizedQuantity = normalizeQuantity(requestDTO.getQuantity());

        validateBatchCodeIsUnique(normalizedBatchCode, id);

        // Actualizamos los campos editables del lote.
        batch.setProduct(product);
        batch.setBatchCode(normalizedBatchCode);
        batch.setQuantity(normalizedQuantity);
        batch.setStatus(requestDTO.getStatus());
        batch.setNotes(normalizeText(requestDTO.getNotes()));

        Batch updatedBatch = batchRepository.save(batch);
        return batchMapper.toResponseDTO(updatedBatch);
    }

    private Batch findBatchEntityById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + id));
    }

    private Product findActiveProductById(Long productId) {
        if (productId == null) {
            throw new BadRequestException("Product id is required.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (Boolean.FALSE.equals(product.getActive())) {
            throw new BadRequestException("Product with id " + productId + " is inactive.");
        }

        return product;
    }

    private void validateProductExists(Long productId) {
        if (productId == null) {
            throw new BadRequestException("Product id is required.");
        }

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
    }

    private void validateBatchCodeIsUnique(String batchCode, Long currentBatchId) {
        batchRepository.findByBatchCodeIgnoreCase(batchCode)
                .filter(existingBatch -> isDifferentBatch(existingBatch, currentBatchId))
                .ifPresent(existingBatch -> {
                    throw new BadRequestException("A batch with code '" + batchCode + "' already exists.");
                });
    }

    private boolean isDifferentBatch(Batch existingBatch, Long currentBatchId) {
        return currentBatchId == null || !existingBatch.getId().equals(currentBatchId);
    }

    private BigDecimal normalizeQuantity(BigDecimal quantity) {
        if (quantity == null) {
            throw new BadRequestException("Batch quantity is required.");
        }

        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Batch quantity cannot be negative.");
        }

        return quantity;
    }

    private String normalizeBatchCode(String value) {
        String normalizedValue = normalizeText(value);

        if (normalizedValue == null) {
            return null;
        }

        return normalizedValue.toUpperCase(Locale.ROOT);
    }

    private String normalizeText(String value) {
        return value == null ? null : value.trim();
    }
}

/*
 * BatchService contiene la lógica de negocio para los lotes de inventario.
 *
 * Esta clase permite listar, consultar, crear y actualizar lotes sin exponer
 * directamente la entidad JPA Batch. También valida que el código de lote sea
 * único, que el producto relacionado exista y esté activo, y que la cantidad
 * del lote nunca sea negativa.
 *
 * En InVault, los lotes son una pieza clave del cálculo de stock. Product no
 * almacena stock actual directamente; el stock disponible se calculará usando
 * los lotes y, en la siguiente fase, los movimientos auditables de stock.
 */
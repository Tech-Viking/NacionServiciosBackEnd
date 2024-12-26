package sube.interviews.mareoenvios.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.model.TopProductResponse;
import sube.interviews.mareoenvios.repository.ShippingItemRepository;

@Service
public class ReportService {

	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
	private final ShippingItemRepository shippingItemRepository;

	public ReportService(ShippingItemRepository shippingItemRepository) {
		this.shippingItemRepository = shippingItemRepository;
	}

	public List<TopProductResponse> getTopSentProducts() {
		logger.info("Getting top sent products");
		try {
			List<Object[]> results = shippingItemRepository.findTop3Products();
			List<TopProductResponse> topProducts = results.stream()
					.map(result -> new TopProductResponse((Integer) result[0], (String) result[1],
							((Number) result[2]).intValue()))
					.collect(Collectors.toList());
			logger.info("Top sent products found: {}", topProducts.size());
			return topProducts;
		} catch (Exception e) {
			logger.error("Error getting top sent products: {}", e.getMessage(), e);
			throw e;
		}

	}
}
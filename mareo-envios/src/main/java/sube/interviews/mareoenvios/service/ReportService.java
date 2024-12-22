package sube.interviews.mareoenvios.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.model.TopProductResponse;
import sube.interviews.mareoenvios.repository.ShippingItemRepository;

@Service
public class ReportService {

    private final ShippingItemRepository shippingItemRepository;

    public ReportService(ShippingItemRepository shippingItemRepository) {
        this.shippingItemRepository = shippingItemRepository;
    }

    public List<TopProductResponse> getTopSentProducts() {
       List<Object[]> results = shippingItemRepository.findTop3Products();
       return results.stream()
            .map(result -> new TopProductResponse(
                (Integer) result[0],
                (String) result[1],
                ((Number) result[2]).intValue()
        )).collect(Collectors.toList());
   }
}
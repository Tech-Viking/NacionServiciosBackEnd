  package sube.interviews.mareoenvios.model;
  public class TopProductResponse {
      private Integer productId;
      private String description;
      private Integer totalCount;

      public TopProductResponse(Integer productId, String description, Integer totalCount) {
          this.productId = productId;
          this.description = description;
          this.totalCount = totalCount;
      }

      public Integer getProductId() {
          return productId;
      }

      public void setProductId(Integer productId) {
          this.productId = productId;
      }

      public String getDescription() {
          return description;
      }

      public void setDescription(String description) {
          this.description = description;
      }

      public Integer getTotalCount() {
          return totalCount;
      }

      public void setTotalCount(Integer totalCount) {
          this.totalCount = totalCount;
      }
  }
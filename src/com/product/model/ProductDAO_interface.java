package com.product.model;

import java.util.List;

public interface ProductDAO_interface {
	public void insert(ProductVO productVO);
    public void update(ProductVO productVO);
    public void delete(String product_id);
    public ProductVO findByPrimaryKey(String product_id);
    public List<ProductVO> getAll();
    public List<ProductVO> getTime();
    public void launch(ProductVO productVO);
    public void dislaunch(ProductVO productVO);
    public void approval(ProductVO productVO);
    public List<ProductVO> getApproval();
    public List<ProductVO> getUnapproval();
    public List<ProductVO> getBand(String band_id);
    public List<ProductVO> getOrder(String product_id);
    
    //這是鈺涵的方法
	    /**
	     * 異動產品的庫存數量
	     * @param productId 產品id
	     * @param stockDifference 庫存變動數
	     */
    void updateStock(String productId,int stockDifference);
}
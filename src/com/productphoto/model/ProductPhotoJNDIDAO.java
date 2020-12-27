package com.productphoto.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ProductPhotoJNDIDAO implements ProductPhotoDAO_interface {

	private static DataSource ds = null;
	static {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestTEA102G6");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private static final String INSERT_STMT = "INSERT INTO productphoto(productphoto_id,product_id,productphoto_photo,productphoto_sort,productphoto_add_time)"
			+ "VALUES('PRODUCTPHOTO'||LPAD(PRODUCTPHOTO_SEQ.NEXTVAL, 5, '0'), ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT * FROM productphoto ORDER BY productphoto_id";

	private static final String GET_ONE_STMT = "SELECT * FROM productphoto WHERE productphoto_id = ?";

	private static final String DELETE = "DELETE FROM productphoto where productphoto_id = ?";
	private static final String UPDATE = "UPDATE productphoto set " + "product_id=?," + "productphoto_photo=?,"
			+ "productphoto_sort=?," + "productphoto_add_time=?" + "where productphoto_id = ?";

	@Override
	public void insert(ProductPhotoVO productPhotoVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con.setAutoCommit(false);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, productPhotoVO.getProduct_id());
			pstmt.setBytes(2, productPhotoVO.getProductphoto_photo());
			pstmt.setInt(3, productPhotoVO.getProductphoto_sort());
			pstmt.setTimestamp(4, productPhotoVO.getProductphoto_add_time());
			pstmt.executeUpdate();
			System.out.println("Operation success!");
			con.commit();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}

	@Override
	public void update(ProductPhotoVO productPhotoVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con.setAutoCommit(false);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, productPhotoVO.getProduct_id());
			pstmt.setBytes(2, productPhotoVO.getProductphoto_photo());
			pstmt.setInt(3, productPhotoVO.getProductphoto_sort());
			pstmt.setTimestamp(4, productPhotoVO.getProductphoto_add_time());
			pstmt.setString(5, productPhotoVO.getProductphoto_id());
			pstmt.executeUpdate();

			System.out.println("Operation success!");
			con.commit();
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}
	

	@Override
	public void delete(String productphoto_id) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con.setAutoCommit(false);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setString(1, productphoto_id);

			pstmt.executeUpdate();

			System.out.println("Operation success!");
			con.commit();
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public ProductPhotoVO findByPrimaryKey(String productphoto_id) {
		ProductPhotoVO productPhotoVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con.setAutoCommit(false);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setString(1, productphoto_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				productPhotoVO = new ProductPhotoVO();
				productPhotoVO.setProductphoto_id(rs.getString("productphoto_id"));
				productPhotoVO.setProduct_id(rs.getString("product_id"));
				productPhotoVO.setProductphoto_photo(rs.getBytes("productphoto_photo"));
				productPhotoVO.setProductphoto_sort(rs.getInt("productphoto_sort"));
				productPhotoVO.setProductphoto_add_time(rs.getTimestamp("productphoto_add_time"));
			}
			System.out.println("Operation success!");
			con.commit();
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return productPhotoVO;
	}

	@Override
	public List<ProductPhotoVO> getAll() {
		List<ProductPhotoVO> list = new ArrayList<ProductPhotoVO>();
		ProductPhotoVO productPhotoVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con.setAutoCommit(false);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				productPhotoVO = new ProductPhotoVO();
				productPhotoVO.setProductphoto_id(rs.getString("productphoto_id"));
				productPhotoVO.setProduct_id(rs.getString("product_id"));
				productPhotoVO.setProductphoto_photo(rs.getBytes("productphoto_photo"));
				productPhotoVO.setProductphoto_sort(rs.getInt("productphoto_sort"));
				productPhotoVO.setProductphoto_add_time(rs.getTimestamp("productphoto_add_time"));
				list.add(productPhotoVO); // Store the row in the list
			}
			System.out.println("Operation success!");
			con.commit();
			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
}
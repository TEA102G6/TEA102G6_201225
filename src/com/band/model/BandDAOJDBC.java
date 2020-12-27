package com.band.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class BandDAOJDBC implements BandDAO_interface {
	
	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
		private static DataSource ds = null;
		static {
			try {
				Context ctx = new InitialContext();
				ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TestDB");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

	private static final String INSERT_PSTMT = "INSERT INTO BAND VALUES ('BAND'||LPAD(BAND_SEQ.NEXTVAL, 5, '0'), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_PSTMT = "UPDATE BAND SET BAND_NAME = ?, BAND_INTRO = ?, BAND_PHOTO = ?, BAND_BANNER = ?, BAND_PIECE_CHECK = ?, BAND_ADD_TIME = ?, BAND_STATUS = ?, BAND_LAST_EDIT_TIME = ?, BAND_LAST_EDITOR = ? WHERE BAND_ID = ?";
	private static final String DELETE_PSTMT = "DELETE FROM BAND WHERE BAND_ID = ?";
	private static final String GET_ONE_PSTMT = "SELECT * FROM BAND WHERE BAND_ID = ?";
	private static final String GET_ALL_PSTMT = "SELECT * FROM BAND ORDER BY BAND_ID";

	@Override
	public void insert(BandVO bandVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_PSTMT);

			pstmt.setString(1, bandVO.getBand_name());
			pstmt.setString(2, bandVO.getBand_intro());
			pstmt.setBytes(3, bandVO.getBand_photo());
			pstmt.setBytes(4, bandVO.getBand_banner());
			pstmt.setBytes(5, bandVO.getBand_piece_check());
			pstmt.setTimestamp(6, bandVO.getBand_add_time());
			pstmt.setInt(7, bandVO.getBand_status());
			pstmt.setTimestamp(8, bandVO.getBand_last_edit_time());
			pstmt.setString(9, bandVO.getBand_last_editor());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public void update(BandVO bandVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_PSTMT);

			pstmt.setString(1, bandVO.getBand_name());
			pstmt.setString(2, bandVO.getBand_intro());
			pstmt.setBytes(3, bandVO.getBand_photo());
			pstmt.setBytes(4, bandVO.getBand_banner());
			pstmt.setBytes(5, bandVO.getBand_piece_check());
			pstmt.setTimestamp(6, bandVO.getBand_add_time());
			pstmt.setInt(7, bandVO.getBand_status());
			pstmt.setTimestamp(8, bandVO.getBand_last_edit_time());
			pstmt.setString(9, bandVO.getBand_last_editor());
			pstmt.setString(10, bandVO.getBand_id());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public void delete(String band_id) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE_PSTMT);

			pstmt.setString(1, band_id);

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public BandVO findByPrimaryKey(String band_id) {
		BandVO bandVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_PSTMT);

			pstmt.setString(1, band_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				bandVO = new BandVO();
				bandVO.setBand_id(rs.getString("BAND_ID"));
				bandVO.setBand_name(rs.getString("BAND_NAME"));
				bandVO.setBand_intro(rs.getString("BAND_INTRO"));
				bandVO.setBand_photo(rs.getBytes("BAND_PHOTO"));
				bandVO.setBand_banner(rs.getBytes("BAND_BANNER"));
				bandVO.setBand_piece_check(rs.getBytes("BAND_PIECE_CHECK"));
				bandVO.setBand_add_time(rs.getTimestamp("BAND_ADD_TIME"));
				bandVO.setBand_status(rs.getInt("BAND_STATUS"));
				bandVO.setBand_last_edit_time(rs.getTimestamp("BAND_LAST_EDIT_TIME"));
				bandVO.setBand_last_editor(rs.getString("BAND_LAST_EDITOR"));

			}

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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

		return bandVO;
	}

	@Override
	public List<BandVO> getAll() {
		List<BandVO> list = new ArrayList();
		BandVO bandVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_PSTMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				bandVO = new BandVO();
				bandVO.setBand_id(rs.getString("BAND_ID"));
				bandVO.setBand_name(rs.getString("BAND_NAME"));
				bandVO.setBand_intro(rs.getString("BAND_INTRO"));
				bandVO.setBand_photo(rs.getBytes("BAND_PHOTO"));
				bandVO.setBand_banner(rs.getBytes("BAND_BANNER"));
				bandVO.setBand_piece_check(rs.getBytes("BAND_PIECE_CHECK"));
				bandVO.setBand_add_time(rs.getTimestamp("BAND_ADD_TIME"));
				bandVO.setBand_status(rs.getInt("BAND_STATUS"));
				bandVO.setBand_last_edit_time(rs.getTimestamp("BAND_LAST_EDIT_TIME"));
				bandVO.setBand_last_editor(rs.getString("BAND_LAST_EDITOR"));
				list.add(bandVO);

			}

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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

		return list;
	}
	

}
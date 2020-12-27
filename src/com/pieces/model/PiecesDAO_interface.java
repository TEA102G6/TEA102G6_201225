package com.pieces.model;

import java.util.List;
import database.util.ConnectionInfo;

public interface PiecesDAO_interface extends ConnectionInfo {
	
	public void insert(PiecesVO piecesVO);
    public void update(PiecesVO piecesVO);
    public void delete(String piece_id);
    public PiecesVO findByPrimaryKey(String piece_id);
    public List<PiecesVO> getAll();
    public PiecesVO getPiece(String piece_id); 
    public List<PiecesVO> getAllByAlbumId(String album_id);
	
}
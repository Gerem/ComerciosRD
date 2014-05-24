package com.comerciosrd.pojos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.comerciosrd.utils.Validations;

public class Categoria implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idCategoriaPk;
	private String categoria;
	transient Bitmap logo;

	public Long getIdCategoriaPk() {
		return idCategoriaPk;
	}

	public void setIdCategoriaPk(Long idCategoriaPk) {
		this.idCategoriaPk = idCategoriaPk;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public Bitmap getLogo() {
		return logo;
	}

	public void setLogo(Bitmap logo) {
		this.logo = logo;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		// This will serialize all fields that you did not mark with 'transient'
		// (Java's default behaviour)
		oos.defaultWriteObject();
		// Now, manually serialize all transient fields that you want to be
		// serialized
		if (logo != null) {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			boolean success = logo.compress(Bitmap.CompressFormat.PNG, 100,
					byteStream);
			if (success) {
				oos.writeObject(byteStream.toByteArray());
			}
		}
	}

	private void readObject(ObjectInputStream ois) throws IOException,
			ClassNotFoundException {
		if(Validations.validateIsNotNull(ois)){
			// Now, all again, deserializing - in the SAME ORDER!
			// All non-transient fields
			ois.defaultReadObject();
			// All other fields that you serialized
			byte[] image = (byte[]) ois.readObject();
			if (image != null && image.length > 0) {
				logo = BitmapFactory.decodeByteArray(image, 0, image.length);
			}
		}
	}
}

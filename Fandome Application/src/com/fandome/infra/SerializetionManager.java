package com.fandome.infra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

public class SerializetionManager {
	public static byte[] seralize(Serializable serializable) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(serializable);
		  byte[] result = bos.toByteArray();
		  return result;
		} finally {
		  out.close();
		  bos.close();
		}
	}
	
	public static Object deserialize(byte[] bytes) throws StreamCorruptedException, IOException, ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		try {
		  in = new ObjectInputStream(bis);
		  Object result = in.readObject();
		  return result;
		} finally {
		  bis.close();
		  in.close();
		}
	}
}

package com.fandome.facebook;

import android.os.Bundle;

public abstract class BaseFacebookObjectConvertor<T> implements IFacebookObjectConvertor{

	private Class<?> handleType;
	public BaseFacebookObjectConvertor(Class<?> handleType){
		this.handleType = handleType;
//		FacebookManager.getInstance().converotrs.add(this);
	}
	
	@SuppressWarnings("unchecked")
	public Bundle convert(Object object) {
		return convertImp((T)object);
	}

	public boolean canHandle(Object object) {
		return object.getClass().equals(handleType);
	}
	
	protected abstract Bundle convertImp(T object);

}

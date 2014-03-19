package com.fandome.facebook;

import android.os.Bundle;

public interface IFacebookObjectConvertor {
	Bundle convert(Object object);
	boolean canHandle(Object object);
}

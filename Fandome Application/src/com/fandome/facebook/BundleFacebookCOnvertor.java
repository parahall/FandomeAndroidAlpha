package com.fandome.facebook;

import android.os.Bundle;

public class BundleFacebookCOnvertor extends BaseFacebookObjectConvertor<Bundle> {

	public BundleFacebookCOnvertor() {
		super(Bundle.class);
	}

	@Override
	protected Bundle convertImp(Bundle object) {
		return object;
	}

}

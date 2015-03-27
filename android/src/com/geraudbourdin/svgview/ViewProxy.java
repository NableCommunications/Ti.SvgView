/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */

package com.geraudbourdin.svgview;

//import ActionBarActivity;
import java.io.IOException;
import java.io.InputStream;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.Log;
import org.appcelerator.titanium.util.TiConfig;
import org.appcelerator.titanium.io.TiBaseFile;
import org.appcelerator.titanium.io.TiFileFactory;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import org.appcelerator.kroll.common.AsyncResult;
import android.os.Message;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;

// This proxy can be created by calling Svgview.createExample({message: "hello world"})
@Kroll.proxy(creatableInModule = SvgViewModule.class)
public class ViewProxy extends TiViewProxy {
	// Standard Debugging variables
	private static final String LCAT = "ViewProxy";

	private String image;
	TiUIView view;

	private static final int MSG_SET_IMAGE = 70000;

	@SuppressWarnings("deprecation")
	private static final boolean DBG = TiConfig.LOGD;

	private class SvgView extends TiUIView {

		Activity activity;
		LinearLayout layout;
		SVGImageView svgImageView;
		InputStream contentFile;
		SVG svgImage;

		public SvgView(TiViewProxy proxy) {
			super(proxy);

			activity = proxy.getActivity();
			layout = new LinearLayout(activity);
			svgImageView = new SVGImageView(activity);
			setImage();
			setNativeView(layout);
		}

		public void setImage() {
			String url = proxy.resolveUrl(null, image);
			TiBaseFile file = TiFileFactory.createTitaniumFile( new String[] { url }, false);
			try {
				contentFile = file.getInputStream();
			} catch (IOException e) {
				Log.d(LCAT, "Failed to get input stream.");
			}

			try {
				svgImage = SVG.getFromInputStream(contentFile);
			} catch (SVGParseException e) {
				Log.d(LCAT, "Failed to set svg from input stream.");
			}
			svgImageView.setSVG(svgImage);
			layout.removeAllViews();
			layout.addView(svgImageView, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		}

		@Override
		public void processProperties(KrollDict d) {
			super.processProperties(d);
		}
	}

	// Constructor
	public ViewProxy() {
		super();
	}

	@Override
	public TiUIView createView(Activity activity) {
		view = new SvgView(this);
		view.getLayoutParams().autoFillsHeight = true;
		view.getLayoutParams().autoFillsWidth = true;
		return view;
	}

	// Handle creation options
	@Override
	public void handleCreationDict(KrollDict options) {
		super.handleCreationDict(options);
		if (options.containsKey("image")) {
			image = (String) options.getString("image");
		}
	}

	@Kroll.setProperty
	@Kroll.method
	public void setImage(String val) {
		image = val;
		// from : https://wiki.appcelerator.org/display/guides2/Android+Module+Quick+Start
		// Get the view object from the proxy and set the image
		if (view != null) {
			if (!TiApplication.isUIThread()) {
				// If we are not on the UI thread, need to use a message to set
				// the image
				TiMessenger.sendBlockingMainMessage(new Handler(TiMessenger
						.getMainMessenger().getLooper(),
						new Handler.Callback() {
							public boolean handleMessage(Message msg) {
								switch (msg.what) {
								case MSG_SET_IMAGE: {
									AsyncResult result = (AsyncResult) msg.obj;
									SvgView fooView = (SvgView) view;
									fooView.setImage();
									result.setResult(null);
									return true;
								}
								}
								return false;
							}
						}).obtainMessage(MSG_SET_IMAGE), val);
			} else {
				SvgView fooView = (SvgView) view;
				fooView.setImage();
			}
		}
		// Updates the property on the JavaScript proxy object
		setProperty("image", val, true);
	}

	@Kroll.getProperty
	@Kroll.method
	public String getImage() {
		return image;
	}
}
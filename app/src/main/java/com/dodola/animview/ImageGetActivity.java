package com.dodola.animview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ImageGetActivity extends AppCompatActivity {

	private ImageTextView mText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_image);
		mText = (ImageTextView) findViewById(R.id.text);
		mText.setImageText("文字前插入图片", "http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg");
	}
}

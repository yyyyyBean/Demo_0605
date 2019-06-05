package com.bw.zxing_demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.android.PermissionUtils;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.encode.CodeCreator;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImage;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.ed_text);
        findViewById(R.id.btn_zxing_1).setOnClickListener(this);
        findViewById(R.id.btn_zxing_2).setOnClickListener(this);
        findViewById(R.id.btn_scan).setOnClickListener(this);
        mImage = (ImageView) findViewById(R.id.image);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zxing_1://不带logo
                setIamge(0);
                break;
            case R.id.btn_zxing_2://带logo
                setIamge(1);
                break;
            case R.id.btn_scan://扫描二维码
                PermissionUtils.permission(this, new PermissionUtils.PermissionListener() {
                    @Override
                    public void success() {
                        Intent intent=new Intent(MainActivity.this, CaptureActivity.class);
                        startActivityForResult(intent,1000);
                    }
                });
                break;
        }
    }

    private void setIamge(int type) {
        String message = editText.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "请输入要生成的内容", Toast.LENGTH_LONG).show();
            return;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        if (type == 0) {
            bitmap = null;
        }
        try {
            Bitmap bitmap1=CodeCreator.createQRCode(message,200,200,bitmap);
            mImage.setImageBitmap(bitmap1);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            String content=data.getStringExtra(Constant.CODED_CONTENT);
            if(content.contains("http")){
                //userid,跳转到添加用户信息页面
                //拿到链接，跳转到 一个webView页面

                Intent intent=new Intent(MainActivity.this,WebViewActivity.class);
                intent.putExtra("url",content);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this,content,Toast.LENGTH_LONG).show();
            }
        }
    }
}

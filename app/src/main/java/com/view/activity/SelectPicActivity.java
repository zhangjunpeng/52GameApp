package com.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.FileUtil;
import com.app.tools.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.MyAccount;
import com.test4s.config.Config;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.index.ChangeEvent;
import com.view.index.MySettingFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeMap;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

/**
 * @author spring sky<br>
 * Email :vipa1888@163.com<br>
 * QQ: 840950105<br>
 * @version 创建时间：2012-11-22 上午9:20:03
 * 说明：主要用于选择文件操作
 */

public class SelectPicActivity extends Activity implements OnClickListener{

    /***
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /***
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

    //得到裁切后的图片
    public static final int GET_PIC=3;

    /***
     * 从Intent获取图片路径的KEY
     */
    public static final String KEY_PHOTO_PATH = "photo_path";

    private static final String TAG = "SelectPicActivity";

    private LinearLayout dialogLayout;
    private TextView takePhotoBtn,pickPhotoBtn,cancelBtn;


    private Intent resultData;

    private String picUrl_cq;


    /**获取到的图片路径*/
    private String picPath;

    private Intent lastIntent ;

    private Uri photoUri;
    private Dialog progressdialog;
    private float density;
    private int windowWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic);
        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        windowWidth=metric.widthPixels;

        getWindow().setBackgroundDrawableResource(R.drawable.border_dialog);
        initView();
    }
    /**
     * 初始化加载View
     */
    private void initView() {
        dialogLayout = (LinearLayout) findViewById(R.id.dialog_layout);
        dialogLayout.setOnClickListener(this);
        takePhotoBtn = (TextView) findViewById(R.id.btn_take_photo);
        takePhotoBtn.setOnClickListener(this);
        pickPhotoBtn = (TextView) findViewById(R.id.btn_pick_photo);
        pickPhotoBtn.setOnClickListener(this);
        cancelBtn = (TextView) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);

        lastIntent = getIntent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_layout:
                finish();
                break;
            case R.id.btn_take_photo:
                takePhoto();
                break;
            case R.id.btn_pick_photo:
                pickPhoto();
                break;
            default:
                finish();
                break;
        }
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if(SDState.equals(Environment.MEDIA_MOUNTED))
        {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            /**-----------------*/
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        }else{
            Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
//        startActivityForResult(intent, 1);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK)
        {
           switch (requestCode){
               case SELECT_PIC_BY_PICK_PHOTO:
                   startPhotoZoom(data.getData());
                   break;
               case SELECT_PIC_BY_TACK_PHOTO:
//                   File temp = new File(picPath);
                   startPhotoZoom(photoUri);
                   break;
               case GET_PIC:
                   resultData=data;
//                  doPhoto(requestCode,data);
                    setPicToView(data);
                   break;
           }
        }else if(resultCode==Activity.RESULT_CANCELED){
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
//            Drawable drawable = new BitmapDrawable(photo);

            /**
             * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
             * 传到服务器，QQ头像上传采用的方法跟这个类似
             */
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
//            byte[] b = stream.toByteArray();
//            InputStream input=photo.compress()
//            MyLog.i("裁切字节大小：："+b.length);
//            ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(b);

            picUrl_cq= FileUtil.saveFile(this,"headcq.jpg",photo);

            toUploadFile(picUrl_cq);
//            picPath = FileUtil.saveFile(mContext, temphead.jpg, photo);

            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            byte[] b = stream.toByteArray();
            // 将图片流以字符串形式存储下来

            tp = new String(Base64Coder.encodeLines(b));
            这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了，
            服务器处理的方法是服务器那边的事了，吼吼

            如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换
            为我们可以用的图片类型就OK啦...吼吼
            Bitmap dBitmap = BitmapFactory.decodeFile(tp);
            Drawable drawable = new BitmapDrawable(dBitmap);
            */
//            ib.setBackgroundDrawable(drawable);
//            iv.setBackgroundDrawable(drawable);
        }
    }

    private void startPhotoZoom(Uri uri) {
         /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
         * 制做的了...吼吼
         */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent,GET_PIC);
    }

    /**
     * 选择图片后，获取图片的路径
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode,Intent data)
    {

        if(requestCode == SELECT_PIC_BY_PICK_PHOTO )  //从相册取图片，有些手机有异常情况，请注意
        {
            if(data == null)
            {
                CusToast.showToast(this,"选择图片文件出错",Toast.LENGTH_SHORT);
                return;
            }
            photoUri = data.getData();
            if(photoUri == null )
            {
                CusToast.showToast(this,"选择图片文件出错",Toast.LENGTH_SHORT);

                return;
            }
        }
        String[] pojo = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(photoUri, pojo, null, null,null);
        if(cursor != null )
        {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            cursor.close();
        }
        Log.i(TAG, "imagePath = "+picPath);
        if(picPath != null && ( picPath.endsWith(".png") || picPath.endsWith(".PNG") ||picPath.endsWith(".jpg") ||picPath.endsWith(".JPG")  ))
        {
            lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
            setResult(Activity.RESULT_OK, lastIntent);
            finish();
        }else{
            CusToast.showToast(this,"选择图片文件不正确",Toast.LENGTH_SHORT);

        }
    }

    private void toUploadFile(String b)
    {

//        UploadUtil uploadUtil = UploadUtil.getInstance();;
//        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
//        uploadUtil.uploadFile( picPath,fileKey, requestURL,params);
        showProgress();

        MyLog.i("url=="+Url.IconUploadUrlPrefix+Url.IconUploadUrl);
        RequestParams baseParams=new RequestParams(Url.IconUploadUrlPrefix+Url.IconUploadUrl);

        baseParams.setMultipart(true);
        baseParams.addBodyParameter("imei", MyApplication.imei);
        baseParams.addBodyParameter("version",MyApplication.versionName);
        baseParams.addBodyParameter("package_name",MyApplication.packageName);
        baseParams.addBodyParameter("channel_id","1");
        baseParams.addBodyParameter("form","app");
        baseParams.addBodyParameter("token", MyAccount.getInstance().getToken());

        TreeMap<String,String> map=new TreeMap<>();
        map.put("imei",MyApplication.imei);
        map.put("version",MyApplication.versionName);
        map.put("package_name",MyApplication.packageName);
        map.put("channel_id","1");
        map.put("form","app");
        map.put("token",MyAccount.getInstance().getToken());
        baseParams.addBodyParameter("sign", Url.getSign(map.entrySet()));
        baseParams.addBodyParameter("filedata",new File(b),null);
//        baseParams.addBodyParameter("filedata",b,"multipart/form-data");
        x.http().post(baseParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("裁切图片上传数据 updataImage==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    if (su){
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
//                        String url=jsonObject1.getString("url");
                        String picurl=jsonObject1.getString("picpath");

                        uploadurl(picurl);
                    }else {
                        setResult(Activity.RESULT_CANCELED);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyLog.i("uploadimage error==="+ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    private void uploadurl(final String url) {
        BaseParams baseParams=new BaseParams("user/upavatar");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("avatar",url);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("上传图片链接"+result);
                progressdialog.dismiss();

                MyAccount.getInstance().setAvatar(url);
                MyAccount.getInstance().getUserInfo().setAvatar(url);
                MySettingFragment.changeIcon=true;
                MyAccount.getInstance().saveUserInfo();
                EventBus.getDefault().post(new ChangeEvent(url,1));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                setResult(Activity.RESULT_OK,resultData);
                finish();

            }
        });

    }

    private void showProgress() {


        progressdialog=new Dialog(this);

        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view= LayoutInflater.from(this).inflate(R.layout.dialog_clearcahe,null);

        progressdialog.getWindow().setBackgroundDrawableResource(R.drawable.border_dialog);
//        MyDialog myDialog=new MyDialog(getActivity(),(int) (windowWidth*0.8),(int) (110*density),view,R.style.MyDialog);

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams((int) (windowWidth*0.8), LinearLayout.LayoutParams.MATCH_PARENT);
        params.leftMargin= (int) (14*density);
        params.rightMargin= (int) (14*density);
        MyLog.i("params width=="+params.width);
        progressdialog.setContentView(view,params);

        progressdialog.show();
    }

}

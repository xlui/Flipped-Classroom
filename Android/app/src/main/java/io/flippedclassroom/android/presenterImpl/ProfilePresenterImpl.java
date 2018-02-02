package io.flippedclassroom.android.presenterImpl;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.flippedclassroom.android.R;
import io.flippedclassroom.android.activity.ProfileActivity;
import io.flippedclassroom.android.app.AppCache;
import io.flippedclassroom.android.base.BasePresenter;
import io.flippedclassroom.android.bean.User;
import io.flippedclassroom.android.model.ProfileModel;
import io.flippedclassroom.android.presenter.ProfilePresenter;
import io.flippedclassroom.android.util.LogUtils;
import io.flippedclassroom.android.util.RetrofitManager;
import io.flippedclassroom.android.util.ToastUtils;
import io.flippedclassroom.android.view.ProfileView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfilePresenterImpl extends BasePresenter implements ProfilePresenter {
    private ProfileView mView;
    private ProfileModel mModel;

    private String[] keys = new String[]{"info_name", "info_value"};
    private int[] ids = new int[]{R.id.tv_info_name, R.id.tv_info_value};
    private List<Map<String, String>> list = new ArrayList<>();

    private final int POSITION_NICKNAME = 1;
    private final int POSITION_GENDER = 2;
    private final int POSITION_SIGNATURE = 3;

    private final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    private boolean hasSetAvatar = false;

    public ProfilePresenterImpl(ProfileActivity activity, Context mContext) {
        super(mContext);
        mView = activity;
        mModel = new ProfileModel(mContext);
    }

    @Override
    public void onClick(int viewId) {
        switch (viewId) {
            case R.id.btn_commit_post:
                postUserInfo();
                break;
            case R.id.civ_avatar:
                mView.openGallery();
                break;
        }
    }

    private void postUserInfo() {
        mView.showProgressDialog();
        User user = mModel.getUser();
        AppCache.getRetrofitService().postProfile(user, mModel.getToken(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                mView.hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //保存List被点击的位置
    private int currentClickPosition = 0;

    @Override
    public void onClick(int view, int position) {
        currentClickPosition = position;
        switch (position) {
            case POSITION_NICKNAME:
            case POSITION_SIGNATURE:
                mView.showEditDialog();
                break;
            case POSITION_GENDER:
                mView.showChooseDialog();
                break;
        }
    }

    @Override
    public void loadUserInfo() {
        //加载处理文字信息
        loadTextInfo();
        //加载头像
        loadAvatar();
    }

    @Override
    public void onDialogClick(ProfileView.DialogType type, View view) {
        switch (type) {
            case DIALOG_TYPE_CHOOSE:
                setGender(view);
                break;
            case DIALOG_TYPE_TEXT_EDIT:
                setListItemText(view);
                break;
        }
    }


    @Override
    public void onSelectImage(Uri uri) {
        //两种情况存在
        String filePath = "";
        long fileSize = 0;
        //URI的scheme直接就是file://.....
        if ("file".equals(uri.getScheme())) {
            //直接调用getPath方法就可以了
            filePath = uri.getPath();
        } else {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = mContext.getContentResolver()
                    .query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int path = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(path);
            cursor.close();
        }
        File file = new File(filePath);

        //判断大小,超过2M的不能选择
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
            //获取大小
            long size = inputStream.available();

            //提示重新选择
            if (size >= MAX_FILE_SIZE) {
                ToastUtils.createToast("选择图片大于2M，请重新选择");
            } else {

                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                //显示新的头像
                hasSetAvatar = true;
                mView.setAvatar(bitmap);
                mView.showProgressDialog();

                //上传头像
                AppCache.getRetrofitService().uploadAvatar(mModel.getToken(), file, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        mView.hideProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LogUtils.show();
                    }
                });
            }

            inputStream.close();
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //设置性别
    private void setGender(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.rg_radio_group);
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_boy:
                updateItem("男", currentClickPosition);
                break;
            case R.id.rb_girl:
                updateItem("女", currentClickPosition);
                break;
            default:
                ToastUtils.createToast("未选择");
        }
    }

    //把键入的文字显示在上面
    private void setListItemText(View view) {
        EditText editText = view.findViewById(R.id.et_info_edit);
        String text = editText.getText().toString();
        updateItem(text, currentClickPosition);
    }

    private void loadAvatar() {
        AppCache.getRetrofitService().getAvatar(mModel.getToken(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (hasSetAvatar) {
                    return;
                }
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                mView.setAvatar(bitmap);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void loadTextInfo() {
        AppCache.getRetrofitService().getProfile(mModel.getToken(), new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                //缓存数据
                mModel.saveUser(user);
                //解析传回的数据
                parse(user);
            }
        });
    }


    private void parse(User user) {
        String nickName = user.getNickName();
        String gender = user.getGender();
        String signature = user.getSignature();
        //用户可能从来没有设置过自己的信息
        //所以本地需要保留默认的
        if (TextUtils.isEmpty(nickName)) {
            nickName = mContext.getString(R.string.default_user_name);
        }
        if (TextUtils.isEmpty(gender)) {
            gender = mContext.getString(R.string.default_gender);
        }
        if (TextUtils.isEmpty(signature)) {
            signature = mContext.getString(R.string.default_user_description);
        }
        //构建map准备创建SimpleAdapter
        String[] values = new String[]{
                "当前账号", mModel.getId() + "",
                "昵称", nickName,
                "性别", gender,
                "个性签名", signature
        };


        for (int i = 0; i < values.length; i += 2) {
            Map<String, String> map = new TreeMap<>();
            map.put(keys[0], values[i]);
            map.put(keys[1], values[i + 1]);
            list.add(map);
        }

        //simpleAdapter的规则
        //一个list保存多个map，每个map对应一个item
        //倒数第二个参数的长度应该和最后一个一样
        //先看list的size，决定由几个item
        //再看最后两个的参数决定有几个需要显示的值
        //把倒数第二个参数当做key，进入map找到value，显示到最后一个参数对应的ViewId上面
        //顺序很重要
        SimpleAdapter adapter = new SimpleAdapter(mContext, list, R.layout.item_profile, keys, ids);
        //显示到ListView上面

        mView.updateList(adapter);
    }

    //更新某一条
    //重置Adapter
    private void updateItem(String newValue, int position) {
        //把修改的数据保存起来
        //之后提交的时候用
        switch (position) {
            case POSITION_NICKNAME:
                mModel.getUser().setNickName(newValue);
                break;
            case POSITION_GENDER:
                mModel.getUser().setGender(newValue);
                break;
            case POSITION_SIGNATURE:
                mModel.getUser().setSignature(newValue);
        }

        if (!list.isEmpty()) {
            Map<String, String> map = list.get(position);
            map.put(keys[1], newValue);
            SimpleAdapter adapter = new SimpleAdapter(mContext, list, R.layout.item_profile, keys, ids);
            mView.updateList(adapter);
        }
    }
}

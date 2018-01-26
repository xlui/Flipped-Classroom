package io.flippedclassroom.android.base;

//Presenter的基类，利用泛型的方法组合Activity作为View
public abstract class BasePresenter<T extends BaseActivity> {
    protected T mView;

    //必须传入Activity才能构造Presenter
    public BasePresenter(T view) {
        mView = view;
    }
}

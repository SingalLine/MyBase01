package com.rilin.lzy.mybase.downmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by rilintech on 16/10/13.
 */
public abstract class FileCallback implements Callback<ResponseBody> {

    //订阅下载进度
    private CompositeSubscription rxSubscriptions = new CompositeSubscription();

    //目标文件存储的文件夹路径
    private String destFileDir;

    //目标文件存储的文件名
    private String destFileName;

    public FileCallback(String destFileDir,String destFileName){
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        //订阅下载进度
        subscribeLoadProgress();
    }

    //成功后的回调
    public abstract  void onSuccess(File file);

    //下载过程回调
    public abstract void onLoading(long progress,long total);

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            saveFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File saveFile(Response<ResponseBody> response){
        File file = null;
        InputStream in = null;
        FileOutputStream out = null;
        byte[] buf = new byte[2048*10];
        int len;

        try {
            File dir = new File(destFileDir);
            if (!dir.exists()){//文件夹不存在
                dir.mkdirs();
            }
            in = response.body().byteStream();
            file = new File(dir,destFileName);

            out = new FileOutputStream(file);
            while ((len = in.read(buf)) != -1){
                out.write(buf,0,len);
            }
            //回调成功的接口
            onSuccess(file);
            //取消订阅
            unSubscribe();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return  file;
    }
    //订阅文件下载进度
    private void subscribeLoadProgress(){
        rxSubscriptions.add(RxBus.getDefault()
                .toObservable(FileLoadingBean.class)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FileLoadingBean>(){
                    @Override
                    public void call(FileLoadingBean fileLoadEvent) {
                        onLoading(fileLoadEvent.getProgress(), fileLoadEvent.getTotal());
                    }
                })
        );
    }

    //取消订阅,防止内存泄漏
    private void unSubscribe(){
        if(!rxSubscriptions.isUnsubscribed()){
            rxSubscriptions.unsubscribe();
        }
    }

}

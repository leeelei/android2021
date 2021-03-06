package com.example.dialogdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_dialog_one;
    private Button btn_dialog_two;
    private Button btn_dialog_three;
    private Button btn_dialog_four;
    //progressDialog
    private Button btn_progress_one;
    private Button btn_progress_two;
    private Button btn_progress_three;
    private ProgressDialog pd1=null;
    private ProgressDialog pd2=null;
    private final static int MAXVALUE=100;
    private int progressStart=0;
    private int add=0;


    private Button btn_show;
    private View view_custom;
    private Context mContext;



    private boolean[] checkItems;

    private AlertDialog alert =null;
    private AlertDialog.Builder builder=null;

    final Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 123){
                pd2.setProgress(progressStart);
            }
            if(progressStart >= MAXVALUE){
                pd2.dismiss();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;
        bindView();
    }
    private void bindView(){
        btn_dialog_one=(Button)findViewById(R.id.btn_dialog_one);
        btn_dialog_two=(Button)findViewById(R.id.btn_dialog_two);
        btn_dialog_three=(Button)findViewById(R.id.btn_dialog_three);
        btn_dialog_four=(Button)findViewById(R.id.btn_dialog_four);
        btn_show=(Button)findViewById(R.id.btn_show);

        btn_progress_one=(Button)findViewById(R.id.btn_progress_one);
        btn_progress_two=(Button)findViewById(R.id.btn_progress_two);
        btn_progress_three=(Button)findViewById(R.id.btn_progress_three);

        btn_dialog_one.setOnClickListener(this);
        btn_dialog_two.setOnClickListener(this);
        btn_dialog_three.setOnClickListener(this);
        btn_dialog_four.setOnClickListener(this);
        btn_show.setOnClickListener(this);

        btn_progress_one.setOnClickListener(this);
        btn_progress_two.setOnClickListener(this);
        btn_progress_three.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_dialog_one:
                    alert=null;
                    builder=new AlertDialog.Builder(mContext);
                    alert=builder.setIcon(R.mipmap.ic_icon_fish).setTitle("????????????:")
                            .setMessage("????????????????????????AlertDialog,\n??????????????????????????????????????????????????????")
                            .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(mContext,"????????????????????????",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(mContext,"????????????????????????",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNeutralButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(mContext,"????????????????????????",Toast.LENGTH_SHORT).show();
                                }

                            }).setCancelable(false).create();
                    alert.show();
                    break;
                case R.id.btn_dialog_two:
                    final String[] lesson=new String[]{"??????","??????","??????","??????","??????","??????","??????"};
                    alert=null;
                    builder=new AlertDialog.Builder(mContext);
                    alert=builder.setIcon(R.mipmap.ic_icon_fish)
                            .setTitle("????????????????????????")
                            .setItems(lesson, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(mContext,"????????????"+lesson[which],Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setCancelable(false)
                            .create();
                    alert.show();
                    break;
                case R.id.btn_dialog_three:
                    final String[] fruits=new String[]{"??????","??????","??????","??????","??????"};
                    alert=null;
                    builder=new AlertDialog.Builder(mContext);
                    alert=builder.setIcon(R.mipmap.ic_icon_fish)
                            .setTitle("?????????????????????????????????????????????")
                            .setSingleChoiceItems(fruits,0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(mContext,"????????????"+fruits[which],Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setCancelable(false)
                            .create();
                    alert.show();
                    break;
                case R.id.btn_dialog_four:
                    final String[] menu=new String[]{"????????????","????????????","?????????","???????????????","?????????"};
                    checkItems =new boolean[]{false,false,false,false,false};
                    alert=null;
                    builder=new AlertDialog.Builder(mContext);
                    alert=builder.setIcon(R.mipmap.ic_icon_fish)
                            .setMultiChoiceItems(menu,checkItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    checkItems[which]=isChecked;
                                }
                            })
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String result="";
                                    for(int i=0;i<checkItems.length;i++){
                                        if(checkItems[i])
                                            result+=menu[i]+" ";
                                    }
                                    Toast.makeText(getApplicationContext(),"????????????"+result,Toast.LENGTH_SHORT).show();
                                }
                            }).create();
                    alert.show();
                    break;
                case R.id.btn_show:
                    alert=null;
                    builder=new AlertDialog.Builder(mContext);
                    final LayoutInflater inflater=MainActivity.this.getLayoutInflater();
                    view_custom=inflater.inflate(R.layout.view_dialog_custom,null,false);
                    builder.setView(view_custom);
                    builder.setCancelable(false);
                    alert=builder.create();

                    view_custom.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });
                    view_custom.findViewById(R.id.btn_blog).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),"????????????",Toast.LENGTH_SHORT).show();
                            Uri uri= Uri.parse("http://blog.csdn.net/coder_pig");
                            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                            startActivity(intent);
                            alert.dismiss();
                        }
                    });
                    view_custom.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),"??????????????????",Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                        }
                    });
                    alert.show();

                case R.id.btn_progress_one:
                    ProgressDialog.show(MainActivity.this,"???????????????","???????????????????????????",false,true);
                    break;
                case R.id.btn_progress_two:
                    pd1=new ProgressDialog(mContext);
                    pd1.setTitle("???????????????");
                    pd1.setMessage("?????????????????????,?????????...");
                    pd1.setCancelable(false);
                    pd1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pd1.setIndeterminate(true);
                    pd1.show();
                    break;
                case R.id.btn_progress_three:
                    progressStart=0;
                    add=0;
                    pd2=new ProgressDialog(MainActivity.this);
                    pd2.setMax(MAXVALUE);
                    pd2.setTitle("???????????????");
                    pd2.setMessage("???????????????,?????????...");
                    pd2.setCancelable(false);
                    pd2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pd2.setIndeterminate(false);
                    pd2.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(progressStart<MAXVALUE){
                                progressStart=2*usetime();
                                handler.sendEmptyMessage(123);
                            }
                        }
                    }).start();


            }
    }
    private int usetime(){
        add++;
        try{
            Thread.sleep(100);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return add;
    }
}
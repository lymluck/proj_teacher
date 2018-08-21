package study.smart.baselib.ui.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import study.smart.baselib.entity.CityTeacherInfo;
import study.smart.baselib.entity.Teacher;
import study.smart.baselib.listener.OnSendMsgDialogClickListener;
import study.smart.baselib.ui.activity.LoginActivity;
import study.smart.baselib.ui.activity.SelectMyVideoActivity;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.KeyBoardUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.ScreenUtils;
import study.smart.baselib.R;


public class DialogCreator {

    public static AppBasicDialog createBaseCustomDialog(Context context, String title, String text,
                                                        View.OnClickListener onClickListener) {
        AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_base, null);
        dialog.setContentView(v);
        TextView titleTv = (TextView) v.findViewById(R.id.dialog_base_title_tv);
        TextView textTv = (TextView) v.findViewById(R.id.dialog_base_text_tv);
        Button confirmBtn = (Button) v.findViewById(R.id.dialog_base_confirm_btn);
        titleTv.setText(title);
        textTv.setText(text);
        confirmBtn.setOnClickListener(onClickListener);
        dialog.setCancelable(false);
        return dialog;
    }

    public static AppBasicDialog createAppBasicDialog(final Context context, String title, String msg_tip, String txt_ok,
                                                      String txt_cancle, View.OnClickListener onClickListener) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.app_basicdialog, null);
        dialog.setContentView(view);
        view.findViewById(R.id.dialog_edit).setVisibility(View.GONE);
        TextView tv_title = (TextView) view.findViewById(R.id.dialog_title);
        if (TextUtils.isEmpty(title)) {
            tv_title.setVisibility(View.GONE);
        } else {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
        }
        ((TextView) view.findViewById(R.id.dialog_info)).setText(msg_tip);
        Button ok_btn = ((Button) view.findViewById(R.id.positive_btn));
        ok_btn.setText(txt_ok);
        ok_btn.setOnClickListener(onClickListener);
        Button cancle_btn = ((Button) view.findViewById(R.id.negative_btn));
        cancle_btn.setText(txt_cancle);
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static void createSendTextMsgDialog(final Context context, String avatar, String name,
                                               String content, final OnSendMsgDialogClickListener onClickListener) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_send_text, null);
        dialog.setContentView(view);
        DisplayImageUtils.displayPersonImage(context, avatar, (ImageView) view.findViewById(R.id.iv_avatar));
        ((TextView) view.findViewById(R.id.tv_name)).setText(name);
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        Button positive_btn = view.findViewById(R.id.positive_btn);
        final EditText et_word = view.findViewById(R.id.dialog_edit);
        positive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(et_word, context);
                onClickListener.onPositive(et_word.getText().toString());
            }
        });
        view.findViewById(R.id.negative_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onNegative();
                KeyBoardUtils.closeKeybord(et_word, context);
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void createSendImgMsgDialog(final Context context, String avatar, String name, String url,
                                              final OnSendMsgDialogClickListener onClickListener) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_send_img, null);
        dialog.setContentView(view);
        DisplayImageUtils.displayPersonImage(context, avatar, (ImageView) view.findViewById(R.id.iv_avatar));
        ((TextView) view.findViewById(R.id.tv_name)).setText(name);
        DisplayImageUtils.displayImage(context, url, (ImageView) view.findViewById(R.id.iv_content));
        Button positive_btn = view.findViewById(R.id.positive_btn);
        final EditText et_word = view.findViewById(R.id.dialog_edit);
        positive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(et_word, context);
                onClickListener.onPositive(et_word.getText().toString());
            }
        });
        view.findViewById(R.id.negative_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onNegative();
                KeyBoardUtils.closeKeybord(et_word, context);
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 是否登录dialog
     *
     * @param context
     */
    public static void createLoginDialog(final Activity context) {
        LayoutInflater factory = LayoutInflater.from(context);
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        final View view = factory.inflate(R.layout.app_basicdialog, null);
        dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.findViewById(R.id.dialog_edit).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.dialog_title)).setText(R.string.login);
        ((TextView) view.findViewById(R.id.dialog_info)).setText(R.string.login_now);
        Button ok_btn = ((Button) view.findViewById(R.id.positive_btn));
        ok_btn.setText(R.string.sure);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登录页面
                context.startActivityForResult(new Intent(context, LoginActivity.class), ParameterUtils.REQUEST_CODE_LOGIN);
                dialog.dismiss();
            }
        });
        Button cacle_btn = ((Button) view.findViewById(R.id.negative_btn));
        cacle_btn.setText(R.string.cancle);
        cacle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static void createWebviewDialog(WebView wv, final Context mContext) {
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                // 构建一个Builder来显示网页中的alert对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("title");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }
        });
    }


    public static void createVedioDialog(final Activity context, final DialogInterface.OnClickListener listener) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_vedio, null);
        dialog.setContentView(view);
        TextView tvNext = view.findViewById(R.id.tv_next);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClick(dialog, R.id.tv_next);
            }
        });
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setWindowAnimations(R.style.DialogNextAnim);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public static void createVedioClaimDialog(final Activity context, final String from) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_vedio_claim, null);
        dialog.setContentView(view);
        TextView tvUrl = view.findViewById(R.id.tv_url);
        TextView tvNext = view.findViewById(R.id.tv_next);
        tvUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent();
                //Url 就是你要打开的网址
                intent.setData(Uri.parse("https://xxd.smartstudy.com/article/1/"));
                intent.setAction(Intent.ACTION_VIEW);
                //启动浏览器
                context.startActivity(intent);
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if ("upLoad".equals(from)) {
                    Intent toVideo = new Intent(context, SelectMyVideoActivity.class);
                    toVideo.putExtra("singlePic", true);
                    context.startActivityForResult(toVideo, ParameterUtils.REQUEST_VIDEO);
                }
            }
        });
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setWindowAnimations(R.style.DialogEndAnim);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static AppBasicDialog createTransferTurnDownDialog(final Activity context, final OnSendMsgDialogClickListener listener) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_transfer_turn_down, null);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        final EditText etReason = view.findViewById(R.id.et_reason);
        KeyBoardUtils.openKeybord(etReason, context);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onNegative();
                }
            }
        });
        TextView tvSure = view.findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPositive(etReason.getText().toString());
                }
            }
        });
        dialog.setContentView(view);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = ScreenUtils.getScreenWidth();
        dialog.getWindow().setAttributes(p);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static AppBasicDialog createTranferTeacherDialog(final Activity context, Teacher teacher, String askName, String content, final OnSendMsgDialogClickListener listener) {
        final AppBasicDialog dialog = new AppBasicDialog(context, R.style.appBasicDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_transfer, null);
        ImageView ivAvatar = view.findViewById(R.id.iv_avatar);
        final EditText etContent = view.findViewById(R.id.et_content);
        etContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etContent.setCursorVisible(true);
            }
        });
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView tvCenterName = view.findViewById(R.id.tv_center_name);
        DisplayImageUtils.displayPersonImage(context, teacher.getAvatar(), ivAvatar);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvStudentName = view.findViewById(R.id.tv_student_name);
        tvStudentName.setText(askName);
        TextView tvContent = view.findViewById(R.id.tv_content);
        tvContent.setText(content);
        tvName.setText(teacher.getRealName());
        tvCenterName.setText(teacher.getGroup());
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onNegative();
                }
            }
        });
        TextView tvSure = view.findViewById(R.id.tv_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPositive(etContent.getText().toString());
                }
            }
        });
        dialog.setContentView(view);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        dialog.getWindow().setAttributes(p);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}

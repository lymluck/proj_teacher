package com.smartstudy.counselor_t.ui.provider;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.smartstudy.counselor_t.R;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.AutoLinkTextView;
import io.rong.imkit.widget.ILinkClickListener;
import io.rong.imkit.widget.LinkTextViewMovementMethod;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;


/**
 * Created by yqy on 2018/1/8.
 */

@ProviderTag(
        messageContent = TextMessage.class,
        showReadState = true
)
public class MyTextMessageItemProvider extends IContainerItemProvider.MessageProvider<TextMessage> {
    private static final String TAG = "TextMessageItemProvider";

    public MyTextMessageItemProvider() {
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.provider_item_text_message, (ViewGroup)null);
        MyTextMessageItemProvider.ViewHolder holder = new MyTextMessageItemProvider.ViewHolder();
        holder.message = (AutoLinkTextView)view.findViewById(R.id.tv_message);
        view.setTag(holder);
        return view;
    }

    @Override
    public Spannable getContentSummary(TextMessage data) {
        if(data == null) {
            return null;
        } else {
            String content = data.getContent();
            if(content != null) {
                if(content.length() > 100) {
                    content = content.substring(0, 100);
                }

                return new SpannableString(AndroidEmoji.ensure(content));
            } else {
                return null;
            }
        }
    }

    @Override
    public void onItemClick(View view, int position, TextMessage content, UIMessage message) {
    }

    @Override
    public void bindView(final View v, int position, TextMessage content, final UIMessage data) {
        MyTextMessageItemProvider.ViewHolder holder = (MyTextMessageItemProvider.ViewHolder)v.getTag();
        if(data.getMessageDirection() == Message.MessageDirection.SEND) {
            holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }

        final AutoLinkTextView textView = holder.message;
        if(data.getTextMessageContent() != null) {
            int len = data.getTextMessageContent().length();
            if(v.getHandler() != null && len > 500) {
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(data.getTextMessageContent());
                    }
                }, 50L);
            } else {
                textView.setText(data.getTextMessageContent());
            }
        }

        holder.message.setMovementMethod(new LinkTextViewMovementMethod(new ILinkClickListener() {
            @Override
            public boolean onLinkClick(String link) {
                RongIM.ConversationBehaviorListener listener = RongContext.getInstance().getConversationBehaviorListener();
                boolean result = false;
                if(listener != null) {
                    result = listener.onMessageLinkClick(v.getContext(), link);
                }

                if(listener == null || !result) {
                    String str = link.toLowerCase();
                    if(str.startsWith("http") || str.startsWith("https")) {
                        Intent intent = new Intent("io.rong.imkit.intent.action.webview");
                        intent.setPackage(v.getContext().getPackageName());
                        intent.putExtra("url", link);
                        v.getContext().startActivity(intent);
                        result = true;
                    }
                }

                return result;
            }
        }));
    }

    private static class ViewHolder {
        AutoLinkTextView message;
        boolean longClick;

        private ViewHolder() {
        }
    }
}

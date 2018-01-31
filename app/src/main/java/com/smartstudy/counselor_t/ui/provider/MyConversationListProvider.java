package com.smartstudy.counselor_t.ui.provider;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.util.RongUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.PrivateConversationProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by yqy on 2017/12/29.
 */
@ConversationProviderTag(
        conversationType = "private",
        portraitPosition = 1
)
public class MyConversationListProvider extends PrivateConversationProvider {

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View result = super.newView(context, viewGroup);
        MyConversationListProvider.MyHolder holder = new MyConversationListProvider.MyHolder();
        holder.tagYear = result.findViewById(R.id.tag_year);
        holder.tagCountry = result.findViewById(R.id.tag_country);
        holder.tagGrade = result.findViewById(R.id.tag_grade);
        result.setTag(R.id.my_holder, holder);
        return result;
    }

    @Override
    public void bindView(View view, int position, final UIConversation data) {
        final MyConversationListProvider.MyHolder holder = (MyConversationListProvider.MyHolder) view.getTag(R.id.my_holder);
        if (data == null) {
            holder.tagYear.setVisibility(View.GONE);
            holder.tagCountry.setVisibility(View.GONE);
            holder.tagGrade.setVisibility(View.GONE);
        } else {
            String extra = RongUtils.getMsgExtra(data.getMessageContent());
            if (!TextUtils.isEmpty(extra)) {
                final JSONObject object = JSON.parseObject(extra);
                if (!object.containsKey("abroadyear")) {
                    RongIM.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE, data.getConversationTargetId(), data.getLatestMessageId(), 100, new RongIMClient.ResultCallback<List<Message>>() {
                        @Override
                        public void onSuccess(List<Message> messages) {
                            for (Message message : messages) {
                                String extra = RongUtils.getMsgExtra(message.getContent());
                                JSONObject obj_msg = JSON.parseObject(extra);
                                if (obj_msg != null && obj_msg.containsKey("abroadyear")) {
                                    handleTag(holder, obj_msg);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                        }
                    });
                } else {
                    handleTag(holder, object);
                }
            }
        }
        super.bindView(view, position, data);
    }

    private void handleTag(MyConversationListProvider.MyHolder holder, JSONObject object) {
        String year = Utils.getStringNum(object.getString("abroadyear"));
        if (!TextUtils.isEmpty(year.trim())) {
            holder.tagYear.setVisibility(View.VISIBLE);
            holder.tagYear.setText(year);
        } else {
            holder.tagYear.setVisibility(View.GONE);
        }
        String country = object.getString("country");
        if (!TextUtils.isEmpty(country.trim())) {
            holder.tagCountry.setVisibility(View.VISIBLE);
            holder.tagCountry.setText(country);
        } else {
            holder.tagCountry.setVisibility(View.GONE);
        }
        String grade = object.getString("grade");
        if (!TextUtils.isEmpty(grade.trim())) {
            holder.tagGrade.setVisibility(View.VISIBLE);
            holder.tagGrade.setText(grade);
        } else {
            holder.tagGrade.setVisibility(View.GONE);
        }
    }

    protected class MyHolder {
        public TextView tagYear;
        public TextView tagCountry;
        public TextView tagGrade;

        protected MyHolder() {
        }
    }

}

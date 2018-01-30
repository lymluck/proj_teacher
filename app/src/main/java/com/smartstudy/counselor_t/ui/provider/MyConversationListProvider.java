package com.smartstudy.counselor_t.ui.provider;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.util.RongUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.util.List;

import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.model.ConversationKey;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.utils.RongDateUtils;
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
        View result = LayoutInflater.from(context).inflate(R.layout.fragment_item_base_conversation, (ViewGroup) null);
        MyConversationListProvider.ViewHolder holder = new MyConversationListProvider.ViewHolder();
        holder.title = (TextView) result.findViewById(R.id.rc_conversation_title);
        holder.time = (TextView) result.findViewById(R.id.rc_conversation_time);
        holder.content = (TextView) result.findViewById(R.id.rc_conversation_content);
        holder.notificationBlockImage = (ImageView) result.findViewById(R.id.rc_conversation_msg_block);
        holder.readStatus = (ImageView) result.findViewById(R.id.rc_conversation_status);
        holder.tagYear = result.findViewById(R.id.tag_year);
        holder.tagCountry = result.findViewById(R.id.tag_country);
        holder.tagGrade = result.findViewById(R.id.tag_grade);
        result.setTag(holder);
        return result;
    }

    @Override
    public void bindView(View view, int position, final UIConversation data) {
        final MyConversationListProvider.ViewHolder holder = (MyConversationListProvider.ViewHolder) view.getTag();
        ProviderTag tag = null;
        if (data == null) {
            holder.title.setText((CharSequence) null);
            holder.time.setText((CharSequence) null);
            holder.content.setText((CharSequence) null);
            holder.tagYear.setVisibility(View.GONE);
            holder.tagCountry.setVisibility(View.GONE);
            holder.tagGrade.setVisibility(View.GONE);
        } else {
            holder.title.setText(data.getUIConversationTitle());
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
            String time = RongDateUtils.getConversationListFormatDate(data.getUIConversationTime(), view.getContext());
            holder.time.setText(time);
            if (TextUtils.isEmpty(data.getDraft()) && !data.getMentionedFlag()) {
                boolean readRec = false;
                try {
                    readRec = view.getResources().getBoolean(io.rong.imkit.R.bool.rc_read_receipt);
                } catch (Resources.NotFoundException var11) {
                    RLog.e("PrivateConversationProvider", "rc_read_receipt not configure in rc_config.xml");
                    var11.printStackTrace();
                }

                if (readRec) {
                    if (data.getSentStatus() == Message.SentStatus.READ && data.getConversationSenderId().equals(RongIM.getInstance().getCurrentUserId())) {
                        holder.readStatus.setVisibility(View.VISIBLE);
                    } else {
                        holder.readStatus.setVisibility(View.GONE);
                    }
                }

                Object cutStr;
                if (holder.content.getWidth() > 40 && data.getConversationContent() != null) {
                    cutStr = TextUtils.ellipsize(data.getConversationContent(), holder.content.getPaint(), (float) (holder.content.getWidth() - 40), TextUtils.TruncateAt.END);
                } else {
                    cutStr = data.getConversationContent();
                }

                holder.content.setText((CharSequence) cutStr, TextView.BufferType.SPANNABLE);
            } else {
                SpannableStringBuilder builder = new SpannableStringBuilder();
                SpannableString string;
                String preStr;
                CharSequence cutStr;
                if (data.getMentionedFlag()) {
                    preStr = view.getContext().getString(R.string.rc_message_content_mentioned);
                    if (holder.content.getWidth() > 0) {
                        cutStr = TextUtils.ellipsize(preStr + " " + data.getConversationContent(), holder.content.getPaint(), (float) (holder.content.getWidth() - 40), TextUtils.TruncateAt.END);
                        string = new SpannableString(cutStr);
                    } else {
                        string = new SpannableString(preStr + " " + data.getConversationContent());
                    }

                    string.setSpan(new ForegroundColorSpan(view.getContext().getResources().getColor(io.rong.imkit.R.color.rc_mentioned_color)), 0, preStr.length(), 33);
                    builder.append(string);
                } else {
                    preStr = view.getContext().getString(R.string.rc_message_content_draft);
                    if (holder.content.getWidth() > 0) {
                        cutStr = TextUtils.ellipsize(preStr + " " + data.getDraft(), holder.content.getPaint(), (float) (holder.content.getWidth() - 40), TextUtils.TruncateAt.END);
                        string = new SpannableString(cutStr);
                    } else {
                        string = new SpannableString(preStr + " " + data.getDraft());
                    }

                    string.setSpan(new ForegroundColorSpan(view.getContext().getResources().getColor(io.rong.imkit.R.color.rc_draft_color)), 0, preStr.length(), 33);
                    builder.append(string);
                }

                AndroidEmoji.ensure(builder);
                holder.content.setText(builder, TextView.BufferType.SPANNABLE);
                holder.readStatus.setVisibility(View.GONE);
            }

            if (RongContext.getInstance() != null && data.getMessageContent() != null) {
                tag = RongContext.getInstance().getMessageProviderTag(data.getMessageContent().getClass());
            }

            if (data.getSentStatus() != null && (data.getSentStatus() == Message.SentStatus.FAILED || data.getSentStatus() == Message.SentStatus.SENDING) && tag != null && tag.showWarning() && data.getConversationSenderId() != null && data.getConversationSenderId().equals(RongIM.getInstance().getCurrentUserId())) {
                Bitmap bitmap = BitmapFactory.decodeResource(view.getResources(), io.rong.imkit.R.drawable.rc_conversation_list_msg_send_failure);
                int width = bitmap.getWidth();
                Drawable drawable = null;
                if (data.getSentStatus() == Message.SentStatus.FAILED && TextUtils.isEmpty(data.getDraft())) {
                    drawable = view.getContext().getResources().getDrawable(R.drawable.rc_conversation_list_msg_send_failure);
                } else if (data.getSentStatus() == Message.SentStatus.SENDING && TextUtils.isEmpty(data.getDraft())) {
                    drawable = view.getContext().getResources().getDrawable(R.drawable.rc_conversation_list_msg_sending);
                }

                if (drawable != null) {
                    drawable.setBounds(0, 0, width, width);
                    holder.content.setCompoundDrawablePadding(10);
                    holder.content.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
                }
            } else {
                holder.content.setCompoundDrawables((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
            }

            ConversationKey key = ConversationKey.obtain(data.getConversationTargetId(), data.getConversationType());
            Conversation.ConversationNotificationStatus status = RongContext.getInstance().getConversationNotifyStatusFromCache(key);
            if (status != null && status.equals(Conversation.ConversationNotificationStatus.DO_NOT_DISTURB)) {
                holder.notificationBlockImage.setVisibility(View.VISIBLE);
            } else {
                holder.notificationBlockImage.setVisibility(View.GONE);
            }
        }
    }

    private void handleTag(MyConversationListProvider.ViewHolder holder, JSONObject object) {
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

    protected class ViewHolder {
        public TextView title;
        public TextView time;
        public TextView content;
        public ImageView notificationBlockImage;
        public ImageView readStatus;
        public TextView tagYear;
        public TextView tagCountry;
        public TextView tagGrade;

        protected ViewHolder() {
        }
    }

}
